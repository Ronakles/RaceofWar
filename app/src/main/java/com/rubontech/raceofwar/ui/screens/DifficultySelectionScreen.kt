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
import com.rubontech.raceofwar.ui.screens.Difficulty


/**
 * Difficulty Selection Screen for Race of War
 * Allows players to choose game difficulty before starting
 */
@Composable
fun DifficultySelectionScreen(
    onDifficultySelected: (Difficulty) -> Unit,
    onStartGame: () -> Unit,
    onBack: () -> Unit
) {
    var selectedDifficulty by remember { mutableStateOf(Difficulty.NORMAL) }
    
    // Animation states
    val infiniteTransition = rememberInfiniteTransition(label = "difficulty_animation")
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
                .padding(12.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
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
                    text = "SELECT DIFFICULTY",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                
                Spacer(modifier = Modifier.width(48.dp))
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
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
                        text = "Choose Your Challenge",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .graphicsLayer(scaleX = titleScale, scaleY = titleScale)
                            .shadow(8.dp)
                    )
                    
                    Text(
                        text = "Select difficulty level",
                        fontSize = 12.sp,
                        color = Color(0xFFB8C5D6),
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(top = 2.dp)
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            // Difficulty Selection
            AnimatedVisibility(
                visible = true,
                enter = slideInVertically(
                    animationSpec = tween(800, delayMillis = 300),
                    initialOffsetY = { it }
                ) + fadeIn(animationSpec = tween(800, delayMillis = 300))
            ) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Difficulty.values().forEach { difficulty ->
                        DifficultyCard(
                            difficulty = difficulty,
                            isSelected = difficulty == selectedDifficulty,
                            onClick = { 
                                selectedDifficulty = difficulty
                                onDifficultySelected(difficulty)
                            }
                        )
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            // Navigation Buttons
            AnimatedVisibility(
                visible = true,
                enter = slideInVertically(
                    animationSpec = tween(600, delayMillis = 600),
                    initialOffsetY = { it }
                ) + fadeIn(animationSpec = tween(600, delayMillis = 600))
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    // Back Button
                    Button(
                        onClick = onBack,
                        modifier = Modifier
                            .width(90.dp)
                            .height(36.dp)
                            .padding(end = 6.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF6B7280)
                        ),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "←",
                                fontSize = 16.sp,
                                modifier = Modifier.padding(end = 4.dp)
                            )
                            Text(
                                text = "BACK",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                    
                    // Start Game Button
                    Button(
                        onClick = onStartGame,
                        modifier = Modifier
                            .width(110.dp)
                            .height(36.dp)
                            .padding(start = 6.dp)
                            .graphicsLayer(alpha = cardGlow),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF10B981)
                        ),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "START",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(end = 4.dp)
                            )
                            Text(
                                text = "▶️",
                                fontSize = 14.sp
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun DifficultyCard(
    difficulty: Difficulty,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val difficultyInfo = when (difficulty) {
        Difficulty.EASY -> DifficultyInfo(
            name = "Easy",
            description = "Beginner friendly",
            icon = "😊",
            color = Color(0xFF10B981)
        )
        Difficulty.NORMAL -> DifficultyInfo(
            name = "Normal",
            description = "Balanced challenge",
            icon = "⚔️",
            color = Color(0xFF3B82F6)
        )
        Difficulty.HARD -> DifficultyInfo(
            name = "Hard",
            description = "Strategy required",
            icon = "🔥",
            color = Color(0xFFF59E0B)
        )
        Difficulty.EXPERT -> DifficultyInfo(
            name = "Expert",
            description = "Ultimate challenge",
            icon = "💀",
            color = Color(0xFFEF4444)
        )
    }
    
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .shadow(
                elevation = if (isSelected) 8.dp else 4.dp,
                spotColor = if (isSelected) difficultyInfo.color else Color.Transparent
            )
            .border(
                width = if (isSelected) 2.dp else 1.dp,
                color = if (isSelected) difficultyInfo.color else Color(0xFF374151),
                shape = RoundedCornerShape(12.dp)
            ),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) Color(0xFF1F2937) else Color(0xFF111827)
        ),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Icon
            Box(
                modifier = Modifier
                    .size(28.dp)
                    .background(
                        color = difficultyInfo.color,
                        shape = RoundedCornerShape(4.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = difficultyInfo.icon,
                    fontSize = 14.sp
                )
            }
            
            Spacer(modifier = Modifier.width(6.dp))
            
            // Content
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = "${difficultyInfo.name}${if (isSelected) " ✓" else ""}",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (isSelected) difficultyInfo.color else Color.White
                )
                
                Text(
                    text = difficultyInfo.description,
                    fontSize = 10.sp,
                    color = Color(0xFFD1D5DB),
                    lineHeight = 12.sp
                )
            }
        }
    }
}

// Data classes
private data class DifficultyInfo(
    val name: String,
    val description: String,
    val icon: String,
    val color: Color
)
