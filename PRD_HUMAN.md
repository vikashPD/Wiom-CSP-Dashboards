# Wiom CSP Onboarding App — Product Requirements Document

**Version:** 3.0 | **Date:** 24 March 2026 | **Status:** Prototype
**Repos:** [vikashPD/Wiom-CSP-Dashboards](https://github.com/vikashPD/Wiom-CSP-Dashboards) | [ashishagrawal-iam/Wiom-csp-onboarding-v2](https://github.com/ashishagrawal-iam/Wiom-csp-onboarding-v2)
**Package:** `com.wiom.csp`

---

## Table of Contents

1. [What This App Does](#1-what-this-app-does)
2. [Who Uses It](#2-who-uses-it)
3. [The Onboarding Journey (18 Screens)](#3-the-onboarding-journey)
4. [Flow Diagram](#4-flow-diagram)
5. [Screen-by-Screen Specification](#5-screen-by-screen-specification)
6. [QA Rejection Reasons (7 Cases)](#6-qa-rejection-reasons)
7. [Error Scenarios (18 Cases)](#7-error-scenarios)
8. [Empty States & Edge Cases](#8-empty-states--edge-cases)
9. [Dashboard System (2 Dashboards)](#9-dashboard-system)
10. [Dashboard-App Interaction](#10-dashboard-app-interaction)
11. [Business Rules & Constants](#11-business-rules--constants)
12. [Validation Rules](#12-validation-rules)
13. [Design System Reference](#13-design-system-reference)
14. [QA Test Cases](#14-qa-test-cases)
15. [UAT Test Cases](#15-uat-test-cases)
16. [What's Prototype vs Production](#16-whats-prototype-vs-production)

---

## 1. What This App Does

This Android app takes a new Channel Sales Partner (CSP) through the complete journey of becoming a Wiom partner — from a pitch screen to going live and serving customers.

The flow has **18 screens** (Pitch + Screens 0-16) across **3 phases**, with **18 documented error scenarios**, **7 QA rejection reasons**, and **2 companion dashboards** (Control + QA Review).

**V3 Changes from V2:**
- Added Pitch screen (welcome/branding)
- Added T&C checkbox on Phone screen (must check to enable OTP)
- Phone validation: error if >10 digits
- Entity type fixed to "Individual" only
- State dropdown with all 36 Indian states/UTs
- Screen order changed: Location → Registration Fee → KYC → Bank → ISP → Shop Photos → Verification
- Added ISP Agreement upload screen
- Added Shop & Equipment Photos screen
- Removed old Agreement screen, replaced with Verification checklist
- Added Technical Assessment as branch point (pass/fail)
- Added Policy Quiz screen (5 questions, pass 3/5)
- Go Live expanded to 9 completion chips
- QA rejection now requires selecting 1 of 7 reasons
- Resolvable rejections show CTA in app to fix the issue
- Dashboard split into Control Dashboard + QA Review Dashboard

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
| **Partner** | New CSP applicant | Goes through the 18-screen onboarding flow on their Android phone |
| **QA Team** | Wiom Business/QA reviewers | Uses the **QA Review Dashboard** to review applications, view submitted data, and approve/reject with reasons |
| **Admin** | Dashboard operator | Uses the **Control Dashboard** to navigate screens, trigger scenarios, manage training modules |
| **System** | Automated backend | Handles OTP, KYC verification, penny drop, dedup check, financial setup |

---

## 3. The Onboarding Journey

### Pitch Screen (Pre-flow)
Welcome screen — "Wiom पार्टनर बनें". CTA: "शुरू करें"

### Phase 1 — Registration (Screens 0-4)

| Screen | Name | Step | What Happens |
|--------|------|------|-------------|
| 0 | Phone Entry | — | Mobile number (+91, 10 digits), T&C checkbox, "नियम व शर्तें पढ़ें" link opens webpage. Error if >10 digits. CTA: "OTP भेजें" (enabled only when 10 digits + T&C checked) |
| 1 | OTP Verification | — | 4-digit OTP with 28-second countdown timer. CTA: "वेरीफाई करें" |
| 2 | Personal Info | Step 1/3 | Name, email, entity type (Individual only), trade name. CTA: "अब लोकेशन बताइए" |
| 3 | Location | Step 2/3 | State (36 Indian states/UTs dropdown), city, pincode, address, GPS capture. CTA: "अब registration शुल्क भरें" |
| 4 | Registration Fee | Step 3/3 | Pay Rs.2,000 (refundable if QA rejects). CTA: "₹2,000 भुगतान करें" |

### Phase 2 — Documentation & Verification (Screens 5-9)

| Screen | Name | Step | What Happens |
|--------|------|------|-------------|
| 5 | KYC Documents | Step 1/5 | Upload PAN, Aadhaar (front+back), GST certificate. CTA: "अब बैंक का विवरण दें" |
| 6 | Bank Details | Step 2/5 | Account holder, bank name, account number, IFSC, penny drop verify. CTA: "अब ISP अनुबंध अपलोड करें" |
| 7 | ISP Agreement | Step 3/5 | DOT compliance info, TRAI guidelines, ISP agreement upload. CTA: "आगे बढ़ें" |
| 8 | Shop & Equipment Photos | Step 4/5 | Shop front photo + router/equipment photo with quality hints. CTA: "सत्यापन के लिए जमा करें" |
| 9 | Verification | Step 5/5 | Checklist of all submitted items. **Branch point:** Approved → Policy, Rejected → shows reason + resolution CTA |

### Phase 3 — Activation (Screens 10-16)

| Screen | Name | Step | What Happens |
|--------|------|------|-------------|
| 10 | Policy & SLA | Step 1/7 | Commission rates (Rs.300), SLA terms, compliance rules. CTA: "समझ गया, आगे बढ़ें" |
| 11 | Onboarding Fee Rs.20K | Step 2/7 | Fee breakdown + payment. CTA: "₹20,000 भुगतान करें" |
| 12 | Technical Assessment | Step 3/7 | Device + infra check. **Branch:** Pass → next, Fail → retry with recommendations |
| 13 | CSP Account Setup | Step 4/7 | Auto-setup: ledger, RazorpayX, Zoho, TDS config. CTA: "Training शुरू करें" |
| 14 | Training Modules | Step 5/7 | 3 video modules with quiz questions |
| 15 | Policy Quiz | Step 6/7 | 5-question quiz on Wiom policies (pass: 3/5+) |
| 16 | Go Live! | Step 7/7 | Celebration with 9 completion chips, quick actions |

---

## 4. Flow Diagram

```
START
  |
  v
[Pitch Screen] ──────────────────────────────────────── CTA: "शुरू करें"
  |
  v
[Screen 0: Phone Entry] ─────error──→ PHONE_DUPLICATE (blocked)
  |  (10 digits + T&C checkbox required)
  |  (>10 digits → error)
  v
[Screen 1: OTP Verify] ──────error──→ OTP_WRONG (retry, 3 max)
  |                       ───error──→ OTP_EXPIRED (resend)
  v
[Screen 2: Personal Info] ──────────── Entity type: Individual only
  |
  v
[Screen 3: Location] ────────error──→ AREA_NOT_SERVICEABLE (waitlist)
  |  (36 Indian states dropdown)
  v
[Screen 4: Rs.2,000 Fee] ───error──→ REGFEE_FAILED (retry)
  |                        ──error──→ REGFEE_TIMEOUT (retry)
  v
[Screen 5: KYC Upload] ─────error──→ KYC_PAN_MISMATCH (blocked)
  |                       ──error──→ KYC_AADHAAR_EXPIRED (blocked)
  |                       ──error──→ KYC_PAN_AADHAAR_UNLINKED (blocked)
  v
[Screen 6: Bank Details] ───error──→ BANK_PENNYDROP_FAIL (retry)
  |                        ──error──→ BANK_NAME_MISMATCH (retry)
  |                        ──error──→ DEDUP_FOUND (blocked)
  v
[Screen 7: ISP Agreement] ──error──→ ISP_DOCUMENT_INVALID (retry)
  |
  v
[Screen 8: Shop Photos]
  |
  v
[Screen 9: Verification] ═══════════ BRANCH POINT ═══════════
  |                                                           |
  |── QA APPROVED                                QA REJECTED ──|
  |   (with reason from 7 options)               (reason shown)|
  v                                                           v
[Screen 10: Policy & SLA]                    RESOLUTION CTA
  |                                          (if resolvable)
  v                                          OR Rs.2K REFUND
[Screen 11: Rs.20,000 Fee] ──error──→ ONBOARDFEE_FAILED (retry)
  |
  v
[Screen 12: Tech Assessment] ═══════ BRANCH POINT ═══════
  |                                                       |
  |── PASS                                      FAIL ─────|
  v                                          (retry with recommendations)
[Screen 13: Account Setup] (auto, no input)
  |
  v
[Screen 14: Training] ──────error──→ TRAINING_QUIZ_FAIL (retry)
  |
  v
[Screen 15: Policy Quiz] ───error──→ POLICY_QUIZ_FAIL (retry)
  |  (pass: 3/5+)
  v
[Screen 16: GO LIVE!] ──── 9 completion chips + quick actions
  |
  END
```

### Error Classification

| Type | Errors | Behavior |
|------|--------|----------|
| **Blocking** | PHONE_DUPLICATE, AREA_NOT_SERVICEABLE, KYC_PAN_MISMATCH, KYC_AADHAAR_EXPIRED, KYC_PAN_AADHAAR_UNLINKED, DEDUP_FOUND, TECH_ASSESSMENT_REJECTED | Cannot proceed — needs external resolution |
| **Retryable** | OTP_WRONG, OTP_EXPIRED, REGFEE_FAILED, REGFEE_TIMEOUT, BANK_PENNYDROP_FAIL, BANK_NAME_MISMATCH, ISP_DOCUMENT_INVALID, ONBOARDFEE_FAILED, TRAINING_QUIZ_FAIL, POLICY_QUIZ_FAIL, VERIFICATION_REJECTED | Can retry immediately or after fixing input |

---

## 5. Screen-by-Screen Specification

### Pitch Screen

**Purpose:** Welcome and branding — first impression before onboarding starts.

**What the partner sees:**
- Wiom branding with handshake emoji
- "Wiom पार्टनर बनें" (Become a Wiom Partner)
- Brief value proposition
- Pink CTA button: "शुरू करें" (Get Started)

**Rules:**
- No form fields, just a CTA to begin
- Navigates to Screen 0 (Phone Entry)

---

### Screen 0: Phone Entry

**Purpose:** Capture mobile number with Terms & Conditions acceptance.

**What the partner sees:**
- Header: "Wiom Partner+"
- Phone input field with +91 country code prefix
- Character count hint showing "X/10 digits"
- Error message if >10 digits entered: "केवल 10 अंकों का नंबर डालें"
- T&C checkbox: "मैं सभी नियम व शर्तें स्वीकार करता/करती हूँ"
- "नियम व शर्तें पढ़ें" link (opens a webpage)
- Pink CTA button: "OTP भेजें" (Send OTP)

**Rules:**
- CTA disabled until BOTH conditions met: exactly 10 digits + T&C checkbox checked
- If user unchecks T&C, CTA becomes disabled again
- Only numeric input allowed
- Error shown if >10 digits entered
- "नियम व शर्तें पढ़ें" opens external webpage

---

### Screen 1: OTP Verification

**Purpose:** Confirm phone ownership via one-time password.

**What the partner sees:**
- Message showing which number OTP was sent to
- 4 input boxes (auto-focus, fills left to right)
- Countdown timer starting at 28 seconds
- After timer expires: "Resend OTP" + "Change Number" links

**Rules:**
- CTA ("वेरीफाई करें") enabled only when all 4 digits filled
- Timer counts down 1 second at a time
- Resend restarts the timer

---

### Screen 2: Personal Info

**Purpose:** Collect identity and business details.

**Step Label:** स्टेप 1/3

**Fields:**
1. **Name (as per Aadhaar)** — Required
2. **Email** — Required, must contain @ and .
3. **Entity Type** — Dropdown with only "Individual" option
4. **Trade Name** — Required; gets locked after registration fee is paid

**CTA:** "अब लोकेशन बताइए" (Now provide location)

**Rules:**
- CTA enabled only when all 4 fields are filled
- Entity type dropdown shows only "Individual"

---

### Screen 3: Location

**Purpose:** Capture shop/office location for service area validation.

**Step Label:** स्टेप 2/3

**Fields:**
1. **State** — Dropdown with all 36 Indian states and union territories
2. **City** — Editable text field
3. **Pincode** — 6 digits only
4. **Full Address** — Editable text field

**Special Elements:**
- GPS badge showing captured coordinates

**CTA:** "अब registration शुल्क भरें" (Now pay registration fee)

**Indian States/UTs in dropdown (36):**
Andhra Pradesh, Arunachal Pradesh, Assam, Bihar, Chhattisgarh, Goa, Gujarat, Haryana, Himachal Pradesh, Jharkhand, Karnataka, Kerala, Madhya Pradesh, Maharashtra, Manipur, Meghalaya, Mizoram, Nagaland, Odisha, Punjab, Rajasthan, Sikkim, Tamil Nadu, Telangana, Tripura, Uttar Pradesh, Uttarakhand, West Bengal, Andaman & Nicobar, Chandigarh, Dadra & Nagar Haveli and Daman & Diu, Delhi, Jammu & Kashmir, Ladakh, Lakshadweep, Puducherry

---

### Screen 4: Registration Fee (Rs.2,000)

**Purpose:** First payment — initiates QA review after documentation.

**Step Label:** स्टेप 3/3

**What the partner sees:**
- Large amount display: Rs.2,000
- Important info card explaining QA review process
- Trust badge: "Full refund if rejected" (with lock icon)

**CTA:** "₹2,000 भुगतान करें"

**On payment:**
- 2-second simulated processing delay
- Trade name gets locked
- Navigates to KYC Documents (Screen 5)

---

### Screen 5: KYC Documents

**Purpose:** Upload and verify identity documents.

**Step Label:** स्टेप 1/5

**4 Documents Required:**
1. PAN Card
2. Aadhaar Card — Front
3. Aadhaar Card — Back
4. GST Certificate

**Upload Flow (3 steps per document):**
1. **Choose Source** — "Take Photo" or "Choose from Gallery"
2. **Preview** — Quality badges + "Save" or "Retake"
3. **Uploading** — Progress bar, then green checkmark

**CTA:** "अब बैंक का विवरण दें" (Now provide bank details)

**Rules:**
- All 4 documents must be uploaded before CTA is enabled
- Each document can be removed and re-uploaded

---

### Screen 6: Bank Details

**Purpose:** Verify partner's bank account via penny drop.

**Step Label:** स्टेप 2/5

**Fields:**
1. Account Holder Name
2. Bank Name
3. Account Number
4. IFSC Code

**Two-Phase Flow:**
1. **Before verification:** CTA: "Penny Drop Verify करें" (enabled when all fields filled)
2. **After verification (2s):** Green success cards (penny drop + dedup check)

**Post-verification CTA:** "अब ISP अनुबंध अपलोड करें" (Now upload ISP agreement)

---

### Screen 7: ISP Agreement (NEW in V3)

**Purpose:** Upload ISP agreement document for DOT compliance.

**Step Label:** स्टेप 3/5

**What the partner sees:**
- Info card: "DOT अनुपालन के लिए अनिवार्य" (Mandatory for DOT compliance)
- "ISP अनुबंध दूरसंचार विभाग की जांच के लिए आवश्यक है।"
- Two badges: "DOT अनुपालन" + "TRAI दिशानिर्देश"
- ISP agreement upload row with "अपलोड" button
- Tip: "साफ़ फ़ोटो लें — सारा टेक्स्ट दिखना चाहिए"

**CTA:** "ISP अनुबंध अपलोड करें"

**Rules:**
- CTA enabled after ISP agreement document uploaded

---

### Screen 8: Shop & Equipment Photos (NEW in V3)

**Purpose:** Capture shop front and equipment photos for verification.

**Step Label:** स्टेप 4/5

**What the partner sees:**
- Shop front photo upload with helper hints
- Router/equipment photo upload
- Quality tips for clear photos

**CTA:** "सत्यापन के लिए जमा करें" (Submit for verification)

**Rules:**
- Both photos must be uploaded before CTA is enabled

---

### Screen 9: Verification (REDESIGNED in V3)

**Purpose:** Major decision point — QA team reviews and approves/rejects.

**Step Label:** स्टेप 5/5

**This screen has THREE views:**

**View A — Pending/Waiting:**
- "Investigation in progress" message with magnifying glass icon
- Checklist showing all completed steps (phone verified, personal info, location, KYC, registration fee, bank, ISP, photos)
- QA Investigation as "waiting"
- Info: "Review may take 2-3 business days"

**View B — Rejected (with reason):**
- Rejection reason displayed (from 7 predefined reasons)
- If resolvable: CTA button to navigate to the relevant screen to fix the issue
- If not resolvable: Rs.2,000 refund information
- Example: "KYC दस्तावेज़ अस्पष्ट" → CTA: "दस्तावेज़ दोबारा अपलोड करें" → navigates to Screen 5

**View C — Approved:**
- Green success message
- Navigates to Policy screen (Screen 10)

**The decision is made by the QA team through the QA Review Dashboard (with mandatory reason selection for rejection).**

---

### Screen 10: Policy & SLA

**Step Label:** स्टेप 1/7

**Shows:**
- Commission: Rs.300/new connection, Rs.300 recharge
- SLA: 4hr complaint resolution, 95%+ uptime
- Equipment care, brand compliance

**CTA:** "समझ गया, आगे बढ़ें"

---

### Screen 11: Onboarding Fee (Rs.20,000)

**Step Label:** स्टेप 2/7

**Shows:**
- Amount: Rs.20,000 (GST inclusive)
- Breakdown: Rs.2K (paid) + Rs.20K = Rs.22K total
- Info: "Training modules will unlock after payment"

**CTA:** "₹20,000 भुगतान करें"

---

### Screen 12: Technical Assessment (REDESIGNED in V3)

**Step Label:** स्टेप 3/7

**Purpose:** Device and infrastructure compatibility check.

**Branch Point:**
- **Pass** → Proceeds to Account Setup (Screen 13)
- **Fail** → Shows failure reason + recommended devices, retry option

**Checks:**
- Device model + Android version
- Internet connection type (Fiber/Cable/Wireless)
- Minimum requirements: Android 11+, 3GB+ RAM

---

### Screen 13: CSP Account Setup (Automated)

**Step Label:** स्टेप 4/7

**Auto-plays through 5 setup items (800ms each):**
1. Partner Ledger Created
2. RazorpayX Payout Link
3. Zoho Invoice Setup
4. Trade Name Locked
5. TDS/TCS Configuration

**CTA:** "Training शुरू करें"

---

### Screen 14: Training Modules

**Step Label:** स्टेप 5/7

**3 Modules:**

| Module | Topic | Questions |
|--------|-------|-----------|
| 1. App Usage | Add customers, check recharge | 2 questions |
| 2. SLA & Exposure | Complaint resolution, uptime | 2 questions |
| 3. Money Matters | Commission, payout frequency | 2 questions |

**Module Flow:** Watch Video → Take Quiz → Complete

---

### Screen 15: Policy Quiz (NEW in V3)

**Step Label:** स्टेप 6/7

**Purpose:** Test partner's understanding of Wiom policies.

**5 questions** covering commission structure, SLA terms, compliance rules.
**Pass threshold:** 3 out of 5 correct.
**Fail:** Score card shown + "Review and retake" option (unlimited retries).

---

### Screen 16: Go Live!

**Step Label:** स्टेप 7/7

**Shows:**
- "Congratulations!" with celebration emoji
- **9 Status Chips** (all green):
  1. Registered
  2. KYC Verified
  3. Bank Verified
  4. ISP Agreement
  5. QA Approved
  6. Policy Accepted
  7. Tech Assessment
  8. Financial Setup
  9. Trained

- **4 Quick Action Cards:** Add Customer, View Earnings, Tasks, Training
- Download prompt for Wiom CSP production app

---

## 6. QA Rejection Reasons

When QA rejects an application via the QA Review Dashboard, they **must** select one of these 7 reasons:

| # | Reason (Hindi) | Reason (English) | Resolvable? | Resolution Screen | App CTA |
|---|---|---|---|---|---|
| 1 | KYC दस्तावेज़ अस्पष्ट / अमान्य | KYC Document Unclear/Invalid | Yes | Screen 5 (KYC) | "दस्तावेज़ दोबारा अपलोड करें" |
| 2 | PAN और आधार में नाम मेल नहीं खाता | PAN-Aadhaar Name Mismatch | Yes | Screen 5 (KYC) | "सही दस्तावेज़ अपलोड करें" |
| 3 | दुकान की फ़ोटो स्वीकार्य नहीं | Shop Photo Not Acceptable | Yes | Screen 8 (Photos) | "फ़ोटो दोबारा अपलोड करें" |
| 4 | ISP अनुबंध अमान्य / अधूरा | ISP Agreement Invalid/Incomplete | Yes | Screen 7 (ISP) | "ISP अनुबंध दोबारा अपलोड करें" |
| 5 | पता सत्यापन विफल | Address Verification Failed | Yes | Screen 3 (Location) | "पता अपडेट करें" |
| 6 | बैंक विवरण मेल नहीं खाता | Bank Details Mismatch | Yes | Screen 6 (Bank) | "बैंक विवरण अपडेट करें" |
| 7 | एरिया में पहले से CSP मौजूद | Duplicate CSP in Area | No | — | "₹2,000 रिफंड प्रक्रिया शुरू" |

**Resolvable rejections:** App shows the reason + a CTA button that navigates the partner directly to the screen where they can fix the issue. After fixing, the application is re-submitted for QA review.

**Non-resolvable rejection (Reason 7):** App shows refund information (Rs.2,000 refund in 5-7 working days).

---

## 7. Error Scenarios (18 Cases)

### Registration & OTP Errors

#### PHONE_DUPLICATE (Screen 0)
**When:** Phone number already registered.
**Shows:** Error card + "Send OTP with new number" or "Login" CTAs.
**Outcome:** Blocked.

#### OTP_WRONG (Screen 1)
**When:** Incorrect OTP digits.
**Shows:** Red boxes, "2 attempts remaining."
**Outcome:** Retryable (3 attempts max).

#### OTP_EXPIRED (Screen 1)
**When:** Timer ran out.
**Shows:** Faded boxes, "Send new OTP" or "Change Number."
**Outcome:** Retryable.

### Location & KYC Errors

#### AREA_NOT_SERVICEABLE (Screen 3)
**When:** Pincode not in service area.
**Shows:** "Join the waitlist!" with count.
**Outcome:** Blocked.

#### KYC_PAN_MISMATCH (Screen 5)
**When:** PAN name differs from Aadhaar.
**Shows:** PAN row red, both names shown.
**Outcome:** Blocked.

#### KYC_AADHAAR_EXPIRED (Screen 5)
**When:** Aadhaar address outdated.
**Shows:** Orange warning, UIDAI link.
**Outcome:** Blocked.

#### KYC_PAN_AADHAAR_UNLINKED (Screen 5)
**When:** PAN-Aadhaar not linked.
**Shows:** Linking error, incometax.gov.in link.
**Outcome:** Blocked.

### Payment Errors

#### REGFEE_FAILED (Screen 4)
**When:** Rs.2K payment declined.
**Shows:** "No money deducted" + retry.
**Outcome:** Retryable.

#### REGFEE_TIMEOUT (Screen 4)
**When:** Payment gateway timeout.
**Shows:** Pending status + auto-refund info.
**Outcome:** Retryable.

#### ONBOARDFEE_FAILED (Screen 11)
**When:** Rs.20K payment declined.
**Shows:** UPI limit info + alternative methods.
**Outcome:** Retryable.

### Bank & Documentation Errors

#### BANK_PENNYDROP_FAIL (Screen 6)
**When:** Penny drop credit failed.
**Shows:** Account field red, fix CTA.
**Outcome:** Retryable.

#### BANK_NAME_MISMATCH (Screen 6)
**When:** Bank holder name differs from KYC.
**Shows:** Side-by-side name comparison.
**Outcome:** Retryable.

#### DEDUP_FOUND (Screen 6)
**When:** Existing partner with same PAN/Bank.
**Shows:** Match details, contact support.
**Outcome:** Blocked.

#### ISP_DOCUMENT_INVALID (Screen 7)
**When:** ISP agreement document is invalid or unreadable.
**Shows:** Error message with re-upload CTA.
**Outcome:** Retryable.

### Verification & Tech Errors

#### VERIFICATION_REJECTED (Screen 9)
**When:** QA rejects the application (with reason).
**Shows:** Rejection reason + resolution CTA (if resolvable) or refund info.
**Outcome:** Retryable (if resolvable) or Blocked (if duplicate area).

#### TECH_ASSESSMENT_REJECTED (Screen 12)
**When:** Device/infra doesn't meet requirements.
**Shows:** Specs comparison + recommended devices.
**Outcome:** Blocked — must upgrade device.

### Training & Quiz Errors

#### TRAINING_QUIZ_FAIL (Screen 14)
**When:** Failed training quiz.
**Shows:** Score card + "Review and retake."
**Outcome:** Retryable (unlimited).

#### POLICY_QUIZ_FAIL (Screen 15)
**When:** Failed policy quiz (<3/5).
**Shows:** Score card + review option.
**Outcome:** Retryable (unlimited).

---

## 8. Empty States & Edge Cases

### Empty States

| Screen | Empty State | Behavior |
|--------|-------------|----------|
| 0 | No phone + T&C unchecked | CTA disabled |
| 0 | 10 digits + T&C unchecked | CTA disabled |
| 0 | <10 digits + T&C checked | CTA disabled |
| 1 | No OTP digits | CTA disabled |
| 2 | Missing any field | CTA disabled |
| 3 | No state selected | CTA still enabled |
| 5 | No documents uploaded | CTA disabled |
| 6 | Bank 3/4 fields | Verify button disabled |
| 7 | No ISP document | CTA disabled |
| 8 | Missing any photo | CTA disabled |
| 14 | No modules done | CTA disabled |
| 15 | Quiz not attempted | CTA disabled |

### Edge Cases

| Case | Expected Behavior |
|------|-------------------|
| Phone >10 digits | Error: "केवल 10 अंकों का नंबर डालें" |
| T&C checkbox toggled | CTA enables/disables accordingly |
| QA rejected with resolvable reason | CTA navigates to fix screen |
| QA rejected with non-resolvable reason | Refund info shown |
| Navigate back then forward | All data preserved |
| Scenario trigger then clear | Returns to happy path |
| Policy quiz 2/5 score | Fail, review + retake shown |
| Policy quiz 3/5 score | Pass, proceeds to GoLive |
| Tech assessment fail | Retry with device recommendations |

---

## 9. Dashboard System (2 Dashboards)

### Dashboard 1: Control Dashboard (`dashboard/control.html`)

**Purpose:** Admin tool for navigating, testing, and controlling the app.

**Sections:**
1. **Control Buttons** — Restart App, Reset, Hindi/English toggle, Fill/Empty data, Screenshot
2. **Screen Navigation** — Grid of 18 screen tiles (Pitch + 0-16), active screen highlighted
3. **Scenario Simulator** — 18 error scenario buttons grouped by category, with "Clear Scenario"
4. **Training Module Manager** — Edit 3 modules (title, questions, answers), "Save to App"

### Dashboard 2: QA Review Dashboard (`dashboard/qa-review.html`)

**Purpose:** QA team tool for reviewing and deciding on CSP applications.

**Layout:** Split panel — Application List (left) + Detail View (right)

**Left Panel — Application List:**
- Filter chips: All | Pending | Approved | Rejected (with counts)
- Search by name, phone, city
- Each card: Name, Phone, City, Status badge, KYC count, time ago
- "LIVE" badge on emulator-connected application

**Right Panel — Application Detail (on click):**
- Applicant name, ID, trade name, submission date
- **Approve/Reject CTAs** (for Pending applications)
- **Mandatory reason selection** on Reject (7 reasons shown as selectable list)
- Current decision badge + "Change Decision" button (for decided applications)
- Summary strip: Phone, Entity Type, KYC count, Rs.2K status, City
- Collapsible sections: Personal Info, Location, KYC Documents (with view option), Registration Fee

**Key Features:**
- Decisions persist in localStorage
- Reversible — any decision can be changed back to Pending
- Live device sends Approve/Reject to emulator via bridge
- Auto-refreshes live app data every 5 seconds
- 7 mock applications + 1 live device application

---

## 10. Dashboard-App Interaction

| Dashboard Action | Intent/Endpoint | App Receiver |
|-----------------|----------------|--------------|
| Navigate to screen | `com.wiom.csp.NAVIGATE` (screen number) | `DashboardReceiver` |
| Navigate to Pitch | `com.wiom.csp.NAVIGATE` (screen: -1) | `DashboardReceiver` |
| Trigger scenario | `com.wiom.csp.SCENARIO` (scenario name) | `DashboardReceiver` |
| Change language | `com.wiom.csp.LANG` (hi/en) | `DashboardReceiver` |
| Reset state | `com.wiom.csp.RESET` | `DashboardReceiver` |
| Fill/Empty forms | `com.wiom.csp.FILL` (filled/empty) | `DashboardReceiver` |
| QA decision | `com.wiom.csp.QA` (approved/rejected + reason) | `DashboardReceiver` |
| Update training | `com.wiom.csp.TRAINING` (JSON config) | `DashboardReceiver` |
| Dump state | `com.wiom.csp.DUMP_STATE` | `DashboardReceiver` → writes state.json |
| Restart app | `adb shell am force-stop` + `am start` | Direct ADB |
| Screenshot | `adb exec-out screencap -p` | Direct ADB |
| Read state | `adb shell run-as com.wiom.csp cat state.json` | Direct ADB |

**Bridge Server:** Python HTTP server on port 8092. Uses `run-as` for reading app-private state files.

---

## 11. Business Rules & Constants

### Fee Structure

| Fee | Amount | Refundable? | When |
|-----|--------|-------------|------|
| Registration | Rs.2,000 | Yes (if QA rejects) | Screen 4 |
| Onboarding | Rs.20,000 (incl. GST) | No | Screen 11 |
| **Total** | **Rs.22,000** | | |

### Commission Structure

| Type | Amount | Frequency |
|------|--------|-----------|
| New Connection | Rs.300 | Per event |
| Recharge | Rs.300 | Per event |
| Payout | Bank transfer (RazorpayX) | Every Monday |

### SLA Terms

| Metric | Requirement |
|--------|-------------|
| Complaint Resolution | 4 hours |
| Connection Uptime | 95%+ |
| Equipment Care | Partner responsibility |
| Brand Compliance | Mandatory |

### Refund Policy

| Scenario | Refund | Timeline |
|----------|--------|----------|
| QA Rejection | Rs.2,000 | 5-7 working days |
| Payment Timeout | Full amount | 48 hours |

### Device Requirements

| Spec | Minimum |
|------|---------|
| Android Version | 11 |
| RAM | 3 GB |
| Recommended | Samsung M34, Redmi Note 12, Realme Narzo 60 |

---

## 12. Validation Rules

| Field | Rules | Error (Hindi) | Error (English) |
|-------|-------|---------------|-----------------|
| Phone | 10 digits exactly, numeric only | "10 अंकों का नंबर डालें" | "Enter 10-digit number" |
| Phone >10 | Error if exceeds 10 digits | "केवल 10 अंकों का नंबर डालें" | "Only 10 digits allowed" |
| T&C | Must be checked | CTA disabled | CTA disabled |
| OTP | All 4 digits | "पूरा OTP डालें" | "Enter complete OTP" |
| Name | Not blank | "नाम डालें" | "Enter name" |
| Email | Contains @ and . | "सही ईमेल डालें" | "Enter valid email" |
| Pincode | 6 digits exactly | "6 अंकों का पिनकोड" | "Enter 6-digit pincode" |
| State | Must select from dropdown | Required | Required |

---

## 13. Design System Reference

### Colors

| Token | Hex | Usage |
|-------|-----|-------|
| Primary | `#D9008D` | CTAs, brand accent |
| Primary Light | `#FFE5F6` | Backgrounds |
| Text | `#161021` | Body text |
| Text Secondary | `#665E75` | Labels |
| Hint | `#A7A1B2` | Placeholders |
| Surface | `#FAF9FC` | Screen backgrounds |
| Positive | `#008043` | Success, verified |
| Negative | `#D92130` | Errors, rejected |
| Warning | `#FF8000` | Pending, caution |
| Info | `#6D17CE` | Info boxes |
| Header | `#443152` | Status bar, app header |

### Corner Radii
- Small: 8dp (tags)
- Medium: 12dp (inputs)
- Large: 16dp (cards, buttons)
- Pill: 888dp (chips, badges)

---

## 14. QA Test Cases

### Happy Path

| ID | Test | Expected |
|----|------|----------|
| HP-01 | Complete full onboarding (18 screens) | Partner reaches Go Live with 9 green chips |
| HP-02 | Language toggle on every screen | All text switches Hindi/English |
| HP-03 | T&C checkbox toggle | CTA enables/disables correctly |
| HP-04 | Phone >10 digits | Error shown |
| HP-05 | State dropdown selection | All 36 states available |
| HP-06 | ISP agreement upload | Document shown as uploaded |
| HP-07 | Shop photos upload | Both photos captured |
| HP-08 | QA reject with reason | Reason shown in app with resolution CTA |
| HP-09 | QA reject → fix → resubmit | Re-enters QA review |
| HP-10 | Policy quiz pass (3/5) | Proceeds to GoLive |
| HP-11 | Policy quiz fail (2/5) | Score shown, retry available |
| HP-12 | Tech assessment pass/fail | Correct branch taken |

### Error Scenarios

| ID | Scenario | Expected |
|----|----------|----------|
| ERR-01 | PHONE_DUPLICATE | Error + login/new number CTAs |
| ERR-02 | OTP_WRONG | Red boxes, attempt counter |
| ERR-03 | OTP_EXPIRED | Faded boxes, resend |
| ERR-04 | AREA_NOT_SERVICEABLE | Waitlist, blocked |
| ERR-05 | KYC_PAN_MISMATCH | PAN red, blocked |
| ERR-06 | KYC_AADHAAR_EXPIRED | Orange warning, blocked |
| ERR-07 | KYC_PAN_AADHAAR_UNLINKED | Linking error, blocked |
| ERR-08 | REGFEE_FAILED | Retry |
| ERR-09 | REGFEE_TIMEOUT | Pending + refresh |
| ERR-10 | BANK_PENNYDROP_FAIL | Fix + retry |
| ERR-11 | BANK_NAME_MISMATCH | Name comparison + fix |
| ERR-12 | DEDUP_FOUND | Blocked, support |
| ERR-13 | ISP_DOCUMENT_INVALID | Re-upload |
| ERR-14 | VERIFICATION_REJECTED | Reason + resolution CTA |
| ERR-15 | TECH_ASSESSMENT_REJECTED | Device recommendations |
| ERR-16 | ONBOARDFEE_FAILED | Retry |
| ERR-17 | TRAINING_QUIZ_FAIL | Review + retake |
| ERR-18 | POLICY_QUIZ_FAIL | Score + retake |

---

## 15. UAT Test Cases

| ID | Persona | Scenario | Acceptance Criteria |
|----|---------|----------|---------------------|
| UAT-01 | Rajesh, Indore, Individual | Happy path (18 screens) | Go Live with 9 green chips |
| UAT-02 | Sunita, Deoghar | Non-serviceable area | Waitlist joined |
| UAT-03 | Anil, PAN mismatch | KYC rejection | QA rejects with reason 2, app shows fix CTA |
| UAT-04 | Deepak, QA rejected (area) | Non-resolvable rejection | Rs.2K refund info shown |
| UAT-05 | Mohit, UPI limit | Payment failure | Alternative method suggested |
| UAT-06 | Kavita, old device | Tech assessment fail | Device recommendations shown |
| UAT-07 | Priya, quiz fail | Policy quiz fail | Score + unlimited retries |
| UAT-08 | Hindi speaker | Hindi-first UX | All text culturally appropriate |
| UAT-09 | QA reviewer | Dashboard QA workflow | Reject with reason → app shows reason + fix CTA |
| UAT-10 | Admin | Control dashboard | Navigate all 18 screens, trigger all 18 scenarios |

---

## 16. What's Prototype vs Production

| Feature | Prototype (Current) | Production (Needed) |
|---------|---------------------|---------------------|
| OTP | Simulated (any 4 digits) | Real SMS/WhatsApp OTP |
| KYC Upload | Simulated progress | Real camera/gallery + OCR |
| Payments | 2-second delay | Real Razorpay gateway |
| Penny Drop | Simulated success | Real Rs.1 bank credit |
| Dedup Check | Simulated pass | Real database cross-reference |
| QA Review | Dashboard + localStorage | Backend queue + admin panel + database |
| Rejection Reasons | 7 predefined, localStorage | Database + custom reasons + audit trail |
| ISP Agreement | Simulated upload | Real document upload + DOT verification |
| Tech Assessment | Simulated check | Real device API + connectivity test |
| Policy Quiz | 5 hardcoded questions | Question bank + randomization |
| Financial Setup | Animated checklist | Real RazorpayX + Zoho APIs |
| Training Videos | Dark placeholder | Real video player + CDN |
| GPS | Hardcoded coordinates | FusedLocationProvider |
| T&C | Checkbox only | Real T&C document + versioning |
| State Dropdown | 36 states | API-driven with city/pincode lookup |
| State Management | In-memory singleton | Room + DataStore |
| Architecture | No ViewModel | MVVM + Hilt + Repository |
| Backend | bridge.py (ADB) | REST API + auth + push notifications |
| Analytics | None | Event tracking + funnel analysis |

---

*This document covers the complete specification of the Wiom CSP Onboarding App V3 — 18 screens, 7 QA rejection reasons, 18 error scenarios, 2 dashboards, business rules, validation, design tokens, QA cases, and UAT cases.*
