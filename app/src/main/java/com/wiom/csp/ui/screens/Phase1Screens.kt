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
import com.wiom.csp.ui.components.*
import com.wiom.csp.ui.theme.*
import com.wiom.csp.util.t

// Screen 0: Phone Entry
@Composable
fun PhoneEntryScreen(onNext: () -> Unit) {
    Column(modifier = Modifier.fillMaxSize().background(WiomSurface)) {
        StatusBar()
        AppHeader(title = "Wiom Partner+")
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            Spacer(Modifier.height(24.dp))
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text("\uD83E\uDD1D", fontSize = 32.sp)
                Spacer(Modifier.height(8.dp))
                Text(
                    t("पार्टनर बनें", "Become a Partner"),
                    fontSize = 24.sp, fontWeight = FontWeight.Bold,
                    color = WiomText, textAlign = TextAlign.Center,
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    t("Wiom के साथ अपना बिज़नेस शुरू करें", "Start your business with Wiom"),
                    fontSize = 14.sp, color = WiomTextSec, textAlign = TextAlign.Center,
                )
            }
            Spacer(Modifier.height(24.dp))
            FieldLabel(t("मोबाइल नंबर", "Mobile Number"))
            // Phone input with +91 prefix
            Row(
                modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Box(
                    modifier = Modifier
                        .height(56.dp)
                        .clip(RoundedCornerShape(topStart = 12.dp, bottomStart = 12.dp))
                        .background(WiomBgSec)
                        .border(1.dp, WiomBorderInput, RoundedCornerShape(topStart = 12.dp, bottomStart = 12.dp))
                        .padding(horizontal = 12.dp),
                    contentAlignment = Alignment.Center,
                ) {
                    Text("+91", fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = WiomTextSec)
                }
                var phone by remember { mutableStateOf("9876543210") }
                OutlinedTextField(
                    value = phone,
                    onValueChange = { phone = it.take(10) },
                    modifier = Modifier.weight(1f).height(56.dp),
                    shape = RoundedCornerShape(topEnd = 12.dp, bottomEnd = 12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedBorderColor = WiomBorderInput,
                        focusedBorderColor = WiomBorderFocus,
                        unfocusedContainerColor = Color.White,
                    ),
                    singleLine = true,
                    placeholder = { Text(t("10 अंकों का नंबर", "10 digit number"), color = WiomHint) }
                )
            }
            InfoBox("🔒", t("OTP आपके नंबर पर भेजा जाएगा", "OTP will be sent to your number"))
        }
        BottomBar {
            WiomButton(t("OTP भेजें", "Send OTP"), onClick = onNext)
        }
    }
}

// Screen 1: OTP Verification
@Composable
fun OtpScreen(onNext: () -> Unit, onBack: () -> Unit) {
    Column(modifier = Modifier.fillMaxSize().background(WiomSurface)) {
        StatusBar()
        AppHeader(title = t("OTP वेरीफाई", "Verify OTP"), onBack = onBack)
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Spacer(Modifier.height(16.dp))
            Text(
                t("OTP डालें", "Enter OTP"),
                fontSize = 20.sp, fontWeight = FontWeight.Bold, color = WiomText,
            )
            Spacer(Modifier.height(4.dp))
            Text(
                t("+91 98765 43210 पर भेजा गया", "Sent to +91 98765 43210"),
                fontSize = 14.sp, color = WiomTextSec,
            )
            OtpRow()
            Text(
                t("OTP दोबारा भेजें", "Resend OTP in") + " 28s",
                fontSize = 12.sp, color = WiomHint,
            )
        }
        BottomBar {
            WiomButton(t("वेरीफाई करें", "Verify"), onClick = onNext)
        }
    }
}

// Screen 2: Personal & Business Info
@Composable
fun PersonalInfoScreen(onNext: () -> Unit, onBack: () -> Unit) {
    Column(modifier = Modifier.fillMaxSize().background(WiomSurface)) {
        StatusBar()
        AppHeader(
            title = t("रजिस्ट्रेशन", "Registration"),
            onBack = onBack,
            rightText = t("स्टेप 1/4", "Step 1/4")
        )
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            Text(
                t("व्यक्तिगत जानकारी", "Personal Information"),
                fontSize = 20.sp, fontWeight = FontWeight.Bold, color = WiomText,
            )
            Spacer(Modifier.height(4.dp))
            Text(
                t("आपकी पहचान और बिज़नेस की जानकारी", "Your identity and business information"),
                fontSize = 14.sp, color = WiomTextSec,
            )
            Spacer(Modifier.height(16.dp))

            FieldLabel(t("पूरा नाम (आधार अनुसार)", "Full Name (as per Aadhaar)"))
            var name by remember { mutableStateOf(t("राजेश कुमार", "Rajesh Kumar")) }
            WiomTextField(value = name, onValueChange = { name = it })

            FieldLabel(t("ईमेल", "Email"))
            var email by remember { mutableStateOf("rajesh@email.com") }
            WiomTextField(value = email, onValueChange = { email = it })

            FieldLabel(t("बिज़नेस प्रकार", "Entity Type"))
            var entityType by remember { mutableStateOf(t("व्यक्तिगत", "Individual")) }
            WiomTextField(value = entityType, onValueChange = { entityType = it }, readOnly = true)

            FieldLabel(t("व्यापार नाम", "Trade Name"))
            var trade by remember { mutableStateOf("Rajesh Telecom") }
            WiomTextField(value = trade, onValueChange = { trade = it })
        }
        BottomBar {
            WiomButton(t("अब लोकेशन बताइए", "Next: Location"), onClick = onNext)
        }
    }
}

