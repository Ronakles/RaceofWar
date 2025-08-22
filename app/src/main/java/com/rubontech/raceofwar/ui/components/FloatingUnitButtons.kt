/*
 * MIT License
 * 
 * Copyright (c) 2024 Rubontech
 */

package com.rubontech.raceofwar.ui.components

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.BorderStroke
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.*
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import android.graphics.BitmapFactory
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size

import com.rubontech.raceofwar.game.entities.UnitEntity
import com.rubontech.raceofwar.game.state.GameState
import com.rubontech.raceofwar.game.GameConfig
import com.rubontech.raceofwar.game.units.UnitType as ProgressionUnitType
import com.rubontech.raceofwar.ui.components.SpawnCooldownManager
import com.rubontech.raceofwar.ui.utils.UnitMapping

/**
 * Minimal floating circular buttons that don't block the game view
 */
@Composable
fun FloatingUnitButtons(
    availableUnits: List<ProgressionUnitType>,
    selectedRace: UnitEntity.Race,
    gameState: GameState,
    onSpawnUnit: (ProgressionUnitType) -> Unit,
    modifier: Modifier = Modifier,
    realGold: Int? = null, // Optional real gold from game engine for accurate cost checking
    currentDifficultyLevel: Int = 1, // Current difficulty level from game engine
    difficultyProgress: Float = 0f // Difficulty progress (0.0 to 1.0)
) {
    // Cooldown manager for spawn buttons
    val cooldownManager = remember { SpawnCooldownManager() }
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp
    
    // Calculate very small, responsive button size (minimal footprint)
    val buttonSize = minOf(40.dp, (screenWidth * 0.06f).dp) // Max 40dp, or 6% of screen width
    val spacing = 4.dp
    
    // Debug: Print available units
    LaunchedEffect(availableUnits) {
        println("🎮 FloatingUnitButtons - Available units: ${availableUnits.map { it.displayName }}")
        println("🎮 FloatingUnitButtons - Units count: ${availableUnits.size}")
    }
    
    // No background container - just the buttons
    if (availableUnits.isNotEmpty()) {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(spacing),
                contentPadding = PaddingValues(horizontal = 4.dp)
            ) {
                items(availableUnits) { unitType ->
                                            FloatingUnitButton(
                            unitType = unitType,
                            selectedRace = selectedRace,
                            gameState = gameState,
                            buttonSize = buttonSize,
                            realGold = realGold,
                            cooldownManager = cooldownManager,
                            onClick = { onSpawnUnit(unitType) }
                        )
                }
            }
        }
    } else {
        // Debug: Show message when no units (minimal)
        Box(
            modifier = modifier
                .fillMaxWidth()
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "No units available",
                color = Color.White,
                fontSize = 12.sp
            )
        }
    }
}

