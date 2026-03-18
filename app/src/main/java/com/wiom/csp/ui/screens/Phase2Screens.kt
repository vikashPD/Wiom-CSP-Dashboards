package com.wiom.csp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.wiom.csp.data.OnboardingState
import com.wiom.csp.ui.components.*
import com.wiom.csp.ui.theme.*
import com.wiom.csp.util.t

// Screen 6: QA Investigation
@Composable
fun QaInvestigationScreen(onNext: () -> Unit) {
    var qaRejected by remember { mutableStateOf(OnboardingState.qaRejected) }

    Column(modifier = Modifier.fillMaxSize().background(WiomSurface)) {
        StatusBar()
        AppHeader(title = if (qaRejected) t("Application स्टेटस", "Application Status") else t("Application स्टेटस", "Application Status"))
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
        ) {
            if (qaRejected) {
                // Rejected state
                Column(
                    modifier = Modifier.fillMaxWidth().padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Text("😔", fontSize = 40.sp)
                    Spacer(Modifier.height(16.dp))
                    Text(
                        t("Profile अभी स्वीकार नहीं हुई", "Profile not accepted yet"),
                        fontSize = 20.sp, fontWeight = FontWeight.Bold, color = WiomNegative,
                        textAlign = TextAlign.Center,
                    )
                    Spacer(Modifier.height(8.dp))
                    Text(
                        t("चिंता न करें — आपका पैसा सुरक्षित है", "Don't worry — your money is safe"),
                        fontSize = 14.sp, color = WiomTextSec, textAlign = TextAlign.Center,
                    )
                    Spacer(Modifier.height(20.dp))
                    WiomCard(borderColor = WiomNegative, backgroundColor = WiomNegative100) {
                        Text(t("कारण", "Reason"), fontSize = 12.sp, fontWeight = FontWeight.Bold, color = WiomNegative)
                        Spacer(Modifier.height(8.dp))
                        Text(
                            t(
                                "Location इस समय service area में नहीं है। Area infrastructure तैयार होने पर दोबारा apply कर सकते हैं।",
                                "Location is not in the service area currently. You can re-apply once area infrastructure is ready."
                            ),
                            fontSize = 14.sp, color = WiomText, lineHeight = 20.sp,
                        )
                    }
                    Spacer(Modifier.height(8.dp))
                    WiomCard(borderColor = WiomPositive, backgroundColor = WiomPositive100) {
                        Text("🔒 ${t("Refund शुरू हो गया", "Refund initiated")}", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = WiomPositive)
                        Spacer(Modifier.height(8.dp))
                        Text(
                            t("₹2,000 आपके bank account में 5-7 working days में आ जाएगा।", "₹2,000 will be credited to your bank account in 5-7 working days."),
                            fontSize = 14.sp, color = WiomText, lineHeight = 20.sp,
                        )
                        Text("Ref: RFD-2026-0042", fontSize = 12.sp, color = WiomHint)
                    }
                    Spacer(Modifier.height(8.dp))
                    InfoBox("🔔", t("जब area तैयार हो, तो दोबारा apply कर सकते हैं।", "You can re-apply when the area is ready."))
                    Spacer(Modifier.height(12.dp))
                    WiomButton(
                        "← ${t("Approved Path देखें", "View Approved Path")}",
                        onClick = {
                            qaRejected = false
                            OnboardingState.qaRejected = false
                        },
                        isSecondary = true,
                    )
                }
            } else {
                // Pending/Approved state
                Column(
                    modifier = Modifier.fillMaxWidth().padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Text("🔍", fontSize = 40.sp)
                    Spacer(Modifier.height(16.dp))
                    Text(
                        t("Investigation चल रही है", "Investigation in progress"),
                        fontSize = 20.sp, fontWeight = FontWeight.Bold, color = WiomText,
                        textAlign = TextAlign.Center,
                    )
                    Spacer(Modifier.height(8.dp))
                    Text(
                        t("Business/QA team आपकी profile चेक कर रही है", "Business/QA team is reviewing your profile"),
                        fontSize = 14.sp, color = WiomTextSec, textAlign = TextAlign.Center,
                    )
                }
                Spacer(Modifier.height(16.dp))
                ChecklistItem(t("फ़ोन वेरीफाइड", "Phone Verified"))
                ChecklistItem(t("व्यक्तिगत जानकारी", "Personal Information"))
                ChecklistItem(t("लोकेशन सबमिट", "Location Submitted"))
                ChecklistItem(t("KYC दस्तावेज़ वेरीफाइड", "KYC Documents Verified"))
                ChecklistItem(t("₹2,000 रजिस्ट्रेशन फ़ीस", "₹2,000 Registration Fee"))
                ChecklistItem(
                    "QA Investigation",
                    subtitle = t("Business/QA team review कर रही है...", "Business/QA team is reviewing..."),
                    isDone = false,
                    isWaiting = true,
                )
                Column(modifier = Modifier.padding(16.dp)) {
                    InfoBox("⏳", t("Review में 2-3 business days लग सकते हैं। Notification मिलेगा।", "Review may take 2-3 business days. You will be notified."), type = InfoBoxType.WARNING)
                    Spacer(Modifier.height(16.dp))
                    WiomButton(
                        "✓ ${t("QA Approved → Documentation Phase", "QA Approved → Documentation Phase")}",
                        onClick = onNext,
                        backgroundColor = WiomPositive,
                    )
                    Spacer(Modifier.height(8.dp))
                    WiomButton(
                        "✗ ${t("QA Rejected → Refund", "QA Rejected → Refund")}",
                        onClick = {
                            qaRejected = true
                            OnboardingState.qaRejected = true
                        },
                        backgroundColor = WiomNegative100,
                        textColor = WiomNegative,
                    )
                }
            }
        }
    }
}

