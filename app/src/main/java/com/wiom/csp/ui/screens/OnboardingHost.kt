package com.wiom.csp.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.wiom.csp.data.OnboardingState
import com.wiom.csp.ui.theme.*
import com.wiom.csp.util.Lang
import com.wiom.csp.util.t

@Composable
fun OnboardingHost() {
    val screen = OnboardingState.currentScreen
    val meta = OnboardingState.screenMetas[screen]

    Column(modifier = Modifier.fillMaxSize().background(WiomDark)) {
        // Top bar with language toggle
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(WiomDark)
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                "Wiom CSP Onboarding",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = WiomPrimary,
            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                Text(
                    "${meta.phase} — ${t(meta.titleHi, meta.titleEn)}",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = WiomHint,
                )
                // Language toggle
                Button(
                    onClick = { Lang.toggle() },
                    modifier = Modifier.height(28.dp),
                    shape = RoundedCornerShape(888.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = WiomPrimaryLight,
                        contentColor = WiomPrimary,
                    ),
                    contentPadding = PaddingValues(horizontal = 12.dp),
                ) {
                    Text(
                        "हि / En",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                    )
                }
            }
        }

        // Progress strip
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(WiomDark)
                .padding(horizontal = 16.dp, vertical = 4.dp),
            horizontalArrangement = Arrangement.spacedBy(2.dp),
        ) {
            repeat(OnboardingState.TOTAL_SCREENS) { i ->
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(3.dp)
                        .clip(RoundedCornerShape(2.dp))
                        .background(
                            when {
                                i < screen -> WiomPositive
                                i == screen -> WiomPrimary
                                else -> WiomNeutral800.copy(alpha = 0.3f)
                            }
                        )
                )
            }
        }

        // Screen content
        AnimatedContent(
            targetState = screen,
            modifier = Modifier.weight(1f),
            transitionSpec = {
                if (targetState > initialState) {
                    slideInHorizontally { it / 3 } + fadeIn() togetherWith
                        slideOutHorizontally { -it / 3 } + fadeOut()
                } else {
                    slideInHorizontally { -it / 3 } + fadeIn() togetherWith
                        slideOutHorizontally { it / 3 } + fadeOut()
                }
            },
            label = "screen_transition",
        ) { targetScreen ->
            when (targetScreen) {
                0 -> PhoneEntryScreen(onNext = { OnboardingState.next() })
                1 -> OtpScreen(onNext = { OnboardingState.next() }, onBack = { OnboardingState.prev() })
                2 -> PersonalInfoScreen(onNext = { OnboardingState.next() }, onBack = { OnboardingState.prev() })
                3 -> LocationScreen(onNext = { OnboardingState.next() }, onBack = { OnboardingState.prev() })
                4 -> KycScreen(onNext = { OnboardingState.next() }, onBack = { OnboardingState.prev() })
                5 -> RegFeeScreen(onNext = { OnboardingState.next() }, onBack = { OnboardingState.prev() })
                6 -> QaInvestigationScreen(onNext = { OnboardingState.next() })
                7 -> PolicyRateCardScreen(onNext = { OnboardingState.next() }, onBack = { OnboardingState.prev() })
                8 -> BankDedupScreen(onNext = { OnboardingState.next() }, onBack = { OnboardingState.prev() })
                9 -> AgreementScreen(onNext = { OnboardingState.next() }, onBack = { OnboardingState.prev() })
                10 -> TechReviewScreen(onNext = { OnboardingState.next() })
                11 -> OnboardingFeeScreen(onNext = { OnboardingState.next() })
                12 -> FinancialSetupScreen(onNext = { OnboardingState.next() })
                13 -> TrainingScreen(onNext = { OnboardingState.next() })
                14 -> GoLiveScreen()
            }
        }
    }
}
