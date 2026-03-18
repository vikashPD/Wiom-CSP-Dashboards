package com.wiom.csp.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes
import androidx.compose.ui.unit.dp

val WiomShapes = Shapes(
    small = RoundedCornerShape(8.dp),      // r-sm
    medium = RoundedCornerShape(12.dp),     // r-input
    large = RoundedCornerShape(16.dp),      // r-card / r-btn
    extraLarge = RoundedCornerShape(888.dp) // r-pill
)