// Screen 7: Policy + Rate Card
@Composable
fun PolicyRateCardScreen(onNext: () -> Unit, onBack: () -> Unit) {
    Column(modifier = Modifier.fillMaxSize().background(WiomSurface)) {
        StatusBar()
        AppHeader(
            title = t("नीतियां और रेट कार्ड", "Policy & Rate Card"),
            onBack = onBack,
            rightText = t("स्टेप 5", "Step 5")
        )
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            Text(
                t("नीतियां और कमीशन", "Policies & Commission"),
                fontSize = 20.sp, fontWeight = FontWeight.Bold, color = WiomText,
            )
            Spacer(Modifier.height(4.dp))
            Text(
                t("QA Approved ✓ — अब policies समझें", "QA Approved ✓ — now review policies"),
                fontSize = 14.sp, color = WiomTextSec,
            )
            Spacer(Modifier.height(16.dp))

            // Commission Structure
            WiomCard {
                Text(
                    t("कमीशन संरचना", "COMMISSION STRUCTURE"),
                    fontSize = 12.sp, fontWeight = FontWeight.Bold, color = WiomTextSec,
                    letterSpacing = 0.5.sp,
                )
                Spacer(Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    Text(t("नया कनेक्शन", "New Connection"), fontSize = 14.sp, color = WiomTextSec)
                    Text("₹300/${t("कनेक्शन", "conn")}", fontWeight = FontWeight.Bold, color = WiomPositive, fontSize = 14.sp)
                }
                HorizontalDivider(color = WiomBorder)
                Row(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    Text(t("रिचार्ज कमीशन", "Recharge Commission"), fontSize = 14.sp, color = WiomTextSec)
                    Text("₹300", fontWeight = FontWeight.Bold, color = WiomPositive, fontSize = 14.sp)
                }
            }
            Spacer(Modifier.height(8.dp))

            // SLA Terms
            WiomCard {
                Text(
                    t("SLA शर्तें", "SLA TERMS"),
                    fontSize = 12.sp, fontWeight = FontWeight.Bold, color = WiomTextSec,
                    letterSpacing = 0.5.sp,
                )
                Spacer(Modifier.height(8.dp))
                val slaItems = listOf(
                    t("ग्राहक शिकायत: 4 घंटे में समाधान", "Customer complaints: 4hr resolution"),
                    t("कनेक्शन 95%+ चालू रहना चाहिए", "Connection 95%+ to be up and running"),
                    t("उपकरण देखभाल की ज़िम्मेदारी", "Equipment care responsibility"),
                    t("Wiom ब्रांड गाइडलाइन का पालन", "Wiom brand guidelines compliance"),
                )
                slaItems.forEach { item ->
                    Text(
                        "• $item",
                        fontSize = 14.sp, color = WiomTextSec,
                        lineHeight = 22.sp,
                        modifier = Modifier.padding(vertical = 1.dp),
                    )
                }
            }
        }
        BottomBar {
            WiomButton(t("समझ गया, आगे बढ़ें", "Understood, proceed"), onClick = onNext)
        }
    }
}

