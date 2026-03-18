package com.wiom.csp.ui.screens

import androidx.compose.foundation.background
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

// Screen 11: Onboarding Fee ₹20,000
@Composable
fun OnboardingFeeScreen(onNext: () -> Unit) {
    Column(modifier = Modifier.fillMaxSize().background(WiomSurface)) {
        StatusBar()
        AppHeader(title = t("ऑनबोर्डिंग फ़ीस", "Onboarding Fee"))
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            Text(
                t("ऑनबोर्डिंग फ़ीस", "Onboarding Fee"),
                fontSize = 20.sp, fontWeight = FontWeight.Bold, color = WiomText,
            )
            Spacer(Modifier.height(12.dp))
            AmountBox("₹20,000", t("ऑनबोर्डिंग फ़ीस (GST सहित)", "Onboarding Fee (incl. GST)"))
            Spacer(Modifier.height(12.dp))
            WiomCard {
                Text("✓ ${t("विवरण", "Details")}", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = WiomText)
                Spacer(Modifier.height(8.dp))
                FeeRow(t("रजिस्ट्रेशन फ़ीस (भुगतान हुआ)", "Registration Fee (paid)"), "₹2,000")
                HorizontalDivider(color = WiomBorder)
                FeeRow(t("ऑनबोर्डिंग फ़ीस", "Onboarding Fee"), "₹20,000")
                HorizontalDivider(color = WiomBorder)
                FeeRow(t("कुल Investment", "Total Investment"), "₹22,000", isBold = true)
            }
            Spacer(Modifier.height(8.dp))
            InfoBox("✓", t("भुगतान के बाद Training modules unlock होंगे", "Training modules will unlock after payment"), type = InfoBoxType.SUCCESS)
        }
        BottomBar {
            WiomButton("₹20,000 ${t("भुगतान करें", "Pay Now")}", onClick = onNext)
        }
    }
}

@Composable
private fun FeeRow(label: String, amount: String, isBold: Boolean = false) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Text(
            label,
            fontSize = 14.sp,
            color = if (isBold) WiomText else WiomTextSec,
            fontWeight = if (isBold) FontWeight.Bold else FontWeight.Normal,
        )
        Text(
            amount,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = WiomText,
        )
    }
}

// Screen 12: Financial Setup
@Composable
fun FinancialSetupScreen(onNext: () -> Unit) {
    Column(modifier = Modifier.fillMaxSize().background(WiomSurface)) {
        StatusBar()
        AppHeader(
            title = t("फ़ाइनेंशियल सेटअप", "Financial Setup"),
            rightText = t("स्टेप 9", "Step 9"),
        )
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
        ) {
            Column(
                modifier = Modifier.fillMaxWidth().padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text("⚙️", fontSize = 40.sp)
                Spacer(Modifier.height(8.dp))
                Text(
                    t("Backend Setup हो रहा है", "Backend Setup in progress"),
                    fontSize = 20.sp, fontWeight = FontWeight.Bold, color = WiomText,
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    t("System आपका financial account तैयार कर रहा है", "System is preparing your financial account"),
                    fontSize = 14.sp, color = WiomTextSec, textAlign = TextAlign.Center,
                )
            }
            ChecklistItem(
                t("पार्टनर लेजर बना", "Partner Ledger Created"),
                subtitle = t("Commission tracking चालू", "Commission tracking active"),
            )
            ChecklistItem(
                t("RazorpayX Payout लिंक", "RazorpayX Payout Link"),
                subtitle = t("SBI A/C XXXX4521 payouts के लिए लिंक", "SBI A/C XXXX4521 linked for payouts"),
            )
            ChecklistItem(
                t("Zoho Invoice सेटअप", "Zoho Invoice Setup"),
                subtitle = t("हर settlement के लिए auto-invoice", "Auto-invoice for every settlement"),
            )
            ChecklistItem(
                t("Trade Name लॉक", "Trade Name Locked"),
                subtitle = t("\"Rajesh Telecom\" — आधिकारिक नाम", "\"Rajesh Telecom\" — official name"),
            )
            ChecklistItem(
                t("TDS/TCS कॉन्फ़िगरेशन", "TDS/TCS Configuration"),
                subtitle = t("PAN ABCDE1234F — auto deduction सेटअप", "PAN ABCDE1234F — auto deduction setup"),
            )
            Column(modifier = Modifier.padding(16.dp)) {
                WiomCard(borderColor = WiomPositive300, backgroundColor = WiomPositive100) {
                    Text("✓ ${t("सब तैयार है!", "All set!")}", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = WiomPositive)
                    Spacer(Modifier.height(4.dp))
                    Text(
                        t("Ledger, payouts, invoicing, और tax setup पूरा। अब training शुरू!", "Ledger, payouts, invoicing, and tax setup complete. Now start training!"),
                        fontSize = 14.sp, color = WiomTextSec, lineHeight = 20.sp,
                    )
                }
                Spacer(Modifier.height(8.dp))
                InfoBox("💰", t("Commission payouts हर Monday, सीधे आपके बैंक में", "Commission payouts every Monday, directly to your bank"), type = InfoBoxType.SUCCESS)
            }
        }
        BottomBar {
            WiomButton(t("Training शुरू करें", "Start Training"), onClick = onNext)
        }
    }
}

