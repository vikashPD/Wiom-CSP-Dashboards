# Wiom CSP Onboarding App

Android app (Kotlin + Jetpack Compose) for the **Wiom Channel Sales Partner (CSP) onboarding flow** — a 15-screen interactive prototype that walks a new partner through registration, verification, payment, training, and go-live.

## Quick Start

```bash
# Prerequisites: Android SDK, Java 17
export ANDROID_HOME=~/Library/Android/sdk

# Build debug APK
./gradlew assembleDebug

# Install on connected device/emulator
adb install app/build/outputs/apk/debug/app-debug.apk

# Launch
adb shell am start -n com.wiom.csp/.MainActivity
```

## Tech Stack

| Layer | Technology |
|-------|-----------|
| Language | Kotlin 2.1.0 |
| UI | Jetpack Compose + Material3 |
| Build | Gradle 8.11.1 (Kotlin DSL) |
| Min SDK | 24 (Android 7.0) |
| Target SDK | 35 |
| Compile SDK | 35 |
| Architecture | Single-activity, composable screens |
| i18n | Runtime bilingual (Hindi/English) via `t()` helper |

## The 15-Screen Onboarding Flow

### Phase 1 — Registration (Screens 0-5)

| # | Screen | What happens |
|---|--------|-------------|
| 0 | **Phone Entry** | Partner enters mobile number (+91). CTA: "OTP भेजें" |
| 1 | **OTP Verification** | 4-digit OTP input, 28s resend timer |
| 2 | **Personal & Business Info** | Name (Aadhaar-linked), email, entity type (Individual/Proprietorship/Partnership/Pvt Ltd/LLP), trade name |
| 3 | **Location** | State, city, pincode, full address, GPS capture (22.71° N, 75.85° E) |
| 4 | **KYC Documents** | PAN Card, Aadhaar Card, GST Certificate — all shown as "Verified ✓" |
| 5 | **Registration Fee ₹2,000** | Payment screen with refund guarantee trust badge |

### Phase 2 — Verification & Documentation (Screens 6-10)

| # | Screen | What happens |
|---|--------|-------------|
| 6 | **QA Investigation** | Checklist of completed steps + waiting state. **Two paths:** Approved → next screen, Rejected → refund screen with reason + ₹2K refund initiated |
| 7 | **Policy + Rate Card** | Commission: ₹300/new connection, ₹300 recharge commission. SLA: 4hr complaint resolution, 95%+ uptime, equipment care, brand compliance |
| 8 | **Bank + Dedup Check** | Bank details (SBI, A/C XXXX4521, IFSC SBIN0001234), penny drop verified, dedup check passed (PAN/Aadhaar/GST/Bank) |
| 9 | **Agreement** | Partner agreement text (scope, responsibilities, commission, term, compliance), ISP/DOT/TRAI verification, Aadhaar e-Sign |
| 10 | **Technical Review** | Device check (Samsung Galaxy M34, Android 14), infra check (Fiber FTTH), shop photo + router photo reviewed |

### Phase 3 — Activation (Screens 11-14)

| # | Screen | What happens |
|---|--------|-------------|
| 11 | **Onboarding Fee ₹20,000** | Fee breakdown: ₹2K reg (paid) + ₹20K onboarding = ₹22K total investment |
| 12 | **Financial Setup** | Backend auto-setup: partner ledger, RazorpayX payout link, Zoho Invoice, trade name lock, TDS/TCS config |
| 13 | **Training Modules** | 3 modules: App Usage (done), SLA & Exposure (done), Money Matters (interactive). Quiz Q&A on TDS and invoicing |
| 14 | **Go Live!** | Celebration screen with 7 status chips, quick actions: Add Customer, View Earnings, Tasks, Training |

## Key Design Decisions

### Commission Structure
- **New Connection:** ₹300 per connection (flat)
- **Recharge Commission:** ₹300 (flat)
- No monthly bonus

### SLA Terms
- Customer complaints: **4-hour resolution**
- Connection uptime: **95%+**
- Equipment care responsibility
- Wiom brand guidelines compliance
- No minimum connections requirement

### Fee Structure
- Registration fee: **₹2,000** (refundable if QA rejected)
- Onboarding fee: **₹20,000** (incl. GST)
- Total investment: **₹22,000**

## Wiom UX Principles (must follow)

