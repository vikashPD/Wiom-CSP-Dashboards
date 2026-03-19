# Wiom CSP Onboarding App — Product Requirements Document (AI-Agent Format)

> **Version:** 2.0
> **Date:** 2026-03-19
> **Status:** Prototype (hardcoded data, no backend)
> **Package:** `com.wiom.csp`
> **Repo:** https://github.com/ashishagrawal-iam/Wiom-csp-onboarding-v2

---

## SYSTEM_OVERVIEW

```yaml
product: Wiom CSP Onboarding App
purpose: End-to-end onboarding flow for new Channel Sales Partners (CSPs)
platform: Android (Kotlin + Jetpack Compose + Material3)
min_sdk: 24 (Android 7.0)
target_sdk: 35
architecture: Single-activity, composable screens, no ViewModel (prototype)
navigation: AnimatedContent keyed on currentScreen integer (0-14)
language: Bilingual Hindi/English via runtime t(hi, en) toggle
state_management: Global singleton OnboardingState with mutableStateOf
build: Gradle 8.11.1 (Kotlin DSL), Kotlin 2.1.0
```

---

## ACTORS

```yaml
actors:
  - id: PARTNER
    description: New CSP applicant going through onboarding
    actions: Fill forms, upload documents, make payments, complete training

  - id: QA_TEAM
    description: Wiom Business/QA team reviewing partner applications
    actions: Approve or reject applications after registration fee
    interface: Dashboard (dashboard/index.html + bridge.py)

  - id: SYSTEM
    description: Automated backend processes
    actions: OTP generation, KYC auto-verify, penny drop, dedup check, financial setup, training module delivery

  - id: ADMIN
    description: Dashboard operator
    actions: Navigate screens, trigger scenarios, control language, manage training modules, approve/reject QA
    interface: Dashboard on localhost:8092 via ADB bridge
```

---

## FLOW_PHASES

```yaml
phases:
  - id: PHASE_1
    name: Registration
    screens: [0, 1, 2, 3, 4, 5]
    description: Partner identity capture, verification, and registration fee collection

  - id: PHASE_2
    name: Verification & Documentation
    screens: [6, 7, 8, 9, 10]
    description: QA review, policy acknowledgment, bank verification, agreement signing, tech review

  - id: PHASE_3
    name: Activation
    screens: [11, 12, 13, 14]
    description: Onboarding fee, financial backend setup, training, go-live
```

---

## SCREENS

### SCREEN_0: Phone Entry

```yaml
id: SCREEN_0
phase: PHASE_1
title_hi: "मोबाइल नंबर"
title_en: "Mobile Number"
step_label: null
purpose: Capture partner phone number for OTP-based authentication

fields:
  - id: phone_number
    type: text_input
    prefix: "+91"
    max_length: 10
    keyboard: numeric
    placeholder_hi: "नया नंबर डालें"
    placeholder_en: "Enter your number"
    validation:
      - rule: not_blank
        error_hi: "नंबर डालें"
        error_en: "Enter phone number"
      - rule: length_exact(10)
        error_hi: "10 अंकों का नंबर डालें"
        error_en: "Enter 10-digit number"
      - rule: digits_only
        error_hi: "केवल अंक डालें"
        error_en: "Enter digits only"

display_elements:
  - emoji: "🤝"
  - title_hi: "पार्टनर बनें"
  - title_en: "Become a Partner"
  - subtitle_hi: "Wiom के साथ अपना बिज़नेस शुरू करें"
  - subtitle_en: "Start your business with Wiom"
  - character_count: "X/10 अंक" (shown when < 10 digits)
  - info_box:
      icon: "ℹ️"
      text_hi: "OTP आपके नंबर पर भेजा जाएगा"
      text_en: "OTP will be sent to your number"
      type: INFO

cta:
  text_hi: "OTP भेजें"
  text_en: "Send OTP"
  enabled_when: phone_number.length == 10
  action: SEND_OTP
  next_screen: 1

error_scenarios:
  - PHONE_DUPLICATE
```

### SCREEN_0_ERROR: PHONE_DUPLICATE

```yaml
id: PHONE_DUPLICATE
trigger: Phone number already registered in system
screen: 0

display:
  emoji: "📱"
  title_hi: "यह नंबर पहले से रजिस्टर्ड है"
  title_en: "This number is already registered"

  error_card:
    type: error
    icon: "📱"
    title_hi: "अकाउंट मौजूद है"
    title_en: "Account Exists"
    message_hi: "इस नंबर से पहले से एक अकाउंट बना हुआ है। आप लॉगिन कर सकते हैं या नए नंबर से रजिस्टर कर सकते हैं।"
    message_en: "An account already exists with this number. You can login or register with a new number."

  info_box:
    icon: "🔒"
    text_hi: "आपका पुराना डेटा सुरक्षित है"
    text_en: "Your existing data is safe"
    type: SUCCESS

ctas:
  - text_hi: "नए नंबर से OTP भेजें"
    text_en: "Send OTP with new number"
    type: primary
    action: CLEAR_AND_RETRY
  - text_hi: "लॉगिन करें"
    text_en: "Login"
    type: secondary
    action: NAVIGATE_TO_LOGIN
```

### SCREEN_1: OTP Verification

```yaml
id: SCREEN_1
phase: PHASE_1
title_hi: "OTP वेरीफाई"
title_en: "Verify OTP"
purpose: Verify phone ownership via 4-digit OTP

fields:
  - id: otp_digits
    type: otp_input
    length: 4
    keyboard: numeric
    auto_focus: true
    validation:
      - rule: all_digits_filled
        error_hi: "पूरा OTP डालें"
        error_en: "Enter complete OTP"

display_elements:
  - subtitle_hi: "+91 XXXXX XXXXX पर भेजा गया"
  - subtitle_en: "Sent to +91 XXXXX XXXXX"

timer:
  initial_seconds: 30
  countdown_interval_ms: 1000
  on_expire:
    show_resend_link: true
    resend_text_hi: "OTP दोबारा भेजें"
    resend_text_en: "Resend OTP"
    resend_action: RESTART_TIMER(30)
    show_change_number: true
    change_number_text_hi: "नंबर बदलें"
    change_number_text_en: "Change Number"
    change_number_action: GO_BACK

otp_box_states:
  empty: { border: dark, background: white }
  focused: { border: pink, background: white, cursor: blinking }
  filled: { border: green, background: green_light }
  error: { border: red, background: white }
  expired: { border: gray, background: white, opacity: 0.5 }

cta:
  text_hi: "वेरीफाई करें"
  text_en: "Verify"
  enabled_when: all_4_digits_filled
  action: VERIFY_OTP
  next_screen: 2

error_scenarios:
  - OTP_WRONG
  - OTP_EXPIRED
```

### SCREEN_1_ERROR: OTP_WRONG

```yaml
id: OTP_WRONG
trigger: Incorrect OTP entered
screen: 1

display:
  otp_boxes: { state: error, border: red, content: empty }
  error_card:
    type: error
    icon: "❌"
    title_hi: "गलत OTP"
    title_en: "Wrong OTP"
    message_hi: "कृपया दोबारा कोशिश करें — 2 प्रयास बाकी हैं"
    message_en: "Please try again — 2 attempts remaining"
  resend_link:
    text_hi: "OTP दोबारा भेजें"
    text_en: "Resend OTP"

cta:
  text_hi: "वेरीफाई करें"
  text_en: "Verify"
  action: RETRY_OTP

business_rules:
  max_attempts: 3
  lockout_after_max: true
```

### SCREEN_1_ERROR: OTP_EXPIRED

```yaml
id: OTP_EXPIRED
trigger: OTP validity period exceeded
screen: 1

display:
  otp_boxes: { state: expired, border: gray, opacity: 0.5 }
  error_card:
    type: warning
    icon: "⏰"
    title_hi: "OTP expired हो गया"
    title_en: "OTP has expired"
    message_hi: "चिंता न करें — नया OTP भेजें"
    message_en: "Don't worry — send a new OTP"

ctas:
  - text_hi: "नया OTP भेजें"
    text_en: "Send new OTP"
    type: primary
    action: RESEND_OTP
  - text_hi: "नंबर बदलें"
    text_en: "Change Number"
    type: secondary
    action: GO_BACK
```

### SCREEN_2: Personal & Business Info

