/*
 * MIT License
 * 
 * Copyright (c) 2024 Rubontech
 */

package com.rubontech.raceofwar.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
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
 * FPS counter component for displaying game performance
 */
@Composable
fun FpsCounter(
    fps: Float,
    modifier: Modifier = Modifier
) {
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp
    val screenHeight = configuration.screenHeightDp
    
    // Calculate responsive sizes
    val fontSize = ScreenUtils.getResponsiveFontSize(screenWidth, screenHeight, 12f)
    val padding = ScreenUtils.getResponsiveSpacing(screenWidth, screenHeight) * 0.5f
    
    Box(
        modifier = modifier
            .background(
                color = Color.Black.copy(alpha = 0.7f),
                shape = RoundedCornerShape(4.dp)
            )
            .padding(padding.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "FPS: ${fps.toInt()}",
            color = when {
                fps >= 55f -> Color.Green
                fps >= 30f -> Color.Yellow
                else -> Color.Red
            },
            fontSize = fontSize.sp,
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.bodySmall
        )
    }
}
