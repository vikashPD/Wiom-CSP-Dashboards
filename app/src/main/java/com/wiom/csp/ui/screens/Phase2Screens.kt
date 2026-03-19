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
import com.wiom.csp.data.Scenario
import com.wiom.csp.ui.components.*
import com.wiom.csp.ui.theme.*
import com.wiom.csp.util.t
import kotlinx.coroutines.delay

// Screen 6: QA Investigation
@Composable
fun QaInvestigationScreen(onNext: () -> Unit) {
    val qaRejected = OnboardingState.qaRejected

    Column(modifier = Modifier.fillMaxSize().background(WiomSurface)) {
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
                    Text("\uD83D\uDE14", fontSize = 40.sp)
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
                        Text("\uD83D\uDD12 ${t("Refund शुरू हो गया", "Refund initiated")}", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = WiomPositive)
                        Spacer(Modifier.height(8.dp))
                        Text(
                            t("₹2,000 आपके bank account में 5-7 working days में आ जाएगा।", "₹2,000 will be credited to your bank account in 5-7 working days."),
                            fontSize = 14.sp, color = WiomText, lineHeight = 20.sp,
                        )
                        Text("Ref: RFD-2026-0042", fontSize = 12.sp, color = WiomHint)
                    }
                    Spacer(Modifier.height(8.dp))
                    InfoBox("\uD83D\uDD14", t("जब area तैयार हो, तो दोबारा apply कर सकते हैं।", "You can re-apply when the area is ready."))
                    Spacer(Modifier.height(12.dp))
                    WiomButton(
                        "← ${t("Approved Path देखें", "View Approved Path")}",
                        onClick = {
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
                    Text("\uD83D\uDD0D", fontSize = 40.sp)
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
                }
            }
        }
    }
}