```yaml
id: SCREEN_2
phase: PHASE_1
title_hi: "व्यक्तिगत जानकारी"
title_en: "Personal Information"
step_label: "स्टेप 1/4 | Step 1/4"
purpose: Capture partner identity and business details

fields:
  - id: personal_name
    type: text_input
    label_hi: "नाम (आधार अनुसार)"
    label_en: "Name (as per Aadhaar)"
    placeholder_hi: "उदाहरण: राजेश कुमार"
    placeholder_en: "Example: Rajesh Kumar"
    required: true
    validation:
      - rule: not_blank
        error_hi: "नाम डालें"
        error_en: "Enter name"

  - id: personal_email
    type: email_input
    label_hi: "ईमेल"
    label_en: "Email"
    placeholder_hi: "उदाहरण: rajesh@email.com"
    placeholder_en: "Example: rajesh@email.com"
    required: true
    validation:
      - rule: not_blank
        error_hi: "ईमेल डालें"
        error_en: "Enter email"
      - rule: contains_at_and_dot
        error_hi: "सही ईमेल डालें"
        error_en: "Enter valid email"

  - id: entity_type
    type: dropdown
    label_hi: "व्यवसाय प्रकार"
    label_en: "Entity Type"
    options: ["Individual", "Proprietorship", "Partnership", "Private Limited", "LLP"]
    required: true

  - id: trade_name
    type: text_input
    label_hi: "ट्रेड नाम"
    label_en: "Trade Name"
    placeholder_hi: "उदाहरण: राजेश टेलीकॉम"
    placeholder_en: "Example: Rajesh Telecom"
    required: true
    lock_after: REGFEE_PAID

cta:
  text_hi: "अब लोकेशन बताइए"
  text_en: "Next: Location"
  enabled_when: all_fields_filled
  next_screen: 3

error_scenarios: []
```

### SCREEN_3: Location

```yaml
id: SCREEN_3
phase: PHASE_1
title_hi: "लोकेशन जानकारी"
title_en: "Location Information"
step_label: "स्टेप 2/4 | Step 2/4"
purpose: Capture shop/office location for service area validation

fields:
  - id: state
    type: text_input
    value: "Madhya Pradesh"
    read_only: true

  - id: city
    type: text_input
    placeholder_hi: "शहर"
    placeholder_en: "City"
    required: false

  - id: pincode
    type: text_input
    max_length: 6
    keyboard: numeric
    required: false
    validation:
      - rule: not_blank
        error_hi: "पिनकोड डालें"
        error_en: "Enter pincode"
      - rule: length_exact(6)
        error_hi: "6 अंकों का पिनकोड डालें"
        error_en: "Enter 6-digit pincode"

  - id: address
    type: text_input
    placeholder_hi: "पूरा पता"
    placeholder_en: "Full Address"
    required: false

display_elements:
  - gps_badge:
      icon: "🏙️"
      text_hi: "GPS कैप्चर हुआ"
      text_en: "GPS Captured"
      coordinates: "22.71° N, 75.85° E"

cta:
  text_hi: "अब KYC दस्तावेज़ दें"
  text_en: "Next: KYC Documents"
  enabled_when: true  # all fields optional
  next_screen: 4

error_scenarios:
  - AREA_NOT_SERVICEABLE
```

### SCREEN_3_ERROR: AREA_NOT_SERVICEABLE

```yaml
id: AREA_NOT_SERVICEABLE
trigger: Pincode/city not in Wiom service area
screen: 3

display:
  error_card:
    type: warning
    icon: "📍"
    title_hi: "यह एरिया अभी सर्विसेबल नहीं है"
    title_en: "This area is not serviceable yet"
    message_hi: "हम जल्द ही इस एरिया में आ रहे हैं। Waitlist में जुड़ें और पहले मौका पाएं!"
    message_en: "We're coming to this area soon. Join the waitlist and get first opportunity!"
  info_box:
    icon: "📋"
    text_hi: "Waitlist में 47 लोग पहले से हैं"
    text_en: "47 people already on waitlist"

ctas:
  - text_hi: "Waitlist में जुड़ें"
    text_en: "Join Waitlist"
    type: primary
    action: JOIN_WAITLIST
  - text_hi: "दूसरा पिनकोड डालें"
    text_en: "Try a different pincode"
    type: secondary
    action: CLEAR_PINCODE

blocks_progression: true
```

### SCREEN_4: KYC Documents

```yaml
id: SCREEN_4
phase: PHASE_1
title_hi: "KYC दस्तावेज़"
title_en: "KYC Documents"
step_label: "स्टेप 3/4 | Step 3/4"
purpose: Upload and auto-verify identity documents

documents:
  - id: pan_card
    icon: "🪪"
    label_hi: "PAN Card अपलोड करें"
    label_en: "Upload PAN Card"
    required: true

  - id: aadhaar_front
    icon: "📄"
    label_hi: "आधार कार्ड — सामने अपलोड करें"
    label_en: "Upload Aadhaar Card — Front"
    required: true

  - id: aadhaar_back
    icon: "📄"
    label_hi: "आधार कार्ड — पीछे अपलोड करें"
    label_en: "Upload Aadhaar Card — Back"
    required: true

  - id: gst_certificate
    icon: "📋"
    label_hi: "GST प्रमाणपत्र अपलोड करें"
    label_en: "Upload GST Certificate"
    required: true

upload_flow:
  step_1_choose_source:
    title_hi: "{docName} अपलोड करें"
    title_en: "Upload {docName}"
    options:
      - icon: "📷"
        label_hi: "कैमरा से फ़ोटो लें"
        label_en: "Take Photo"
      - icon: "🖼️"
        label_hi: "गैलरी से चुनें"
        label_en: "Choose from Gallery"
    tip_hi: "💡 साफ़ फ़ोटो लें — सारे अक्षर दिखने चाहिए"
    tip_en: "Take a clear photo — all text must be visible"

  step_2_preview:
    title_hi: "फ़ोटो रिव्यू करें"
    title_en: "Review Photo"
    quality_badges:
      - "✓ साफ़ दिख रहा है | Clear"
      - "✓ पूरा दिख रहा है | Complete"
    cta_save_hi: "यह फ़ोटो सेव करें"
    cta_save_en: "Save this photo"
    cta_retake_hi: "दोबारा फ़ोटो लें"
    cta_retake_en: "Retake photo"

  step_3_uploading:
    simulation_steps: 50
    simulation_interval_ms: 80
    total_time_ms: 4000
    success_text_hi: "अपलोड हो गया!"
    success_text_en: "Upload complete!"

document_states:
  not_uploaded: { border: gray, background: white, badge: none }
  uploaded: { border: green, background: green_light, badge: "✓", show_remove_button: true }
  error: { border: red, background: red_light, badge: "✗" }
  warning: { border: orange, background: orange_light, badge: "⚠" }

cta:
  text_hi: "अब रजिस्ट्रेशन फ़ीस भरें"
  text_en: "Next: Registration Fee"
  enabled_when: all_4_documents_uploaded
  next_screen: 5

error_scenarios:
  - KYC_PAN_MISMATCH
  - KYC_AADHAAR_EXPIRED
  - KYC_PAN_AADHAAR_UNLINKED
```

### SCREEN_4_ERROR: KYC_PAN_MISMATCH

```yaml
id: KYC_PAN_MISMATCH
trigger: PAN card name does not match Aadhaar name
screen: 4

display:
  document_states:
    pan_card: { state: error, status_hi: "नाम मेल नहीं खाता", status_en: "Name Mismatch ✗" }
    aadhaar_front: { state: uploaded }
    aadhaar_back: { state: uploaded }
    gst_certificate: { state: uploaded }
  error_card:
    type: error
    icon: "🪪"
    title_hi: "PAN नाम मेल नहीं खाता"
    title_en: "PAN Name Mismatch"
    message_hi: "PAN पर नाम: Rajesh K Sharma\nआपने डाला: राजेश कुमार\n\nकृपया सही नाम डालें या PAN अपडेट कराएं"
    message_en: "Name on PAN: Rajesh K Sharma\nYou entered: Rajesh Kumar\n\nPlease enter correct name or update PAN"

blocks_progression: true
```

### SCREEN_4_ERROR: KYC_AADHAAR_EXPIRED

```yaml
id: KYC_AADHAAR_EXPIRED
trigger: Aadhaar card address is outdated
screen: 4

display:
  document_states:
    pan_card: { state: uploaded }
    aadhaar_front: { state: warning, status_hi: "पता पुराना", status_en: "Address Update Required" }
    aadhaar_back: { state: warning }
    gst_certificate: { state: uploaded }
  error_card:
    type: warning
    icon: "⚠️"
    title_hi: "Aadhaar पता पुराना है"
    title_en: "Aadhaar Address Outdated"
    message_hi: "आपके Aadhaar पर पता पुराना है। कृपया UIDAI पोर्टल पर अपडेट करें।"
    message_en: "Address on Aadhaar is outdated. Please update on UIDAI portal."
  info_box:
    icon: "🌐"
    text_hi: "uidai.gov.in पर अपडेट करें"
    text_en: "Update at uidai.gov.in"

blocks_progression: true
```

### SCREEN_4_ERROR: KYC_PAN_AADHAAR_UNLINKED

```yaml
id: KYC_PAN_AADHAAR_UNLINKED
trigger: PAN and Aadhaar are not linked in NSDL database
screen: 4

display:
  document_states:
    pan_card: { state: uploaded }
    aadhaar_front: { state: uploaded }
    aadhaar_back: { state: uploaded }
    gst_certificate: { state: uploaded }
  error_card:
    type: error
    icon: "🔗"
    title_hi: "PAN-Aadhaar लिंक नहीं है"
    title_en: "PAN-Aadhaar Not Linked"
    message_hi: "आगे बढ़ने के लिए PAN और Aadhaar लिंक होना ज़रूरी है।"
    message_en: "PAN and Aadhaar must be linked to proceed."
  info_box:
    icon: "🌐"
    text_hi: "incometax.gov.in पर लिंक करें"
    text_en: "Link at incometax.gov.in"

blocks_progression: true
```

