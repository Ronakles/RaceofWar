/*
 * MIT License
 * 
 * Copyright (c) 2024 Rubontech
 */

package com.rubontech.raceofwar.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rubontech.raceofwar.ui.utils.ScreenUtils

/**
 * XP progress bar showing current XP and next tier threshold
 */
@Composable
fun XPBar(
    currentXP: Int,
    nextXPThreshold: Int?,
    currentTier: String,
    modifier: Modifier = Modifier
) {
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp
    val screenHeight = configuration.screenHeightDp
    
    // Calculate responsive sizes
    val spacing = ScreenUtils.getResponsiveSpacing(screenWidth, screenHeight)
    val fontSize = ScreenUtils.getResponsiveFontSize(screenWidth, screenHeight, 12f)
    val iconFontSize = ScreenUtils.getResponsiveFontSize(screenWidth, screenHeight, 14f)
    val cornerRadius = (spacing * 0.8f).coerceAtLeast(4f)
    
    // Calculate progress
    val progress = if (nextXPThreshold != null && nextXPThreshold > 0) {
        val previousThreshold = when (nextXPThreshold) {
            100 -> 0    // Light to Medium
            300 -> 100  // Medium to Heavy
            else -> 0
        }
        val progressInCurrentTier = currentXP - previousThreshold
        val tierRange = nextXPThreshold - previousThreshold
        (progressInCurrentTier.toFloat() / tierRange.toFloat()).coerceIn(0f, 1f)
    } else {
        1f // All tiers unlocked
    }
    
    Row(
        modifier = modifier
            .background(
                color = Color.Black.copy(alpha = 0.8f),
                shape = RoundedCornerShape(cornerRadius.dp)
            )
            .padding(horizontal = spacing.dp, vertical = spacing.dp),
        horizontalArrangement = Arrangement.spacedBy(spacing.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // XP icon
        Text(
            text = "💎",
            fontSize = iconFontSize.sp
        )
        
        Column(
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            // XP text and tier
            Row(
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "${currentXP} XP",
                    color = Color.Cyan,
                    fontSize = fontSize.sp,
                    fontWeight = FontWeight.Bold
                )
                
                Text(
                    text = "($currentTier)",
                    color = when (currentTier) {
                        "Light" -> Color.Green
                        "Medium" -> Color.Yellow
                        "Heavy" -> Color.Red
                        else -> Color.White
                    },
                    fontSize = (fontSize * 0.8f).sp,
                    fontWeight = FontWeight.Bold
                )
                
                if (nextXPThreshold != null) {
                    Text(
                        text = "→ ${nextXPThreshold}",
                        color = Color.Gray,
                        fontSize = (fontSize * 0.8f).sp
                    )
                }
            }
            
            // Progress bar
            if (nextXPThreshold != null) {
                LinearProgressIndicator(
                    progress = progress,
                    modifier = Modifier
                        .width(120.dp)
                        .height(4.dp)
                        .clip(RoundedCornerShape(2.dp)),
                    color = when (currentTier) {
                        "Light" -> Color.Green
                        "Medium" -> Color.Yellow
                        "Heavy" -> Color.Red
                        else -> Color.Cyan
                    },
                    trackColor = Color.Gray.copy(alpha = 0.3f)
                )
            }
        }
    }
}
