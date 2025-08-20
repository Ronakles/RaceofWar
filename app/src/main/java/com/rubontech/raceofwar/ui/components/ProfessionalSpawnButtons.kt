/*
 * MIT License
 * 
 * Copyright (c) 2024 Rubontech
 */

package com.rubontech.raceofwar.ui.components

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.*
import android.graphics.BitmapFactory
import com.rubontech.raceofwar.game.GameConfig
import com.rubontech.raceofwar.game.entities.UnitEntity
import com.rubontech.raceofwar.ui.utils.ScreenUtils
import kotlinx.coroutines.delay
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.material3.Icon
import androidx.compose.ui.res.painterResource

/**
 * Professional Age of Wars / Stick Wars style spawn buttons
 */
@Composable
fun ProfessionalSpawnButtons(
    gold: Int,
    currentLevel: Int,
    selectedRace: UnitEntity.Race = UnitEntity.Race.MECHANICAL_LEGION,
    isUnitUnlocked: (UnitEntity.UnitType) -> Boolean,
    getUnlockLevel: (UnitEntity.UnitType) -> Int,
    onSpawnUnit: (UnitEntity.UnitType) -> Unit,
    onRaceChanged: (UnitEntity.Race) -> Unit = {},
    modifier: Modifier = Modifier
) {
    val cooldownManager = rememberSpawnCooldownManager()
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp
    val screenHeight = configuration.screenHeightDp
    
    // Calculate responsive sizes
    val buttonWidth = (screenWidth * 0.16f).dp // Each button takes ~16% of screen width
    val buttonHeight = (screenHeight * 0.12f).dp // Professional height ratio
    val spacing = (screenWidth * 0.01f).dp
    
    // Define unit order (most basic to most advanced)
    val unitOrder = listOf(
        UnitEntity.UnitType.SPEARMAN,
        UnitEntity.UnitType.ARCHER,
        UnitEntity.UnitType.HEAVY_WEAPON,
        UnitEntity.UnitType.CAVALRY,
        UnitEntity.UnitType.KNIGHT
    )
    
    Row(
        horizontalArrangement = Arrangement.spacedBy(spacing),
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        unitOrder.forEach { unitType ->
            val cost = when (unitType) {
                UnitEntity.UnitType.CAVALRY -> GameConfig.CAVALRY_COST
                UnitEntity.UnitType.SPEARMAN -> GameConfig.SPEARMAN_COST
                UnitEntity.UnitType.ARCHER -> GameConfig.ARCHER_COST
                UnitEntity.UnitType.KNIGHT -> GameConfig.KNIGHT_COST
                UnitEntity.UnitType.HEAVY_WEAPON -> GameConfig.HEAVY_WEAPON_COST
                UnitEntity.UnitType.ELF_KNIGHT -> GameConfig.KNIGHT_COST
            }
            
            val isUnlocked = isUnitUnlocked(unitType)
            val unlockLevel = getUnlockLevel(unitType)
            val canAfford = gold >= cost
            val isOnCooldown = cooldownManager.isOnCooldown(unitType)
            val isEnabled = isUnlocked && canAfford && !isOnCooldown
            
            ProfessionalUnitButton(
                unitType = unitType,
                selectedRace = selectedRace,
                cost = cost,
                isUnlocked = isUnlocked,
                unlockLevel = unlockLevel,
                canAfford = canAfford,
                isEnabled = isEnabled,
                cooldownManager = cooldownManager,
                onClick = { 
                    if (isEnabled) {
                        cooldownManager.startCooldown(unitType)
                        onSpawnUnit(unitType)
                    }
                },
                modifier = Modifier
                    .width(buttonWidth)
                    .height(buttonHeight)
                    .weight(1f)
            )
        }
    }
}