### SCREEN_5: Registration Fee

```yaml
id: SCREEN_5
phase: PHASE_1
title_hi: "रजिस्ट्रेशन फ़ीस"
title_en: "Registration Fee"
step_label: "स्टेप 4/4 | Step 4/4"
purpose: Collect ₹2,000 registration fee to initiate QA review

display_elements:
  - amount_box:
      amount: "₹2,000"
      label_hi: "रजिस्ट्रेशन फ़ीस"
      label_en: "Registration Fee"
  - info_card:
      icon: "ℹ️"
      title_hi: "जरूरी जानकारी"
      title_en: "Important Information"
      message_hi: "भुगतान के बाद आपकी profile Business/QA team द्वारा review की जाएगी।"
      message_en: "After payment, your profile will be reviewed by the Business/QA team."
      trust_badge:
        icon: "🔒"
        text_hi: "Reject होने पर full refund मिलेगा"
        text_en: "Full refund if rejected"
  - info_box:
      icon: "💰"
      text_hi: "फ़ीस के बाद QA investigation शुरू होगी"
      text_en: "QA investigation will start after fee payment"

cta:
  text_hi: "₹2,000 भुगतान करें"
  text_en: "Pay Now"
  action: PROCESS_PAYMENT
  simulation_delay_ms: 2000
  on_success:
    set: regFeePaid = true
    lock: trade_name
    next_screen: 6

error_scenarios:
  - REGFEE_FAILED
  - REGFEE_TIMEOUT
```

### SCREEN_5_ERROR: REGFEE_FAILED

```yaml
id: REGFEE_FAILED
trigger: Payment gateway declined the transaction
screen: 5

display:
  emoji: "😟"
  title_hi: "भुगतान नहीं हो पाया"
  title_en: "Payment could not be processed"
  reassurance_card:
    type: success
    icon: "✅"
    title_hi: "पैसा कटा नहीं है"
    title_en: "No money deducted"
    message_hi: "चिंता न करें — आपके अकाउंट से कोई पैसा नहीं कटा है।"
    message_en: "Don't worry — no money has been deducted from your account."
  transaction_details:
    amount: "₹2,000"
    error_code: "BANK_GATEWAY_TIMEOUT"
    time: "just now"
  info_box:
    type: warning
    icon: "💡"
    text_hi: "2-3 मिनट बाद दोबारा कोशिश करें"
    text_en: "Try again after 2-3 minutes"

ctas:
  - text_hi: "दोबारा भुगतान करें"
    text_en: "Retry Payment"
    type: primary
  - text_hi: "बाद में करें"
    text_en: "Pay Later"
    type: secondary
```

### SCREEN_5_ERROR: REGFEE_TIMEOUT

```yaml
id: REGFEE_TIMEOUT
trigger: Payment gateway timeout / connection lost during transaction
screen: 5

display:
  emoji: "⏳"
  title_hi: "भुगतान pending है"
  title_en: "Payment is pending"
  error_card:
    type: warning
    icon: "⏳"
    title_hi: "Bank response में देरी"
    title_en: "Bank response delayed"
    message_hi: "Bank से response आने में 2-5 मिनट लग सकते हैं। कृपया थोड़ा इंतज़ार करें।"
    message_en: "Bank response may take 2-5 minutes. Please wait."
  transaction_details:
    amount: "₹2,000"
    upi_ref: "UPI123456789"
    status: "⏳ Pending"
  info_box:
    icon: "🔒"
    text_hi: "48 घंटे में auto-refund अगर fail हो"
    text_en: "Auto-refund within 48hrs if failed"

ctas:
  - text_hi: "Status Refresh करें"
    text_en: "Refresh Status"
    type: primary
  - text_hi: "हमसे बात करें"
    text_en: "Talk to us"
    type: secondary
```

### SCREEN_6: QA Investigation

```yaml
id: SCREEN_6
phase: PHASE_2
title_hi: "QA Investigation"
title_en: "QA Investigation"
purpose: Async review by Wiom QA team — branch point (approve/reject)

decision_point:
  decision_maker: QA_TEAM (via Dashboard)
  outcomes:
    - APPROVED → SCREEN_7
    - REJECTED → REFUND_FLOW

approved_state:
  emoji: "🔍"
  title_hi: "Investigation चल रही है"
  title_en: "Investigation in progress"
  subtitle_hi: "Business/QA team आपकी profile चेक कर रही है"
  subtitle_en: "Business/QA team is reviewing your profile"
  checklist:
    - { text_hi: "फ़ोन वेरीफाइड", text_en: "Phone Verified", status: done }
    - { text_hi: "व्यक्तिगत जानकारी", text_en: "Personal Information", status: done }
    - { text_hi: "लोकेशन सबमिट", text_en: "Location Submitted", status: done }
    - { text_hi: "KYC दस्तावेज़ वेरीफाइड", text_en: "KYC Documents Verified", status: done }
    - { text_hi: "₹2,000 रजिस्ट्रेशन फ़ीस", text_en: "₹2,000 Registration Fee", status: done }
    - { text_hi: "QA Investigation", text_en: "QA Investigation", status: waiting }
  info_box:
    type: warning
    icon: "⏳"
    text_hi: "Review में 2-3 business days लग सकते हैं। Notification मिलेगा।"
    text_en: "Review may take 2-3 business days. You will be notified."
  cta: null  # No CTA — waits for QA decision

rejected_state:
  emoji: "😔"
  title_hi: "Profile अभी स्वीकार नहीं हुई"
  title_en: "Profile not accepted yet"
  subtitle_hi: "चिंता न करें — आपका पैसा सुरक्षित है"
  subtitle_en: "Don't worry — your money is safe"
  reason_card:
    type: error
    title_hi: "कारण"
    title_en: "Reason"
    message_hi: "Location इस समय service area में नहीं है। Area infrastructure तैयार होने पर दोबारा apply कर सकते हैं।"
    message_en: "Location is not in the service area currently. You can re-apply once area infrastructure is ready."
  refund_card:
    type: success
    icon: "🔒"
    title_hi: "Refund शुरू हो गया"
    title_en: "Refund initiated"
    amount: "₹2,000"
    timeline: "5-7 working days"
    ref: "RFD-2026-0042"
  info_box:
    icon: "🔔"
    text_hi: "जब area तैयार हो, तो दोबारा apply कर सकते हैं।"
    text_en: "You can re-apply when the area is ready."
```

### SCREEN_7: Policy & Rate Card

```yaml
id: SCREEN_7
phase: PHASE_2
title_hi: "नीतियां और रेट कार्ड"
title_en: "Policy & Rate Card"
step_label: "स्टेप 5 | Step 5"
purpose: Partner reviews and acknowledges commission structure and SLA terms

display_elements:
  - commission_card:
      title_hi: "कमीशन संरचना"
      title_en: "COMMISSION STRUCTURE"
      rows:
        - { label_hi: "नया कनेक्शन", label_en: "New Connection", value: "₹300/कनेक्शन", color: green }
        - { label_hi: "रिचार्ज कमीशन", label_en: "Recharge Commission", value: "₹300", color: green }

  - sla_card:
      title_hi: "SLA शर्तें"
      title_en: "SLA TERMS"
      items:
        - hi: "ग्राहक शिकायत: 4 घंटे में समाधान"
          en: "Customer complaints: 4hr resolution"
        - hi: "कनेक्शन 95%+ चालू रहना चाहिए"
          en: "Connection 95%+ to be up and running"
        - hi: "उपकरण देखभाल की ज़िम्मेदारी"
          en: "Equipment care responsibility"
        - hi: "Wiom ब्रांड गाइडलाइन का पालन"
          en: "Wiom brand guidelines compliance"

cta:
  text_hi: "समझ गया, आगे बढ़ें"
  text_en: "Understood, proceed"
  next_screen: 8

error_scenarios: []
```

### SCREEN_8: Bank + Dedup Check