// Screen 7: Policy + Rate Card
@Composable
fun PolicyRateCardScreen(onNext: () -> Unit, onBack: () -> Unit) {
    Column(modifier = Modifier.fillMaxSize().background(WiomSurface)) {
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
    val scenario = OnboardingState.activeScenario
    var isVerifying by remember { mutableStateOf(false) }
    val bankVerified = OnboardingState.bankVerified

    // Bank verification loading simulation
    if (isVerifying) {
        LaunchedEffect(Unit) {
            delay(2000)
            OnboardingState.bankVerified = true
            isVerifying = false
        }
    }

    val allFieldsFilled = OnboardingState.bankAccountHolder.isNotBlank() &&
            OnboardingState.bankName.isNotBlank() &&
            OnboardingState.bankAccountNumber.isNotBlank() &&
            OnboardingState.bankIfsc.isNotBlank()

    Box(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize().background(WiomSurface)) {
            AppHeader(
                title = t("Bank वेरिफिकेशन", "Bank Verification"),
                onBack = onBack,
                rightText = t("स्टेप 6", "Step 6")
            )

            when (scenario) {
                Scenario.BANK_PENNYDROP_FAIL -> {
                    // ─── BANK_PENNYDROP_FAIL ───
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
                        WiomTextField(value = "Rajesh Kumar", onValueChange = {})

                        FieldLabel(t("बैंक नाम", "Bank Name"))
                        WiomTextField(value = "State Bank of India", onValueChange = {}, readOnly = true)

                        FieldLabel(t("अकाउंट नंबर", "Account Number"))
                        WiomTextField(
                            value = "XXXX XXXX 4521",
                            onValueChange = {},
                            isError = true,
                            errorMessage = t("अकाउंट नंबर गलत हो सकता है", "Account number may be incorrect"),
                        )

                        FieldLabel("IFSC Code")
                        WiomTextField(value = "SBIN0001234", onValueChange = {})

                        Spacer(Modifier.height(8.dp))
                        ErrorCard(
                            icon = "🏦",
                            titleHi = "₹1 credit नहीं हो पाया",
                            titleEn = "Penny drop failed",
                            messageHi = "अकाउंट नंबर गलत हो सकता है या बैंक सर्वर डाउन है।",
                            messageEn = "Account number may be wrong or bank server is down.",
                            type = "error",
                        )
                        Spacer(Modifier.height(12.dp))
                        WiomButton(t("अकाउंट नंबर ठीक करें", "Fix Account Number"), onClick = {})
                    }
                }
                Scenario.BANK_NAME_MISMATCH -> {
                    // ─── BANK_NAME_MISMATCH ───
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
                        Spacer(Modifier.height(16.dp))

                        FieldLabel(t("अकाउंट होल्डर नाम", "Account Holder Name"))
                        WiomTextField(value = "Rajesh Kumar", onValueChange = {})

                        FieldLabel(t("बैंक नाम", "Bank Name"))
                        WiomTextField(value = "State Bank of India", onValueChange = {}, readOnly = true)

                        FieldLabel(t("अकाउंट नंबर", "Account Number"))
                        WiomTextField(value = "XXXX XXXX 4521", onValueChange = {}, isVerified = true)

                        FieldLabel("IFSC Code")
                        WiomTextField(value = "SBIN0001234", onValueChange = {}, isVerified = true)

                        Spacer(Modifier.height(8.dp))
                        WiomCard(borderColor = WiomWarning, backgroundColor = WiomWarning200) {
                            Text(
                                t("Bank में नाम: Rajesh Kumar Sharma\nआपने डाला: Rajesh Kumar",
                                  "Name in Bank: Rajesh Kumar Sharma\nYou entered: Rajesh Kumar"),
                                fontSize = 14.sp, color = WiomText, lineHeight = 20.sp,
                            )
                        }
                        Spacer(Modifier.height(8.dp))
                        ErrorCard(
                            icon = "👤",
                            titleHi = "Penny Drop — नाम मेल नहीं खाता",
                            titleEn = "Penny Drop — Name Mismatch",
                            messageHi = "Bank account का नाम और आपका नाम अलग है।",
                            messageEn = "Bank account name and your name are different.",
                            type = "warning",
                        )
                        Spacer(Modifier.height(12.dp))
                        WiomButton(t("नाम ठीक करें और Retry", "Fix Name and Retry"), onClick = {})
                    }
                }
                Scenario.DEDUP_FOUND -> {
                    // ─── DEDUP_FOUND ───
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
                        Spacer(Modifier.height(16.dp))

                        // Penny drop verified
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
                        Spacer(Modifier.height(12.dp))
                        ErrorCard(
                            icon = "🔍",
                            titleHi = "DEDUP CHECK — Match Found!",
                            titleEn = "DEDUP CHECK — Match Found!",
                            messageHi = "इस PAN और Bank Account से पहले से एक पार्टनर रजिस्टर्ड है।",
                            messageEn = "A partner is already registered with this PAN and Bank Account.",
                            type = "error",
                        )
                        Spacer(Modifier.height(8.dp))
                        WiomCard {
                            Text(t("मैच विवरण", "MATCH DETAILS"), fontSize = 12.sp, fontWeight = FontWeight.Bold, color = WiomTextSec, letterSpacing = 0.5.sp)
                            Spacer(Modifier.height(8.dp))
                            Row(Modifier.fillMaxWidth().padding(vertical = 2.dp), horizontalArrangement = Arrangement.SpaceBetween) {
                                Text("Partner ID", fontSize = 13.sp, color = WiomTextSec)
                                Text("CSP-0031", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = WiomText)
                            }
                            Row(Modifier.fillMaxWidth().padding(vertical = 2.dp), horizontalArrangement = Arrangement.SpaceBetween) {
                                Text("Name", fontSize = 13.sp, color = WiomTextSec)
                                Text("Rajesh K.", fontSize = 13.sp, color = WiomText)
                            }
                            Row(Modifier.fillMaxWidth().padding(vertical = 2.dp), horizontalArrangement = Arrangement.SpaceBetween) {
                                Text("City", fontSize = 13.sp, color = WiomTextSec)
                                Text("Indore", fontSize = 13.sp, color = WiomText)
                            }
                            Row(Modifier.fillMaxWidth().padding(vertical = 2.dp), horizontalArrangement = Arrangement.SpaceBetween) {
                                Text("Match On", fontSize = 13.sp, color = WiomTextSec)
                                Text("PAN + Bank A/C", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = WiomNegative)
                            }
                        }
                        Spacer(Modifier.height(8.dp))
                        InfoBox("\uD83D\uDCDE", t("Wiom सपोर्ट से बात करें", "Talk to Wiom support"))
                        Spacer(Modifier.height(12.dp))
                        WiomButton(t("हमसे बात करें", "Talk to us"), onClick = {})
                    }
                }
                else -> {
                    // ─── Normal happy path (interactive) ───
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
                        WiomTextField(
                            value = OnboardingState.bankAccountHolder,
                            onValueChange = { OnboardingState.bankAccountHolder = it },
                            placeholder = t("नाम डालें", "Enter name"),
                            isVerified = bankVerified,
                        )

                        FieldLabel(t("बैंक नाम", "Bank Name"))
                        WiomTextField(
                            value = OnboardingState.bankName,
                            onValueChange = { OnboardingState.bankName = it },
                            placeholder = t("बैंक नाम डालें", "Enter bank name"),
                            isVerified = bankVerified,
                        )

                        FieldLabel(t("अकाउंट नंबर", "Account Number"))
                        WiomTextField(
                            value = OnboardingState.bankAccountNumber,
                            onValueChange = { OnboardingState.bankAccountNumber = it },
                            placeholder = t("अकाउंट नंबर डालें", "Enter account number"),
                            isVerified = bankVerified,
                        )

                        FieldLabel("IFSC Code")
                        WiomTextField(
                            value = OnboardingState.bankIfsc,
                            onValueChange = { OnboardingState.bankIfsc = it },
                            placeholder = t("IFSC कोड डालें", "Enter IFSC code"),
                            isVerified = bankVerified,
                        )

                        if (bankVerified) {
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
                        } else if (!allFieldsFilled) {
                            InfoBox("📝", t("सभी बैंक डिटेल्स भरें", "Fill all bank details to proceed"))
                        }
                    }
                    BottomBar {
                        if (bankVerified) {
                            WiomButton(t("अब Agreement करें", "Next: Agreement"), onClick = onNext)
                        } else {
                            WiomButton(
                                t("Penny Drop Verify करें", "Verify via Penny Drop"),
                                onClick = { isVerifying = true },
                                enabled = allFieldsFilled,
                            )
                        }
                    }
                }
            }
        }

        // Loading overlay
        LoadingOverlay(
            messageHi = "Penny Drop वेरीफाई हो रहा है...",
            messageEn = "Verifying via Penny Drop...",
            isVisible = isVerifying,
        )
    }
}