@Composable
private fun ProfessionalUnitButton(
    unitType: UnitEntity.UnitType,
    selectedRace: UnitEntity.Race,
    cost: Int,
    isUnlocked: Boolean,
    unlockLevel: Int,
    canAfford: Boolean,
    isEnabled: Boolean,
    cooldownManager: SpawnCooldownManager,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var isPressed by remember { mutableStateOf(false) }
    var bitmap by remember { mutableStateOf<android.graphics.Bitmap?>(null) }
    var showTooltip by remember { mutableStateOf(false) }
    
    // Animation states
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.95f else 1f,
        animationSpec = spring(dampingRatio = 0.7f, stiffness = 300f),
        label = "button_scale"
    )
    
    val glowAlpha by animateFloatAsState(
        targetValue = if (isEnabled) 0.3f else 0f,
        animationSpec = tween(500),
        label = "glow_alpha"
    )
    
    // Load unit asset
    val assetName = getAssetName(unitType, selectedRace)
    LaunchedEffect(assetName) {
        try {
            val inputStream = context.assets.open(assetName)
            bitmap = BitmapFactory.decodeStream(inputStream)
            inputStream.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
    
    Box(
        modifier = modifier
            .scale(scale)
            .shadow(
                elevation = if (isEnabled) 8.dp else 2.dp,
                shape = RoundedCornerShape(12.dp),
                ambientColor = if (isEnabled) Color(0xFFFFD700) else Color.Gray,
                spotColor = if (isEnabled) Color(0xFFFFD700) else Color.Gray
            )
            .clip(RoundedCornerShape(12.dp))
            .background(
                brush = if (isEnabled) {
                    Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFF2A2A2A),
                            Color(0xFF1A1A1A),
                            Color(0xFF0A0A0A)
                        )
                    )
                } else {
                    Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFF404040),
                            Color(0xFF303030),
                            Color(0xFF202020)
                        )
                    )
                }
            )
            .border(
                width = if (isEnabled) 2.dp else 1.dp,
                color = when {
                    !isUnlocked -> Color(0xFF666666)
                    !canAfford -> Color(0xFFFF6B6B)
                    isEnabled -> Color(0xFFFFD700)
                    else -> Color(0xFF888888)
                },
                shape = RoundedCornerShape(12.dp)
            )
            .clickable(
                enabled = isEnabled,
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ) {
                onClick()
            }
            .pointerInput(isEnabled) {
                detectTapGestures(
                    onPress = { offset ->
                        if (isEnabled) {
                            isPressed = true
                            val released = tryAwaitRelease()
                            isPressed = false
                        }
                    },
                    onLongPress = { offset ->
                        showTooltip = true
                    }
                )
            }
    ) {
        // Glow effect for enabled buttons
        if (isEnabled && glowAlpha > 0f) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                drawRoundRect(
                    color = Color(0xFFFFD700).copy(alpha = glowAlpha),
                    size = size,
                    cornerRadius = CornerRadius(12.dp.toPx()),
                    style = Stroke(width = 4.dp.toPx())
                )
            }
        }
        
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Unit icon area
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .clip(RoundedCornerShape(8.dp)),
                contentAlignment = Alignment.Center
            ) {
                if (isUnlocked && bitmap != null) {
                    Image(
                        bitmap = bitmap!!.asImageBitmap(),
                        contentDescription = "${unitType.name} icon",
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(4.dp),
                        contentScale = ContentScale.Fit,
                        colorFilter = if (!canAfford) {
                            ColorFilter.colorMatrix(
                                ColorMatrix().apply {
                                    setToSaturation(0f)
                                    set(4, 4, 0.5f) // Reduce brightness
                                }
                            )
                        } else null
                    )
                } else if (isUnlocked) {
                    // Fallback icon
                    Text(
                        text = getUnitEmoji(unitType),
                        fontSize = 24.sp,
                        color = if (canAfford) Color.White else Color.Gray
                    )
                } else {
                    // Locked state
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                Color.Black.copy(alpha = 0.7f),
                                RoundedCornerShape(8.dp)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                painter = painterResource(android.R.drawable.ic_lock_idle_lock),
                                contentDescription = "Locked",
                                tint = Color.Gray,
                                modifier = Modifier.size(20.dp)
                            )
                            Text(
                                text = "Lv.$unlockLevel",
                                fontSize = 10.sp,
                                color = Color.Gray,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
            
            // Cost and status bar
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(24.dp)
                    .background(
                        Color.Black.copy(alpha = 0.8f),
                        RoundedCornerShape(4.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                if (isUnlocked) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "🪙",
                            fontSize = 12.sp
                        )
                        Spacer(modifier = Modifier.width(2.dp))
                        Text(
                            text = cost.toString(),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (canAfford) Color(0xFFFFD700) else Color(0xFFFF6B6B)
                        )
                    }
                } else {
                    Text(
                        text = "LEVEL $unlockLevel",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Gray,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
        
        // Cooldown overlay
        SpawnCooldownOverlay(
            unitType = unitType,
            cooldownManager = cooldownManager,
            modifier = Modifier.fillMaxSize()
        )
        
        // Unit tooltip
        UnitTooltip(
            unitType = unitType,
            isVisible = showTooltip,
            onDismiss = { showTooltip = false }
        )
    }
}

@Composable
private fun SpawnEffectOverlay() {
    var showEffect by remember { mutableStateOf(false) }
    
    if (showEffect) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Color(0xFFFFD700).copy(alpha = 0.3f),
                    RoundedCornerShape(12.dp)
                )
        )
        
        LaunchedEffect(Unit) {
            delay(100)
            showEffect = false
        }
    }
}

