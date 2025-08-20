/*
 * MIT License
 * 
 * Copyright (c) 2024 Rubontech
 */

package com.rubontech.raceofwar.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.platform.LocalConfiguration
import com.rubontech.raceofwar.game.entities.UnitEntity
import com.rubontech.raceofwar.ui.utils.ScreenUtils

/**
 * Race information display component
 */
@Composable
fun RaceInfo(
    selectedRace: UnitEntity.Race,
    modifier: Modifier = Modifier
) {
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp
    val screenHeight = configuration.screenHeightDp
    
    // Calculate responsive sizes
    val spacing = ScreenUtils.getResponsiveSpacing(screenWidth, screenHeight)
    val titleFontSize = ScreenUtils.getResponsiveFontSize(screenWidth, screenHeight, 9f)
    val descriptionFontSize = ScreenUtils.getResponsiveFontSize(screenWidth, screenHeight, 7f)
    val emojiFontSize = ScreenUtils.getResponsiveFontSize(screenWidth, screenHeight, 12f)
    val raceInfo = when (selectedRace) {
        UnitEntity.Race.MECHANICAL_LEGION -> RaceData(
            name = "Mekanik Lejyon",
            emoji = "🤖",
            description = "Robot ve mech birimleri",
            color = Color(0xFF2F4F4F)
        )
        UnitEntity.Race.HUMAN_EMPIRE -> RaceData(
            name = "İnsan İmparatorluğu",
            emoji = "👑",
            description = "Geleneksel askeri birimler",
            color = Color(0xFF8B4513)
        )
        UnitEntity.Race.NATURE_TRIBE -> RaceData(
            name = "Doğa Kabilesi",
            emoji = "🌿",
            description = "Doğal ve organik birimler",
            color = Color(0xFF228B22)
        )
        UnitEntity.Race.DARK_CULT -> RaceData(
            name = "Karanlık Kült",
            emoji = "💀",
            description = "Karanlık güçler",
            color = Color(0xFF4B0082)
        )
    }
    
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = Color.Black.copy(alpha = 0.7f)
        )
    ) {
        Row(
            modifier = Modifier.padding(spacing.dp),
            horizontalArrangement = Arrangement.spacedBy(spacing.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = raceInfo.emoji,
                fontSize = emojiFontSize.sp
            )
            
            Column {
                Text(
                    text = raceInfo.name,
                    color = Color.White,
                    fontSize = titleFontSize.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = raceInfo.description,
                    color = Color.White.copy(alpha = 0.8f),
                    fontSize = descriptionFontSize.sp
                )
            }
        }
    }
}

private data class RaceData(
    val name: String,
    val emoji: String,
    val description: String,
    val color: Color
)

