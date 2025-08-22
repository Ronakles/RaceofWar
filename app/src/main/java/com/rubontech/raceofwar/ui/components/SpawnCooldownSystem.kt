/*
 * MIT License
 * 
 * Copyright (c) 2024 Rubontech
 */

package com.rubontech.raceofwar.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rubontech.raceofwar.game.entities.UnitEntity
import kotlinx.coroutines.delay
import kotlin.math.cos
import kotlin.math.sin

/**
 * Cooldown system for spawn buttons - Age of Wars style
 */
class SpawnCooldownManager {
    private val cooldowns = mutableMapOf<UnitEntity.UnitType, Long>()
    private val cooldownDurations = mapOf(
        UnitEntity.UnitType.SPEARMAN to 2000L, // 2 seconds
        UnitEntity.UnitType.ARCHER to 2000L,   // 2 seconds
        UnitEntity.UnitType.HEAVY_WEAPON to 2000L, // 2 seconds
        UnitEntity.UnitType.CAVALRY to 2000L,  // 2 seconds
        UnitEntity.UnitType.KNIGHT to 2000L,   // 2 seconds
        UnitEntity.UnitType.ELF_KNIGHT to 2000L // 2 seconds
    )
    
    fun startCooldown(unitType: UnitEntity.UnitType) {
        val duration = cooldownDurations[unitType] ?: 1000L
        cooldowns[unitType] = System.currentTimeMillis() + duration
        println("⏰ Cooldown started for $unitType - Duration: ${duration}ms")
    }
    
    fun isOnCooldown(unitType: UnitEntity.UnitType): Boolean {
        val endTime = cooldowns[unitType] ?: return false
        return System.currentTimeMillis() < endTime
    }
    
    fun getCooldownProgress(unitType: UnitEntity.UnitType): Float {
        val endTime = cooldowns[unitType] ?: return 1f
        val duration = cooldownDurations[unitType] ?: 1000L
        val currentTime = System.currentTimeMillis()
        
        if (currentTime >= endTime) {
            cooldowns.remove(unitType)
            return 1f
        }
        
        val startTime = endTime - duration
        val elapsed = currentTime - startTime
        return (elapsed.toFloat() / duration.toFloat()).coerceIn(0f, 1f)
    }
    
    fun getRemainingTime(unitType: UnitEntity.UnitType): Long {
        val endTime = cooldowns[unitType] ?: return 0L
        val remaining = endTime - System.currentTimeMillis()
        return if (remaining > 0) remaining else 0L
    }
}

@Composable
fun SpawnCooldownOverlay(
    unitType: UnitEntity.UnitType,
    cooldownManager: SpawnCooldownManager,
    modifier: Modifier = Modifier
) {
    var progress by remember { mutableStateOf(1f) }
    var isOnCooldown by remember { mutableStateOf(false) }
    var remainingTime by remember { mutableStateOf(0L) }
    
    LaunchedEffect(unitType) {
        while (true) {
            val onCooldown = cooldownManager.isOnCooldown(unitType)
            val currentProgress = cooldownManager.getCooldownProgress(unitType)
            val remaining = cooldownManager.getRemainingTime(unitType)
            
            progress = currentProgress
            isOnCooldown = onCooldown
            remainingTime = remaining
            
            delay(50) // Update every 50ms for smooth animation
        }
    }
    
    if (isOnCooldown) {
        Box(
            modifier = modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            // Circular progress indicator
            Canvas(modifier = Modifier.fillMaxSize()) {
                drawCooldownProgress(progress)
            }
            
            // Remaining time text
            if (remainingTime > 0) {
                Text(
                    text = "${(remainingTime / 1000f).toInt() + 1}",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        }
    }
}

private fun DrawScope.drawCooldownProgress(progress: Float) {
    val center = Offset(size.width / 2, size.height / 2)
    val radius = minOf(size.width, size.height) / 2 - 8.dp.toPx()
    val strokeWidth = 4.dp.toPx()
    
    // Background circle
    drawCircle(
        color = Color.Black.copy(alpha = 0.7f),
        radius = radius,
        center = center
    )
    
    // Progress arc
    val sweepAngle = 360f * progress
    val startAngle = -90f // Start from top
    
    drawArc(
        color = Color(0xFF4CAF50), // Green progress
        startAngle = startAngle,
        sweepAngle = sweepAngle,
        useCenter = false,
        topLeft = Offset(center.x - radius, center.y - radius),
        size = Size(radius * 2, radius * 2),
        style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
    )
    
    // Overlay for incomplete progress
    if (progress < 1f) {
        drawCircle(
            color = Color.Black.copy(alpha = 0.5f),
            radius = radius - strokeWidth / 2,
            center = center
        )
    }
}

@Composable
fun rememberSpawnCooldownManager(): SpawnCooldownManager {
    return remember { SpawnCooldownManager() }
}