```yaml
id: SCREEN_8
phase: PHASE_2
title_hi: "Bank वेरिफिकेशन"
title_en: "Bank Verification"
step_label: "स्टेप 6 | Step 6"
purpose: Verify bank account via penny drop and run dedup check

fields:
  - id: bank_account_holder
    type: text_input
    label_hi: "खाताधारक का नाम"
    label_en: "Account Holder Name"
    placeholder_hi: "नाम डालें"
    placeholder_en: "Enter name"
    required: true

  - id: bank_name
    type: text_input
    label_hi: "बैंक का नाम"
    label_en: "Bank Name"
    placeholder_hi: "बैंक नाम डालें"
    placeholder_en: "Enter bank name"
    required: true

  - id: bank_account_number
    type: text_input
    label_hi: "अकाउंट नंबर"
    label_en: "Account Number"
    placeholder_hi: "अकाउंट नंबर डालें"
    placeholder_en: "Enter account number"
    required: true

  - id: bank_ifsc
    type: text_input
    label_hi: "IFSC कोड"
    label_en: "IFSC Code"
    placeholder_hi: "IFSC कोड डालें"
    placeholder_en: "Enter IFSC code"
    required: true

verification_flow:
  pre_verify:
    info_box:
      icon: "📝"
      text_hi: "सभी बैंक डिटेल्स भरें"
      text_en: "Fill all bank details to proceed"
    cta:
      text_hi: "Penny Drop Verify करें"
      text_en: "Verify via Penny Drop"
      enabled_when: all_fields_filled
      simulation_delay_ms: 2000

  post_verify:
    penny_drop_card:
      type: success
      icon: "✓"
      title_hi: "पेनी ड्रॉप वेरीफाइड"
      title_en: "PENNY DROP VERIFIED"
      message_hi: "₹1 क्रेडिट और वेरीफाइड — नाम मैच कन्फ़र्म"
      message_en: "₹1 credited & verified — Name match confirmed"
    dedup_card:
      type: success
      icon: "✓"
      title_hi: "डीडप चेक पास"
      title_en: "DEDUP CHECK PASSED"
      message_hi: "PAN, आधार, GST, Bank — कोई डुप्लिकेट नहीं मिला"
      message_en: "PAN, Aadhaar, GST, Bank — No duplicates found"
    cta:
      text_hi: "अब Agreement करें"
      text_en: "Next: Agreement"
      next_screen: 9

error_scenarios:
  - BANK_PENNYDROP_FAIL
  - BANK_NAME_MISMATCH
  - DEDUP_FOUND
```

### SCREEN_8_ERROR: BANK_PENNYDROP_FAIL

```yaml
id: BANK_PENNYDROP_FAIL
trigger: ₹1 penny drop credit failed
screen: 8

display:
  field_states:
    bank_account_number: { state: error, border: red }
    others: { state: normal }
  error_card:
    type: error
    icon: "🏦"
    title_hi: "₹1 credit नहीं हो पाया"
    title_en: "Penny drop failed"
    message_hi: "अकाउंट नंबर गलत हो सकता है या बैंक सर्वर डाउन है।"
    message_en: "Account number may be wrong or bank server is down."

cta:
  text_hi: "अकाउंट नंबर ठीक करें"
  text_en: "Fix Account Number"
  action: FOCUS_ACCOUNT_NUMBER
```

### SCREEN_8_ERROR: BANK_NAME_MISMATCH

```yaml
id: BANK_NAME_MISMATCH
trigger: Bank account holder name differs from KYC name
screen: 8

display:
  mismatch_card:
    bank_name: "Rajesh Kumar Sharma"
    entered_name: "Rajesh Kumar"
  error_card:
    type: warning
    icon: "👤"
    title_hi: "Penny Drop — नाम मेल नहीं खाता"
    title_en: "Penny Drop — Name Mismatch"
    message_hi: "Bank account का नाम और आपका नाम अलग है।"
    message_en: "Bank account name and your name are different."

cta:
  text_hi: "नाम ठीक करें और Retry"
  text_en: "Fix Name and Retry"
```

### SCREEN_8_ERROR: DEDUP_FOUND

```yaml
id: DEDUP_FOUND
trigger: Existing partner found with same PAN/Bank account
screen: 8

display:
  penny_drop: { status: verified }  # penny drop passes even when dedup fails
  error_card:
    type: error
    icon: "🔍"
    title_hi: "DEDUP CHECK — Match Found!"
    title_en: "DEDUP CHECK — Match Found!"
    message_hi: "इस PAN और Bank Account से पहले से एक पार्टनर रजिस्टर्ड है।"
    message_en: "A partner is already registered with this PAN and Bank Account."
  match_details:
    partner_id: "CSP-0031"
    name: "Rajesh K."
    city: "Indore"
    match_on: "PAN + Bank A/C"
  info_box:
    icon: "📞"
    text_hi: "Wiom सपोर्ट से बात करें"
    text_en: "Talk to Wiom support"

cta:
  text_hi: "हमसे बात करें"
  text_en: "Talk to us"
  action: CONTACT_SUPPORT

blocks_progression: true
```

### SCREEN_9: Agreement Signing

```yaml
id: SCREEN_9
phase: PHASE_2
title_hi: "पार्टनर एग्रीमेंट"
title_en: "Partner Agreement"
step_label: "स्टेप 7 | Step 7"
purpose: Legal agreement review and Aadhaar e-Sign

display_elements:
  - agreement_text:
      scrollable: true
      max_height: 140dp
      content: |
        WIOM CHANNEL SALES PARTNER AGREEMENT
        1. SCOPE: Partner shall act as authorized CSP for Wiom internet services in designated territory.
        2. RESPONSIBILITIES: Customer acquisition, service activation, first-level support, equipment care.
        3. COMMISSION: As per Rate Card shared and acknowledged. Subject to SLA compliance.
        4. TERM: 12 months, auto-renewable. 30-day notice for termination.
        5. COMPLIANCE: Partner shall comply with ISP license terms (DOT/TRAI) and Wiom brand guidelines.

  - verification_card:
      type: success
      items:
        - "✓ ISP License: DOT Compliance वेरीफाइड | ISP License: DOT Compliance Verified"
        - "✓ TRAI Guidelines: स्वीकृत | TRAI Guidelines: Acknowledged"

  - checkbox:
      id: terms_accepted
      default: true
      text_hi: "मैंने सभी नियम और शर्तें पढ़ लिए और accept करता हूं"
      text_en: "I have read and accept all terms and conditions"

  - info_box:
      icon: "🔒"
      text_hi: "Aadhaar e-Sign से agreement sign होगा"
      text_en: "Agreement will be signed via Aadhaar e-Sign"

cta:
  text_hi: "e-Sign करें"
  text_en: "e-Sign"
  enabled_when: terms_accepted == true
  simulation_delay_ms: 2000
  next_screen: 10

error_scenarios:
  - ESIGN_FAILED
```

### SCREEN_9_ERROR: ESIGN_FAILED

```yaml
id: ESIGN_FAILED
trigger: Aadhaar e-Sign connection error or OTP failure
screen: 9

display:
  emoji: "✍️"
  title_hi: "e-Sign नहीं हो पाया"
  title_en: "e-Sign could not be completed"
  error_card:
    type: warning
    icon: "✍️"
    title_hi: "Aadhaar e-Sign कनेक्शन एरर"
    title_en: "Aadhaar e-Sign Connection Error"
    message_hi: "UIDAI सर्वर से कनेक्ट नहीं हो पाया। कृपया दोबारा कोशिश करें।"
    message_en: "Could not connect to UIDAI server. Please try again."
  instructions_card:
    title_hi: "क्या करें?"
    title_en: "What to do?"
    steps:
      - hi: "इंटरनेट कनेक्शन चेक करें"
        en: "Check internet connection"
      - hi: "2-3 मिनट इंतज़ार करें"
        en: "Wait 2-3 minutes"
      - hi: "दोबारा कोशिश करें"
        en: "Retry"

ctas:
  - text_hi: "e-Sign Retry करें"
    text_en: "Retry e-Sign"
    type: primary
  - text_hi: "हमसे बात करें"
    text_en: "Talk to us"
    type: secondary
```

### SCREEN_10: Technical Review

```yaml
id: SCREEN_10
phase: PHASE_2
title_hi: "तकनीकी समीक्षा"
title_en: "Technical Review"
purpose: Verify device compatibility, infrastructure, and upload shop/equipment photos

sections:
  - device_check:
      title_hi: "डिवाइस चेक"
      title_en: "DEVICE CHECK"
      items:
        - { text: "Samsung Galaxy M34", status: verified }
        - { text_hi: "Android 14 — संगत", text_en: "Android 14 — Compatible", status: verified }
        - { text_hi: "Wiom OS: तैयार", text_en: "Wiom OS: Ready", status: verified }

  - infra_check:
      title_hi: "इन्फ्रा चेक"
      title_en: "INFRA CHECK"
      fields:
        - id: internet_setup_type
          type: dropdown
          options: ["Fiber (FTTH)", "Cable", "Wireless"]
          required: true
        - id: shop_photo
          type: upload
          label_hi: "दुकान की फ़ोटो"
          label_en: "Shop Front Photo"
          required: true
        - id: equipment_photo
          type: upload
          label_hi: "राऊटर / उपकरण"
          label_en: "Router / Equipment"
          required: true

completion_state:
  title_hi: "दस्तावेज़ पूरे!"
  title_en: "Documentation complete!"
  info_box:
    type: success
    icon: "✓"
    text_hi: "Tech review पूरी! अब onboarding fee भरें।"
    text_en: "Tech review complete! Now pay the onboarding fee."

cta:
  text_hi: "Onboarding Fee भरें"
  text_en: "Pay Onboarding Fee"
  enabled_when: internet_setup_selected AND shop_photo_uploaded AND equipment_photo_uploaded
  next_screen: 11

error_scenarios:
  - TECH_DEVICE_INCOMPATIBLE
```

