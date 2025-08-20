/*
 * MIT License
 * 
 * Copyright (c) 2024 Rubontech
 */

package com.rubontech.raceofwar.ui.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.horizontalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rubontech.raceofwar.game.entities.UnitEntity
import kotlinx.coroutines.delay

/**
 * Race Selection Screen for Race of War
 * Allows players to choose their race before starting the game
 */
@Composable
fun RaceSelectionScreen(
    selectedRace: UnitEntity.Race,
    onRaceSelected: (UnitEntity.Race) -> Unit,
    onNext: () -> Unit,
    onBack: () -> Unit
) {
    var showRaceInfo by remember { mutableStateOf(false) }
    
    // Animation states
    val infiniteTransition = rememberInfiniteTransition(label = "race_selection_animation")
    val titleScale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.05f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "title_scale"
    )
    
    val cardGlow by infiniteTransition.animateFloat(
        initialValue = 0.8f,
        targetValue = 1.2f,
        animationSpec = infiniteRepeatable(
            animation = tween(3000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "card_glow"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF0D1B2A), // Dark blue
                        Color(0xFF1B263B), // Medium blue
                        Color(0xFF2C3E50)  // Lighter blue
                    )
                )
            )
    ) {
        // Background pattern
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.radialGradient(
                        colors = listOf(
                            Color(0x1AFFFFFF),
                            Color(0x00FFFFFF)
                        ),
                        center = androidx.compose.ui.geometry.Offset(0f, 0f),
                        radius = 800f
                    )
                )
        )
        
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = onBack,
                    colors = IconButtonDefaults.iconButtonColors(
                        contentColor = Color.White
                    )
                ) {
                    Text(
                        text = "←",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                
                Text(
                    text = "CHOOSE YOUR RACE",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                
                Spacer(modifier = Modifier.width(48.dp))
            }
            
            Spacer(modifier = Modifier.height(20.dp))
            
            // Title
            AnimatedVisibility(
                visible = true,
                enter = slideInVertically(
                    animationSpec = tween(1000),
                    initialOffsetY = { -it }
                ) + fadeIn(animationSpec = tween(1000))
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Select Your Faction",
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .graphicsLayer(scaleX = titleScale, scaleY = titleScale)
                            .shadow(8.dp)
                    )
                    
                    Text(
                        text = "Each race has unique abilities and strategies",
                        fontSize = 14.sp,
                        color = Color(0xFFB8C5D6),
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Race Selection
            AnimatedVisibility(
                visible = true,
                enter = slideInVertically(
                    animationSpec = tween(800, delayMillis = 300),
                    initialOffsetY = { it }
                ) + fadeIn(animationSpec = tween(800, delayMillis = 300))
            ) {
                RaceSelectionSection(
                    selectedRace = selectedRace,
                    onRaceSelected = { onRaceSelected(it) },
                    onRaceInfoClick = { showRaceInfo = !showRaceInfo },
                    onNext = onNext
                )
            }
            
            // Race Info
            AnimatedVisibility(
                visible = showRaceInfo,
                enter = fadeIn(animationSpec = tween(500)) + slideInVertically(
                    animationSpec = tween(500),
                    initialOffsetY = { 50 }
                ),
                exit = fadeOut(animationSpec = tween(300)) + slideOutVertically(
                    animationSpec = tween(300),
                    targetOffsetY = { 50 }
                )
            ) {
                RaceInfoCard(selectedRace = selectedRace)
            }
            
            Spacer(modifier = Modifier.height(20.dp))
            
            // Back Button Only (Next is automatic)
            AnimatedVisibility(
                visible = true,
                enter = slideInVertically(
                    animationSpec = tween(600, delayMillis = 600),
                    initialOffsetY = { it }
                ) + fadeIn(animationSpec = tween(600, delayMillis = 600))
            ) {
                Button(
                    onClick = onBack,
                    modifier = Modifier
                        .width(120.dp)
                        .height(48.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF6B7280)
                    ),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Row(
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "←",
                            fontSize = 20.sp,
                            modifier = Modifier.padding(end = 8.dp)
                        )
                        Text(
                            text = "BACK",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun RaceSelectionSection(
    selectedRace: UnitEntity.Race,
    onRaceSelected: (UnitEntity.Race) -> Unit,
    onRaceInfoClick: () -> Unit,
    onNext: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.horizontalScroll(rememberScrollState())
        ) {
            UnitEntity.Race.values().forEach { race ->
                RaceCard(
                    race = race,
                    isSelected = race == selectedRace,
                    onClick = { 
                        onRaceSelected(race)
                        onNext()
                    }
                )
            }
        }
        
        // Race Info Button
        TextButton(
            onClick = onRaceInfoClick,
            colors = ButtonDefaults.textButtonColors(
                contentColor = Color(0xFF60A5FA)
            )
        ) {
            Text(
                text = if (selectedRace == UnitEntity.Race.NATURE_TRIBE) "ℹ️ Learn about Nature Tribe" 
                       else "ℹ️ Learn about ${selectedRace.displayName}",
                fontSize = 14.sp
            )
        }
    }
}

@Composable
private fun RaceCard(
    race: UnitEntity.Race,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val raceColors = when (race) {
        UnitEntity.Race.NATURE_TRIBE -> RaceColors(
            primary = Color(0xFF10B981),
            secondary = Color(0xFF059669),
            accent = Color(0xFF34D399)
        )
        UnitEntity.Race.MECHANICAL_LEGION -> RaceColors(
            primary = Color(0xFF6B7280),
            secondary = Color(0xFF4B5563),
            accent = Color(0xFF9CA3AF)
        )
        UnitEntity.Race.HUMAN_EMPIRE -> RaceColors(
            primary = Color(0xFFF59E0B),
            secondary = Color(0xFFD97706),
            accent = Color(0xFFFBBF24)
        )
        UnitEntity.Race.DARK_CULT -> RaceColors(
            primary = Color(0xFF7C3AED),
            secondary = Color(0xFF6D28D9),
            accent = Color(0xFFA78BFA)
        )
    }
    
    Card(
        modifier = Modifier
            .size(120.dp)
            .clickable { onClick() }
            .shadow(
                elevation = if (isSelected) 16.dp else 8.dp,
                spotColor = if (isSelected) raceColors.accent else Color.Transparent
            )
            .border(
                width = if (isSelected) 3.dp else 1.dp,
                color = if (isSelected) raceColors.accent else Color(0xFF374151),
                shape = RoundedCornerShape(16.dp)
            ),
        colors = CardDefaults.cardColors(
            containerColor = raceColors.secondary
        ),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = race.emoji,
                fontSize = 32.sp
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                text = race.displayName,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                textAlign = TextAlign.Center
            )
            
            if (isSelected) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "✓ SELECTED",
                    fontSize = 10.sp,
                    color = raceColors.accent,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
private fun RaceInfoCard(selectedRace: UnitEntity.Race) {
    val raceInfo = when (selectedRace) {
        UnitEntity.Race.HUMAN_EMPIRE -> RaceInfo(
            name = "👑 Humans – Origins",
            description = "Once scattered tribes, Humans rose by forging kingdoms and disciplined armies. Their strength comes from unity, strategy, and the will to lead.",
            strengths = listOf("Unity", "Strategy", "Leadership"),
            weaknesses = listOf("Average stats", "No special abilities")
        )
        UnitEntity.Race.DARK_CULT -> RaceInfo(
            name = "☠️ Dark Cultists – Origins",
            description = "Born of forbidden rites and ancient heresies, the Cultists draw power from shadows, curses, and demonic bargains. They spread chaos wherever they march.",
            strengths = listOf("Dark Magic", "Curses", "Chaos"),
            weaknesses = listOf("Low HP", "Vulnerable to light")
        )
        UnitEntity.Race.NATURE_TRIBE -> RaceInfo(
            name = "🌿 Elves – Origins",
            description = "Descendants of the ancient forests, Elves live in harmony with nature and magic. Swift, graceful, and wise, they defend their lands with unmatched precision.",
            strengths = listOf("Nature Magic", "Agility", "Precision"),
            weaknesses = listOf("Low armor", "Vulnerable to fire")
        )
        UnitEntity.Race.MECHANICAL_LEGION -> RaceInfo(
            name = "⚙️ Mechanical Legion – Origins",
            description = "Created as weapons of war, the Legion is a relentless machine host. They know no fear, no mercy—only the perfection of cold steel and technology.",
            strengths = listOf("Technology", "Relentless", "Cold Steel"),
            weaknesses = listOf("Slow movement", "High cost")
        )
    }
    
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF1F2937)
        ),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = raceInfo.name,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            
            Text(
                text = raceInfo.description,
                fontSize = 13.sp,
                color = Color(0xFFD1D5DB),
                lineHeight = 18.sp
            )
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Strengths:",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF10B981)
                    )
                    raceInfo.strengths.forEach { strength ->
                        Text(
                            text = "• $strength",
                            fontSize = 11.sp,
                            color = Color(0xFFD1D5DB)
                        )
                    }
                }
                
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Weaknesses:",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFEF4444)
                    )
                    raceInfo.weaknesses.forEach { weakness ->
                        Text(
                            text = "• $weakness",
                            fontSize = 11.sp,
                            color = Color(0xFFD1D5DB)
                        )
                    }
                }
            }
        }
    }
}

// Extension properties for Race enum
private val UnitEntity.Race.displayName: String
    get() = when (this) {
        UnitEntity.Race.NATURE_TRIBE -> "Nature"
        UnitEntity.Race.MECHANICAL_LEGION -> "Mech"
        UnitEntity.Race.HUMAN_EMPIRE -> "Human"
        UnitEntity.Race.DARK_CULT -> "Dark"
    }

private val UnitEntity.Race.emoji: String
    get() = when (this) {
        UnitEntity.Race.NATURE_TRIBE -> "🌿"
        UnitEntity.Race.MECHANICAL_LEGION -> "🤖"
        UnitEntity.Race.HUMAN_EMPIRE -> "👑"
        UnitEntity.Race.DARK_CULT -> "💀"
    }

// Data classes
private data class RaceColors(
    val primary: Color,
    val secondary: Color,
    val accent: Color
)

private data class RaceInfo(
    val name: String,
    val description: String,
    val strengths: List<String>,
    val weaknesses: List<String>
)
