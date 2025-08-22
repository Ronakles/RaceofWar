/*
 * MIT License
 * 
 * Copyright (c) 2024 Rubontech
 */

package com.rubontech.raceofwar.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rubontech.raceofwar.ui.utils.ScreenUtils

/**
 * Game time display showing elapsed game time
 */
@Composable
fun GameTimeDisplay(
    gameTimeMinutes: Double,
    modifier: Modifier = Modifier
) {
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp
    val screenHeight = configuration.screenHeightDp
    
    // Calculate responsive sizes
    val spacing = ScreenUtils.getResponsiveSpacing(screenWidth, screenHeight)
    val fontSize = ScreenUtils.getResponsiveFontSize(screenWidth, screenHeight, 14f)
    val iconFontSize = ScreenUtils.getResponsiveFontSize(screenWidth, screenHeight, 16f)
    val cornerRadius = (spacing * 0.8f).coerceAtLeast(4f)
    
    // Format time as MM:SS
    val minutes = gameTimeMinutes.toInt()
    val seconds = ((gameTimeMinutes % 1) * 60).toInt()
    val formattedTime = String.format("%d:%02d", minutes, seconds)
    
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
        // Time icon
        Text(
            text = "⏱️",
            fontSize = iconFontSize.sp
        )
        
        Text(
            text = formattedTime,
            color = Color.White,
            fontSize = fontSize.sp,
            fontWeight = FontWeight.Bold
        )
    }
}