### SCREEN_10_ERROR: TECH_DEVICE_INCOMPATIBLE

```yaml
id: TECH_DEVICE_INCOMPATIBLE
trigger: Device does not meet minimum Android/RAM requirements
screen: 10

display:
  emoji: "📵"
  title_hi: "Device Compatible नहीं है"
  title_en: "Device is not compatible"
  error_card:
    type: error
    title: "DEVICE CHECK FAILED"
    message_hi: "आपका device minimum requirements पूरी नहीं करता।"
    message_en: "Your device does not meet minimum requirements."
    details:
      device: "Samsung Galaxy J2 Core"
      android: { value: "Android 8.1", min: "11", status: fail }
      ram: { value: "1GB", min: "3GB", status: fail }
  recommended_devices:
    - "Samsung M34"
    - "Redmi Note 12"
    - "Realme Narzo 60"
  info_box:
    icon: "📱"
    text_hi: "नया device लेने के बाद यहीं से आगे बढ़ सकते हैं"
    text_en: "You can continue from here after getting a new device"

cta:
  text_hi: "Device बदलें और Retry"
  text_en: "Change Device and Retry"

blocks_progression: true
```

### SCREEN_11: Onboarding Fee

```yaml
id: SCREEN_11
phase: PHASE_3
title_hi: "ऑनबोर्डिंग फ़ीस"
title_en: "Onboarding Fee"
purpose: Collect ₹20,000 onboarding fee to unlock training and financial setup

display_elements:
  - amount_box:
      amount: "₹20,000"
      label_hi: "ऑनबोर्डिंग फ़ीस (GST सहित)"
      label_en: "Onboarding Fee (incl. GST)"
  - details_card:
      rows:
        - { label_hi: "रजिस्ट्रेशन फ़ीस (भुगतान हुआ)", label_en: "Registration Fee (paid)", value: "₹2,000" }
        - { label_hi: "ऑनबोर्डिंग फ़ीस", label_en: "Onboarding Fee", value: "₹20,000" }
        - { label_hi: "कुल Investment", label_en: "Total Investment", value: "₹22,000", bold: true }
  - info_box:
      type: success
      icon: "✓"
      text_hi: "भुगतान के बाद Training modules unlock होंगे"
      text_en: "Training modules will unlock after payment"

cta:
  text_hi: "₹20,000 भुगतान करें"
  text_en: "Pay Now"
  simulation_delay_ms: 2000
  on_success:
    set: onboardFeePaid = true
    next_screen: 12

error_scenarios:
  - ONBOARDFEE_FAILED
```

### SCREEN_11_ERROR: ONBOARDFEE_FAILED

```yaml
id: ONBOARDFEE_FAILED
trigger: ₹20,000 payment declined
screen: 11

display:
  emoji: "😟"
  title_hi: "भुगतान नहीं हो पाया"
  title_en: "Payment could not be processed"
  reassurance_card:
    type: success
    icon: "✅"
    title_hi: "पैसा कटा नहीं है"
    title_en: "No money deducted"
    message_hi: "चिंता न करें — आपके अकाउंट से कोई पैसा नहीं कटा है।"
    message_en: "Don't worry — no money has been deducted from your account."
  transaction_details:
    amount: "₹20,000"
    error_code: "UPI_LIMIT_EXCEEDED"
    time: "just now"
  info_box:
    type: warning
    icon: "💡"
    text_hi: "UPI limit ₹1L/day — NEFT/RTGS या कार्ड से भुगतान करें"
    text_en: "UPI limit ₹1L/day — try NEFT/RTGS or card"

ctas:
  - text_hi: "दोबारा भुगतान करें"
    text_en: "Retry Payment"
    type: primary
  - text_hi: "बाद में करें"
    text_en: "Pay Later"
    type: secondary
```

### SCREEN_12: Financial Setup

```yaml
id: SCREEN_12
phase: PHASE_3
title_hi: "फ़ाइनेंशियल सेटअप"
title_en: "Financial Setup"
step_label: "स्टेप 9 | Step 9"
purpose: Automated backend setup of partner financial accounts

interaction: NONE (auto-progression, no user input)

animation:
  type: sequential_checklist
  delay_per_item_ms: 800
  total_items: 5

checklist_items:
  - id: ledger
    title_hi: "पार्टनर लेजर बना"
    title_en: "Partner Ledger Created"
    subtitle_hi: "Commission tracking चालू"
    subtitle_en: "Commission tracking active"

  - id: razorpayx
    title_hi: "RazorpayX Payout लिंक"
    title_en: "RazorpayX Payout Link"
    subtitle_hi: "SBI A/C XXXX4521 payouts के लिए लिंक"
    subtitle_en: "SBI A/C XXXX4521 linked for payouts"

  - id: zoho
    title_hi: "Zoho Invoice सेटअप"
    title_en: "Zoho Invoice Setup"
    subtitle_hi: "हर settlement के लिए auto-invoice"
    subtitle_en: "Auto-invoice for every settlement"

  - id: trade_name_lock
    title_hi: "Trade Name लॉक"
    title_en: "Trade Name Locked"
    subtitle_hi: '"Rajesh Telecom" — आधिकारिक नाम'
    subtitle_en: '"Rajesh Telecom" — official name'

  - id: tds_tcs
    title_hi: "TDS/TCS कॉन्फ़िगरेशन"
    title_en: "TDS/TCS Configuration"
    subtitle_hi: "PAN ABCDE1234F — auto deduction सेटअप"
    subtitle_en: "PAN ABCDE1234F — auto deduction setup"

item_states:
  pending: { icon: gray_circle, text: "⋯" }
  processing: { icon: spinner, text_hi: "प्रोसेस हो रहा है...", text_en: "Processing..." }
  done: { icon: green_check, text: "✓" }

completion_state:
  title_hi: "सेटअप पूरा!"
  title_en: "Setup Complete!"
  success_card:
    type: success
    icon: "✓"
    title_hi: "सब तैयार है!"
    title_en: "All set!"
    message_hi: "Ledger, payouts, invoicing, और tax setup पूरा। अब training शुरू!"
    message_en: "Ledger, payouts, invoicing, and tax setup complete. Now start training!"
  info_box:
    type: success
    icon: "💰"
    text_hi: "Commission payouts हर Monday, सीधे आपके बैंक में"
    text_en: "Commission payouts every Monday, directly to your bank"

cta:
  text_hi: "Training शुरू करें"
  text_en: "Start Training"
  enabled_when: all_items_done
  next_screen: 13

error_scenarios: []
```

### SCREEN_13: Training Modules