// Screen 13: Training Modules
@Composable
fun TrainingScreen(onNext: () -> Unit) {
    var module3Done by remember { mutableStateOf(false) }

    Column(modifier = Modifier.fillMaxSize().background(WiomSurface)) {
        StatusBar()
        AppHeader(title = "🎓 ${t("ट्रेनिंग", "Training")}", rightText = "2/3")
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            Text(
                t("पार्टनर ट्रेनिंग", "Partner Training"),
                fontSize = 20.sp, fontWeight = FontWeight.Bold, color = WiomText,
            )
            Spacer(Modifier.height(4.dp))
            Text(
                t("पूरा करें, फिर काम शुरू!", "Complete to start working!"),
                fontSize = 14.sp, color = WiomTextSec,
            )
            Spacer(Modifier.height(4.dp))
            WiomProgressBar(if (module3Done) 1f else 0.66f)
            Spacer(Modifier.height(12.dp))

            ModuleCard(
                icon = "📱",
                title = t("App कैसे चलाएं", "How to use the App"),
                subtitle = t("Customer, रिचार्ज, शिकायतें", "Customer, recharge, complaints"),
                isDone = true,
            )
            Spacer(Modifier.height(8.dp))
            ModuleCard(
                icon = "📈",
                title = t("SLA और Exposure", "SLA & Exposure"),
                subtitle = t("नियम, स्तर, प्रभाव", "Rules, levels, impact"),
                isDone = true,
            )
            Spacer(Modifier.height(8.dp))
            ModuleCard(
                icon = "💰",
                title = t("पैसों की बात", "Money Matters"),
                subtitle = "Commission, TDS, TCS",
                isDone = module3Done,
                isCurrent = !module3Done,
                badgeText = t("शुरू करें", "Start"),
                onClick = { module3Done = true },
            )
            Spacer(Modifier.height(12.dp))

            WiomCard {
                Text(t("TDS क्या है?", "What is TDS?"), fontWeight = FontWeight.Bold, fontSize = 14.sp, color = WiomText)
                Spacer(Modifier.height(4.dp))
                Text(
                    t("Tax Deducted at Source — Wiom TDS काटता है, certificate मिलेगा।", "Tax Deducted at Source — Wiom deducts TDS, you will receive a certificate."),
                    fontSize = 12.sp, color = WiomTextSec, lineHeight = 18.sp,
                )
                Spacer(Modifier.height(12.dp))
                Text(t("Invoice कब?", "When is the Invoice?"), fontWeight = FontWeight.Bold, fontSize = 14.sp, color = WiomText)
                Spacer(Modifier.height(4.dp))
                Text(
                    t("हर settlement के साथ auto-generated।", "Auto-generated with every settlement."),
                    fontSize = 12.sp, color = WiomTextSec, lineHeight = 18.sp,
                )
            }
        }
        BottomBar {
            WiomButton(t("Quiz पूरा करें", "Complete Quiz"), onClick = onNext)
        }
    }
}

// Screen 14: Go Live!
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun GoLiveScreen() {
    Column(modifier = Modifier.fillMaxSize().background(WiomSurface)) {
        StatusBar()
        AppHeader(title = t("पार्टनर ऐप होम", "Partner App Home"))
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
        ) {
            Column(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text("🎉", fontSize = 40.sp)
                Spacer(Modifier.height(8.dp))
                Text(
                    t("बधाई हो, राजेश!", "Congratulations, Rajesh!"),
                    fontSize = 24.sp, fontWeight = FontWeight.Bold, color = WiomText,
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    t("आप अब Wiom Partner हैं", "You are now a Wiom Partner"),
                    fontSize = 14.sp, color = WiomTextSec,
                )
                Spacer(Modifier.height(16.dp))
                // Status chips
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(4.dp, Alignment.CenterHorizontally),
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                ) {
                    WiomChip("✓ ${t("रजिस्टर्ड", "Registered")}")
                    WiomChip("✓ ${t("QA Approved", "QA Approved")}")
                    WiomChip("✓ ${t("Bank वेरीफाइड", "Bank Verified")}")
                    WiomChip("✓ ${t("एग्रीमेंट", "Agreement")}")
                    WiomChip("✓ ${t("Tech Review", "Tech Review")}")
                    WiomChip("✓ ${t("फ़ाइनेंशियल सेटअप", "Financial Setup")}")
                    WiomChip("✓ ${t("ट्रेनिंग पूरी", "Trained")}")
                }
            }
            SectionHeader(t("क्विक एक्शन", "QUICK ACTIONS"))
            Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                QuickActionCard("👤", t("Customer जोड़ें", "Add Customer"), t("नया कनेक्शन", "New connection"))
                Spacer(Modifier.height(8.dp))
                QuickActionCard("💰", t("कमाई देखें", "View Earnings"), "Commission, TDS")
                Spacer(Modifier.height(8.dp))
                QuickActionCard("📝", t("टास्क", "Tasks"), t("रिस्टोर, शिकायतें", "Restore, complaints"))
                Spacer(Modifier.height(8.dp))
                QuickActionCard("🎓", t("ट्रेनिंग", "Training"), t("Module दोबारा देखें", "Revisit modules"))
                Spacer(Modifier.height(16.dp))
            }
        }
    }
}