// Screen 9: Agreement Signing
@Composable
fun AgreementScreen(onNext: () -> Unit, onBack: () -> Unit) {
    val scenario = OnboardingState.activeScenario
    var agreed by remember { mutableStateOf(true) }
    var isEsigning by remember { mutableStateOf(false) }

    // e-Sign loading simulation
    if (isEsigning) {
        LaunchedEffect(Unit) {
            delay(2000)
            isEsigning = false
            onNext()
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize().background(WiomSurface)) {
            AppHeader(
                title = t("पार्टनर एग्रीमेंट", "Partner Agreement"),
                onBack = onBack,
                rightText = t("स्टेप 7", "Step 7")
            )

            if (scenario == Scenario.ESIGN_FAILED) {
                // ─── ESIGN_FAILED error screen ───
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .verticalScroll(rememberScrollState())
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Spacer(Modifier.height(32.dp))
                    Text("✍\uFE0F", fontSize = 40.sp)
                    Spacer(Modifier.height(16.dp))
                    Text(
                        t("e-Sign नहीं हो पाया", "e-Sign could not be completed"),
                        fontSize = 20.sp, fontWeight = FontWeight.Bold, color = WiomWarning700,
                        textAlign = TextAlign.Center,
                    )
                    Spacer(Modifier.height(16.dp))
                    ErrorCard(
                        icon = "✍\uFE0F",
                        titleHi = "Aadhaar e-Sign कनेक्शन एरर",
                        titleEn = "Aadhaar e-Sign Connection Error",
                        messageHi = "UIDAI सर्वर से कनेक्ट नहीं हो पाया। कृपया दोबारा कोशिश करें।",
                        messageEn = "Could not connect to UIDAI server. Please try again.",
                        type = "warning",
                    )
                    Spacer(Modifier.height(12.dp))
                    WiomCard {
                        Text(t("क्या करें?", "What to do?"), fontWeight = FontWeight.Bold, fontSize = 14.sp, color = WiomText)
                        Spacer(Modifier.height(8.dp))
                        Text("1. ${t("इंटरनेट कनेक्शन चेक करें", "Check internet connection")}", fontSize = 14.sp, color = WiomTextSec, lineHeight = 22.sp)
                        Text("2. ${t("2-3 मिनट इंतज़ार करें", "Wait 2-3 minutes")}", fontSize = 14.sp, color = WiomTextSec, lineHeight = 22.sp)
                        Text("3. ${t("दोबारा कोशिश करें", "Retry")}", fontSize = 14.sp, color = WiomTextSec, lineHeight = 22.sp)
                    }
                    Spacer(Modifier.height(16.dp))
                    WiomButton(t("e-Sign Retry करें", "Retry e-Sign"), onClick = {})
                    Spacer(Modifier.height(8.dp))
                    WiomButton(t("हमसे बात करें", "Talk to us"), onClick = {}, isSecondary = true)
                }
            } else {
                // ─── Normal happy path ───
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
                    InfoBox("\uD83D\uDD12", t("Aadhaar e-Sign से agreement sign होगा", "Agreement will be signed via Aadhaar e-Sign"))
                }
                BottomBar {
                    WiomButton(t("e-Sign करें", "e-Sign"), onClick = { isEsigning = true }, enabled = agreed && !isEsigning)
                }
            }
        }

        // Loading overlay
        LoadingOverlay(
            messageHi = "e-Sign प्रोसेस हो रहा है...",
            messageEn = "Processing e-Sign...",
            isVisible = isEsigning,
        )
    }
}