```yaml
id: SCREEN_13
phase: PHASE_3
title_hi: "ट्रेनिंग"
title_en: "Training"
purpose: Partner completes 3 training modules with video + quiz before go-live

module_list_view:
  progress_bar: completed_count / total_count
  subtitle_hi: "पूरा करें, फिर काम शुरू!"
  subtitle_en: "Complete to start working!"

modules:
  - id: app_usage
    icon: "📱"
    title_hi: "App कैसे चलाएं"
    title_en: "How to use the App"
    subtitle_hi: "Customer, रिचार्ज, शिकायतें"
    subtitle_en: "Customer, Recharge, Complaints"
    questions:
      - question_hi: "ग्राहक का नया कनेक्शन कैसे बनाएं?"
        question_en: "How to create a new customer connection?"
        options:
          - ["Settings से", "From Settings"]
          - ["Home > Add Customer से", "From Home > Add Customer"]
          - ["Profile से", "From Profile"]
          - ["Help से", "From Help"]
        correct_index: 1
        hint_hi: "Home screen पर 'Add Customer' बटन दबाएं"
        hint_en: "Press 'Add Customer' button on Home screen"
      - question_hi: "रीचार्ज status कहां देखें?"
        question_en: "Where to check recharge status?"
        options:
          - ["Settings", "Settings"]
          - ["Home", "Home"]
          - ["Earnings > Transactions", "Earnings > Transactions"]
          - ["Profile", "Profile"]
        correct_index: 2
        hint_hi: "Earnings section में Transactions tab पर जाएं"
        hint_en: "Go to Transactions tab in Earnings section"

  - id: sla_exposure
    icon: "📊"
    title_hi: "SLA और Exposure"
    title_en: "SLA & Exposure"
    subtitle_hi: "नियम, स्तर, प्रभाव"
    subtitle_en: "Rules, Levels, Impact"
    questions:
      - question_hi: "ग्राहक शिकायत का resolution time क्या है?"
        question_en: "What is customer complaint resolution time?"
        options:
          - ["24 घंटे", "24 hours"]
          - ["4 घंटे", "4 hours"]
          - ["48 घंटे", "48 hours"]
          - ["1 हफ़्ता", "1 week"]
        correct_index: 1
        hint_hi: "SLA के अनुसार 4 घंटे में समाधान करना ज़रूरी है"
        hint_en: "Resolution within 4 hours is required as per SLA"
      - question_hi: "Minimum uptime requirement क्या है?"
        question_en: "What is minimum uptime requirement?"
        options:
          - ["90%", "90%"]
          - ["95%", "95%"]
          - ["99%", "99%"]
          - ["80%", "80%"]
        correct_index: 1
        hint_hi: "Connection 95%+ चालू रहना चाहिए"
        hint_en: "Connection should be running 95%+"

  - id: money_matters
    icon: "💰"
    title_hi: "पैसों की बात"
    title_en: "Money Matters"
    subtitle_hi: "Commission, TDS, TCS"
    subtitle_en: "Commission, TDS, TCS"
    questions:
      - question_hi: "नए कनेक्शन पर कमीशन कितना है?"
        question_en: "Commission on new connection?"
        options:
          - ["₹150", "₹150"]
          - ["₹200", "₹200"]
          - ["₹300", "₹300"]
          - ["₹500", "₹500"]
        correct_index: 2
        hint_hi: "हर नए कनेक्शन पर ₹300 मिलता है"
        hint_en: "You get ₹300 for every new connection"
      - question_hi: "Commission payout कब होता है?"
        question_en: "When is commission payout?"
        options:
          - ["Daily", "Daily"]
          - ["Weekly (Monday)", "Weekly (Monday)"]
          - ["Monthly", "Monthly"]
          - ["Quarterly", "Quarterly"]
        correct_index: 1
        hint_hi: "हर Monday को bank account में payout होता है"
        hint_en: "Payout happens every Monday to bank account"

module_detail_view:
  video_section:
    before_play: { icon: "▶️", title: module.title, cta_hi: "Video देखें", cta_en: "Watch Video" }
    playing: { icon: "🎬", text_hi: "चल रहा है...", text_en: "Playing...", simulation: "10 steps x 100ms" }
    after_play: { icon: "✅", text_hi: "Video देखा गया", text_en: "Video watched", background: green }

  quiz_section:
    appears_after: video_watched
    answer_flow:
      1_select_option: { border: pink, background: pink_light }
      2_check_button_hi: "जवाब चेक करें"
      2_check_button_en: "Check Answer"
      3a_correct: { background: green, icon: "✓", auto_advance_ms: 800 }
      3b_wrong: { background: red, icon: "✗", show_hint: true, retry_button_hi: "फिर से कोशिश करें", retry_button_en: "Try Again" }
    on_all_correct:
      celebration_hi: "🎉 Quiz पास!"
      celebration_en: "Quiz Passed!"
      auto_complete_ms: 1000

module_card_states:
  done: { background: green_light, border: green, badge: "✓" }
  current: { background: pink_light, border: pink, badge_hi: "शुरू करें", badge_en: "Start" }
  not_started: { background: white, border: gray, badge_hi: "शुरू करें", badge_en: "Start" }

cta:
  text_hi: "Quiz पूरा करें"
  text_en: "Complete Quiz"
  enabled_when: all_modules_completed
  next_screen: 14

error_scenarios:
  - TRAINING_QUIZ_FAIL
```

### SCREEN_13_ERROR: TRAINING_QUIZ_FAIL

```yaml
id: TRAINING_QUIZ_FAIL
trigger: Partner fails training quiz (wrong answers)
screen: 13

display:
  emoji: "📝"
  title_hi: "Quiz पास नहीं हुई"
  title_en: "Quiz not passed"
  score_card:
    score: "2/5"
    passing: "4/5"
    progress: 0.4
    color: red
  error_card:
    type: warning
    icon: "📝"
    title_hi: "चिंता न करें"
    title_en: "Don't worry"
    message_hi: "Modules दोबारा review करें और फिर quiz दें"
    message_en: "Review modules again and retake quiz"

ctas:
  - text_hi: "Modules Review करें"
    text_en: "Review Modules"
    type: primary
  - text_hi: "Quiz दोबारा दें"
    text_en: "Retake Quiz"
    type: secondary
```

### SCREEN_14: Go Live

```yaml
id: SCREEN_14
phase: PHASE_3
title_hi: "पार्टनर ऐप होम"
title_en: "Partner App Home"
purpose: Celebration and activation — partner is now live

display_elements:
  - emoji: "🎉"
  - title_hi: "बधाई हो, राजेश!"
  - title_en: "Congratulations, Rajesh!"
  - subtitle_hi: "आप अब Wiom Partner हैं"
  - subtitle_en: "You are now a Wiom Partner"

  - status_chips:
      - { text_hi: "✓ रजिस्टर्ड", text_en: "✓ Registered" }
      - { text: "✓ QA Approved" }
      - { text_hi: "✓ Bank वेरीफाइड", text_en: "✓ Bank Verified" }
      - { text_hi: "✓ एग्रीमेंट", text_en: "✓ Agreement" }
      - { text: "✓ Tech Review" }
      - { text_hi: "✓ फ़ाइनेंशियल सेटअप", text_en: "✓ Financial Setup" }
      - { text_hi: "✓ ट्रेनिंग पूरी", text_en: "✓ Trained" }

  - quick_actions:
      - icon: "👤"
        title_hi: "Customer जोड़ें"
        title_en: "Add Customer"
        subtitle_hi: "नया कनेक्शन"
        subtitle_en: "New connection"
      - icon: "💰"
        title_hi: "कमाई देखें"
        title_en: "View Earnings"
        subtitle: "Commission, TDS"
      - icon: "📝"
        title_hi: "टास्क"
        title_en: "Tasks"
        subtitle_hi: "रिस्टोर, शिकायतें"
        subtitle_en: "Restore, complaints"
      - icon: "🎓"
        title_hi: "ट्रेनिंग"
        title_en: "Training"
        subtitle_hi: "Module दोबारा देखें"
        subtitle_en: "Revisit modules"

  - download_card:
      icon: "🚀"
      title_hi: "Wiom CSP App डाउनलोड करें"
      title_en: "Download Wiom CSP App"
      message_hi: "नए कनेक्शन बनाने के लिए Wiom CSP App इस्तेमाल करें"
      message_en: "Use Wiom CSP App to create new connections"
      cta_hi: "App डाउनलोड करें"
      cta_en: "Download App"

error_scenarios: []
```

---

## DASHBOARD_SYSTEM

```yaml
dashboard:
  location: dashboard/index.html + dashboard/bridge.py
  server_port: 8092
  connection: ADB over USB/WiFi
  protocol: HTTP POST with JSON body
  status_check_interval_ms: 5000

  controls:
    - id: restart_app
      action: { action: "restart" }
      adb: "am force-stop com.wiom.csp && am start -n com.wiom.csp/.MainActivity"

    - id: reset_to_screen_0
      action: { action: "reset" }
      intent: "com.wiom.csp.RESET"

    - id: set_hindi
      action: { action: "lang", lang: "hi" }
      intent: "com.wiom.csp.LANG --es lang hi"

    - id: set_english
      action: { action: "lang", lang: "en" }
      intent: "com.wiom.csp.LANG --es lang en"

    - id: fill_all
      action: { action: "fill", mode: "filled" }
      intent: "com.wiom.csp.FILL --es mode filled"

    - id: empty_all
      action: { action: "fill", mode: "empty" }
      intent: "com.wiom.csp.FILL --es mode empty"

    - id: navigate_to_screen
      action: { action: "navigate", screen: N }
      intent: "com.wiom.csp.NAVIGATE --ei screen N"

    - id: trigger_scenario
      action: { action: "scenario", name: "SCENARIO_NAME" }
      intent: "com.wiom.csp.SCENARIO --es name SCENARIO_NAME"

    - id: clear_scenario
      action: { action: "scenario", name: "NONE" }

    - id: qa_approve
      action: { action: "qa", decision: "approved" }
      intent: "com.wiom.csp.QA --es action approved"

    - id: qa_reject
      action: { action: "qa", decision: "rejected" }
      intent: "com.wiom.csp.QA --es action rejected"

    - id: save_training
      action: { action: "training_config", modules: [...] }
      intent: "com.wiom.csp.TRAINING --es config JSON"

  screenshot:
    endpoint: "GET /screenshot"
    adb: "adb exec-out screencap -p"
    temp_file: "/tmp/csp_dash_screen.png"
    format: PNG

  bridge_receiver: "com.wiom.csp/.DashboardReceiver"
  intent_actions:
    - "com.wiom.csp.SCENARIO"
    - "com.wiom.csp.NAVIGATE"
    - "com.wiom.csp.LANG"
    - "com.wiom.csp.RESET"
    - "com.wiom.csp.FILL"
    - "com.wiom.csp.TRAINING"
    - "com.wiom.csp.QA"
```

---

## BUSINESS_CONSTANTS

