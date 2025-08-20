/*
 * MIT License
 * 
 * Copyright (c) 2024 Rubontech
 */

package com.rubontech.raceofwar.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.platform.LocalConfiguration
import com.rubontech.raceofwar.ui.utils.ScreenUtils

/**
 * Gold display component - Vertical stack like example UI
 */
@Composable
fun GoldBar(
    gold: Int,
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
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(spacing.dp)
    ) {
        // Gold display
        Row(
            modifier = Modifier
                .background(
                    color = Color.Black.copy(alpha = 0.8f),
                    shape = RoundedCornerShape(cornerRadius.dp)
                )
                .padding(horizontal = spacing.dp, vertical = spacing.dp),
            horizontalArrangement = Arrangement.spacedBy(spacing.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Gold coin emoji as icon
            Text(
                text = "🪙",
                fontSize = iconFontSize.sp
            )
            
            Text(
                text = "$gold Altın",
                color = Color.Yellow,
                fontSize = fontSize.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}
