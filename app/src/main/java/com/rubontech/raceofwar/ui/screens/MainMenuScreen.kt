/*
 * MIT License
 * 
 * Copyright (c) 2024 Rubontech
 */

package com.rubontech.raceofwar.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.*
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
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
import com.rubontech.raceofwar.ui.theme.*

/**
 * Main Menu Screen for Race of War
 * Features race selection, game options, and beautiful animations
 */
@Composable
fun MainMenuScreen(
    onStartGame: () -> Unit,
    onSettings: () -> Unit,
    onAchievements: () -> Unit,
    onStore: () -> Unit,
    onBookOfRaces: () -> Unit,
    onExit: () -> Unit,
    onCredits: () -> Unit
) {
    
    // Animation states
    val infiniteTransition = rememberInfiniteTransition(label = "menu_animation")
    val titleScale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.05f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "title_scale"
    )
    
    val buttonOffset by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 10f,
        animationSpec = infiniteRepeatable(
            animation = tween(3000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "button_offset"
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
            Spacer(modifier = Modifier.height(20.dp))
            
            // Game Title
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
                        text = "RACE OF WAR",
                        fontSize = 36.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .graphicsLayer(scaleX = titleScale, scaleY = titleScale)
                            .shadow(8.dp)
                    )
                    
                    Text(
                        text = "Epic Strategy Battle",
                        fontSize = 16.sp,
                        color = Color(0xFFB8C5D6),
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(20.dp))
            
            // Main Menu Buttons
            AnimatedVisibility(
                visible = true,
                enter = slideInVertically(
                    animationSpec = tween(600, delayMillis = 600),
                    initialOffsetY = { it }
                ) + fadeIn(animationSpec = tween(600, delayMillis = 600))
            ) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Play Button
                    MainMenuButton(
                        text = "PLAY",
                        icon = "▶️",
                        onClick = onStartGame,
                        modifier = Modifier.offset(y = buttonOffset.dp),
                        isPrimary = true
                    )
                    
                    // Settings Button
                    MainMenuButton(
                        text = "SETTINGS",
                        icon = "⚙️",
                        onClick = onSettings,
                        modifier = Modifier.offset(y = buttonOffset.dp)
                    )
                    
                    // Achievements Button
                    MainMenuButton(
                        text = "ACHIEVEMENTS",
                        icon = "🏆",
                        onClick = onAchievements,
                        modifier = Modifier.offset(y = buttonOffset.dp)
                    )
                    
                    // Store Button
                    MainMenuButton(
                        text = "STORE",
                        icon = "🛒",
                        onClick = onStore,
                        modifier = Modifier.offset(y = buttonOffset.dp)
                    )
                    
                    // Book of Races Button
                    MainMenuButton(
                        text = "BOOK OF RACES",
                        icon = "📖",
                        onClick = onBookOfRaces,
                        modifier = Modifier.offset(y = buttonOffset.dp)
                    )
                    
                    // Credits Button
                    MainMenuButton(
                        text = "CREDITS",
                        icon = "📜",
                        onClick = onCredits,
                        modifier = Modifier.offset(y = buttonOffset.dp)
                    )
                    
                    // Exit Button
                    MainMenuButton(
                        text = "EXIT",
                        icon = "🚪",
                        onClick = onExit,
                        modifier = Modifier.offset(y = buttonOffset.dp),
                        isDestructive = true
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(20.dp))
            
            // Version Info
            AnimatedVisibility(
                visible = true,
                enter = fadeIn(animationSpec = tween(1000, delayMillis = 1200))
            ) {
                Text(
                    text = "Race of War v1.0.0 • Rubontech",
                    fontSize = 12.sp,
                    color = Color(0xFF6B7280),
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}



@Composable
private fun MainMenuButton(
    text: String,
    icon: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    isPrimary: Boolean = false,
    isDestructive: Boolean = false
) {
    val buttonColors = when {
        isPrimary -> ButtonDefaults.buttonColors(
            containerColor = Color(0xFF3B82F6),
            contentColor = Color.White
        )
        isDestructive -> ButtonDefaults.buttonColors(
            containerColor = Color(0xFFEF4444),
            contentColor = Color.White
        )
        else -> ButtonDefaults.buttonColors(
            containerColor = Color(0xFF374151),
            contentColor = Color.White
        )
    }
    
    Button(
        onClick = onClick,
        modifier = modifier
            .width(280.dp)
            .height(48.dp)
            .shadow(8.dp),
        colors = buttonColors,
        shape = RoundedCornerShape(16.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = icon,
                fontSize = 20.sp,
                modifier = Modifier.padding(end = 12.dp)
            )
            Text(
                text = text,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}


