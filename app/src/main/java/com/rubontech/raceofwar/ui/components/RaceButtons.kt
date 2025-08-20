/*
 * MIT License
 * 
 * Copyright (c) 2024 Rubontech
 */

package com.rubontech.raceofwar.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.platform.LocalConfiguration
import com.rubontech.raceofwar.game.entities.UnitEntity
import com.rubontech.raceofwar.ui.utils.ScreenUtils

/**
 * Race selection buttons component
 */
@Composable
fun RaceButtons(
    selectedRace: UnitEntity.Race,
    onRaceChanged: (UnitEntity.Race) -> Unit,
    modifier: Modifier = Modifier
) {
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp
    val screenHeight = configuration.screenHeightDp
    
    // Calculate responsive sizes
    val buttonSize = ScreenUtils.getResponsiveButtonSize(screenWidth, screenHeight) * 0.4f
    val spacing = ScreenUtils.getResponsiveSpacing(screenWidth, screenHeight)
    val fontSize = ScreenUtils.getResponsiveFontSize(screenWidth, screenHeight, 10f)
    
    Row(
        horizontalArrangement = Arrangement.spacedBy(spacing.dp),
        modifier = modifier.fillMaxWidth()
    ) {
        // Mechanical Legion (Gray)
        RaceButton(
            race = UnitEntity.Race.MECHANICAL_LEGION,
            name = "Mekanik",
            color = Color(0xFF2F4F4F), // Dark Slate Gray
            isSelected = selectedRace == UnitEntity.Race.MECHANICAL_LEGION,
            onClick = { onRaceChanged(UnitEntity.Race.MECHANICAL_LEGION) },
            buttonSize = buttonSize,
            fontSize = fontSize,
            modifier = Modifier.weight(1f)
        )
        
        // Human Empire (Yellow)
        RaceButton(
            race = UnitEntity.Race.HUMAN_EMPIRE,
            name = "İnsan",
            color = Color(0xFFFFD700), // Gold/Yellow
            isSelected = selectedRace == UnitEntity.Race.HUMAN_EMPIRE,
            onClick = { onRaceChanged(UnitEntity.Race.HUMAN_EMPIRE) },
            buttonSize = buttonSize,
            fontSize = fontSize,
            modifier = Modifier.weight(1f)
        )
        
        // Nature Tribe -> Elf (Green)
        RaceButton(
            race = UnitEntity.Race.NATURE_TRIBE,
            name = "Elf",
            color = Color(0xFF228B22), // Forest Green
            isSelected = selectedRace == UnitEntity.Race.NATURE_TRIBE,
            onClick = { onRaceChanged(UnitEntity.Race.NATURE_TRIBE) },
            buttonSize = buttonSize,
            fontSize = fontSize,
            modifier = Modifier.weight(1f)
        )
        
        // Dark Cult (White)
        RaceButton(
            race = UnitEntity.Race.DARK_CULT,
            name = "Karanlık",
            color = Color(0xFFFFFFFF), // White
            isSelected = selectedRace == UnitEntity.Race.DARK_CULT,
            onClick = { onRaceChanged(UnitEntity.Race.DARK_CULT) },
            buttonSize = buttonSize,
            fontSize = fontSize,
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun RaceButton(
    race: UnitEntity.Race,
    name: String,
    color: Color,
    isSelected: Boolean,
    onClick: () -> Unit,
    buttonSize: Float,
    fontSize: Float,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Race button
        Box(
            modifier = Modifier
                .size(buttonSize.dp)
                .clip(CircleShape)
                .background(color)
                .clickable { onClick() }
                .border(
                    width = if (isSelected) 3.dp else 1.dp,
                    color = if (isSelected) Color(0xFFFFD700) else Color.Gray,
                    shape = CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            // Race icon/emoji
            Text(
                text = when (race) {
                    UnitEntity.Race.MECHANICAL_LEGION -> "⚙️"
                    UnitEntity.Race.HUMAN_EMPIRE -> "👑"
                    UnitEntity.Race.NATURE_TRIBE -> "🌿"
                    UnitEntity.Race.DARK_CULT -> "💀"
                },
                fontSize = (fontSize * 1.5).sp,
                color = if (color == Color.White) Color.Black else Color.White
            )
        }
        
        // Race name
        Text(
            text = name,
            fontSize = fontSize.sp,
            fontWeight = FontWeight.Bold,
            color = if (isSelected) Color(0xFFFFD700) else Color.White,
            modifier = Modifier.padding(top = 4.dp)
        )
    }
}

