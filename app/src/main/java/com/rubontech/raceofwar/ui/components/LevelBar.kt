/*
 * MIT License
 * 
 * Copyright (c) 2024 Rubontech
 */

package com.rubontech.raceofwar.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.platform.LocalConfiguration
import com.rubontech.raceofwar.game.GameConfig
import com.rubontech.raceofwar.ui.utils.ScreenUtils

/**
 * Level and XP progress bar component - Green outline style like example UI
 */
@Composable
fun LevelBar(
    currentLevel: Int,
    currentXP: Int,
    modifier: Modifier = Modifier
) {
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp
    val screenHeight = configuration.screenHeightDp
    
    // Debug XP values
    println("📊 LevelBar - Level: $currentLevel, XP: $currentXP")
    
    // Calculate responsive sizes
    val spacing = ScreenUtils.getResponsiveSpacing(screenWidth, screenHeight)
    val fontSize = ScreenUtils.getResponsiveFontSize(screenWidth, screenHeight, 14f)
    val iconFontSize = ScreenUtils.getResponsiveFontSize(screenWidth, screenHeight, 16f)
    val cornerRadius = (spacing * 0.8f).coerceAtLeast(4f)
    val borderWidth = (spacing * 0.2f).coerceAtLeast(1f)
    val barHeight = (spacing * 2f).coerceAtLeast(8f)
    
    val xpNeeded = currentLevel * GameConfig.XP_NEEDED_PER_LEVEL
    val progress = currentXP.toFloat() / xpNeeded.toFloat()
    val nextLevel = currentLevel + 1
    
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(spacing.dp)
    ) {
        // Level display
        Row(
            horizontalArrangement = Arrangement.spacedBy(spacing.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Seviye $currentLevel",
                color = Color.Green,
                fontSize = fontSize.sp,
                fontWeight = FontWeight.Bold
            )
            
            Text(
                text = "→",
                color = Color.Green,
                fontSize = iconFontSize.sp
            )
            
            Text(
                text = "Seviye $nextLevel",
                color = Color.Gray,
                fontSize = fontSize.sp,
                fontWeight = FontWeight.Bold
            )
        }
        
        // XP Progress Bar
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(barHeight.dp)
                .border(
                    width = borderWidth.dp,
                    color = Color.Green,
                    shape = RoundedCornerShape(cornerRadius.dp)
                )
                .background(
                    color = Color.Black.copy(alpha = 0.3f),
                    shape = RoundedCornerShape(cornerRadius.dp)
                )
        ) {
            // Progress fill
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth(progress)
                    .background(
                        color = Color.Green,
                        shape = RoundedCornerShape(cornerRadius.dp)
                    )
            )
            
            // XP text overlay
            Text(
                text = "$currentXP / $xpNeeded XP",
                color = Color.White,
                fontSize = fontSize.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .padding(horizontal = spacing.dp)
                    .align(Alignment.Center)
            )
        }
    }
}

