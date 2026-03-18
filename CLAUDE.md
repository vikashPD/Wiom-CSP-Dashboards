# CLAUDE.md — Context for AI-assisted development

## What is this project?

Wiom CSP (Channel Sales Partner) onboarding Android app — a 15-screen flow that takes a new internet service partner from registration to going live. Built as an interactive prototype for stakeholder review and developer handoff.

## Build & Run

```bash
export ANDROID_HOME=~/Library/Android/sdk
./gradlew assembleDebug
# APK at: app/build/outputs/apk/debug/app-debug.apk
```

## Architecture

- **Single Activity** (`MainActivity`) → `OnboardingHost` composable → 15 screen composables
- **No ViewModel/Repository pattern yet** — state is in `OnboardingState` singleton (intentional for prototype speed)
- **No navigation library** — `OnboardingHost` uses `AnimatedContent` keyed on `currentScreen` integer
- **Bilingual via `t(hi, en)` function** — NOT Android string resources. This is intentional for instant toggle without activity recreation.

## Wiom UX Rules (MUST follow)

1. **Hindi-first**: Default language is Hindi. English is secondary.
2. **No-blame errors**: Never say "you entered wrong X". Say "X मेल नहीं खाता" (X doesn't match).
3. **Benefit-first**: Lead with what user gains. "QA investigation शुरू होगी" not "You must wait".
4. **Trust badges**: Always show 🔒 and ✓ for verified/secure states.
5. **Warm tone**: "चिंता न करें" (don't worry), "बधाई हो" (congratulations).
6. **No fear language**: Never use words like "penalty", "punishment", "disconnection".

## Key Business Values (hardcoded in Screen 7)

- New Connection commission: ₹300/connection
- Recharge commission: ₹300
- Registration fee: ₹2,000 (refundable)
- Onboarding fee: ₹20,000
- SLA: 4hr complaint resolution, 95%+ uptime

## Design Tokens

All Wiom colors are in `ui/theme/Color.kt`. The primary brand color is `#D9008D` (magenta pink).
Corner radii: 8dp (small), 12dp (input), 16dp (card/button), 888dp (pill).

## Screen Flow

0:Phone → 1:OTP → 2:Personal → 3:Location → 4:KYC → 5:₹2K Fee → 6:QA (branch: approved/rejected) → 7:Policy → 8:Bank+Dedup → 9:Agreement → 10:TechReview → 11:₹20K Fee → 12:FinancialSetup → 13:Training → 14:GoLive

## File Layout

- `Phase1Screens.kt` — Screens 0-5 (registration)
- `Phase2Screens.kt` — Screens 6-10 (verification & documentation)
- `Phase3Screens.kt` — Screens 11-14 (activation)
- `Common.kt` — All reusable components (20+ composables)
- `OnboardingHost.kt` — Screen router, progress bar, language toggle

## What NOT to change without checking

- Commission values (₹300/₹300) — business decision
- SLA terms (4hr, 95%+) — contractual
- Fee amounts (₹2K, ₹20K) — pricing decision
- Hindi copy — reviewed and approved
- Color tokens — from Figma design system, not arbitrary
