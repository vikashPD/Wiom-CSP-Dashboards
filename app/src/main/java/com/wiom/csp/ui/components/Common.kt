package com.wiom.csp.ui.components

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.wiom.csp.ui.theme.*

// Status bar mockup
@Composable
fun StatusBar() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(32.dp)
            .background(WiomHeader)
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text("9:41", color = WiomSurface, fontSize = 10.sp, fontWeight = FontWeight.SemiBold)
        Text("100%", color = WiomSurface, fontSize = 10.sp, fontWeight = FontWeight.SemiBold)
    }
}

// App header bar
@Composable
fun AppHeader(
    title: String,
    onBack: (() -> Unit)? = null,
    rightText: String? = null
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp)
            .background(WiomHeader)
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        if (onBack != null) {
            IconButton(onClick = onBack, modifier = Modifier.size(24.dp)) {
                Icon(
                    Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = WiomSurface,
                    modifier = Modifier.size(20.dp)
                )
            }
            Spacer(Modifier.width(8.dp))
        }
        Text(
            title,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = WiomSurface,
            modifier = Modifier.weight(1f)
        )
        if (rightText != null) {
            Text(rightText, fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = WiomSurface.copy(alpha = 0.5f))
        }
    }
}

// Primary CTA button
@Composable
fun WiomButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    isSecondary: Boolean = false,
    backgroundColor: Color = if (isSecondary) WiomPrimaryLight else WiomPrimary,
    textColor: Color = if (isSecondary) WiomPrimary else Color.White,
) {
    Button(
        onClick = onClick,
        enabled = enabled,
        modifier = modifier
            .fillMaxWidth()
            .height(48.dp),
        shape = RoundedCornerShape(16.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = backgroundColor,
            contentColor = textColor,
            disabledContainerColor = WiomHint,
            disabledContentColor = WiomSurface,
        ),
        elevation = if (!isSecondary && enabled) ButtonDefaults.buttonElevation(
            defaultElevation = 4.dp
        ) else ButtonDefaults.buttonElevation(defaultElevation = 0.dp)
    ) {
        Text(text, fontWeight = FontWeight.Bold, fontSize = 16.sp)
    }
}

// Bottom bar with CTA
@Composable
fun BottomBar(content: @Composable ColumnScope.() -> Unit) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shadowElevation = 8.dp,
        color = WiomSurface,
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
            content = content
        )
    }
}

// Card component
@Composable
fun WiomCard(
    modifier: Modifier = Modifier,
    borderColor: Color = WiomBorder,
    backgroundColor: Color = Color.White,
    content: @Composable ColumnScope.() -> Unit
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        color = backgroundColor,
        border = androidx.compose.foundation.BorderStroke(1.dp, borderColor),
        shadowElevation = 1.dp,
    ) {
        Column(modifier = Modifier.padding(16.dp), content = content)
    }
}

// Input field label
@Composable
fun FieldLabel(text: String) {
    Text(
        text,
        fontSize = 12.sp,
        fontWeight = FontWeight.SemiBold,
        color = WiomTextSec,
        modifier = Modifier.padding(bottom = 4.dp)
    )
}

// Styled text field
@Composable
fun WiomTextField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String = "",
    modifier: Modifier = Modifier,
    isVerified: Boolean = false,
    readOnly: Boolean = false,
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier
            .fillMaxWidth()
            .padding(bottom = 12.dp),
        readOnly = readOnly,
        placeholder = { Text(placeholder, color = WiomHint) },
        shape = RoundedCornerShape(12.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = if (isVerified) WiomPositive else WiomBorderFocus,
            unfocusedBorderColor = if (isVerified) WiomPositive else WiomBorderInput,
            focusedContainerColor = if (isVerified) WiomPositive100 else Color.White,
            unfocusedContainerColor = if (isVerified) WiomPositive100 else Color.White,
        ),
        trailingIcon = if (isVerified) {
            { Icon(Icons.Default.Check, "Verified", tint = WiomPositive) }
        } else null,
        singleLine = true,
    )
}