1. **Hindi-first** — All text defaults to Hindi. English is the alternate via toggle.
2. **No-blame errors** — Never blame the user. Use reassuring language: "चिंता न करें" (Don't worry).
3. **Benefit-first** — Lead with what the user gains, not what they must do.
4. **Trust badges** — Use 🔒 lock icons and green verification badges to build confidence.
5. **Warm tone** — Conversational, friendly, never bureaucratic.
6. **Family-oriented** — Speak to Bharat users with empathy and respect.

## Bilingual System

The app uses a runtime toggle (not Android resources) for instant language switching:

```kotlin
// util/Strings.kt
object Lang {
    var isHindi by mutableStateOf(true)
    fun toggle() { isHindi = !isHindi }
}

fun t(hi: String, en: String): String = if (Lang.isHindi) hi else en

// Usage in any composable:
Text(t("पार्टनर बनें", "Become a Partner"))
```

The **हि / En** toggle button is in the top bar of every screen.

## Project Structure

```
app/src/main/java/com/wiom/csp/
├── CspApplication.kt          # Application class
├── MainActivity.kt             # Single activity, sets up Compose
├── data/
│   └── OnboardingState.kt      # Global state: currentScreen, qaRejected, screen metadata
├── util/
│   └── Strings.kt              # Bilingual t() helper + Lang toggle
└── ui/
    ├── theme/
    │   ├── Color.kt            # Wiom Design System colors (exact hex tokens)
    │   ├── Theme.kt            # MaterialTheme wiring
    │   ├── Type.kt             # Typography (Noto Sans family)
    │   └── Shape.kt            # Corner radii: 8/12/16/888dp
    ├── components/
    │   └── Common.kt           # 20+ reusable components (WiomButton, WiomCard,
    │                           #   InfoBox, UploadRow, AmountBox, ChecklistItem,
    │                           #   TrustBadge, OtpRow, ModuleCard, etc.)
    └── screens/
        ├── OnboardingHost.kt   # Screen router + progress strip + language toggle
        ├── Phase1Screens.kt    # Screens 0-5 (Phone → Reg Fee)
        ├── Phase2Screens.kt    # Screens 6-10 (QA → Tech Review)
        └── Phase3Screens.kt    # Screens 11-14 (Onboard Fee → Go Live)
```

## Wiom Design System Tokens

### Colors
| Token | Hex | Usage |
|-------|-----|-------|
| Primary | `#D9008D` | CTAs, brand accent |
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

### Corner Radii
- Small: `8dp` (tags, small cards)
- Input: `12dp` (text fields, info boxes)
- Card/Button: `16dp` (cards, CTAs)
- Pill: `888dp` (chips, badges)

### Shadows
- Level 1: `0 1px 3px rgba(0,0,0,0.15)` — cards
- Level 2: `0 2px 6px rgba(0,0,0,0.15)` — elevated cards
- Level 4: `0 4px 12px rgba(0,0,0,0.15)` — modals
- Pink glow: `0 4px 12px rgba(217,43,144,0.3)` — primary CTA

## What to Build Next

This is a **prototype/demo app**. To make it production-ready:

- [ ] Replace hardcoded data with API calls (registration, OTP verify, KYC upload, payments)
- [ ] Add real OTP input handling with auto-advance between digits
- [ ] Integrate payment gateway (Razorpay) for ₹2K and ₹20K payments
- [ ] Add camera/gallery picker for KYC document upload and shop photos
- [ ] Implement Aadhaar e-Sign integration for agreement signing
- [ ] Add GPS location capture using FusedLocationProvider
- [ ] Connect to backend for QA investigation status polling
- [ ] Add push notifications for status updates
- [ ] Implement proper state persistence (Room/DataStore)
- [ ] Add Hilt dependency injection
- [ ] Add error/corner case screens (16 scenarios defined in the HTML prototype)
- [ ] Add loading states and skeleton screens
- [ ] Implement proper back navigation with confirmation dialogs
- [ ] Add analytics/event tracking
- [ ] ProGuard/R8 minification for release builds

## Related Resources

- **HTML Prototype:** `../csp-flow-preview/index.html` — the original interactive prototype with admin dashboard and 16 error scenarios
- **Design System:** Figma file with full Wiom design tokens
- **Package name:** `com.wiom.csp`