// Screen 10: Technical Review
@Composable
fun TechReviewScreen(onNext: () -> Unit) {
    val scenario = OnboardingState.activeScenario
    val shopPhotoUploaded = OnboardingState.shopPhotoUploaded
    val equipmentReviewed = OnboardingState.equipmentReviewed
    val internetSetupType = OnboardingState.internetSetupType
    var dropdownExpanded by remember { mutableStateOf(false) }
    val internetOptions = listOf("Fiber (FTTH)", "Cable", "Wireless")

    val allDone = shopPhotoUploaded && equipmentReviewed && internetSetupType.isNotBlank()

    Column(modifier = Modifier.fillMaxSize().background(WiomSurface)) {
        AppHeader(title = t("तकनीकी समीक्षा", "Technical Review"))

        if (scenario == Scenario.TECH_DEVICE_INCOMPATIBLE) {
            // ─── TECH_DEVICE_INCOMPATIBLE error screen ───
            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Spacer(Modifier.height(24.dp))
                Text("\uD83D\uDCF5", fontSize = 40.sp)
                Spacer(Modifier.height(16.dp))
                Text(
                    t("Device Compatible नहीं है", "Device is not compatible"),
                    fontSize = 20.sp, fontWeight = FontWeight.Bold, color = WiomNegative,
                    textAlign = TextAlign.Center,
                )
                Spacer(Modifier.height(16.dp))
                ErrorCard(
                    icon = "📵",
                    titleHi = "DEVICE CHECK FAILED",
                    titleEn = "DEVICE CHECK FAILED",
                    messageHi = "आपका device minimum requirements पूरी नहीं करता।",
                    messageEn = "Your device does not meet minimum requirements.",
                    type = "error",
                ) {
                    Spacer(Modifier.height(4.dp))
                    HorizontalDivider(color = WiomNegative200)
                    Spacer(Modifier.height(4.dp))
                    Text("Samsung Galaxy J2 Core", fontSize = 13.sp, color = WiomText)
                    Text("Android 8.1 (Min: 11)", fontSize = 13.sp, color = WiomNegative)
                    Text("RAM 1GB (Min: 3GB)", fontSize = 13.sp, color = WiomNegative)
                }
                Spacer(Modifier.height(12.dp))
                WiomCard {
                    Text(t("Recommended Devices", "Recommended Devices"), fontWeight = FontWeight.Bold, fontSize = 14.sp, color = WiomText)
                    Spacer(Modifier.height(8.dp))
                    Text("• Samsung M34", fontSize = 14.sp, color = WiomTextSec)
                    Text("• Redmi Note 12", fontSize = 14.sp, color = WiomTextSec)
                    Text("• Realme Narzo 60", fontSize = 14.sp, color = WiomTextSec)
                }
                Spacer(Modifier.height(8.dp))
                InfoBox("\uD83D\uDCF1", t("नया device लेने के बाद यहीं से आगे बढ़ सकते हैं", "You can continue from here after getting a new device"))
                Spacer(Modifier.height(16.dp))
                WiomButton(t("Device बदलें और Retry", "Change Device and Retry"), onClick = {})
            }
        } else {
            // ─── Normal happy path (interactive) ───
            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth().padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Text("\uD83D\uDEE0\uFE0F", fontSize = 40.sp)
                    Spacer(Modifier.height(8.dp))
                    Text(
                        if (allDone) t("दस्तावेज़ पूरे!", "Documentation complete!")
                        else t("Technical Review", "Technical Review"),
                        fontSize = 20.sp, fontWeight = FontWeight.Bold,
                        color = if (allDone) WiomPositive else WiomText,
                    )
                    Spacer(Modifier.height(4.dp))
                    Text(
                        if (allDone) t("अब Technical Review हो रही है", "Technical Review in progress")
                        else t("फ़ोटो अपलोड करें और setup चुनें", "Upload photos and select setup"),
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

                        // Internet Setup Dropdown
                        FieldLabel(t("इंटरनेट सेटअप", "Internet Setup"))
                        Box {
                            WiomTextField(
                                value = if (internetSetupType.isNotBlank()) internetSetupType else "",
                                onValueChange = {},
                                readOnly = true,
                                placeholder = t("चुनें", "Select"),
                                isVerified = internetSetupType.isNotBlank(),
                                modifier = Modifier.clickable { dropdownExpanded = true },
                            )
                            DropdownMenu(
                                expanded = dropdownExpanded,
                                onDismissRequest = { dropdownExpanded = false },
                            ) {
                                internetOptions.forEach { option ->
                                    DropdownMenuItem(
                                        text = { Text(option, fontSize = 14.sp) },
                                        onClick = {
                                            OnboardingState.internetSetupType = option
                                            dropdownExpanded = false
                                        },
                                    )
                                }
                            }
                        }

                        // Shop photo upload
                        UploadRow(
                            "\uD83D\uDCF7",
                            t("दुकान की फ़ोटो", "Shop Front Photo"),
                            if (shopPhotoUploaded) t("रिव्यूड ✓", "Reviewed ✓")
                            else t("अपलोड करें", "Upload"),
                            isVerified = shopPhotoUploaded,
                            onClick = { OnboardingState.shopPhotoUploaded = true },
                        )
                        Spacer(Modifier.height(8.dp))

                        // Router/Equipment upload
                        UploadRow(
                            "\uD83D\uDCF7",
                            t("राऊटर / उपकरण", "Router / Equipment"),
                            if (equipmentReviewed) t("रिव्यूड ✓", "Reviewed ✓")
                            else t("अपलोड करें", "Upload"),
                            isVerified = equipmentReviewed,
                            onClick = { OnboardingState.equipmentReviewed = true },
                        )
                    }
                    Spacer(Modifier.height(8.dp))
                    if (allDone) {
                        InfoBox("✓", t("Tech review पूरी! अब onboarding fee भरें।", "Tech review complete! Now pay the onboarding fee."), type = InfoBoxType.SUCCESS)
                    } else {
                        InfoBox("📝", t("सभी आइटम पूरे करें", "Complete all items to proceed"))
                    }
                    Spacer(Modifier.height(8.dp))
                }
            }
            BottomBar {
                WiomButton(t("Onboarding Fee भरें", "Pay Onboarding Fee"), onClick = onNext, enabled = allDone)
            }
        }
    }
}
