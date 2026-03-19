#!/usr/bin/env python3
"""Bridge server: receives HTTP requests from dashboard, sends adb broadcasts to the app."""
import http.server
import json
import subprocess
import os
import sys
import time

ADB = os.path.expanduser("~/Library/Android/sdk/platform-tools/adb")

class Handler(http.server.BaseHTTPRequestHandler):
    def _cors(self):
        self.send_header("Access-Control-Allow-Origin", "*")
        self.send_header("Access-Control-Allow-Methods", "POST, GET, OPTIONS")
        self.send_header("Access-Control-Allow-Headers", "Content-Type")

    def do_OPTIONS(self):
        self.send_response(200)
        self._cors()
        self.end_headers()

    def do_GET(self):
        path = self.path.split("?")[0]

        if path == "/status":
            result = subprocess.run([ADB, "devices"], capture_output=True, text=True)
            connected = "device" in result.stdout.split("\n", 1)[-1]
            self.send_response(200)
            self._cors()
            self.send_header("Content-Type", "application/json")
            self.end_headers()
            self.wfile.write(json.dumps({"connected": connected}).encode())

        elif path == "/screenshot":
            subprocess.run([ADB, "exec-out", "screencap", "-p"],
                         stdout=open("/tmp/csp_dash_screen.png", "wb"))
            self.send_response(200)
            self._cors()
            self.send_header("Content-Type", "image/png")
            self.end_headers()
            with open("/tmp/csp_dash_screen.png", "rb") as f:
                self.wfile.write(f.read())

        elif path == "/data":
            # Trigger DUMP_STATE broadcast, wait, then read the JSON
            subprocess.run([ADB, "shell", "am", "broadcast", "-a", "com.wiom.csp.DUMP_STATE",
                           "-n", "com.wiom.csp/.DashboardReceiver"], capture_output=True)
            time.sleep(0.3)
            result = subprocess.run(
                [ADB, "shell", "run-as", "com.wiom.csp", "cat", "/data/data/com.wiom.csp/files/state.json"],
                capture_output=True, text=True
            )
            if result.returncode == 0 and result.stdout.strip():
                self.send_response(200)
                self._cors()
                self.send_header("Content-Type", "application/json")
                self.end_headers()
                self.wfile.write(result.stdout.strip().encode())
            else:
                self.send_response(200)
                self._cors()
                self.send_header("Content-Type", "application/json")
                self.end_headers()
                self.wfile.write(json.dumps({"error": "No data yet. Fill the app first."}).encode())

        elif path == "/kyc-image":
            # Serve a placeholder KYC document image (screenshot of the KYC screen)
            # In production this would fetch actual uploaded documents
            self.send_response(200)
            self._cors()
            self.send_header("Content-Type", "application/json")
            self.end_headers()
            self.wfile.write(json.dumps({"info": "KYC images are stored on device. Use screenshot to view."}).encode())

        else:
            self.send_response(404)
            self.end_headers()

    def do_POST(self):
        length = int(self.headers.get("Content-Length", 0))
        body = json.loads(self.rfile.read(length)) if length else {}
        action = body.get("action", "")

        cmd = None
        if action == "scenario":
            name = body.get("name", "NONE")
            cmd = [ADB, "shell", "am", "broadcast", "-a", "com.wiom.csp.SCENARIO",
                   "--es", "name", name, "-n", "com.wiom.csp/.DashboardReceiver"]
        elif action == "navigate":
            screen = body.get("screen", 0)
            cmd = [ADB, "shell", "am", "broadcast", "-a", "com.wiom.csp.NAVIGATE",
                   "--ei", "screen", str(screen), "-n", "com.wiom.csp/.DashboardReceiver"]
        elif action == "lang":
            lang = body.get("lang", "toggle")
            cmd = [ADB, "shell", "am", "broadcast", "-a", "com.wiom.csp.LANG",
                   "--es", "lang", lang, "-n", "com.wiom.csp/.DashboardReceiver"]
        elif action == "reset":
            cmd = [ADB, "shell", "am", "broadcast", "-a", "com.wiom.csp.RESET",
                   "-n", "com.wiom.csp/.DashboardReceiver"]
        elif action == "fill":
            mode = body.get("mode", "empty")
            cmd = [ADB, "shell", "am", "broadcast", "-a", "com.wiom.csp.FILL",
                   "--es", "mode", mode, "-n", "com.wiom.csp/.DashboardReceiver"]
        elif action == "qa":
            decision = body.get("decision", "approved")
            cmd = [ADB, "shell", "am", "broadcast", "-a", "com.wiom.csp.QA",
                   "--es", "action", decision, "-n", "com.wiom.csp/.DashboardReceiver"]
        elif action == "training_config":
            modules = body.get("modules", [])
            config_json = json.dumps(modules)
            cmd = [ADB, "shell", "am", "broadcast", "-a", "com.wiom.csp.TRAINING",
                   "--es", "config", config_json, "-n", "com.wiom.csp/.DashboardReceiver"]
        elif action == "restart":
            subprocess.run([ADB, "shell", "am", "force-stop", "com.wiom.csp"])
            cmd = [ADB, "shell", "am", "start", "-n", "com.wiom.csp/.MainActivity"]

        if cmd:
            result = subprocess.run(cmd, capture_output=True, text=True)
            self.send_response(200)
            self._cors()
            self.send_header("Content-Type", "application/json")
            self.end_headers()
            self.wfile.write(json.dumps({"ok": True, "output": result.stdout}).encode())
        else:
            self.send_response(400)
            self._cors()
            self.end_headers()
            self.wfile.write(json.dumps({"ok": False, "error": "Unknown action"}).encode())

    def log_message(self, format, *args):
        print(f"[Bridge] {args[0]}")

if __name__ == "__main__":
    port = 8092
    print(f"CSP Dashboard Bridge running on http://localhost:{port}")
    print(f"Using ADB at: {ADB}")
    server = http.server.HTTPServer(("", port), Handler)
    server.serve_forever()