// Screen 8: Bank + Dedup Check
@Composable
fun BankDedupScreen(onNext: () -> Unit, onBack: () -> Unit) {
    Column(modifier = Modifier.fillMaxSize().background(WiomSurface)) {
        StatusBar()
        AppHeader(
            title = t("Bank वेरिफिकेशन", "Bank Verification"),
            onBack = onBack,
            rightText = t("स्टेप 6", "Step 6")
        )
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            Text(
                t("बैंक खाता जानकारी", "Bank Account Details"),
                fontSize = 20.sp, fontWeight = FontWeight.Bold, color = WiomText,
            )
            Spacer(Modifier.height(4.dp))
            Text(
                t("Commission payment आपके बैंक अकाउंट में आएगा", "Commission payments will be sent to your bank account"),
                fontSize = 14.sp, color = WiomTextSec,
            )
            Spacer(Modifier.height(16.dp))

            FieldLabel(t("अकाउंट होल्डर नाम", "Account Holder Name"))
            WiomTextField(value = "Rajesh Kumar", onValueChange = {}, isVerified = true)

            FieldLabel(t("बैंक नाम", "Bank Name"))
            WiomTextField(value = "State Bank of India", onValueChange = {}, readOnly = true)

            FieldLabel(t("अकाउंट नंबर", "Account Number"))
            WiomTextField(value = "XXXX XXXX 4521", onValueChange = {}, isVerified = true)

            FieldLabel("IFSC Code")
            WiomTextField(value = "SBIN0001234", onValueChange = {}, isVerified = true)

            WiomCard(borderColor = WiomPositive300, backgroundColor = WiomPositive100) {
                Text(
                    "✓ ${t("पेनी ड्रॉप वेरीफाइड", "PENNY DROP VERIFIED")}",
                    fontSize = 12.sp, fontWeight = FontWeight.Bold, color = WiomPositive,
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    t("₹1 क्रेडिट और वेरीफाइड — नाम मैच कन्फ़र्म", "₹1 credited & verified — Name match confirmed"),
                    fontSize = 14.sp, color = WiomTextSec, lineHeight = 20.sp,
                )
            }
            Spacer(Modifier.height(8.dp))
            WiomCard(borderColor = WiomPositive300, backgroundColor = WiomPositive100) {
                Text(
                    "✓ ${t("डीडप चेक पास", "DEDUP CHECK PASSED")}",
                    fontSize = 12.sp, fontWeight = FontWeight.Bold, color = WiomPositive,
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    t("PAN, आधार, GST, Bank — कोई डुप्लिकेट नहीं मिला", "PAN, Aadhaar, GST, Bank — No duplicates found"),
                    fontSize = 14.sp, color = WiomTextSec, lineHeight = 20.sp,
                )
            }
        }
        BottomBar {
            WiomButton(t("अब Agreement करें", "Next: Agreement"), onClick = onNext)
        }
    }
}