private fun getAssetName(unitType: UnitEntity.UnitType, race: UnitEntity.Race): String {
    return when (race) {
        UnitEntity.Race.NATURE_TRIBE -> when (unitType) {
            UnitEntity.UnitType.HEAVY_WEAPON -> "heavyweapnelfbirimi.png"
            UnitEntity.UnitType.SPEARMAN -> "elfsperamanbirimi.png"
            UnitEntity.UnitType.ARCHER -> "archerbirimielf.png"
            UnitEntity.UnitType.KNIGHT -> "knightelfbirimi.png"
            UnitEntity.UnitType.CAVALRY -> "cavarlyelifbirimi.png"
            UnitEntity.UnitType.ELF_KNIGHT -> "knightelfbirimi.png"
        }
        UnitEntity.Race.MECHANICAL_LEGION -> when (unitType) {
            UnitEntity.UnitType.HEAVY_WEAPON -> "mekaniklejyonheavyweaponbirimi.png"
            UnitEntity.UnitType.SPEARMAN -> "mekaniklejyonmızrakçıbirimi.png"
            UnitEntity.UnitType.ARCHER -> "mekaniklejyonuzakçıbirimi.png"
            UnitEntity.UnitType.KNIGHT -> "mekaniklejyonşövalyebirimi.png"
            UnitEntity.UnitType.CAVALRY -> "mekaniklejyonatlıbirimi.png"
            UnitEntity.UnitType.ELF_KNIGHT -> "mekaniklejyonşövalyebirimi.png"
        }
        else -> when (unitType) {
            UnitEntity.UnitType.HEAVY_WEAPON -> "mekaniklejyonheavyweaponbirimi.png"
            UnitEntity.UnitType.SPEARMAN -> "mekaniklejyonmızrakçıbirimi.png"
            UnitEntity.UnitType.ARCHER -> "mekaniklejyonuzakçıbirimi.png"
            UnitEntity.UnitType.KNIGHT -> "mekaniklejyonşövalyebirimi.png"
            UnitEntity.UnitType.CAVALRY -> "mekaniklejyonatlıbirimi.png"
            UnitEntity.UnitType.ELF_KNIGHT -> "mekaniklejyonşövalyebirimi.png"
        }
    }
}

private fun getUnitEmoji(unitType: UnitEntity.UnitType): String {
    return when (unitType) {
        UnitEntity.UnitType.HEAVY_WEAPON -> "🔨"
        UnitEntity.UnitType.SPEARMAN -> "🗡️"
        UnitEntity.UnitType.ARCHER -> "🏹"
        UnitEntity.UnitType.KNIGHT -> "⚔️"
        UnitEntity.UnitType.CAVALRY -> "🐎"
        UnitEntity.UnitType.ELF_KNIGHT -> "🛡️"
    }
}