```yaml
fees:
  registration: 2000  # INR, refundable if QA rejected
  onboarding: 20000   # INR, inclusive GST
  total_investment: 22000

commissions:
  new_connection: 300  # INR per connection
  recharge: 300        # INR flat
  payout_frequency: "Weekly (Monday)"
  payout_method: "Bank transfer via RazorpayX"

sla:
  complaint_resolution: "4 hours"
  uptime_requirement: "95%+"
  equipment_care: "Partner responsibility"
  brand_compliance: "Mandatory"

agreement:
  term: "12 months, auto-renewable"
  termination_notice: "30 days"
  compliance: ["DOT", "TRAI", "Wiom brand guidelines"]

refund:
  qa_rejection_refund: 2000
  refund_timeline: "5-7 working days"
  payment_timeout_auto_refund: "48 hours"

training:
  modules_count: 3
  passing_score: "4/5"
  quiz_retry: unlimited

device_requirements:
  min_android: 11
  min_ram_gb: 3
  recommended_devices: ["Samsung M34", "Redmi Note 12", "Realme Narzo 60"]
```

---

## STATE_MACHINE

```yaml
states:
  - NEW → SCREEN_0 (Phone Entry)
  - OTP_SENT → SCREEN_1 (OTP Verification)
  - REGISTERED → SCREEN_2-5 (Personal → Fee)
  - INVESTIGATION → SCREEN_6 (QA Review) [BRANCH POINT]
    - → APPROVED → SCREEN_7 (Policy)
    - → REJECTED → REFUND_FLOW (end)
  - VERIFIED → SCREEN_7-10 (Policy → Tech Review)
  - PAYMENT_2 → SCREEN_11 (Onboarding Fee)
  - SETUP → SCREEN_12 (Financial Setup, auto)
  - TRAINING → SCREEN_13 (Training Modules)
  - LIVE → SCREEN_14 (Go Live)

error_states:
  PHONE_DUPLICATE: blocks at SCREEN_0
  OTP_WRONG: retryable at SCREEN_1 (max 3 attempts)
  OTP_EXPIRED: retryable at SCREEN_1
  AREA_NOT_SERVICEABLE: blocks at SCREEN_3
  KYC_PAN_MISMATCH: blocks at SCREEN_4
  KYC_AADHAAR_EXPIRED: blocks at SCREEN_4
  KYC_PAN_AADHAAR_UNLINKED: blocks at SCREEN_4
  REGFEE_FAILED: retryable at SCREEN_5
  REGFEE_TIMEOUT: retryable at SCREEN_5
  BANK_PENNYDROP_FAIL: retryable at SCREEN_8
  BANK_NAME_MISMATCH: retryable at SCREEN_8
  DEDUP_FOUND: blocks at SCREEN_8 (needs support)
  ESIGN_FAILED: retryable at SCREEN_9
  TECH_DEVICE_INCOMPATIBLE: blocks at SCREEN_10
  ONBOARDFEE_FAILED: retryable at SCREEN_11
  TRAINING_QUIZ_FAIL: retryable at SCREEN_13
```

---

## VALIDATION_RULES

```yaml
phone:
  - not_blank → "नंबर डालें / Enter phone number"
  - length == 10 → "10 अंकों का नंबर डालें / Enter 10-digit number"
  - digits_only → "केवल अंक डालें / Enter digits only"

otp:
  - all_4_filled → "पूरा OTP डालें / Enter complete OTP"

name:
  - not_blank → "नाम डालें / Enter name"

email:
  - not_blank → "ईमेल डालें / Enter email"
  - contains_at_and_dot → "सही ईमेल डालें / Enter valid email"

pincode:
  - not_blank → "पिनकोड डालें / Enter pincode"
  - length == 6 → "6 अंकों का पिनकोड डालें / Enter 6-digit pincode"
```

---

## MOCK_DATA (Filled Mode)

```yaml
phone: "9876543210"
otp: ["4", "7", "2", "9"]
name: "राजेश कुमार"
email: "rajesh@email.com"
entity_type: "Individual"
trade_name: "Rajesh Telecom"
city: "Indore"
pincode: "452010"
address: "123, Vijay Nagar, Indore"
state: "Madhya Pradesh"
gps: "22.71° N, 75.85° E"
bank_holder: "राजेश कुमार"
bank_name: "State Bank of India"
bank_account: "XXXX XXXX 4521"
bank_ifsc: "SBIN0001234"
pan: "ABCDE1234F"
internet_setup: "Fiber (FTTH)"
device: "Samsung Galaxy M34"
android: "14"
```

---

## QA_TEST_CASES

### Happy Path Tests

```yaml
TC_HP_001:
  name: "Complete onboarding end-to-end"
  steps: Screen 0 → 1 → 2 → 3 → 4 → 5 → 6 (approved) → 7 → 8 → 9 → 10 → 11 → 12 → 13 → 14
  expected: Partner reaches Go Live screen with all 7 status chips green

TC_HP_002:
  name: "Language toggle works on all screens"
  steps: Navigate to each screen, toggle हि/En, verify all text switches
  expected: All labels, buttons, messages switch between Hindi and English

TC_HP_003:
  name: "Back navigation preserves form data"
  steps: Fill Screen 2, go to Screen 3, go back to Screen 2
  expected: Personal info fields retain entered values

TC_HP_004:
  name: "OTP timer countdown and resend"
  steps: Enter phone → go to OTP screen → wait 30s → tap resend
  expected: Timer counts from 30 to 0, resend link appears, new timer starts

TC_HP_005:
  name: "KYC document upload flow (camera + gallery)"
  steps: Tap upload → choose camera → preview → save → verify uploaded state
  expected: Document shows green verified state with remove option

TC_HP_006:
  name: "Bank verification with penny drop"
  steps: Fill all bank fields → tap Verify → wait 2s → verify success cards
  expected: Penny drop verified + dedup check passed cards appear

TC_HP_007:
  name: "Financial setup auto-progression"
  steps: Reach Screen 12 → observe 5 items completing sequentially
  expected: Items complete one by one (800ms each), success card at end

TC_HP_008:
  name: "Training module video + quiz flow"
  steps: Open module → watch video → answer quiz correctly → complete
  expected: Module marked done, progress bar updates

TC_HP_009:
  name: "Go Live quick actions navigation"
  steps: Reach Screen 14 → tap each quick action card
  expected: Each card opens detail view with video placeholder and CTA

TC_HP_010:
  name: "Filled mode populates all screens"
  steps: Trigger fill command from dashboard → navigate through all screens
  expected: All form fields pre-filled with mock data
```

### Error Scenario Tests

```yaml
TC_ERR_001:
  name: "Phone duplicate detection"
  scenario: PHONE_DUPLICATE
  screen: 0
  expected: Error card shown, login + new number CTAs available

TC_ERR_002:
  name: "Wrong OTP with attempt counter"
  scenario: OTP_WRONG
  screen: 1
  expected: Red OTP boxes, "2 attempts remaining" message

TC_ERR_003:
  name: "OTP expiry with resend option"
  scenario: OTP_EXPIRED
  screen: 1
  expected: Faded OTP boxes, "send new OTP" + "change number" buttons

TC_ERR_004:
  name: "Non-serviceable area with waitlist"
  scenario: AREA_NOT_SERVICEABLE
  screen: 3
  expected: Orange error card, waitlist CTA, progression blocked

TC_ERR_005:
  name: "PAN name mismatch"
  scenario: KYC_PAN_MISMATCH
  screen: 4
  expected: PAN card red, other docs green, mismatch details shown, progression blocked

TC_ERR_006:
  name: "Aadhaar address expired"
  scenario: KYC_AADHAAR_EXPIRED
  screen: 4
  expected: Aadhaar orange warning, UIDAI update link shown, progression blocked

TC_ERR_007:
  name: "PAN-Aadhaar not linked"
  scenario: KYC_PAN_AADHAAR_UNLINKED
  screen: 4
  expected: All docs green but linking error, incometax.gov.in link shown, blocked

TC_ERR_008:
  name: "Registration fee payment failed"
  scenario: REGFEE_FAILED
  screen: 5
  expected: Reassurance card (no money deducted), retry + later CTAs

TC_ERR_009:
  name: "Registration fee payment timeout"
  scenario: REGFEE_TIMEOUT
  screen: 5
  expected: Pending status with UPI ref, auto-refund info, refresh + support CTAs

TC_ERR_010:
  name: "Penny drop verification failed"
  scenario: BANK_PENNYDROP_FAIL
  screen: 8
  expected: Account number field red, penny drop failed card, fix CTA

TC_ERR_011:
  name: "Bank account name mismatch"
  scenario: BANK_NAME_MISMATCH
  screen: 8
  expected: Mismatch comparison shown, fix name + retry CTA

TC_ERR_012:
  name: "Dedup match found"
  scenario: DEDUP_FOUND
  screen: 8
  expected: Penny drop passes, dedup alert with match details, contact support CTA, blocked

TC_ERR_013:
  name: "e-Sign failed"
  scenario: ESIGN_FAILED
  screen: 9
  expected: Connection error card, troubleshooting steps, retry + support CTAs

TC_ERR_014:
  name: "Device incompatible"
  scenario: TECH_DEVICE_INCOMPATIBLE
  screen: 10
  expected: Device check failed card with specs, recommended devices list, blocked

TC_ERR_015:
  name: "Onboarding fee payment failed"
  scenario: ONBOARDFEE_FAILED
  screen: 11
  expected: Reassurance card, UPI limit info, retry + later CTAs

TC_ERR_016:
  name: "Training quiz failed"
  scenario: TRAINING_QUIZ_FAIL
  screen: 13
  expected: Score card (2/5), review modules + retake quiz CTAs
```

