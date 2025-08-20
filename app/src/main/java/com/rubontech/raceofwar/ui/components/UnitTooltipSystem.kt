/*
 * MIT License
 * 
 * Copyright (c) 2024 Rubontech
 */

package com.rubontech.raceofwar.ui.components

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import com.rubontech.raceofwar.game.GameConfig
import com.rubontech.raceofwar.game.entities.UnitEntity

/**
 * Tooltip system for unit information - shows stats when hovering/long pressing
 */
data class UnitStats(
    val hp: Float,
    val damage: Float,
    val range: Float,
    val attackSpeed: Float,
    val dps: Float
)

@Composable
fun UnitTooltip(
    unitType: UnitEntity.UnitType,
    isVisible: Boolean,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    if (isVisible) {
        Popup(
            onDismissRequest = onDismiss,
            properties = PopupProperties(focusable = true)
        ) {
            AnimatedVisibility(
                visible = isVisible,
                enter = scaleIn(
                    animationSpec = spring(dampingRatio = 0.8f, stiffness = 300f)
                ) + fadeIn(),
                exit = scaleOut() + fadeOut()
            ) {
                TooltipContent(
                    unitType = unitType,
                    modifier = modifier
                )
            }
        }
    }
}

@Composable
private fun TooltipContent(
    unitType: UnitEntity.UnitType,
    modifier: Modifier = Modifier
) {
    val stats = getUnitStats(unitType)
    val unitName = getUnitDisplayName(unitType)
    val unitDescription = getUnitDescription(unitType)
    
    Card(
        modifier = modifier
            .width(280.dp)
            .shadow(8.dp, RoundedCornerShape(12.dp)),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF1A1A1A)
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Unit name and type
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = getUnitEmoji(unitType),
                    fontSize = 24.sp
                )
                Column {
                    Text(
                        text = unitName,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFFFD700)
                    )
                    Text(
                        text = getUnitTypeText(unitType),
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
                }
            }
            
            Divider(color = Color.Gray.copy(alpha = 0.3f))
            
            // Description
            Text(
                text = unitDescription,
                fontSize = 14.sp,
                color = Color.White.copy(alpha = 0.9f),
                textAlign = TextAlign.Start
            )
            
            Spacer(modifier = Modifier.height(4.dp))
            
            // Stats
            Column(
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                StatRow("❤️ Health", "${stats.hp.toInt()}")
                StatRow("⚔️ Damage", "${stats.damage.toInt()}")
                StatRow("🎯 Range", "${stats.range.toInt()}")
                StatRow("⚡ Attack Speed", String.format("%.1fx", stats.attackSpeed))
                StatRow("💥 DPS", String.format("%.1f", stats.dps), isHighlight = true)
            }
        }
    }
}

@Composable
private fun StatRow(
    label: String,
    value: String,
    isHighlight: Boolean = false
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            fontSize = 13.sp,
            color = Color.White.copy(alpha = 0.8f)
        )
        Text(
            text = value,
            fontSize = 13.sp,
            fontWeight = if (isHighlight) FontWeight.Bold else FontWeight.Normal,
            color = if (isHighlight) Color(0xFF4CAF50) else Color(0xFFFFD700)
        )
    }
}

