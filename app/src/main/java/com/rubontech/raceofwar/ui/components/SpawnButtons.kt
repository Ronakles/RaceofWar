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
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.layout.ContentScale
import androidx.compose.foundation.Image
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.graphics.asImageBitmap
import android.graphics.BitmapFactory
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.foundation.clickable
import androidx.compose.foundation.border
import com.rubontech.raceofwar.game.GameConfig
import com.rubontech.raceofwar.game.entities.UnitEntity
import com.rubontech.raceofwar.ui.utils.ScreenUtils

/**
 * Unit spawn buttons component with PNG asset backgrounds
 */
@Composable
fun SpawnButtons(
    gold: Int,
    currentLevel: Int,
    selectedRace: UnitEntity.Race = UnitEntity.Race.MECHANICAL_LEGION,
    isUnitUnlocked: (UnitEntity.UnitType) -> Boolean,
    getUnlockLevel: (UnitEntity.UnitType) -> Int,
    onSpawnUnit: (UnitEntity.UnitType) -> Unit,
    onRaceChanged: (UnitEntity.Race) -> Unit = {},
    modifier: Modifier = Modifier
) {
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp
    val screenHeight = configuration.screenHeightDp
    
    // Calculate responsive sizes
    val buttonSize = ScreenUtils.getResponsiveButtonSize(screenWidth, screenHeight)
    val spacing = ScreenUtils.getResponsiveSpacing(screenWidth, screenHeight)
    val unitButtonSize = buttonSize * 0.7f // Unit buttons are larger
    val smallFontSize = ScreenUtils.getResponsiveFontSize(screenWidth, screenHeight, 12f)
    

    
    // Unit buttons only - horizontal row with 5 units
    Row(
        horizontalArrangement = Arrangement.spacedBy(spacing.dp),
        modifier = modifier.fillMaxWidth()
    ) {
        // 1. Heavy Weapon
        if (selectedRace == UnitEntity.Race.NATURE_TRIBE) {
            // Elf race - use PNG asset
            UnitButtonWithAsset(
                assetName = "heavyweapnelfbirimi.png",
                cost = GameConfig.HEAVY_WEAPON_COST,
                isUnlocked = currentLevel >= GameConfig.HEAVY_WEAPON_UNLOCK_LEVEL,
                unlockLevel = GameConfig.HEAVY_WEAPON_UNLOCK_LEVEL,
                onClick = { onSpawnUnit(UnitEntity.UnitType.HEAVY_WEAPON) },
                enabled = gold >= GameConfig.HEAVY_WEAPON_COST && currentLevel >= GameConfig.HEAVY_WEAPON_UNLOCK_LEVEL,
                modifier = Modifier.weight(1f)
            )
        } else if (selectedRace == UnitEntity.Race.MECHANICAL_LEGION) {
            // Mechanical Legion race - use Heavy Weapon PNG asset
            UnitButtonWithAsset(
                assetName = "mekaniklejyonheavyweaponbirimi.png",
                cost = GameConfig.HEAVY_WEAPON_COST,
                isUnlocked = currentLevel >= GameConfig.HEAVY_WEAPON_UNLOCK_LEVEL,
                unlockLevel = GameConfig.HEAVY_WEAPON_UNLOCK_LEVEL,
                onClick = { onSpawnUnit(UnitEntity.UnitType.HEAVY_WEAPON) },
                enabled = gold >= GameConfig.HEAVY_WEAPON_COST && currentLevel >= GameConfig.HEAVY_WEAPON_UNLOCK_LEVEL,
                modifier = Modifier.weight(1f)
            )
        } else {
            // Other races - use simple shape
            UnitButtonPlaceholder(
                icon = "🔨",
                cost = GameConfig.HEAVY_WEAPON_COST,
                isUnlocked = currentLevel >= GameConfig.HEAVY_WEAPON_UNLOCK_LEVEL,
                unlockLevel = GameConfig.HEAVY_WEAPON_UNLOCK_LEVEL,
                onClick = { onSpawnUnit(UnitEntity.UnitType.HEAVY_WEAPON) },
                enabled = gold >= GameConfig.HEAVY_WEAPON_COST && currentLevel >= GameConfig.HEAVY_WEAPON_UNLOCK_LEVEL,
                modifier = Modifier.weight(1f)
            )
        }
        
        // 2. Spearman
        if (selectedRace == UnitEntity.Race.NATURE_TRIBE) {
            // Elf race - use Elf PNG asset
            UnitButtonWithAsset(
                assetName = "elfsperamanbirimi.png",
                cost = GameConfig.SPEARMAN_COST,
                isUnlocked = currentLevel >= GameConfig.SPEARMAN_UNLOCK_LEVEL,
                unlockLevel = GameConfig.SPEARMAN_UNLOCK_LEVEL,
                onClick = { onSpawnUnit(UnitEntity.UnitType.SPEARMAN) },
                enabled = gold >= GameConfig.SPEARMAN_COST && currentLevel >= GameConfig.SPEARMAN_UNLOCK_LEVEL,
                modifier = Modifier.weight(1f)
            )
        } else if (selectedRace == UnitEntity.Race.MECHANICAL_LEGION) {
            // Mechanical Legion race - use Mızrakçı PNG asset
            UnitButtonWithAsset(
                assetName = "mekaniklejyonmızrakçıbirimi.png",
                cost = GameConfig.SPEARMAN_COST,
                isUnlocked = currentLevel >= GameConfig.SPEARMAN_UNLOCK_LEVEL,
                unlockLevel = GameConfig.SPEARMAN_UNLOCK_LEVEL,
                onClick = { onSpawnUnit(UnitEntity.UnitType.SPEARMAN) },
                enabled = gold >= GameConfig.SPEARMAN_COST && currentLevel >= GameConfig.SPEARMAN_UNLOCK_LEVEL,
                modifier = Modifier.weight(1f)
            )
        } else {
            // Other races - use simple shape
            UnitButtonPlaceholder(
                icon = "🗡️",
                cost = GameConfig.SPEARMAN_COST,
                isUnlocked = currentLevel >= GameConfig.SPEARMAN_UNLOCK_LEVEL,
                unlockLevel = GameConfig.SPEARMAN_UNLOCK_LEVEL,
                onClick = { onSpawnUnit(UnitEntity.UnitType.SPEARMAN) },
                enabled = gold >= GameConfig.SPEARMAN_COST && currentLevel >= GameConfig.SPEARMAN_UNLOCK_LEVEL,
                modifier = Modifier.weight(1f)
            )
        }
        
        // 3. Archer
        if (selectedRace == UnitEntity.Race.NATURE_TRIBE) {
            // Elf race - use Elf PNG asset
            UnitButtonWithAsset(
                assetName = "archerbirimielf.png",
                cost = GameConfig.ARCHER_COST,
                isUnlocked = currentLevel >= GameConfig.ARCHER_UNLOCK_LEVEL,
                unlockLevel = GameConfig.ARCHER_UNLOCK_LEVEL,
                onClick = { onSpawnUnit(UnitEntity.UnitType.ARCHER) },
                enabled = gold >= GameConfig.ARCHER_COST && currentLevel >= GameConfig.ARCHER_UNLOCK_LEVEL,
                modifier = Modifier.weight(1f)
            )
        } else if (selectedRace == UnitEntity.Race.MECHANICAL_LEGION) {
            // Mechanical Legion race - use Okçu PNG asset
            UnitButtonWithAsset(
                assetName = "mekaniklejyonuzakçıbirimi.png",
                cost = GameConfig.ARCHER_COST,
                isUnlocked = currentLevel >= GameConfig.ARCHER_UNLOCK_LEVEL,
                unlockLevel = GameConfig.ARCHER_UNLOCK_LEVEL,
                onClick = { onSpawnUnit(UnitEntity.UnitType.ARCHER) },
                enabled = gold >= GameConfig.ARCHER_COST && currentLevel >= GameConfig.ARCHER_UNLOCK_LEVEL,
                modifier = Modifier.weight(1f)
            )
        } else {
            // Other races - use simple shape
            UnitButtonPlaceholder(
                icon = "🏹",
                cost = GameConfig.ARCHER_COST,
                isUnlocked = currentLevel >= GameConfig.ARCHER_UNLOCK_LEVEL,
                unlockLevel = GameConfig.ARCHER_UNLOCK_LEVEL,
                onClick = { onSpawnUnit(UnitEntity.UnitType.ARCHER) },
                enabled = gold >= GameConfig.ARCHER_COST && currentLevel >= GameConfig.ARCHER_UNLOCK_LEVEL,
                modifier = Modifier.weight(1f)
            )
        }
        
        // 4. Knight
        if (selectedRace == UnitEntity.Race.NATURE_TRIBE) {
            // Elf race - use Elf PNG asset
            UnitButtonWithAsset(
                assetName = "knightelfbirimi.png",
                cost = GameConfig.KNIGHT_COST,
                isUnlocked = isUnitUnlocked(UnitEntity.UnitType.KNIGHT),
                unlockLevel = getUnlockLevel(UnitEntity.UnitType.KNIGHT),
                onClick = { onSpawnUnit(UnitEntity.UnitType.KNIGHT) },
                enabled = gold >= GameConfig.KNIGHT_COST && isUnitUnlocked(UnitEntity.UnitType.KNIGHT),
                modifier = Modifier.weight(1f)
            )
        } else if (selectedRace == UnitEntity.Race.MECHANICAL_LEGION) {
            // Mechanical Legion race - use Şövalye PNG asset
            UnitButtonWithAsset(
                assetName = "mekaniklejyonşövalyebirimi.png",
                cost = GameConfig.KNIGHT_COST,
                isUnlocked = isUnitUnlocked(UnitEntity.UnitType.KNIGHT),
                unlockLevel = getUnlockLevel(UnitEntity.UnitType.KNIGHT),
                onClick = { onSpawnUnit(UnitEntity.UnitType.KNIGHT) },
                enabled = gold >= GameConfig.KNIGHT_COST && isUnitUnlocked(UnitEntity.UnitType.KNIGHT),
                modifier = Modifier.weight(1f)
            )
        } else {
            // Other races - use simple shape
            UnitButtonPlaceholder(
                icon = "⚔️",
                cost = GameConfig.KNIGHT_COST,
                isUnlocked = isUnitUnlocked(UnitEntity.UnitType.KNIGHT),
                unlockLevel = getUnlockLevel(UnitEntity.UnitType.KNIGHT),
                onClick = { onSpawnUnit(UnitEntity.UnitType.KNIGHT) },
                enabled = gold >= GameConfig.KNIGHT_COST && isUnitUnlocked(UnitEntity.UnitType.KNIGHT),
                modifier = Modifier.weight(1f)
            )
        }
        
        // 5. Cavalry
        if (selectedRace == UnitEntity.Race.NATURE_TRIBE) {
            // Elf race - use PNG asset
            UnitButtonWithAsset(
                assetName = "cavarlyelifbirimi.png",
                cost = GameConfig.CAVALRY_COST,
                isUnlocked = currentLevel >= GameConfig.CAVALRY_UNLOCK_LEVEL,
                unlockLevel = GameConfig.CAVALRY_UNLOCK_LEVEL,
                onClick = { onSpawnUnit(UnitEntity.UnitType.CAVALRY) },
                enabled = gold >= GameConfig.CAVALRY_COST && currentLevel >= GameConfig.CAVALRY_UNLOCK_LEVEL,
                modifier = Modifier.weight(1f)
            )
        } else if (selectedRace == UnitEntity.Race.MECHANICAL_LEGION) {
            // Mechanical Legion race - use Cavalry PNG asset
            UnitButtonWithAsset(
                assetName = "mekaniklejyonatlıbirimi.png",
                cost = GameConfig.CAVALRY_COST,
                isUnlocked = currentLevel >= GameConfig.CAVALRY_UNLOCK_LEVEL,
                unlockLevel = GameConfig.CAVALRY_UNLOCK_LEVEL,
                onClick = { onSpawnUnit(UnitEntity.UnitType.CAVALRY) },
                enabled = gold >= GameConfig.CAVALRY_COST && currentLevel >= GameConfig.CAVALRY_UNLOCK_LEVEL,
                modifier = Modifier.weight(1f)
            )
        } else {
            // Other races - use simple shape
            UnitButtonPlaceholder(
                icon = "🐎",
                cost = GameConfig.CAVALRY_COST,
                isUnlocked = currentLevel >= GameConfig.CAVALRY_UNLOCK_LEVEL,
                unlockLevel = GameConfig.CAVALRY_UNLOCK_LEVEL,
                onClick = { onSpawnUnit(UnitEntity.UnitType.CAVALRY) },
                enabled = gold >= GameConfig.CAVALRY_COST && currentLevel >= GameConfig.CAVALRY_UNLOCK_LEVEL,
                modifier = Modifier.weight(1f)
            )
        }
        

    }
}

