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
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * Credits Screen for Race of War
 * Shows game developers, artists, and acknowledgments
 */
@Composable
fun CreditsScreen(
    onBack: () -> Unit
) {
    // Animation states
    val infiniteTransition = rememberInfiniteTransition(label = "credits_animation")
    val titleGlow by infiniteTransition.animateFloat(
        initialValue = 0.8f,
        targetValue = 1.2f,
        animationSpec = infiniteRepeatable(
            animation = tween(3000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "title_glow"
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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(32.dp)
        ) {
            // Header
            AnimatedVisibility(
                visible = true,
                enter = slideInVertically(
                    animationSpec = tween(800),
                    initialOffsetY = { -it }
                ) + fadeIn(animationSpec = tween(800))
            ) {
                CreditsHeader(onBack = onBack)
            }
            
            // Game Title
            AnimatedVisibility(
                visible = true,
                enter = slideInVertically(
                    animationSpec = tween(600, delayMillis = 200),
                    initialOffsetY = { -it }
                ) + fadeIn(animationSpec = tween(600, delayMillis = 200))
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
                        modifier = Modifier.shadow(16.dp)
                    )
                    
                    Text(
                        text = "Epic Strategy Battle Game",
                        fontSize = 16.sp,
                        color = Color(0xFFB8C5D6),
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                    
                    Text(
                        text = "Version 1.0.0",
                        fontSize = 14.sp,
                        color = Color(0xFF6B7280),
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
            }
            
            // Development Team
            AnimatedVisibility(
                visible = true,
                enter = slideInVertically(
                    animationSpec = tween(600, delayMillis = 400),
                    initialOffsetY = { it }
                ) + fadeIn(animationSpec = tween(600, delayMillis = 400))
            ) {
                CreditsCard(
                    title = "Development Team",
                    icon = "👨‍💻"
                ) {
                    CreditsSection(
                        title = "Lead Developer",
                        items = listOf(
                            "Rubontech - Game Engine & Core Systems"
                        )
                    )
                    
                    CreditsSection(
                        title = "Game Design",
                        items = listOf(
                            "Rubontech - Game Mechanics & Balance",
                            "Community - Feedback & Testing"
                        )
                    )
                    
                    CreditsSection(
                        title = "Technical Implementation",
                        items = listOf(
                            "Kotlin & Jetpack Compose",
                            "Custom 2D Game Engine",
                            "Android SurfaceView Rendering"
                        )
                    )
                }
            }
            
            // Graphics & Assets
            AnimatedVisibility(
                visible = true,
                enter = slideInVertically(
                    animationSpec = tween(600, delayMillis = 600),
                    initialOffsetY = { it }
                ) + fadeIn(animationSpec = tween(600, delayMillis = 600))
            ) {
                CreditsCard(
                    title = "Graphics & Assets",
                    icon = "🎨"
                ) {
                    CreditsSection(
                        title = "Sprite Sheets",
                        items = listOf(
                            "Elf Knight Animations - 64x64 Grid",
                            "Unit Sprites - Custom Design",
                            "Background Assets - Race-themed"
                        )
                    )
                    
                    CreditsSection(
                        title = "UI Design",
                        items = listOf(
                            "Material Design 3",
                            "Custom Game HUD",
                            "Animated Menu Elements"
                        )
                    )
                }
            }
            
            // Special Thanks
            AnimatedVisibility(
                visible = true,
                enter = slideInVertically(
                    animationSpec = tween(600, delayMillis = 800),
                    initialOffsetY = { it }
                ) + fadeIn(animationSpec = tween(600, delayMillis = 800))
            ) {
                CreditsCard(
                    title = "Special Thanks",
                    icon = "🙏"
                ) {
                    CreditsSection(
                        title = "Open Source",
                        items = listOf(
                            "Android Development Tools",
                            "Jetpack Compose",
                            "Kotlin Language"
                        )
                    )
                    
                    CreditsSection(
                        title = "Community",
                        items = listOf(
                            "Beta Testers",
                            "Feedback Contributors",
                            "Gaming Community"
                        )
                    )
                }
            }
            
            // Copyright & License
            AnimatedVisibility(
                visible = true,
                enter = slideInVertically(
                    animationSpec = tween(600, delayMillis = 1000),
                    initialOffsetY = { it }
                ) + fadeIn(animationSpec = tween(600, delayMillis = 1000))
            ) {
                CreditsCard(
                    title = "Copyright & License",
                    icon = "📜"
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Text(
                            text = "MIT License",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF10B981)
                        )
                        
                        Text(
                            text = "Copyright (c) 2024 Rubontech",
                            fontSize = 14.sp,
                            color = Color(0xFFD1D5DB),
                            textAlign = TextAlign.Center
                        )
                        
                        Text(
                            text = "Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the \"Software\"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:",
                            fontSize = 12.sp,
                            color = Color(0xFF9CA3AF),
                            textAlign = TextAlign.Center,
                            lineHeight = 16.sp
                        )
                        
                        Text(
                            text = "The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.",
                            fontSize = 12.sp,
                            color = Color(0xFF9CA3AF),
                            textAlign = TextAlign.Center,
                            lineHeight = 16.sp
                        )
                    }
                }
            }
            
            // Back Button
            AnimatedVisibility(
                visible = true,
                enter = slideInVertically(
                    animationSpec = tween(600, delayMillis = 1200),
                    initialOffsetY = { it }
                ) + fadeIn(animationSpec = tween(600, delayMillis = 1200))
            ) {
                Button(
                    onClick = onBack,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                        .shadow(8.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF6B7280)
                    ),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Text(
                        text = "↩️ BACK TO MENU",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}

@Composable
private fun CreditsHeader(onBack: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(
            onClick = onBack,
            modifier = Modifier
                .size(48.dp)
                .shadow(4.dp)
                .background(
                    color = Color(0xFF374151),
                    shape = RoundedCornerShape(12.dp)
                )
        ) {
            Text(
                text = "←",
                fontSize = 24.sp,
                color = Color.White
            )
        }
        
        Spacer(modifier = Modifier.width(16.dp))
        
        Text(
            text = "CREDITS",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )
    }
}

@Composable
private fun CreditsCard(
    title: String,
    icon: String,
    content: @Composable () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF1F2937)
        ),
        shape = RoundedCornerShape(20.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = icon,
                    fontSize = 24.sp
                )
                
                Spacer(modifier = Modifier.width(12.dp))
                
                Text(
                    text = title,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
            
            content()
        }
    }
}

@Composable
private fun CreditsSection(
    title: String,
    items: List<String>
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = title,
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            color = Color(0xFF10B981)
        )
        
        items.forEach { item ->
            Text(
                text = "• $item",
                fontSize = 14.sp,
                color = Color(0xFFD1D5DB),
                modifier = Modifier.padding(start = 16.dp)
            )
        }
    }
}