@Composable
private fun FloatingUnitButton(
    unitType: ProgressionUnitType,
    selectedRace: UnitEntity.Race,
    gameState: GameState,
    buttonSize: Dp,
    onClick: () -> Unit,
    realGold: Int? = null,
    cooldownManager: SpawnCooldownManager
) {
    val context = LocalContext.current
    val isAvailable = gameState.isUnitAvailable(unitType)
    
    // Separate queue count (user clicks) from spawned units
    var queueCount by remember { mutableStateOf(0) }
    var isPressed by remember { mutableStateOf(false) }
    var bitmap by remember { mutableStateOf<android.graphics.Bitmap?>(null) }
    
    // Calculate real unit cost and affordability
    val unitCost = UnitMapping.getUnitCost(unitType)
    val currentGold = realGold ?: 0
    val canAfford = currentGold >= unitCost
    
    // Check cooldown
    val engineUnitType = UnitMapping.convertToEngineUnitType(unitType)
    val isOnCooldown = cooldownManager.isOnCooldown(engineUnitType)
    val canSpawn = canAfford && !isOnCooldown
    
    // Debug cooldown status
    LaunchedEffect(isOnCooldown) {
        if (isOnCooldown) {
            val remainingTime = cooldownManager.getRemainingTime(engineUnitType)
            println("⏰ Cooldown active for: $engineUnitType (${unitType.displayName}) - Remaining: ${remainingTime}ms")
        }
    }
    
    // Auto-spawn system: When cooldown ends and queue has units, spawn automatically
    LaunchedEffect(isOnCooldown, queueCount) {
        if (!isOnCooldown && queueCount > 0) {
            // Cooldown ended and we have units in queue, spawn one
            println("🚀 Auto-spawning: ${unitType.displayName} (Queue before: $queueCount)")
            
            // Spawn the unit first
            onClick() // This actually spawns the unit
            
            // Then decrease queue count
            queueCount--
            println("✅ Unit spawned: ${unitType.displayName} (Remaining in queue: $queueCount)")
            
            // Start new cooldown if more units in queue
            if (queueCount > 0) {
                val engineUnitType = UnitMapping.convertToEngineUnitType(unitType)
                cooldownManager.startCooldown(engineUnitType)
                println("⏰ Starting cooldown for next unit in queue")
            }
        }
    }
    
    // Smooth animations
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.85f else 1f,
        animationSpec = spring(dampingRatio = 0.6f, stiffness = 500f),
        label = "button_scale"
    )
    
    val elevation by animateDpAsState(
        targetValue = if (isPressed) 2.dp else if (isAvailable) 6.dp else 2.dp,
        animationSpec = tween(150),
        label = "button_elevation"
    )
    
    // Load unit asset based on selected race
    LaunchedEffect(unitType, selectedRace) {
        try {
            val assetName = getAssetName(unitType, selectedRace)
            val inputStream = context.assets.open(assetName)
            bitmap = BitmapFactory.decodeStream(inputStream)
            inputStream.close()
        } catch (e: Exception) {
            e.printStackTrace()
            // Keep bitmap as null, will fallback to emoji
        }
    }
    
    // Circular floating button
    Box(
        modifier = Modifier.size(buttonSize),
        contentAlignment = Alignment.Center
    ) {
                // Main circular button
        Surface(
            modifier = Modifier
                .size(buttonSize)
                .scale(scale)
                .shadow(elevation, CircleShape)
                .clickable(enabled = isAvailable && canAfford) {
                if (isAvailable && canAfford) {
                    isPressed = true
                    
                    if (!isOnCooldown) {
                        // No cooldown - spawn immediately
                        println("🚀 Immediate spawn: ${unitType.displayName}")
                        onClick() // Spawn immediately
                        
                        // Start cooldown for next spawn
                        val engineUnitType = UnitMapping.convertToEngineUnitType(unitType)
                        cooldownManager.startCooldown(engineUnitType)
                    } else {
                        // On cooldown - add to queue
                        queueCount++
                        println("➕ Added to queue: ${unitType.displayName} (Queue count: $queueCount)")
                    }
                }
            },
            shape = CircleShape,
            color = when {
                !isAvailable -> Color(0xFF424242).copy(alpha = 0.7f) // Semi-transparent dark gray for locked
                !canAfford -> Color(0xFFFF6B6B).copy(alpha = 0.7f) // Semi-transparent red for unaffordable
                isOnCooldown -> Color(0xFFFF9800).copy(alpha = 0.7f) // Semi-transparent orange for cooldown
                queueCount > 0 -> Color(0xFF4CAF50).copy(alpha = 0.8f) // Semi-transparent green for active
                else -> Color(0xFF2196F3).copy(alpha = 0.7f) // Semi-transparent blue for available
            }
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                // Unit image or fallback emoji
                if (bitmap != null) {
                    Image(
                        bitmap = bitmap!!.asImageBitmap(),
                        contentDescription = "${unitType.displayName} icon",
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(4.dp), // Small padding inside button
                        contentScale = ContentScale.Fit,
                        alpha = if (!isAvailable || !canAfford) 0.4f else 0.9f, // More transparent when disabled or unaffordable
                        colorFilter = if (!isAvailable) {
                            ColorFilter.colorMatrix(
                                ColorMatrix().apply {
                                    setToSaturation(0f) // Grayscale when disabled
                                }
                            )
                        } else null
                    )
                } else {
                    // Fallback to emoji if image fails to load
                    Text(
                        text = UnitMapping.getUnitEmoji(unitType),
                        fontSize = 16.sp,
                        color = if (isAvailable && canAfford) Color.White else Color.Gray,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
        
        // Cost display - Integrated text below character
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 2.dp)
        ) {
            Text(
                text = "$unitCost",
                fontSize = 7.sp,
                fontWeight = FontWeight.Bold,
                color = if (canAfford) Color(0xFFFFD700) else Color(0xFFFF6B6B),
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .background(
                        color = Color(0xCC000000), // Semi-transparent black
                        shape = RoundedCornerShape(4.dp)
                    )
                    .padding(horizontal = 4.dp, vertical = 1.dp)
            )
        }
        
        // Queue count badge (top-right) - Shows user click count (units to spawn)
        if (queueCount > 0) {
            Surface(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .size(16.dp), // Slightly larger for queue numbers
                shape = CircleShape,
                color = Color(0xFF4CAF50) // Green for active queue
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = if (queueCount > 9) "9+" else queueCount.toString(),
                        fontSize = 9.sp, // Slightly larger text
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
        
        // Remove from queue button (X button) - Top-left when queue exists
        if (queueCount > 0) {
            Surface(
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .size(14.dp) // Small X button
                    .clickable {
                        // Remove one unit from queue
                        if (queueCount > 0) {
                            queueCount--
                            println("🗑️ Removed from queue: ${unitType.displayName} (New queue count: $queueCount)")
                        }
                    },
                shape = CircleShape,
                color = Color(0xFFFF5722) // Red for remove action
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "✕",
                        fontSize = 8.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
        
        // Spawn progress indicator (overlay) - Ultra-smooth circle filling animation
        if (isOnCooldown) {
            val cooldownProgress = cooldownManager.getCooldownProgress(convertToEngineUnitType(unitType))
            
            Box(
                modifier = Modifier
                    .align(Alignment.Center)
                    .size(buttonSize * 0.9f), // Slightly smaller than button
                contentAlignment = Alignment.Center
            ) {
                // Transparent background circle with smooth animation
                androidx.compose.foundation.Canvas(
                    modifier = Modifier.fillMaxSize()
                ) {
                    val strokeWidth = 4f
                    val radius = (size.minDimension - strokeWidth) / 2
                    val center = Offset(size.width / 2, size.height / 2)
                    
                    // Background circle (very transparent)
                    drawCircle(
                        color = Color(0x15000000), // Ultra-transparent black
                        radius = radius,
                        center = center,
                        style = Stroke(width = strokeWidth)
                    )
                    
                    // Progress circle (smooth transparent blue filling)
                    drawArc(
                        color = Color(0x300096FF), // More transparent blue for smoother look
                        startAngle = -90f, // Start from top
                        sweepAngle = 360f * cooldownProgress, // Fill based on progress
                        useCenter = false, // Don't fill center for better performance
                        style = Stroke(
                            width = strokeWidth, 
                            cap = androidx.compose.ui.graphics.StrokeCap.Round
                        )
                    )
                }
                

            }
        }
        
        // Disabled overlay
        if (!isAvailable) {
            Surface(
                modifier = Modifier
                    .size(buttonSize)
                    .scale(scale),
                shape = CircleShape,
                color = Color.Black.copy(alpha = 0.5f)
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "🔒",
                        fontSize = 10.sp, // Fixed small size for lock icon
                        color = Color.White.copy(alpha = 0.7f)
                    )
                }
            }
        }
    }
    
    // Reset press state
    LaunchedEffect(isPressed) {
        if (isPressed) {
            kotlinx.coroutines.delay(100)
            isPressed = false
        }
    }
}

/**
 * Convert ProgressionUnitType to Engine UnitType for cooldown system
 */
private fun convertToEngineUnitType(progressionUnitType: ProgressionUnitType): UnitEntity.UnitType {
    return when (progressionUnitType) {
        // Human Empire Units
        ProgressionUnitType.HUMAN_KILICU -> UnitEntity.UnitType.SPEARMAN
        ProgressionUnitType.HUMAN_OKCU -> UnitEntity.UnitType.ARCHER
        ProgressionUnitType.HUMAN_ZIRHLI_PIYADE -> UnitEntity.UnitType.SPEARMAN
        ProgressionUnitType.HUMAN_ATLI -> UnitEntity.UnitType.CAVALRY
        ProgressionUnitType.HUMAN_MIZRAKCI -> UnitEntity.UnitType.SPEARMAN
        ProgressionUnitType.HUMAN_SIFACI -> UnitEntity.UnitType.HEAVY_WEAPON
        ProgressionUnitType.HUMAN_MANCINIK -> UnitEntity.UnitType.HEAVY_WEAPON
        ProgressionUnitType.HUMAN_CIFT_OK_ATAN_OKCU -> UnitEntity.UnitType.ARCHER
        ProgressionUnitType.HUMAN_KOMUTAN -> UnitEntity.UnitType.KNIGHT
        ProgressionUnitType.HUMAN_LIDER -> UnitEntity.UnitType.KNIGHT
        
        // Dark Cultist Units
        ProgressionUnitType.DARK_GOLGE_DRUID -> UnitEntity.UnitType.SPEARMAN
        ProgressionUnitType.DARK_CADI -> UnitEntity.UnitType.ARCHER
        ProgressionUnitType.DARK_MIZRAKCI -> UnitEntity.UnitType.SPEARMAN
        ProgressionUnitType.DARK_ATLI -> UnitEntity.UnitType.CAVALRY
        ProgressionUnitType.DARK_ATES_CUCESI -> UnitEntity.UnitType.HEAVY_WEAPON
        ProgressionUnitType.DARK_KARANLIK_SOVALYE -> UnitEntity.UnitType.KNIGHT
        ProgressionUnitType.DARK_KUTSA_SAPAN -> UnitEntity.UnitType.HEAVY_WEAPON
        ProgressionUnitType.DARK_SEYTAN_SAPAN -> UnitEntity.UnitType.HEAVY_WEAPON
        ProgressionUnitType.DARK_CIFT_TIRMIK_SAPAN -> UnitEntity.UnitType.HEAVY_WEAPON
        ProgressionUnitType.DARK_SAPAN_KARAM -> UnitEntity.UnitType.HEAVY_WEAPON
        
        // Elven Units
        ProgressionUnitType.ELF_HAFIF_ELF_ASKER -> UnitEntity.UnitType.SPEARMAN
        ProgressionUnitType.ELF_ELF_OKCUSU -> UnitEntity.UnitType.ARCHER
        ProgressionUnitType.ELF_ELF_ATLI -> UnitEntity.UnitType.CAVALRY
        ProgressionUnitType.ELF_ELF_MIZRAKCI -> UnitEntity.UnitType.SPEARMAN
        ProgressionUnitType.ELF_BUYUCU -> UnitEntity.UnitType.HEAVY_WEAPON
        ProgressionUnitType.ELF_SIFACI_ELF -> UnitEntity.UnitType.HEAVY_WEAPON
        ProgressionUnitType.ELF_ELIT_MANCINIK -> UnitEntity.UnitType.HEAVY_WEAPON
        ProgressionUnitType.ELF_CIFT_OK_SAPNAC_ISI -> UnitEntity.UnitType.ARCHER
        ProgressionUnitType.ELF_ELF_PRENSI -> UnitEntity.UnitType.KNIGHT
        ProgressionUnitType.ELF_ELF_PRENSESI -> UnitEntity.UnitType.KNIGHT
        
        // Mechanical Legion Units
        ProgressionUnitType.MECH_BASIT_DROID -> UnitEntity.UnitType.SPEARMAN
        ProgressionUnitType.MECH_LAZER_TARET -> UnitEntity.UnitType.ARCHER
        ProgressionUnitType.MECH_MIZRAKCI -> UnitEntity.UnitType.SPEARMAN
        ProgressionUnitType.MECH_KALKANLI -> UnitEntity.UnitType.SPEARMAN
        ProgressionUnitType.MECH_ZIRHLI_DROID -> UnitEntity.UnitType.HEAVY_WEAPON
        ProgressionUnitType.MECH_HIZLI_DRONE -> UnitEntity.UnitType.CAVALRY
        ProgressionUnitType.MECH_TANK_DROID -> UnitEntity.UnitType.HEAVY_WEAPON
        ProgressionUnitType.MECH_ROKETATAR_DROID -> UnitEntity.UnitType.HEAVY_WEAPON
        ProgressionUnitType.MECH_MECHA_SAVASCI -> UnitEntity.UnitType.KNIGHT
        ProgressionUnitType.MECH_PLAZMA_TOPU -> UnitEntity.UnitType.HEAVY_WEAPON
    }
}

private fun getAssetName(unitType: com.rubontech.raceofwar.game.units.UnitType, race: UnitEntity.Race): String {
    // Convert to a consistent format for asset mapping
    val unitTypeName = when {
        unitType.displayName.lowercase().contains("spear") || unitType.displayName.lowercase().contains("mızrak") -> "spearman"
        unitType.displayName.lowercase().contains("archer") || unitType.displayName.lowercase().contains("okç") -> "archer"
        unitType.displayName.lowercase().contains("knight") || unitType.displayName.lowercase().contains("şövalye") -> "knight"
        unitType.displayName.lowercase().contains("cavalry") || unitType.displayName.lowercase().contains("atlı") -> "cavalry"
        unitType.displayName.lowercase().contains("heavy") || unitType.displayName.lowercase().contains("ağır") -> "heavy weapon"
        else -> "archer" // Default fallback
    }
    
    return when (race) {
        UnitEntity.Race.NATURE_TRIBE -> when (unitTypeName) {
            "heavy weapon" -> "heavyweapnelfbirimi.png"
            "spearman" -> "elfsperamanbirimi.png"
            "archer" -> "archerbirimielf.png"
            "knight" -> "knightelfbirimi.png"
            "cavalry" -> "cavarlyelifbirimi.png"
            else -> "archerbirimielf.png"
        }
        UnitEntity.Race.MECHANICAL_LEGION -> when (unitTypeName) {
            "heavy weapon" -> "mekaniklejyonheavyweaponbirimi.png"
            "spearman" -> "mekaniklejyonmızrakçıbirimi.png"
            "archer" -> "mekaniklejyonuzakçıbirimi.png"
            "knight" -> "mekaniklejyonşövalyebirimi.png"
            "cavalry" -> "mekaniklejyonatlıbirimi.png"
            else -> "mekaniklejyonuzakçıbirimi.png"
        }
        else -> when (unitTypeName) {
            "heavy weapon" -> "mekaniklejyonheavyweaponbirimi.png"
            "spearman" -> "mekaniklejyonmızrakçıbirimi.png"
            "archer" -> "mekaniklejyonuzakçıbirimi.png"
            "knight" -> "mekaniklejyonşövalyebirimi.png"
            "cavalry" -> "mekaniklejyonatlıbirimi.png"
            else -> "mekaniklejyonuzakçıbirimi.png"
        }
    }
}

// Removed old helper function - now using UnitMapping.getUnitCost()

// Removed old helper function - now using UnitMapping.getUnitEmoji()

/**
 * Difficulty progress indicator showing current difficulty level and progress
 */
@Composable
fun DifficultyIndicator(
    currentLevel: Int,
    progress: Float,
    modifier: Modifier = Modifier
) {
    val difficultyColor = when (currentLevel) {
        in 1..3 -> Color(0xFF4CAF50) // Green for easy
        in 4..6 -> Color(0xFFFF9800) // Orange for medium
        in 7..8 -> Color(0xFFFF5722) // Red for hard
        else -> Color(0xFF9C27B0) // Purple for expert
    }
    
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Difficulty level text
        Text(
            text = "Difficulty: $currentLevel",
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold,
            color = difficultyColor,
            textAlign = TextAlign.Center
        )
        
        // Progress bar
        Box(
            modifier = Modifier
                .width(80.dp)
                .height(4.dp)
                .background(
                    color = Color(0xFF424242),
                    shape = RoundedCornerShape(2.dp)
                )
                .padding(1.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth(progress)
                    .background(
                        color = difficultyColor,
                        shape = RoundedCornerShape(2.dp)
                    )
            )
        }
        
        // Time indicator
        Text(
            text = "${(progress * 9).toInt()}:00",
            fontSize = 8.sp,
            color = Color(0xFFB8C5D6),
            textAlign = TextAlign.Center
        )
    }
}