// Screen 9: Agreement Signing
@Composable
fun AgreementScreen(onNext: () -> Unit, onBack: () -> Unit) {
    var agreed by remember { mutableStateOf(true) }

    Column(modifier = Modifier.fillMaxSize().background(WiomSurface)) {
        StatusBar()
        AppHeader(
            title = t("पार्टनर एग्रीमेंट", "Partner Agreement"),
            onBack = onBack,
            rightText = t("स्टेप 7", "Step 7")
        )
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            Text(
                t("पार्टनर एग्रीमेंट", "Partner Agreement"),
                fontSize = 20.sp, fontWeight = FontWeight.Bold, color = WiomText,
            )
            Spacer(Modifier.height(4.dp))
            Text(
                t("Review और e-Sign करें", "Review and e-Sign"),
                fontSize = 14.sp, color = WiomTextSec,
            )
            Spacer(Modifier.height(16.dp))

            // Agreement text box
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 140.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .border(1.dp, WiomBorder, RoundedCornerShape(12.dp))
                    .background(Color.White)
                    .padding(12.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                Text(
                    "WIOM CHANNEL SALES PARTNER AGREEMENT\n\n" +
                    "This Agreement is entered between Wiom Technologies Pvt. Ltd. (\"Company\") and the Partner identified herein.\n\n" +
                    "1. SCOPE: Partner shall act as authorized CSP for Wiom internet services in designated territory.\n\n" +
                    "2. RESPONSIBILITIES: Customer acquisition, service activation, first-level support, equipment care.\n\n" +
                    "3. COMMISSION: As per Rate Card shared and acknowledged. Subject to SLA compliance.\n\n" +
                    "4. TERM: 12 months, auto-renewable. 30-day notice for termination.\n\n" +
                    "5. COMPLIANCE: Partner shall comply with ISP license terms (DOT/TRAI) and Wiom brand guidelines.",
                    fontSize = 12.sp, color = WiomTextSec, lineHeight = 18.sp,
                )
            }
            Spacer(Modifier.height(12.dp))

            WiomCard(borderColor = WiomPositive300, backgroundColor = WiomPositive100) {
                VerificationItem(t("ISP License: DOT Compliance वेरीफाइड", "ISP License: DOT Compliance Verified"))
                VerificationItem(t("TRAI Guidelines: स्वीकृत", "TRAI Guidelines: Acknowledged"))
            }
            Spacer(Modifier.height(12.dp))

            // Checkbox
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { agreed = !agreed }
                    .padding(vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                Box(
                    modifier = Modifier
                        .size(24.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(if (agreed) WiomPositive else Color.Transparent)
                        .border(
                            2.dp,
                            if (agreed) WiomPositive else WiomBorderInput,
                            RoundedCornerShape(8.dp)
                        ),
                    contentAlignment = Alignment.Center,
                ) {
                    if (agreed) Text("✓", color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                }
                Text(
                    t("मैंने सभी नियम और शर्तें पढ़ लिए और accept करता हूं", "I have read and accept all terms and conditions"),
                    fontSize = 14.sp, color = WiomText, lineHeight = 20.sp,
                )
            }
            Spacer(Modifier.height(8.dp))
            InfoBox("🔒", t("Aadhaar e-Sign से agreement sign होगा", "Agreement will be signed via Aadhaar e-Sign"))
        }
        BottomBar {
            WiomButton(t("e-Sign करें", "e-Sign"), onClick = onNext, enabled = agreed)
        }
    }
}

// Screen 10: Technical Review
@Composable
fun TechReviewScreen(onNext: () -> Unit) {
    Column(modifier = Modifier.fillMaxSize().background(WiomSurface)) {
        StatusBar()
        AppHeader(title = t("तकनीकी समीक्षा", "Technical Review"))
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
        ) {
            Column(
                modifier = Modifier.fillMaxWidth().padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text("🛠️", fontSize = 40.sp)
                Spacer(Modifier.height(8.dp))
                Text(
                    t("दस्तावेज़ पूरे!", "Documentation complete!"),
                    fontSize = 20.sp, fontWeight = FontWeight.Bold, color = WiomPositive,
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    t("अब Technical Review हो रही है", "Technical Review in progress"),
                    fontSize = 14.sp, color = WiomTextSec,
                )
            }
            Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                WiomCard {
                    Text(
                        t("डिवाइस चेक", "DEVICE CHECK"),
                        fontSize = 12.sp, fontWeight = FontWeight.Bold, color = WiomTextSec,
                        letterSpacing = 0.5.sp,
                    )
                    Spacer(Modifier.height(8.dp))
                    VerificationItem("Samsung Galaxy M34")
                    VerificationItem("Android 14 — ${t("संगत", "Compatible")}")
                    VerificationItem("Wiom OS: ${t("तैयार", "Ready")}")
                }
                Spacer(Modifier.height(8.dp))
                WiomCard {
                    Text(
                        t("इन्फ्रा चेक", "INFRA CHECK"),
                        fontSize = 12.sp, fontWeight = FontWeight.Bold, color = WiomTextSec,
                        letterSpacing = 0.5.sp,
                    )
                    Spacer(Modifier.height(8.dp))
                    FieldLabel(t("इंटरनेट सेटअप", "Internet Setup"))
                    WiomTextField(value = "Fiber (FTTH)", onValueChange = {}, readOnly = true)
                    UploadRow("📷", t("दुकान की फ़ोटो", "Shop Front Photo"), t("रिव्यूड ✓", "Reviewed ✓"), isVerified = true)
                    Spacer(Modifier.height(8.dp))
                    UploadRow("📷", t("राऊटर / उपकरण", "Router / Equipment"), t("रिव्यूड ✓", "Reviewed ✓"), isVerified = true)
                }
                Spacer(Modifier.height(8.dp))
                InfoBox("✓", t("Tech review पूरी! अब onboarding fee भरें।", "Tech review complete! Now pay the onboarding fee."), type = InfoBoxType.SUCCESS)
                Spacer(Modifier.height(8.dp))
            }
        }
        BottomBar {
            WiomButton(t("Onboarding Fee भरें", "Pay Onboarding Fee"), onClick = onNext)
        }
    }
}
