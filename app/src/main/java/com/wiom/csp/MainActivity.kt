package com.wiom.csp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.wiom.csp.ui.screens.OnboardingHost
import com.wiom.csp.ui.theme.WiomCspTheme
import com.wiom.csp.ui.theme.WiomSurface

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            WiomCspTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = WiomSurface,
                ) {
                    OnboardingHost()
                }
            }
        }
    }
}