@Composable
private fun UnitButtonWithAsset(
    assetName: String,
    cost: Int,
    isUnlocked: Boolean,
    unlockLevel: Int,
    onClick: () -> Unit,
    enabled: Boolean,
    modifier: Modifier = Modifier
) {
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp
    val screenHeight = configuration.screenHeightDp
    val context = LocalContext.current
    
    // Calculate responsive sizes
    val unitButtonSize = ScreenUtils.getResponsiveButtonSize(screenWidth, screenHeight) * 0.7f
    val smallFontSize = ScreenUtils.getResponsiveFontSize(screenWidth, screenHeight, 12f)
    
    // Load bitmap from assets
    var bitmap by remember { mutableStateOf<android.graphics.Bitmap?>(null) }
    
    LaunchedEffect(assetName) {
        try {
            val inputStream = context.assets.open(assetName)
            bitmap = BitmapFactory.decodeStream(inputStream)
            inputStream.close()
            println("Successfully loaded bitmap: $assetName")
        } catch (e: Exception) {
            e.printStackTrace()
            println("Failed to load bitmap: $assetName - ${e.message}")
        }
    }
    
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Unit button with PNG background
        Box(
            modifier = Modifier
                .size(unitButtonSize.dp)
                .clip(CircleShape)
                .clickable(enabled = enabled && isUnlocked) { onClick() }
                .border(
                    width = if (enabled && isUnlocked) 3.dp else 1.dp,
                    color = if (enabled && isUnlocked) Color(0xFFFFD700) else Color.Gray,
                    shape = CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            // PNG background image or fallback
            if (isUnlocked && bitmap != null) {
                Image(
                    bitmap = bitmap!!.asImageBitmap(),
                    contentDescription = "Unit icon",
                    modifier = Modifier.fillMaxHeight(),
                    contentScale = ContentScale.Crop
                )
            } else if (isUnlocked) {
                // Fallback to emoji if PNG fails to load
                Text(
                    text = "❓",
                    fontSize = (smallFontSize * 2).sp,
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
            } else {
                // Locked state - dark overlay
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .background(Color.Black.copy(alpha = 0.7f))
                )
            }
            
            // Cost text overlay at bottom
            Box(
                modifier = Modifier.fillMaxHeight(),
                contentAlignment = Alignment.BottomCenter
            ) {
                Text(
                    text = if (isUnlocked) "🪙$cost" else "Lv.$unlockLevel",
                    color = Color.White,
                    fontSize = smallFontSize.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .background(Color.Black.copy(alpha = 0.8f), RoundedCornerShape(4.dp))
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                )
            }
        }
    }
}