// Info box (ibox-p, ibox-g, ibox-o)
@Composable
fun InfoBox(
    icon: String,
    text: String,
    type: InfoBoxType = InfoBoxType.INFO,
    modifier: Modifier = Modifier,
) {
    val (bg, textColor) = when (type) {
        InfoBoxType.INFO -> WiomInfo100 to WiomTextSec
        InfoBoxType.SUCCESS -> WiomPositive100 to Color(0xFF005C30)
        InfoBoxType.WARNING -> WiomWarning200 to WiomWarning700
    }
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(bg)
            .padding(12.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Text(icon, fontSize = 16.sp)
        Text(text, fontSize = 14.sp, color = textColor, lineHeight = 20.sp)
    }
}

enum class InfoBoxType { INFO, SUCCESS, WARNING }

// Upload row
@Composable
fun UploadRow(
    icon: String,
    name: String,
    statusText: String,
    isVerified: Boolean = false,
    onClick: () -> Unit = {},
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(if (isVerified) WiomPositive100 else Color.White)
            .border(
                1.dp,
                if (isVerified) WiomPositive300 else WiomBorder,
                RoundedCornerShape(12.dp)
            )
            .clickable(onClick = onClick)
            .padding(12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column {
            Text("$icon $name", fontWeight = FontWeight.SemiBold, fontSize = 14.sp, color = WiomText)
            Text(
                statusText,
                fontSize = 12.sp,
                color = if (isVerified) WiomPositive else WiomHint,
                fontWeight = if (isVerified) FontWeight.SemiBold else FontWeight.Normal,
            )
        }
        if (isVerified) {
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .background(WiomPositive100)
                    .border(1.dp, WiomPositive, RoundedCornerShape(8.dp))
                    .padding(horizontal = 12.dp, vertical = 6.dp)
            ) {
                Text("✓", color = WiomPositive, fontWeight = FontWeight.Bold, fontSize = 12.sp)
            }
        }
    }
}

// Amount display box
@Composable
fun AmountBox(amount: String, label: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(WiomBgSec)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(amount, fontSize = 32.sp, fontWeight = FontWeight.Bold, color = WiomText)
        Spacer(Modifier.height(4.dp))
        Text(label, fontSize = 14.sp, color = WiomTextSec)
    }
}

// Checklist item
@Composable
fun ChecklistItem(
    text: String,
    subtitle: String? = null,
    isDone: Boolean = true,
    isWaiting: Boolean = false,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Box(
            modifier = Modifier
                .size(24.dp)
                .clip(CircleShape)
                .background(
                    when {
                        isDone -> WiomPositive
                        isWaiting -> WiomWarning200
                        else -> WiomBgSec
                    }
                ),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                if (isDone) "✓" else "⋯",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = if (isDone) Color.White else WiomWarning700,
            )
        }
        Column(modifier = Modifier.weight(1f)) {
            Text(text, fontWeight = FontWeight.SemiBold, fontSize = 14.sp, color = WiomText)
            if (subtitle != null) {
                Text(subtitle, fontSize = 12.sp, color = if (isWaiting) WiomWarning700 else WiomTextSec)
            }
        }
    }
}

// Trust badge
@Composable
fun TrustBadge(icon: String, text: String) {
    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(888.dp))
            .background(WiomPositive100)
            .border(1.dp, WiomPositive300, RoundedCornerShape(888.dp))
            .padding(horizontal = 12.dp, vertical = 6.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(icon, fontSize = 14.sp)
        Text(text, fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = WiomPositive)
    }
}

// Chip
@Composable
fun WiomChip(
    text: String,
    backgroundColor: Color = WiomPositive100,
    textColor: Color = WiomPositive,
) {
    Text(
        text,
        modifier = Modifier
            .clip(RoundedCornerShape(888.dp))
            .background(backgroundColor)
            .padding(horizontal = 10.dp, vertical = 4.dp),
        fontSize = 12.sp,
        fontWeight = FontWeight.SemiBold,
        color = textColor,
    )
}

