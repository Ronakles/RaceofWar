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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import com.rubontech.raceofwar.game.units.UnitProgression
import com.rubontech.raceofwar.game.units.UnitType as ProgressionUnitType
import com.rubontech.raceofwar.ui.utils.UnitMapping
import com.rubontech.raceofwar.ui.utils.UnitTier
import com.rubontech.raceofwar.game.state.GameState

/**
 * Professional Age of Wars / Stick Wars style spawn buttons
 */
@Composable
fun ProfessionalSpawnButtons(
    gameState: GameState,
    selectedRace: UnitEntity.Race = UnitEntity.Race.MECHANICAL_LEGION,
    onSpawnUnit: (ProgressionUnitType) -> Unit,
    onRaceChanged: (UnitEntity.Race) -> Unit = {},
    modifier: Modifier = Modifier,
    realGold: Int? = null
) {
    val cooldownManager = rememberSpawnCooldownManager()
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp
    val screenHeight = configuration.screenHeightDp
    
    // Calculate responsive sizes
    val buttonWidth = (screenWidth * 0.16f).dp // Each button takes ~16% of screen width
    val buttonHeight = (screenHeight * 0.12f).dp // Professional height ratio
    val spacing = (screenWidth * 0.01f).dp
    
    // Get available units from progression system
    val availableUnits = gameState.availableUnits
    val currentGold = realGold ?: 0
    
    if (availableUnits.isNotEmpty()) {
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(spacing),
            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp),
            modifier = modifier.fillMaxWidth()
        ) {
            items(availableUnits) { progressionUnitType ->
                val cost = UnitMapping.getUnitCost(progressionUnitType)
                val engineUnitType = UnitMapping.convertToEngineUnitType(progressionUnitType)
                val isAvailable = gameState.isUnitAvailable(progressionUnitType)
                val unlockXP = gameState.getUnitUnlockXP(progressionUnitType)
                val unitTier = gameState.getUnitTier(progressionUnitType)
                val canAfford = currentGold >= cost
                val isOnCooldown = cooldownManager.isOnCooldown(engineUnitType)
                val isEnabled = isAvailable && canAfford && !isOnCooldown
                
                ProfessionalUnitButton(
                    progressionUnitType = progressionUnitType,
                    selectedRace = selectedRace,
                    cost = cost,
                    isUnlocked = isAvailable,
                    unlockXP = unlockXP,
                    unitTier = unitTier,
                    currentXP = gameState.currentXP,
                    canAfford = canAfford,
                    isEnabled = isEnabled,
                    cooldownManager = cooldownManager,
                    onClick = { 
                        if (isEnabled) {
                            cooldownManager.startCooldown(engineUnitType)
                            onSpawnUnit(progressionUnitType)
                        }
                    },
                    modifier = Modifier
                        .width(buttonWidth)
                        .height(buttonHeight)
                )
            }
        }
    } else {
        // Show message when no units available
        Box(
            modifier = modifier
                .fillMaxWidth()
                .height(buttonHeight)
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "No units available",
                color = Color.White,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
private fun ProfessionalUnitButton(
    progressionUnitType: ProgressionUnitType,
    selectedRace: UnitEntity.Race,
    cost: Int,
    isUnlocked: Boolean,
    unlockXP: Int,
    unitTier: UnitTier,
    currentXP: Int,
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
    
    // Use placeholder emoji instead of assets for now
    val unitEmoji = UnitMapping.getUnitEmoji(progressionUnitType)
    val unitDescription = UnitMapping.getUnitDescription(progressionUnitType)
    
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
                if (isUnlocked) {
                    // Use placeholder emoji
                    Text(
                        text = unitEmoji,
                        fontSize = 24.sp,
                        color = if (canAfford) Color.White else Color.Gray,
                        modifier = Modifier.padding(4.dp)
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
                                text = "${unitTier.displayName}",
                                fontSize = 8.sp,
                                color = Color.Gray,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "${unlockXP}XP",
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
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = unitTier.displayName.uppercase(),
                            fontSize = 8.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Gray,
                            textAlign = TextAlign.Center
                        )
                        Text(
                            text = "${unlockXP} XP",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Gray,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }
        
        // Cooldown overlay
        SpawnCooldownOverlay(
            unitType = UnitMapping.convertToEngineUnitType(progressionUnitType),
            cooldownManager = cooldownManager,
            modifier = Modifier.fillMaxSize()
        )
        
        // Unit tooltip - show description
        if (showTooltip) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.Black.copy(alpha = 0.9f), RoundedCornerShape(4.dp))
                    .padding(8.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = progressionUnitType.displayName,
                        color = Color.White,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "${unitTier.displayName} Tier • ${unlockXP} XP Required",
                        color = Color.Yellow,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )
                    Text(
                        text = unitDescription,
                        color = Color.Gray,
                        fontSize = 10.sp,
                        textAlign = TextAlign.Center
                    )
                }
            }
            
            LaunchedEffect(Unit) {
                delay(2000)
                showTooltip = false
            }
        }
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

// Removed old asset and emoji functions - now using UnitMapping