// Screen 3: Location
@Composable
fun LocationScreen(onNext: () -> Unit, onBack: () -> Unit) {
    Column(modifier = Modifier.fillMaxSize().background(WiomSurface)) {
        StatusBar()
        AppHeader(
            title = t("रजिस्ट्रेशन", "Registration"),
            onBack = onBack,
            rightText = t("स्टेप 2/4", "Step 2/4")
        )
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            Text(
                t("लोकेशन जानकारी", "Location Information"),
                fontSize = 20.sp, fontWeight = FontWeight.Bold, color = WiomText,
            )
            Spacer(Modifier.height(4.dp))
            Text(
                t("आपकी दुकान/ऑफिस की लोकेशन", "Your shop/office location"),
                fontSize = 14.sp, color = WiomTextSec,
            )
            Spacer(Modifier.height(16.dp))

            FieldLabel(t("राज्य", "State"))
            var state by remember { mutableStateOf("Madhya Pradesh") }
            WiomTextField(value = state, onValueChange = { state = it }, readOnly = true)

            FieldLabel(t("शहर", "City"))
            var city by remember { mutableStateOf("Indore") }
            WiomTextField(value = city, onValueChange = { city = it })

            FieldLabel(t("पिनकोड", "Pincode"))
            var pincode by remember { mutableStateOf("452010") }
            WiomTextField(value = pincode, onValueChange = { pincode = it.take(6) })

            FieldLabel(t("पूरा पता", "Full Address"))
            var address by remember { mutableStateOf("123, Vijay Nagar, Indore") }
            WiomTextField(value = address, onValueChange = { address = it })

            Spacer(Modifier.height(8.dp))
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                TrustBadge("📍", t("GPS कैप्चर हुआ", "GPS Captured"))
                Text("22.71° N, 75.85° E", fontSize = 12.sp, color = WiomHint)
            }
        }
        BottomBar {
            WiomButton(t("अब KYC दस्तावेज़ दें", "Next: KYC Documents"), onClick = onNext)
        }
    }
}

// Screen 4: KYC Documents
@Composable
fun KycScreen(onNext: () -> Unit, onBack: () -> Unit) {
    Column(modifier = Modifier.fillMaxSize().background(WiomSurface)) {
        StatusBar()
        AppHeader(
            title = t("KYC दस्तावेज़", "KYC Documents"),
            onBack = onBack,
            rightText = t("स्टेप 3/4", "Step 3/4")
        )
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            Text(
                t("KYC दस्तावेज़", "KYC Documents"),
                fontSize = 20.sp, fontWeight = FontWeight.Bold, color = WiomText,
            )
            Spacer(Modifier.height(4.dp))
            Text(
                t("अपलोड करें — सिस्टम तुरंत वेरीफाई करेगा", "Upload — system will verify instantly"),
                fontSize = 14.sp, color = WiomTextSec,
            )
            Spacer(Modifier.height(16.dp))

            UploadRow("📄", t("PAN कार्ड", "PAN Card"), t("वेरीफाइड ✓", "Verified ✓"), isVerified = true)
            Spacer(Modifier.height(8.dp))
            UploadRow("📄", t("आधार कार्ड", "Aadhaar Card"), t("वेरीफाइड ✓", "Verified ✓"), isVerified = true)
            Spacer(Modifier.height(8.dp))
            UploadRow("📄", t("GST प्रमाणपत्र", "GST Certificate"), t("वेरीफाइड ✓", "Verified ✓"), isVerified = true)
        }
        BottomBar {
            WiomButton(t("अब रजिस्ट्रेशन फ़ीस भरें", "Next: Registration Fee"), onClick = onNext)
        }
    }
}

// Screen 5: Registration Fee ₹2,000
@Composable
fun RegFeeScreen(onNext: () -> Unit, onBack: () -> Unit) {
    Column(modifier = Modifier.fillMaxSize().background(WiomSurface)) {
        StatusBar()
        AppHeader(
            title = t("रजिस्ट्रेशन फ़ीस", "Registration Fee"),
            onBack = onBack,
            rightText = t("स्टेप 4/4", "Step 4/4")
        )
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            Text(
                t("रजिस्ट्रेशन फ़ीस", "Registration Fee"),
                fontSize = 20.sp, fontWeight = FontWeight.Bold, color = WiomText,
            )
            Spacer(Modifier.height(12.dp))
            AmountBox("₹2,000", t("रजिस्ट्रेशन फ़ीस", "Registration Fee"))
            Spacer(Modifier.height(12.dp))
            WiomCard {
                Text(
                    "ℹ️ ${t("जरूरी जानकारी", "Important Information")}",
                    fontWeight = FontWeight.Bold, fontSize = 14.sp, color = WiomText,
                )
                Spacer(Modifier.height(8.dp))
                Text(
                    t(
                        "भुगतान के बाद आपकी profile Business/QA team द्वारा review की जाएगी।",
                        "After payment, your profile will be reviewed by the Business/QA team."
                    ),
                    fontSize = 14.sp, color = WiomTextSec, lineHeight = 20.sp,
                )
                Spacer(Modifier.height(8.dp))
                TrustBadge("🔒", t("Reject होने पर full refund मिलेगा", "Full refund if rejected"))
            }
            Spacer(Modifier.height(8.dp))
            InfoBox("💰", t("फ़ीस के बाद QA investigation शुरू होगी", "QA investigation will start after fee payment"))
        }
        BottomBar {
            WiomButton("₹2,000 ${t("भुगतान करें", "Pay Now")}", onClick = onNext)
        }
    }
}
