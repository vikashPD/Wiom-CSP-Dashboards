# Wiom CSP Onboarding App — Product Requirements Document

**Version:** 2.0 | **Date:** 19 March 2026 | **Status:** Prototype
**Repo:** [github.com/ashishagrawal-iam/Wiom-csp-onboarding-v2](https://github.com/ashishagrawal-iam/Wiom-csp-onboarding-v2)
**Package:** `com.wiom.csp`

---

## Table of Contents

1. [What This App Does](#1-what-this-app-does)
2. [Who Uses It](#2-who-uses-it)
3. [The Onboarding Journey (15 Screens)](#3-the-onboarding-journey)
4. [Flow Diagram](#4-flow-diagram)
5. [Screen-by-Screen Specification](#5-screen-by-screen-specification)
6. [Error Scenarios (16 Cases)](#6-error-scenarios)
7. [Empty States & Edge Cases](#7-empty-states--edge-cases)
8. [Dashboard System](#8-dashboard-system)
9. [Dashboard-App Interaction](#9-dashboard-app-interaction)
10. [Business Rules & Constants](#10-business-rules--constants)
11. [Validation Rules](#11-validation-rules)
12. [Design System Reference](#12-design-system-reference)
13. [QA Test Cases](#13-qa-test-cases)
14. [UAT Test Cases](#14-uat-test-cases)
15. [What's Prototype vs Production](#15-whats-prototype-vs-production)

---

## 1. What This App Does

This Android app takes a new Channel Sales Partner (CSP) through the complete journey of becoming a Wiom partner — from entering their phone number to going live and serving customers.

The flow has **15 screens** across **3 phases**, with **16 documented error scenarios** and a companion **admin dashboard** for QA teams.

**Key design philosophy:**
- **Hindi-first** — All text defaults to Hindi with a runtime toggle to English
- **No-blame errors** — Never blame the user; always reassure ("Don't worry")
- **Benefit-first** — Lead with what the partner gains
- **Trust badges** — Lock icons and verification badges build confidence
- **Warm tone** — Conversational, friendly, never bureaucratic

---

## 2. Who Uses It

| Actor | Role | How They Interact |
|-------|------|-------------------|
| **Partner** | New CSP applicant | Goes through the 15-screen onboarding flow on their Android phone |
| **QA Team** | Wiom Business/QA reviewers | Uses the Dashboard to approve or reject applications |
| **Admin** | Dashboard operator | Manages scenarios, training modules, and controls the app remotely |
| **System** | Automated backend | Handles OTP, KYC verification, penny drop, dedup check, financial setup |

---

## 3. The Onboarding Journey

### Phase 1 — Registration (Screens 0-5)

| Screen | Name | What Happens |
|--------|------|-------------|
| 0 | Phone Entry | Partner enters mobile number (+91, 10 digits) |
| 1 | OTP Verification | 4-digit OTP with 30-second countdown timer |
| 2 | Personal & Business Info | Name, email, entity type, trade name |
| 3 | Location | State, city, pincode, address, GPS capture |
| 4 | KYC Documents | Upload PAN, Aadhaar (front+back), GST certificate |
| 5 | Registration Fee | Pay Rs.2,000 (refundable if QA rejects) |

### Phase 2 — Verification & Documentation (Screens 6-10)

| Screen | Name | What Happens |
|--------|------|-------------|
| 6 | QA Investigation | **Decision point** — QA team approves or rejects (via dashboard) |
| 7 | Policy & Rate Card | Partner reviews commission structure and SLA terms |
| 8 | Bank + Dedup Check | Bank account entry, penny drop verification, duplicate check |
| 9 | Agreement | Legal agreement review + Aadhaar e-Sign |
| 10 | Technical Review | Device compatibility, infra check, shop/equipment photos |

### Phase 3 — Activation (Screens 11-14)

| Screen | Name | What Happens |
|--------|------|-------------|
| 11 | Onboarding Fee | Pay Rs.20,000 (total investment becomes Rs.22,000) |
| 12 | Financial Setup | Automated: ledger, RazorpayX, Zoho, trade name lock, TDS/TCS |
| 13 | Training | 3 modules with video + quiz (App Usage, SLA, Money Matters) |
| 14 | Go Live! | Celebration screen with status chips and quick actions |

---

## 4. Flow Diagram

```
START
  |
  v
[Screen 0: Phone Entry] ----error----> PHONE_DUPLICATE (blocked)
  |
  v
[Screen 1: OTP Verify] ----error----> OTP_WRONG (retry, 3 max)
  |                     ----error----> OTP_EXPIRED (resend)
  v
[Screen 2: Personal Info]
  |
  v
[Screen 3: Location] ------error----> AREA_NOT_SERVICEABLE (waitlist, blocked)
  |
  v
[Screen 4: KYC Upload] ----error----> KYC_PAN_MISMATCH (blocked)
  |                     ----error----> KYC_AADHAAR_EXPIRED (blocked)
  |                     ----error----> KYC_PAN_AADHAAR_UNLINKED (blocked)
  v
[Screen 5: Rs.2,000 Fee] --error----> REGFEE_FAILED (retry)
  |                       --error----> REGFEE_TIMEOUT (retry)
  v
[Screen 6: QA Investigation] =========== BRANCH POINT ===========
  |                                                              |
  |--- QA APPROVED                                  QA REJECTED ---|
  |                                                              |
  v                                                              v
[Screen 7: Policy & Rate Card]                    REFUND Rs.2,000
  |                                              (5-7 working days)
  v                                                   END
[Screen 8: Bank + Dedup] ---error----> BANK_PENNYDROP_FAIL (retry)
  |                      ---error----> BANK_NAME_MISMATCH (retry)
  |                      ---error----> DEDUP_FOUND (blocked, support)
  v
[Screen 9: Agreement] -----error----> ESIGN_FAILED (retry)
  |
  v
[Screen 10: Tech Review] --error----> TECH_DEVICE_INCOMPATIBLE (blocked)
  |
  v
[Screen 11: Rs.20,000 Fee] -error---> ONBOARDFEE_FAILED (retry)
  |
  v
[Screen 12: Financial Setup] (auto, no user input)
  |
  v
[Screen 13: Training] -----error----> TRAINING_QUIZ_FAIL (retry)
  |
  v
[Screen 14: GO LIVE!]
  |
  END
```

### Error Classification

| Type | Errors | Behavior |
|------|--------|----------|
| **Blocking** | PHONE_DUPLICATE, AREA_NOT_SERVICEABLE, KYC_PAN_MISMATCH, KYC_AADHAAR_EXPIRED, KYC_PAN_AADHAAR_UNLINKED, DEDUP_FOUND, TECH_DEVICE_INCOMPATIBLE | Cannot proceed — needs external resolution |
| **Retryable** | OTP_WRONG, OTP_EXPIRED, REGFEE_FAILED, REGFEE_TIMEOUT, BANK_PENNYDROP_FAIL, BANK_NAME_MISMATCH, ESIGN_FAILED, ONBOARDFEE_FAILED, TRAINING_QUIZ_FAIL | Can retry immediately or after fixing input |

---

## 5. Screen-by-Screen Specification

### Screen 0: Phone Entry

**Purpose:** First touchpoint — capture the partner's mobile number.

**What the partner sees:**
- Header: "Wiom Partner+"
- Greeting with handshake emoji and "Become a Partner" message
- Phone input field with +91 country code prefix
- Character count hint showing "X/10 digits" until complete
- Info message: "OTP will be sent to your number"
- Pink CTA button: "OTP bhejein" (Send OTP)

**Rules:**
- CTA stays disabled until exactly 10 digits entered
- Only numeric input allowed
- On success: navigates to OTP screen

---

### Screen 1: OTP Verification

**Purpose:** Confirm phone ownership via one-time password.

**What the partner sees:**
- Message showing which number OTP was sent to
- 4 input boxes (auto-focus, fills left to right with blinking cursor)
- Countdown timer starting at 30 seconds
- After timer expires: "Resend OTP" link + "Change Number" link

**OTP Box Visual States:**
- Empty: dark border, white background
- Focused: pink border, blinking cursor
- Filled: green border, light green background
- Error: red border
- Expired: gray border, faded (50% opacity)

**Rules:**
- CTA ("Verify") enabled only when all 4 digits filled
- Timer counts down 1 second at a time
- Resend restarts the 30-second timer

---

### Screen 2: Personal & Business Info

**Purpose:** Collect identity and business details.

**Fields:**
1. **Name (as per Aadhaar)** — Required
2. **Email** — Required, must contain @ and .
3. **Entity Type** — Dropdown: Individual, Proprietorship, Partnership, Private Limited, LLP
4. **Trade Name** — Required; gets locked permanently after registration fee is paid

**Rules:**
- CTA enabled only when all 4 fields are filled
- No error scenarios on this screen — it's purely data collection

---

### Screen 3: Location

**Purpose:** Capture shop/office location for service area validation.

**Fields:**
1. **State** — Read-only (pre-set to "Madhya Pradesh")
2. **City** — Editable
3. **Pincode** — 6 digits only
4. **Full Address** — Editable

**Special Elements:**
- GPS badge showing captured coordinates (22.71 N, 75.85 E)

**Rules:**
- All fields are technically optional for CTA (but pincode validated if entered)
- Service area check happens against pincode

---

### Screen 4: KYC Documents

**Purpose:** Upload and verify identity documents.

**4 Documents Required:**
1. PAN Card
2. Aadhaar Card — Front
3. Aadhaar Card — Back
4. GST Certificate

**Upload Flow (3 steps per document):**

1. **Choose Source** — Bottom sheet with "Take Photo" (camera) and "Choose from Gallery" options. Tip: "Take a clear photo — all text must be visible"

2. **Preview** — Shows captured photo with quality badges ("Clear" + "Complete"). Two CTAs: "Save this photo" or "Retake"

3. **Uploading** — Simulated progress bar (50 steps x 80ms = 4 seconds), then green checkmark with "Upload complete!"

**Document Card States:**
- Not uploaded: Gray border, placeholder, tap to upload
- Uploaded: Green border, green background, checkmark badge, "Remove" button available

**Rules:**
- All 4 documents must show "uploaded" state before CTA is enabled
- Each uploaded document can be individually removed and re-uploaded

---

### Screen 5: Registration Fee (Rs.2,000)

**Purpose:** Collect the first payment, which initiates QA review.

**What the partner sees:**
- Large amount display: Rs.2,000
- Important info card explaining QA review process
- Trust badge: "Full refund if rejected" (with lock icon)
- Info: "QA investigation will start after fee payment"

**On payment:**
- 2-second simulated processing delay
- On success: trade name gets locked, navigates to QA Investigation

---

### Screen 6: QA Investigation (Decision Point)

**Purpose:** This is the major branch point — QA team decides approve or reject.

**This screen has TWO completely different views:**

**View A — Pending/Waiting:**
- "Investigation in progress" message
- Checklist showing 5 completed steps + QA as "waiting"
- Info: "Review may take 2-3 business days"
- No CTA — partner waits for notification

**View B — Rejected:**
- "Profile not accepted yet" (with sad emoji)
- Reassurance: "Don't worry — your money is safe"
- Reason card explaining why (e.g., "Location not in service area")
- Refund card: "Rs.2,000 will be credited in 5-7 working days" with reference number
- Toggle button to view the approved path (for demo purposes)

**The decision is made by the QA team through the Dashboard (approve/reject buttons).**

---

### Screen 7: Policy & Rate Card

**Purpose:** Partner reviews and acknowledges the business terms.

**Shows:**
- Commission Structure:
  - New Connection: Rs.300 per connection
  - Recharge Commission: Rs.300

- SLA Terms:
  - Customer complaints: 4-hour resolution
  - Connection uptime: 95%+
  - Equipment care: partner's responsibility
  - Wiom brand guidelines: mandatory compliance

**Rules:**
- Simple acknowledgment screen — CTA is "Understood, proceed"

---

### Screen 8: Bank + Dedup Check

**Purpose:** Verify the partner's bank account and check for duplicates.

**Fields (all required):**
1. Account Holder Name
2. Bank Name
3. Account Number
4. IFSC Code

**Two-Phase Flow:**
1. **Before verification:** Info message "Fill all bank details," CTA is "Verify via Penny Drop" (enabled only when all fields filled)
2. **After verification (2s delay):** Two green success cards appear:
   - "Penny Drop Verified" — Rs.1 credited, name match confirmed
   - "Dedup Check Passed" — PAN, Aadhaar, GST, Bank — no duplicates

**Rules:**
- All fields must be filled to enable the verify button
- Penny drop and dedup check happen together (simulated as one 2-second operation)

---

### Screen 9: Agreement Signing

**Purpose:** Legal agreement review and digital signature.

**Shows:**
- Scrollable agreement text (5 clauses: Scope, Responsibilities, Commission, Term, Compliance)
- Verification badges: DOT Compliance Verified, TRAI Guidelines Acknowledged
- Checkbox: "I have read and accept all terms" (pre-checked by default)
- Info: "Agreement will be signed via Aadhaar e-Sign"

**Rules:**
- CTA ("e-Sign") disabled if checkbox unchecked
- 2-second simulated e-Sign process

---

### Screen 10: Technical Review

**Purpose:** Verify the partner's device and infrastructure.

**Three Sections:**

1. **Device Check** (auto-verified):
   - Device model: Samsung Galaxy M34
   - Android 14 — Compatible
   - Wiom OS: Ready

2. **Infrastructure Check:**
   - Internet Setup dropdown: Fiber (FTTH) / Cable / Wireless
   - Shop Front Photo: uploadable (clickable toggle in prototype)
   - Router/Equipment Photo: uploadable (clickable toggle in prototype)

3. **Completion State** — When all 3 items done:
   - "Tech review complete! Now pay the onboarding fee."

**Rules:**
- CTA enabled when: internet type selected AND both photos uploaded

---

### Screen 11: Onboarding Fee (Rs.20,000)

**Purpose:** Final payment before training unlocks.

**Shows:**
- Amount: Rs.20,000 (GST inclusive)
- Breakdown:
  - Registration Fee (paid): Rs.2,000
  - Onboarding Fee: Rs.20,000
  - **Total Investment: Rs.22,000**
- Info: "Training modules will unlock after payment"

**On payment:**
- 2-second processing delay → navigates to Financial Setup

---

### Screen 12: Financial Setup (Automated)

**Purpose:** System automatically configures the partner's financial accounts.

**No user interaction required.** The screen auto-plays through 5 setup items (800ms each):

1. Partner Ledger Created — "Commission tracking active"
2. RazorpayX Payout Link — "SBI A/C XXXX4521 linked for payouts"
3. Zoho Invoice Setup — "Auto-invoice for every settlement"
4. Trade Name Locked — "Rajesh Telecom — official name"
5. TDS/TCS Configuration — "PAN ABCDE1234F — auto deduction setup"

**Item Visual States:**
- Pending: gray circle with dots
- Processing: spinning indicator with "Processing..."
- Done: green checkmark

**On completion:**
- "All set!" success card
- Info: "Commission payouts every Monday, directly to your bank"

---

### Screen 13: Training Modules

**Purpose:** Partner completes 3 training modules before going live.

**Module List View:**
- Progress bar showing X/3 completed
- 3 module cards with state indicators (done/current/not started)

**The 3 Modules:**

| Module | Topic | Questions |
|--------|-------|-----------|
| 1. App Usage | How to add customers, check recharge status | 2 questions |
| 2. SLA & Exposure | Complaint resolution time, uptime requirement | 2 questions |
| 3. Money Matters | Commission per connection, payout frequency | 2 questions |

**Module Detail Flow:**
1. **Watch Video** — Simulated video playback (10 steps x 100ms)
2. **Take Quiz** — Appears after video watched
   - Select an option → "Check Answer" button
   - Correct: green flash, auto-advance to next question (800ms)
   - Wrong: red flash, hint shown, "Try Again" button
3. **Complete** — Celebration card, module marked done

**Module Card States:**
- Done: green background + border, checkmark badge
- Current (first incomplete): pink background + border, "Start" badge
- Not started: white background, "Start" badge

**Rules:**
- All 3 modules must be completed to enable "Complete Quiz" CTA
- Training modules are configurable via the Dashboard

---

### Screen 14: Go Live!

**Purpose:** Celebration and activation.

**Shows:**
- "Congratulations, Rajesh!" with confetti emoji
- "You are now a Wiom Partner"
- **7 Status Chips** (all green with checkmarks):
  - Registered, QA Approved, Bank Verified, Agreement, Tech Review, Financial Setup, Trained

- **4 Quick Action Cards:**
  - Add Customer — "New connection"
  - View Earnings — "Commission, TDS"
  - Tasks — "Restore, complaints"
  - Training — "Revisit modules"

- **Download Card:** Prompt to download the Wiom CSP App for actual operations

**Quick Action Detail:** Each card opens a sub-view with a video placeholder and a "Open Wiom CSP App" CTA.

---

## 6. Error Scenarios

### Registration & OTP Errors

#### PHONE_DUPLICATE (Screen 0)
**When:** Phone number already has an account in the system.
**What partner sees:** "This number is already registered" with error card. Two options: "Send OTP with new number" or "Login."
**Outcome:** Blocked — cannot proceed without changing number or logging in.

#### OTP_WRONG (Screen 1)
**When:** Partner enters incorrect OTP digits.
**What partner sees:** OTP boxes turn red. Error message: "Wrong OTP — 2 attempts remaining." Resend link available.
**Outcome:** Retryable — up to 3 attempts total.

#### OTP_EXPIRED (Screen 1)
**When:** OTP validity period exceeded (timer ran out).
**What partner sees:** OTP boxes become faded/disabled. Message: "OTP has expired — don't worry, send a new OTP." Two options: "Send new OTP" or "Change Number."
**Outcome:** Retryable — request new OTP.

---

### Location & KYC Errors

#### AREA_NOT_SERVICEABLE (Screen 3)
**When:** Partner's pincode is not in Wiom's service area.
**What partner sees:** Orange warning card: "This area is not serviceable yet. Join the waitlist!" Shows "47 people already on waitlist."
**Outcome:** **Blocked** — must join waitlist or try different pincode.

#### KYC_PAN_MISMATCH (Screen 4)
**When:** Name on PAN card doesn't match name on Aadhaar.
**What partner sees:** PAN card row turns red with "Name Mismatch" badge. Other docs stay green. Error shows both names side-by-side.
**Outcome:** **Blocked** — must correct name or re-upload PAN.

#### KYC_AADHAAR_EXPIRED (Screen 4)
**When:** Aadhaar card address is outdated.
**What partner sees:** Aadhaar row turns orange with "Address Update Required." Info directs to uidai.gov.in.
**Outcome:** **Blocked** — must update Aadhaar at UIDAI portal.

#### KYC_PAN_AADHAAR_UNLINKED (Screen 4)
**When:** PAN and Aadhaar are not linked in NSDL database.
**What partner sees:** All docs show verified, but linking error card appears. Directs to incometax.gov.in.
**Outcome:** **Blocked** — must link PAN-Aadhaar at income tax portal.

---

### Payment Errors

#### REGFEE_FAILED (Screen 5)
**When:** Rs.2,000 payment declined by bank/gateway.
**What partner sees:** Sad emoji, "Payment could not be processed." Green reassurance: "No money deducted." Transaction details (error: BANK_GATEWAY_TIMEOUT). Tip: "Try again after 2-3 minutes."
**Outcome:** Retryable — "Retry Payment" or "Pay Later."

#### REGFEE_TIMEOUT (Screen 5)
**When:** Payment gateway timed out during processing.
**What partner sees:** Hourglass emoji, "Payment is pending." Orange card: "Bank response delayed — may take 2-5 minutes." Shows UPI reference number and pending status. Info: "Auto-refund within 48hrs if failed."
**Outcome:** Retryable — "Refresh Status" or "Talk to us."

#### ONBOARDFEE_FAILED (Screen 11)
**When:** Rs.20,000 payment declined.
**What partner sees:** Same structure as REGFEE_FAILED but with different amount and error (UPI_LIMIT_EXCEEDED). Tip: "UPI limit Rs.1L/day — try NEFT/RTGS or card."
**Outcome:** Retryable — alternative payment method suggested.

---

### Bank & Dedup Errors

#### BANK_PENNYDROP_FAIL (Screen 8)
**When:** Rs.1 penny drop credit failed.
**What partner sees:** Account number field turns red. Error: "Penny drop failed — account number may be wrong or bank server is down."
**Outcome:** Retryable — fix account number and retry.

#### BANK_NAME_MISMATCH (Screen 8)
**When:** Bank account holder name differs from KYC name.
**What partner sees:** Orange mismatch card showing both names side-by-side (Bank: "Rajesh Kumar Sharma" vs Entered: "Rajesh Kumar").
**Outcome:** Retryable — fix name and retry verification.

#### DEDUP_FOUND (Screen 8)
**When:** Existing partner already registered with same PAN/Bank account.
**What partner sees:** Penny drop passes (green), but dedup alert appears (red). Shows matching partner details (ID: CSP-0031, Name: Rajesh K., City: Indore, Match: PAN + Bank A/C).
**Outcome:** **Blocked** — must contact Wiom support.

---

### Documentation & Tech Errors

#### ESIGN_FAILED (Screen 9)
**When:** Aadhaar e-Sign connection error or UIDAI server down.
**What partner sees:** Orange error: "Could not connect to UIDAI server." Troubleshooting steps: check internet, wait 2-3 minutes, retry.
**Outcome:** Retryable — "Retry e-Sign" or "Talk to us."

#### TECH_DEVICE_INCOMPATIBLE (Screen 10)
**When:** Partner's device doesn't meet minimum requirements.
**What partner sees:** Red "DEVICE CHECK FAILED" card. Shows current device specs vs minimum (Android 8.1 vs min 11, RAM 1GB vs min 3GB). Lists recommended devices: Samsung M34, Redmi Note 12, Realme Narzo 60.
**Outcome:** **Blocked** — must upgrade device.

---

### Training Error

#### TRAINING_QUIZ_FAIL (Screen 13)
**When:** Partner fails the training quiz.
**What partner sees:** Score card showing 2/5 (need 4/5 to pass, red progress bar at 40%). Encouraging message: "Don't worry — review modules again and retake quiz."
**Outcome:** Retryable — unlimited retries, can review modules first.

---

## 7. Empty States & Edge Cases

### Empty States

| Screen | Empty State | Behavior |
|--------|-------------|----------|
| 0 | No phone entered | CTA disabled, character counter shows "0/10" |
| 1 | No OTP digits | CTA disabled, boxes empty with dark border |
| 2 | No personal info | CTA disabled until all 4 fields filled |
| 3 | No location data | CTA still enabled (fields optional) |
| 4 | No documents uploaded | CTA disabled, all 4 rows show "Upload" |
| 8 | No bank details | Verify button disabled, info message shown |
| 10 | No photos/setup | CTA disabled until all 3 items completed |
| 13 | No modules done | All modules show "Start" badge, CTA disabled |

### Edge Cases

| Case | Expected Behavior |
|------|-------------------|
| Partial KYC (2 of 4 docs) | CTA stays disabled, uploaded docs retain their state |
| Bank 3 of 4 fields filled | Verify button stays disabled |
| Agreement checkbox unchecked | e-Sign CTA becomes disabled |
| OTP timer at exactly 0 | Timer text disappears, resend + change number links appear |
| Remove uploaded document | Document returns to "not uploaded" state, CTA re-evaluates |
| Navigate back then forward | All form data is preserved (global singleton state) |
| Scenario trigger then clear | Screen returns to happy path state |
| Rapid screen navigation (dashboard) | App navigates to final target without crash |
| Dashboard disconnect during command | Dashboard shows "No Device," commands fail gracefully |
| Financial setup interruption | Items continue processing from where they were |
| Training module revisit after complete | Opens in completed state (video watched, quiz done) |
| QA rejected → toggle to approved view | Both views are available via toggle on Screen 6 |

### Corner Cases

| Case | Expected Behavior |
|------|-------------------|
| Phone with spaces/dashes | Only digits accepted (validation strips non-digits) |
| Email without @ | Validation error: "Enter valid email" |
| Pincode < 6 digits | Validation error: "Enter 6-digit pincode" |
| Same phone registered twice | PHONE_DUPLICATE error on Screen 0 |
| PAN linked to different Aadhaar | KYC_PAN_AADHAAR_UNLINKED error |
| Bank account matches existing partner | DEDUP_FOUND error |
| Device with Android < 11 | TECH_DEVICE_INCOMPATIBLE error |
| Payment exactly at UPI daily limit | ONBOARDFEE_FAILED with UPI limit info |
| UIDAI server down during e-Sign | ESIGN_FAILED with retry + support options |
| Quiz answer selection then change | Previous selection cleared, new selection highlighted |
| All training modules pre-completed | CTA immediately enabled |

---

## 8. Dashboard System

The dashboard is a web-based control panel (`dashboard/index.html`) that connects to the Android app via an ADB bridge server (`dashboard/bridge.py` running on port 8092).

### Dashboard Layout

**Header:**
- Logo: "Wiom CSP — Scenario Dashboard"
- Connection status indicator (green = connected, red = offline)
- Polls every 5 seconds

**Control Buttons (6):**
- Restart App — force-stops and relaunches
- Reset to Screen 0 — resets all state
- Hindi / English — language switch
- Refresh Screenshot — captures current device screen
- Fill All Screens — auto-fills all form fields with demo data
- Empty All Screens — clears all fields

**QA Decision Panel:**
- "QA Approved → Documentation Phase" (green) — moves to Screen 7
- "QA Rejected → Refund" (red) — shows rejection on Screen 6

**Screen Navigator:**
- Grid of 15 buttons (5 columns)
- Each shows screen number + Hindi name + English name
- Active screen highlighted in magenta

**Scenario Simulator:**
- 16 scenario buttons grouped by category
- Click to trigger error state on app
- Active scenario highlighted in red
- "Clear Scenario" button to reset

**Training Module Manager:**
- Edit all 3 training modules (title, subtitle, icon, video URL)
- Add/remove questions with bilingual text and correct answer selection
- "Save to App" pushes configuration to the device

---

## 9. Dashboard-App Interaction

The dashboard communicates with the app via Android broadcast intents sent through ADB.

| Dashboard Action | Intent Sent | App Receiver |
|-----------------|-------------|--------------|
| Navigate to screen | `com.wiom.csp.NAVIGATE` (screen number) | `DashboardReceiver` |
| Trigger scenario | `com.wiom.csp.SCENARIO` (scenario name) | `DashboardReceiver` |
| Change language | `com.wiom.csp.LANG` (hi/en) | `DashboardReceiver` |
| Reset state | `com.wiom.csp.RESET` | `DashboardReceiver` |
| Fill/Empty forms | `com.wiom.csp.FILL` (filled/empty) | `DashboardReceiver` |
| QA decision | `com.wiom.csp.QA` (approved/rejected) | `DashboardReceiver` |
| Update training | `com.wiom.csp.TRAINING` (JSON config) | `DashboardReceiver` |
| Restart app | `adb shell am force-stop` + `am start` | Direct ADB |
| Screenshot | `adb exec-out screencap -p` | Direct ADB |

**Bridge Server:** Python HTTP server on port 8092, translates dashboard HTTP requests into ADB commands.

**Important:** The dashboard is one-way control — it sends commands but does not receive live state from the app. The screen highlighting in the dashboard is based on what was last navigated to, not the app's actual current state.

---

## 10. Business Rules & Constants

### Fee Structure

| Fee | Amount | Refundable? | When |
|-----|--------|-------------|------|
| Registration | Rs.2,000 | Yes (if QA rejects) | Screen 5 |
| Onboarding | Rs.20,000 (incl. GST) | No | Screen 11 |
| **Total Investment** | **Rs.22,000** | | |

### Commission Structure

| Type | Amount | Frequency |
|------|--------|-----------|
| New Connection | Rs.300 per connection | Per event |
| Recharge | Rs.300 | Per event |
| Payout | Bank transfer via RazorpayX | Every Monday |

### SLA Terms

| Metric | Requirement |
|--------|-------------|
| Complaint Resolution | 4 hours |
| Connection Uptime | 95%+ |
| Equipment Care | Partner responsibility |
| Brand Compliance | Mandatory |

### Agreement Terms

| Clause | Value |
|--------|-------|
| Term | 12 months, auto-renewable |
| Termination Notice | 30 days |
| Compliance | DOT + TRAI + Wiom brand guidelines |

### Refund Policy

| Scenario | Refund | Timeline |
|----------|--------|----------|
| QA Rejection | Rs.2,000 | 5-7 working days |
| Payment Timeout (auto) | Full amount | 48 hours |

### Device Requirements

| Spec | Minimum |
|------|---------|
| Android Version | 11 |
| RAM | 3 GB |
| Recommended Devices | Samsung M34, Redmi Note 12, Realme Narzo 60 |

---

## 11. Validation Rules

| Field | Rules | Hindi Error | English Error |
|-------|-------|-------------|---------------|
| Phone | Not blank, exactly 10 digits, digits only | "नंबर डालें" / "10 अंकों का नंबर डालें" / "केवल अंक डालें" | "Enter phone number" / "Enter 10-digit number" / "Enter digits only" |
| OTP | All 4 digits filled | "पूरा OTP डालें" | "Enter complete OTP" |
| Name | Not blank | "नाम डालें" | "Enter name" |
| Email | Not blank, contains @ and . | "ईमेल डालें" / "सही ईमेल डालें" | "Enter email" / "Enter valid email" |
| Pincode | Not blank, exactly 6 digits | "पिनकोड डालें" / "6 अंकों का पिनकोड डालें" | "Enter pincode" / "Enter 6-digit pincode" |

---

## 12. Design System Reference

### Colors

| Token | Hex | Usage |
|-------|-----|-------|
| Primary | `#D9008D` | CTAs, brand accent, active elements |
| Primary Light | `#FFE5F6` | Backgrounds, secondary buttons |
| Text | `#161021` | Body text |
| Text Secondary | `#665E75` | Labels, descriptions |
| Hint | `#A7A1B2` | Placeholders |
| Surface | `#FAF9FC` | Screen backgrounds |
| Positive | `#008043` | Success, verified states |
| Negative | `#D92130` | Errors, rejected states |
| Warning | `#FF8000` | Pending, caution states |
| Info | `#6D17CE` | Informational boxes |
| Header | `#443152` | Status bar, app header |

### Typography (Noto Sans / Noto Sans Devanagari)

| Style | Size | Weight | Line Height |
|-------|------|--------|-------------|
| Headline Large | 24sp | Bold | 32sp |
| Headline Medium | 20sp | Bold | 28sp |
| Title Large | 16sp | Bold | 24sp |
| Title Medium | 14sp | SemiBold | 20sp |
| Body Large | 14sp | Normal | 20sp |
| Body Medium | 12sp | Normal | 16sp |
| Body Small | 10sp | Normal | 14sp |

### Corner Radii

| Size | Value | Usage |
|------|-------|-------|
| Small | 8dp | Tags, small cards |
| Medium | 12dp | Text fields, info boxes |
| Large | 16dp | Cards, buttons |
| Pill | 888dp | Chips, badges |

### Key Component Sizes
- Button height: 48dp
- Header height: 48dp
- OTP box: 48dp x 56dp

---

## 13. QA Test Cases

### Happy Path

| ID | Test | Expected |
|----|------|----------|
| HP-01 | Complete full onboarding (all 15 screens) | Partner reaches Go Live with all 7 green chips |
| HP-02 | Language toggle on every screen | All text switches between Hindi/English |
| HP-03 | Back navigation preserves data | Form fields retain values when going back |
| HP-04 | OTP timer and resend | Timer counts 30→0, resend link appears, new timer starts |
| HP-05 | KYC upload flow (camera + gallery) | Document shows green verified state |
| HP-06 | Bank penny drop verification | Both success cards appear after 2s |
| HP-07 | Financial setup auto-progression | 5 items complete sequentially (800ms each) |
| HP-08 | Training video + quiz | Module marked done after all correct answers |
| HP-09 | Go Live quick actions | Each card opens detail view |
| HP-10 | Filled mode via dashboard | All form fields pre-populated |

### Error Scenarios

| ID | Test | Scenario | Expected |
|----|------|----------|----------|
| ERR-01 | Phone duplicate | PHONE_DUPLICATE | Error card + login/new number CTAs |
| ERR-02 | Wrong OTP | OTP_WRONG | Red boxes, attempt counter |
| ERR-03 | OTP expired | OTP_EXPIRED | Faded boxes, resend/change options |
| ERR-04 | Non-serviceable area | AREA_NOT_SERVICEABLE | Waitlist CTA, blocked |
| ERR-05 | PAN name mismatch | KYC_PAN_MISMATCH | PAN red, details shown, blocked |
| ERR-06 | Aadhaar expired | KYC_AADHAAR_EXPIRED | Orange warning, UIDAI link, blocked |
| ERR-07 | PAN-Aadhaar not linked | KYC_PAN_AADHAAR_UNLINKED | All green but linking error, blocked |
| ERR-08 | Reg fee failed | REGFEE_FAILED | Reassurance + retry |
| ERR-09 | Reg fee timeout | REGFEE_TIMEOUT | Pending status + refresh |
| ERR-10 | Penny drop fail | BANK_PENNYDROP_FAIL | Account field red, fix CTA |
| ERR-11 | Bank name mismatch | BANK_NAME_MISMATCH | Side-by-side comparison, fix CTA |
| ERR-12 | Dedup match | DEDUP_FOUND | Match details, contact support, blocked |
| ERR-13 | e-Sign failed | ESIGN_FAILED | Troubleshooting steps, retry |
| ERR-14 | Device incompatible | TECH_DEVICE_INCOMPATIBLE | Specs comparison, recommended devices, blocked |
| ERR-15 | Onboard fee failed | ONBOARDFEE_FAILED | Reassurance + retry |
| ERR-16 | Quiz failed | TRAINING_QUIZ_FAIL | Score card, review + retake |

### Edge Cases

| ID | Test | Expected |
|----|------|----------|
| EDGE-01 | Empty form CTA tap | CTA stays disabled |
| EDGE-02 | Partial KYC (2/4 docs) | CTA disabled |
| EDGE-03 | QA rejected → toggle approved | Both views accessible |
| EDGE-04 | Bank 3/4 fields filled | Verify button disabled |
| EDGE-05 | Revisit completed training module | Shows completed state |
| EDGE-06 | Remove uploaded document | Returns to upload state |
| EDGE-07 | Timer exactly at 0 | Resend + change links appear |
| EDGE-08 | Uncheck agreement checkbox | e-Sign CTA disabled |
| EDGE-09 | Navigate back then forward | Data preserved |
| EDGE-10 | Scenario trigger then clear | Returns to happy path |

---

## 14. UAT Test Cases

| ID | Persona | Scenario | Flow | Acceptance Criteria |
|----|---------|----------|------|---------------------|
| UAT-01 | Rajesh Kumar, Indore, Individual | **Happy path** | All 15 screens end-to-end | Partner goes live with all chips green |
| UAT-02 | Sunita Devi, Deoghar | **Non-serviceable area** | Phone → OTP → Personal → Location (blocked) | Waitlist joined, informed of next steps |
| UAT-03 | Anil Verma, PAN mismatch | **KYC issue** | Phone → ... → KYC (blocked) → Fix → Retry | Clear error, resolution path shown |
| UAT-04 | Deepak Jain, QA rejected | **Rejection + refund** | Phone → ... → Rs.2K → QA Rejected | Rs.2K refund initiated with tracking |
| UAT-05 | Mohit Patel, UPI limit | **Payment failure** | ... → Rs.20K (failed) → Retry | Helpful error, alternative suggested |
| UAT-06 | Kavita Singh, old device | **Device incompatible** | ... → Tech Review (blocked) | Requirements + recommendations shown |
| UAT-07 | Priya Sharma, quiz fail | **Training fail** | ... → Training → Quiz (fail) → Retry → Pass | Encouraging message, unlimited retries |
| UAT-08 | Any Hindi speaker | **Hindi-first UX** | Complete flow in Hindi | All text meaningful, culturally appropriate |
| UAT-09 | QA team member | **Dashboard QA workflow** | Dashboard → Review → Approve/Reject | Real-time app update via dashboard |
| UAT-10 | Admin | **Training customization** | Dashboard → Edit module → Save → Verify | Custom content appears in app |

---

## 15. What's Prototype vs Production

This app is a **working prototype** with hardcoded data. Here's what needs to change for production:

| Feature | Prototype (Current) | Production (Needed) |
|---------|---------------------|---------------------|
| OTP | Simulated (any 4 digits work) | Real SMS/WhatsApp OTP via API |
| KYC Upload | Simulated progress bar | Real camera/gallery + OCR + API |
| Payments | 2-second delay simulation | Real Razorpay gateway integration |
| Penny Drop | Simulated success | Real Rs.1 bank credit + API |
| Dedup Check | Simulated pass | Real database cross-reference |
| e-Sign | Simulated delay | Real Aadhaar e-Sign integration |
| QA Review | Dashboard button | Backend queue + admin panel |
| Financial Setup | Animated checklist | Real RazorpayX + Zoho + ledger APIs |
| Training Videos | Dark placeholder box | Real video player + content CDN |
| GPS | Hardcoded coordinates | FusedLocationProvider API |
| State | In-memory singleton | Room database + DataStore |
| Navigation | AnimatedContent + integer | Navigation library + deep links |
| Architecture | No ViewModel | MVVM + Hilt DI + Repository pattern |
| Backend | None | REST API + auth + push notifications |
| Analytics | None | Event tracking + funnel analysis |
| Release | Debug APK | ProGuard/R8 + signed release |

---

*This document covers the complete specification of the Wiom CSP Onboarding App v2 — all 15 screens, 16 error scenarios, empty states, edge cases, dashboard interaction, business rules, validation, design tokens, QA cases, and UAT cases.*
