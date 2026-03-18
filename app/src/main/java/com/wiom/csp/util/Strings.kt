package com.wiom.csp.util

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue

object Lang {
    var isHindi by mutableStateOf(true)

    fun toggle() { isHindi = !isHindi }
}

fun t(hi: String, en: String): String = if (Lang.isHindi) hi else en