private fun getUnitStats(unitType: UnitEntity.UnitType): UnitStats {
    return when (unitType) {
        UnitEntity.UnitType.CAVALRY -> UnitStats(
            hp = GameConfig.CAVALRY_HP,
            damage = GameConfig.CAVALRY_DAMAGE,
            range = GameConfig.CAVALRY_RANGE,
            attackSpeed = GameConfig.CAVALRY_ATTACK_SPEED,
            dps = GameConfig.CAVALRY_DAMAGE * GameConfig.CAVALRY_ATTACK_SPEED
        )
        UnitEntity.UnitType.SPEARMAN -> UnitStats(
            hp = GameConfig.SPEARMAN_HP,
            damage = GameConfig.SPEARMAN_DAMAGE,
            range = GameConfig.SPEARMAN_RANGE,
            attackSpeed = GameConfig.SPEARMAN_ATTACK_SPEED,
            dps = GameConfig.SPEARMAN_DAMAGE * GameConfig.SPEARMAN_ATTACK_SPEED
        )
        UnitEntity.UnitType.ARCHER -> UnitStats(
            hp = GameConfig.ARCHER_HP,
            damage = GameConfig.ARCHER_DAMAGE,
            range = GameConfig.ARCHER_RANGE,
            attackSpeed = GameConfig.ARCHER_ATTACK_SPEED,
            dps = GameConfig.ARCHER_DAMAGE * GameConfig.ARCHER_ATTACK_SPEED
        )
        UnitEntity.UnitType.KNIGHT -> UnitStats(
            hp = GameConfig.KNIGHT_HP,
            damage = GameConfig.KNIGHT_DAMAGE,
            range = GameConfig.KNIGHT_RANGE,
            attackSpeed = GameConfig.KNIGHT_ATTACK_SPEED,
            dps = GameConfig.KNIGHT_DAMAGE * GameConfig.KNIGHT_ATTACK_SPEED
        )
        UnitEntity.UnitType.HEAVY_WEAPON -> UnitStats(
            hp = GameConfig.HEAVY_WEAPON_HP,
            damage = GameConfig.HEAVY_WEAPON_DAMAGE,
            range = GameConfig.HEAVY_WEAPON_RANGE,
            attackSpeed = GameConfig.HEAVY_WEAPON_ATTACK_SPEED,
            dps = GameConfig.HEAVY_WEAPON_DAMAGE * GameConfig.HEAVY_WEAPON_ATTACK_SPEED
        )
        UnitEntity.UnitType.ELF_KNIGHT -> UnitStats(
            hp = GameConfig.KNIGHT_HP,
            damage = GameConfig.KNIGHT_DAMAGE,
            range = GameConfig.KNIGHT_RANGE,
            attackSpeed = GameConfig.KNIGHT_ATTACK_SPEED,
            dps = GameConfig.KNIGHT_DAMAGE * GameConfig.KNIGHT_ATTACK_SPEED
        )
    }
}

private fun getUnitDisplayName(unitType: UnitEntity.UnitType): String {
    return when (unitType) {
        UnitEntity.UnitType.CAVALRY -> "Cavalry"
        UnitEntity.UnitType.SPEARMAN -> "Spearman"
        UnitEntity.UnitType.ARCHER -> "Archer"
        UnitEntity.UnitType.KNIGHT -> "Knight"
        UnitEntity.UnitType.HEAVY_WEAPON -> "Heavy Weapon"
        UnitEntity.UnitType.ELF_KNIGHT -> "Elf Knight"
    }
}

private fun getUnitDescription(unitType: UnitEntity.UnitType): String {
    return when (unitType) {
        UnitEntity.UnitType.CAVALRY -> "Fast mobile unit with high damage. Excellent for hit-and-run tactics."
        UnitEntity.UnitType.SPEARMAN -> "Balanced infantry unit with good range. Reliable frontline fighter."
        UnitEntity.UnitType.ARCHER -> "Long-range unit with high DPS. Fragile but deadly from distance."
        UnitEntity.UnitType.KNIGHT -> "Heavy armored unit with high HP. Tank that can absorb damage."
        UnitEntity.UnitType.HEAVY_WEAPON -> "Powerful siege unit with high damage. Slow but devastating."
        UnitEntity.UnitType.ELF_KNIGHT -> "Elite magical knight with balanced stats. Versatile combat unit."
    }
}

private fun getUnitTypeText(unitType: UnitEntity.UnitType): String {
    return when (unitType) {
        UnitEntity.UnitType.CAVALRY -> "Fast Attacker"
        UnitEntity.UnitType.SPEARMAN -> "Infantry"
        UnitEntity.UnitType.ARCHER -> "Ranged DPS"
        UnitEntity.UnitType.KNIGHT -> "Tank"
        UnitEntity.UnitType.HEAVY_WEAPON -> "Siege"
        UnitEntity.UnitType.ELF_KNIGHT -> "Elite"
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