// Section header
@Composable
fun SectionHeader(text: String) {
    Text(
        text,
        modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
        fontSize = 12.sp,
        fontWeight = FontWeight.Bold,
        color = WiomTextSec,
        letterSpacing = 0.5.sp,
    )
}

// Progress bar
@Composable
fun WiomProgressBar(progress: Float) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(4.dp)
            .clip(RoundedCornerShape(2.dp))
            .background(WiomBorderInput)
    ) {
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth(progress)
                .clip(RoundedCornerShape(2.dp))
                .background(WiomPositive)
        )
    }
}

// Verification row
@Composable
fun VerificationItem(text: String) {
    Row(
        modifier = Modifier.padding(vertical = 3.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text("✓", fontSize = 14.sp, color = WiomPositive, fontWeight = FontWeight.Bold)
        Text(text, fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = WiomPositive)
    }
}

// Module card for training
@Composable
fun ModuleCard(
    icon: String,
    title: String,
    subtitle: String,
    isDone: Boolean = false,
    isCurrent: Boolean = false,
    badgeText: String = "",
    onClick: () -> Unit = {},
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(
                when {
                    isDone -> WiomPositive100
                    isCurrent -> WiomPrimaryLight
                    else -> Color.White
                }
            )
            .border(
                1.dp,
                when {
                    isDone -> WiomPositive300
                    isCurrent -> WiomPrimary
                    else -> WiomBorder
                },
                RoundedCornerShape(16.dp)
            )
            .clickable(onClick = onClick)
            .padding(12.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier = Modifier
                .size(36.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(if (isDone) WiomPositive100 else WiomWarning200),
            contentAlignment = Alignment.Center,
        ) {
            Text(icon, fontSize = 16.sp)
        }
        Column(modifier = Modifier.weight(1f)) {
            Text(title, fontWeight = FontWeight.Bold, fontSize = 14.sp, color = WiomText)
            Text(subtitle, fontSize = 12.sp, color = WiomTextSec)
        }
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(8.dp))
                .background(if (isDone) WiomPositive100 else WiomWarning200)
                .padding(horizontal = 8.dp, vertical = 4.dp)
        ) {
            Text(
                if (isDone) "✓" else badgeText,
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                color = if (isDone) WiomPositive else WiomWarning700,
            )
        }
    }
}

// Quick action card for Go Live screen
@Composable
fun QuickActionCard(icon: String, title: String, subtitle: String, onClick: () -> Unit = {}) {
    WiomCard(modifier = Modifier.clickable(onClick = onClick)) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Text(icon, fontSize = 20.sp)
            Column {
                Text(title, fontWeight = FontWeight.Bold, fontSize = 14.sp, color = WiomText)
                Text(subtitle, fontSize = 12.sp, color = WiomTextSec)
            }
        }
    }
}

// OTP input row
@Composable
fun OtpRow(values: List<String> = listOf("4", "7", "2", "9")) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 20.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp, Alignment.CenterHorizontally),
    ) {
        values.forEach { digit ->
            Box(
                modifier = Modifier
                    .size(48.dp, 56.dp)
                    .border(2.dp, WiomBorderFocus, RoundedCornerShape(12.dp)),
                contentAlignment = Alignment.Center,
            ) {
                Text(digit, fontSize = 20.sp, fontWeight = FontWeight.Bold, color = WiomText)
            }
        }
    }
}

// Stepper dots
@Composable
fun StepperDots(total: Int, current: Int) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(WiomBgSec)
            .padding(8.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp, Alignment.CenterHorizontally),
    ) {
        repeat(total) { i ->
            Box(
                modifier = Modifier
                    .weight(1f, fill = false)
                    .widthIn(max = 32.dp)
                    .height(3.dp)
                    .clip(RoundedCornerShape(2.dp))
                    .background(
                        when {
                            i < current -> WiomPositive
                            i == current -> WiomPrimary
                            else -> WiomBorderInput
                        }
                    )
            )
        }
    }
}