@Composable
private fun UnitButtonPlaceholder(
    icon: String,
    cost: Int,
    isUnlocked: Boolean,
    unlockLevel: Int,
    onClick: () -> Unit,
    enabled: Boolean,
    modifier: Modifier = Modifier
) {
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp
    val screenHeight = configuration.screenHeightDp
    
    // Calculate responsive sizes
    val unitButtonSize = ScreenUtils.getResponsiveButtonSize(screenWidth, screenHeight) * 0.7f
    val iconFontSize = ScreenUtils.getResponsiveFontSize(screenWidth, screenHeight, 24f)
    val smallFontSize = ScreenUtils.getResponsiveFontSize(screenWidth, screenHeight, 12f)
    
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Placeholder button
        Box(
            modifier = Modifier
                .size(unitButtonSize.dp)
                .background(
                    color = if (isUnlocked) Color(0xFF4A4A4A) else Color.Gray,
                    shape = CircleShape
                )
                .clip(CircleShape)
                .clickable(enabled = enabled && isUnlocked) { onClick() }
                .border(
                    width = if (enabled && isUnlocked) 3.dp else 1.dp,
                    color = if (enabled && isUnlocked) Color(0xFFFFD700) else Color.Gray,
                    shape = CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                // Unit icon - using emoji
                Text(
                    text = icon,
                    fontSize = iconFontSize.sp,
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
                
                // Cost text
                Text(
                    text = if (isUnlocked) "🪙$cost" else "Lv.$unlockLevel",
                    color = Color.White,
                    fontSize = smallFontSize.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}
