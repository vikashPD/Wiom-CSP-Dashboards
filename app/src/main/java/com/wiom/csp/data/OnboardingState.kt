package com.wiom.csp.data

import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue

object OnboardingState {
    var currentScreen by mutableIntStateOf(0)
    var qaRejected by mutableStateOf(false)
    const val TOTAL_SCREENS = 15

    fun next() {
        if (currentScreen < TOTAL_SCREENS - 1) currentScreen++
    }

    fun prev() {
        if (currentScreen > 0) currentScreen--
    }

    fun goTo(index: Int) {
        if (index in 0 until TOTAL_SCREENS) currentScreen = index
    }

    data class ScreenMeta(
        val phase: String,
        val titleHi: String,
        val titleEn: String
    )

    val screenMetas = listOf(
        ScreenMeta("Phase 1", "मोबाइल नंबर", "Mobile Number"),
        ScreenMeta("Phase 1", "OTP वेरिफिकेशन", "OTP Verification"),
        ScreenMeta("Phase 1", "व्यक्तिगत जानकारी", "Personal Info"),
        ScreenMeta("Phase 1", "लोकेशन", "Location"),
        ScreenMeta("Phase 1", "KYC दस्तावेज़", "KYC Documents"),
        ScreenMeta("Phase 1", "रजिस्ट्रेशन फ़ीस", "Registration Fee"),
        ScreenMeta("Phase 2", "QA Investigation", "QA Investigation"),
        ScreenMeta("Phase 2", "नीतियां और रेट कार्ड", "Policy & Rate Card"),
        ScreenMeta("Phase 2", "Bank + Dedup Check", "Bank + Dedup"),
        ScreenMeta("Phase 2", "एग्रीमेंट", "Agreement"),
        ScreenMeta("Phase 2", "तकनीकी समीक्षा", "Technical Review"),
        ScreenMeta("Phase 3", "ऑनबोर्डिंग फ़ीस", "Onboarding Fee"),
        ScreenMeta("Phase 3", "फ़ाइनेंशियल सेटअप", "Financial Setup"),
        ScreenMeta("Phase 3", "ट्रेनिंग", "Training"),
        ScreenMeta("Phase 3", "Go Live!", "Go Live!"),
    )
}