### Edge Case Tests

```yaml
TC_EDGE_001:
  name: "Empty form submission blocked"
  steps: On each screen with required fields, try tapping CTA without filling anything
  expected: CTA remains disabled, no navigation occurs

TC_EDGE_002:
  name: "Partial KYC upload"
  steps: Upload 2 of 4 documents, try to proceed
  expected: CTA disabled until all 4 documents uploaded

TC_EDGE_003:
  name: "QA rejected then view approved path"
  steps: Trigger QA rejection → tap "View Approved Path" toggle
  expected: Toggles between rejected and approved views

TC_EDGE_004:
  name: "Bank fields partially filled"
  steps: Fill 3 of 4 bank fields, try Penny Drop
  expected: Verify button remains disabled

TC_EDGE_005:
  name: "Training module revisit after completion"
  steps: Complete module → return to list → tap completed module
  expected: Module opens with completed state, video watched, quiz done

TC_EDGE_006:
  name: "Remove uploaded KYC document"
  steps: Upload PAN → tap remove button
  expected: Document returns to not-uploaded state

TC_EDGE_007:
  name: "OTP timer edge at exactly 0"
  steps: Watch timer count to 0
  expected: Timer disappears, resend link + change number link appear

TC_EDGE_008:
  name: "Agreement checkbox unchecked"
  steps: Uncheck the terms checkbox on Screen 9
  expected: e-Sign CTA becomes disabled

TC_EDGE_009:
  name: "Navigate backward then forward"
  steps: Go to Screen 5 → back to Screen 2 → forward to Screen 5
  expected: All data preserved, no duplicate submissions

TC_EDGE_010:
  name: "Scenario trigger then clear"
  steps: Trigger any error scenario → clear scenario
  expected: Screen returns to happy path state

TC_EDGE_011:
  name: "Dashboard disconnect during operation"
  steps: Disconnect USB while dashboard is sending commands
  expected: Dashboard shows "No Device" status, commands fail gracefully

TC_EDGE_012:
  name: "Multiple rapid screen navigations"
  steps: Click screen buttons rapidly in dashboard
  expected: App navigates to final target without crash
```

### UAT Test Cases

```yaml
TC_UAT_001:
  name: "New partner completes full onboarding"
  persona: "Rajesh Kumar, Indore, Individual, first-time partner"
  flow: Phone → OTP → Personal → Location → KYC → ₹2K → QA Approved → Policy → Bank → Agreement → Tech → ₹20K → Finance → Training → Go Live
  acceptance: All 15 screens visited, all data captured, partner live

TC_UAT_002:
  name: "Partner in non-serviceable area"
  persona: "Sunita Devi, Deoghar (not serviceable)"
  flow: Phone → OTP → Personal → Location (pincode rejected) → Waitlist
  acceptance: Partner added to waitlist, informed about next steps

TC_UAT_003:
  name: "Partner with KYC issues"
  persona: "Anil Verma, PAN name different from Aadhaar"
  flow: Phone → OTP → Personal → Location → KYC (PAN mismatch) → Fix → Retry
  acceptance: Clear error message, path to resolution shown

TC_UAT_004:
  name: "QA-rejected partner with refund"
  persona: "Deepak Jain, rejected after ₹2K payment"
  flow: Phone → OTP → Personal → Location → KYC → ₹2K → QA Rejected → Refund
  acceptance: ₹2K refund initiated with tracking, reapply option shown

TC_UAT_005:
  name: "Partner with payment issues"
  persona: "Mohit Patel, UPI limit exceeded for ₹20K"
  flow: ... → ₹20K (failed) → Switch to NEFT → ₹20K (success) → Continue
  acceptance: Helpful error message, alternative payment suggestion, no money lost

TC_UAT_006:
  name: "Partner with incompatible device"
  persona: "Kavita Singh, Samsung J2 Core (Android 8.1, 1GB RAM)"
  flow: ... → Tech Review (device incompatible) → Recommended devices shown
  acceptance: Clear requirements listed, recommended devices shown, progress preserved

TC_UAT_007:
  name: "Partner fails training quiz"
  persona: "Priya Sharma, answers 2/5 correctly"
  flow: ... → Training → Quiz (fail) → Review → Retry → Pass → Go Live
  acceptance: Encouraging failure message, modules available for review, unlimited retries

TC_UAT_008:
  name: "Hindi-first UX verification"
  persona: Any partner, Hindi-speaking
  flow: Complete entire flow in Hindi
  acceptance: All text meaningful in Hindi, no English-only screens, culturally appropriate

TC_UAT_009:
  name: "Dashboard-driven QA workflow"
  persona: QA team member using dashboard
  flow: Open dashboard → Select partner → Review docs → Approve/Reject
  acceptance: Dashboard updates app in real-time, correct screen shown

TC_UAT_010:
  name: "Training module customization via dashboard"
  persona: Admin configuring training
  flow: Open dashboard → Edit module → Add question → Save to app → Verify in app
  acceptance: Custom training content appears in app after restart
```

---

## DESIGN_SYSTEM_TOKENS

```yaml
colors:
  primary: "#D9008D"
  primary_light: "#FFE5F6"
  primary_200: "#FFCCED"
  primary_300: "#FFB2E4"
  text: "#161021"
  text_secondary: "#665E75"
  hint: "#A7A1B2"
  surface: "#FAF9FC"
  bg_secondary: "#F1EDF7"
  bg_tertiary: "#E8E4F0"
  border: "#E8E4F0"
  border_input: "#D7D3E0"
  border_focus: "#352D42"
  positive: "#008043"
  positive_100: "#E1FAED"
  positive_200: "#C9F0DD"
  positive_300: "#A5E5C6"
  negative: "#D92130"
  negative_100: "#FFE5E7"
  negative_200: "#FFCCD0"
  warning: "#FF8000"
  warning_dark: "#B85C00"
  warning_100: "#FFE6CC"
  info: "#6D17CE"
  info_100: "#F1E5FF"
  info_200: "#E4CCFF"
  header: "#443152"
  dark: "#161021"

typography:
  family: "Noto Sans, Noto Sans Devanagari"
  headline_large: { size: 24sp, weight: Bold, line_height: 32sp }
  headline_medium: { size: 20sp, weight: Bold, line_height: 28sp }
  title_large: { size: 16sp, weight: Bold, line_height: 24sp }
  title_medium: { size: 14sp, weight: SemiBold, line_height: 20sp }
  body_large: { size: 14sp, weight: Normal, line_height: 20sp }
  body_medium: { size: 12sp, weight: Normal, line_height: 16sp }
  body_small: { size: 10sp, weight: Normal, line_height: 14sp }
  label_large: { size: 16sp, weight: Bold, line_height: 24sp }
  label_medium: { size: 12sp, weight: SemiBold, line_height: 16sp }
  label_small: { size: 10sp, weight: Bold, line_height: 14sp }

corner_radius:
  small: 8dp
  medium: 12dp
  large: 16dp
  pill: 888dp

elevation:
  level_1: "0 1px 3px rgba(0,0,0,0.15)"
  level_2: "0 2px 6px rgba(0,0,0,0.15)"
  level_4: "0 4px 12px rgba(0,0,0,0.15)"
  pink_glow: "0 4px 12px rgba(217,43,144,0.3)"

component_sizes:
  button_height: 48dp
  header_height: 48dp
  status_bar_height: 32dp
  otp_box: { width: 48dp, height: 56dp }
  module_icon_box: 36dp
```

---

## FILE_STRUCTURE

```yaml
repo_root:
  - README.md
  - CLAUDE.md
  - apk/
    - csp_app.apk (1.7 MB)
    - wiom-csp-onboarding-v1.apk (20 MB)
  - prototype/
    - index.html (15 screens + 16 error scenarios + admin dashboard)
  - dashboard/
    - index.html (scenario dashboard + training manager)
    - bridge.py (ADB bridge server on port 8092)
  - app/src/main/java/com/wiom/csp/
    - CspApplication.kt
    - MainActivity.kt
    - data/
      - OnboardingState.kt (global state singleton)
    - util/
      - Strings.kt (bilingual t() helper)
      - Validation.kt (form validation functions)
    - ui/
      - theme/
        - Color.kt
        - Type.kt
        - Shape.kt
        - Theme.kt
      - components/
        - Common.kt (20+ reusable composables)
      - screens/
        - OnboardingHost.kt (screen router + progress strip)
        - Phase1Screens.kt (Screens 0-5)
        - Phase2Screens.kt (Screens 6-10)
        - Phase3Screens.kt (Screens 11-14)
    - DashboardReceiver.kt (BroadcastReceiver for dashboard commands)
```
