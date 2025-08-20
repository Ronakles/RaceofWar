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
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
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
 * Settings Screen for Race of War
 * Features game options, audio settings, and graphics settings
 */
@Composable
fun SettingsScreen(
    onBack: () -> Unit,
    onSaveSettings: (GameSettings) -> Unit
) {
    var settings by remember { mutableStateOf(GameSettings()) }
    var showSaveConfirmation by remember { mutableStateOf(false) }
    
    // Animation states
    val infiniteTransition = rememberInfiniteTransition(label = "settings_animation")
    val cardGlow by infiniteTransition.animateFloat(
        initialValue = 0.8f,
        targetValue = 1.2f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = FastOutSlowInEasing),
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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            // Header
            AnimatedVisibility(
                visible = true,
                enter = slideInHorizontally(
                    animationSpec = tween(800),
                    initialOffsetX = { -it }
                ) + fadeIn(animationSpec = tween(800))
            ) {
                SettingsHeader(onBack = onBack)
            }
            
            // Game Settings
            AnimatedVisibility(
                visible = true,
                enter = slideInHorizontally(
                    animationSpec = tween(600, delayMillis = 200),
                    initialOffsetX = { -it }
                ) + fadeIn(animationSpec = tween(600, delayMillis = 200))
            ) {
                SettingsCard(
                    title = "Game Settings",
                    icon = "🎮"
                ) {
                    GameSettingsSection(
                        settings = settings,
                        onSettingsChanged = { settings = it }
                    )
                }
            }
            
            // Audio Settings
            AnimatedVisibility(
                visible = true,
                enter = slideInHorizontally(
                    animationSpec = tween(600, delayMillis = 400),
                    initialOffsetX = { -it }
                ) + fadeIn(animationSpec = tween(600, delayMillis = 400))
            ) {
                SettingsCard(
                    title = "Audio Settings",
                    icon = "🔊"
                ) {
                    AudioSettingsSection(
                        settings = settings,
                        onSettingsChanged = { settings = it }
                    )
                }
            }
            
            // Graphics Settings
            AnimatedVisibility(
                visible = true,
                enter = slideInHorizontally(
                    animationSpec = tween(600, delayMillis = 600),
                    initialOffsetX = { -it }
                ) + fadeIn(animationSpec = tween(600, delayMillis = 600))
            ) {
                SettingsCard(
                    title = "Graphics Settings",
                    icon = "🎨"
                ) {
                    GraphicsSettingsSection(
                        settings = settings,
                        onSettingsChanged = { settings = it }
                    )
                }
            }
            
            // Save Button
            AnimatedVisibility(
                visible = true,
                enter = slideInHorizontally(
                    animationSpec = tween(600, delayMillis = 800),
                    initialOffsetX = { -it }
                ) + fadeIn(animationSpec = tween(600, delayMillis = 800))
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Button(
                        onClick = {
                            onSaveSettings(settings)
                            showSaveConfirmation = true
                        },
                        modifier = Modifier
                            .weight(1f)
                            .height(56.dp)
                            .shadow(8.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF10B981)
                        ),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Text(
                            text = "💾 SAVE SETTINGS",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    
                    Button(
                        onClick = onBack,
                        modifier = Modifier
                            .weight(1f)
                            .height(56.dp)
                            .shadow(8.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF6B7280)
                        ),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Text(
                            text = "↩️ BACK",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(40.dp))
        }
        
        // Save Confirmation
        if (showSaveConfirmation) {
            SaveConfirmationDialog(
                onDismiss = { showSaveConfirmation = false }
            )
        }
    }
}

@Composable
private fun SettingsHeader(onBack: () -> Unit) {
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
            text = "SETTINGS",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )
    }
}

@Composable
private fun SettingsCard(
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
private fun GameSettingsSection(
    settings: GameSettings,
    onSettingsChanged: (GameSettings) -> Unit
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Difficulty setting removed - now handled in game flow
        
        // Auto Save Setting
        SettingRow(
            label = "Auto Save",
            description = "Automatically save game progress"
        ) {
            Switch(
                checked = settings.autoSave,
                onCheckedChange = {
                    onSettingsChanged(settings.copy(autoSave = it))
                },
                colors = SwitchDefaults.colors(
                    checkedThumbColor = Color(0xFF10B981),
                    checkedTrackColor = Color(0xFF10B981)
                )
            )
        }
        
        // Tutorial Setting
        SettingRow(
            label = "Show Tutorial",
            description = "Display helpful game tips"
        ) {
            Switch(
                checked = settings.showTutorial,
                onCheckedChange = {
                    onSettingsChanged(settings.copy(showTutorial = it))
                },
                colors = SwitchDefaults.colors(
                    checkedThumbColor = Color(0xFF10B981),
                    checkedTrackColor = Color(0xFF10B981)
                )
            )
        }
    }
}

@Composable
private fun AudioSettingsSection(
    settings: GameSettings,
    onSettingsChanged: (GameSettings) -> Unit
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Master Volume
        SettingRow(
            label = "Master Volume",
            description = "Overall game audio level"
        ) {
            Slider(
                value = settings.masterVolume,
                onValueChange = {
                    onSettingsChanged(settings.copy(masterVolume = it))
                },
                valueRange = 0f..1f,
                steps = 20,
                colors = SliderDefaults.colors(
                    thumbColor = Color(0xFF3B82F6),
                    activeTrackColor = Color(0xFF3B82F6)
                ),
                modifier = Modifier.width(200.dp)
            )
            
            Text(
                text = "${(settings.masterVolume * 100).toInt()}%",
                fontSize = 12.sp,
                color = Color(0xFF9CA3AF),
                modifier = Modifier.padding(start = 8.dp)
            )
        }
        
        // Music Volume
        SettingRow(
            label = "Music Volume",
            description = "Background music level"
        ) {
            Slider(
                value = settings.musicVolume,
                onValueChange = {
                    onSettingsChanged(settings.copy(musicVolume = it))
                },
                valueRange = 0f..1f,
                steps = 20,
                colors = SliderDefaults.colors(
                    thumbColor = Color(0xFF10B981),
                    activeTrackColor = Color(0xFF10B981)
                ),
                modifier = Modifier.width(200.dp)
            )
            
            Text(
                text = "${(settings.musicVolume * 100).toInt()}%",
                fontSize = 12.sp,
                color = Color(0xFF9CA3AF),
                modifier = Modifier.padding(start = 8.dp)
            )
        }
        
        // Sound Effects Volume
        SettingRow(
            label = "Sound Effects",
            description = "Game action sounds level"
        ) {
            Slider(
                value = settings.sfxVolume,
                onValueChange = {
                    onSettingsChanged(settings.copy(sfxVolume = it))
                },
                valueRange = 0f..1f,
                steps = 20,
                colors = SliderDefaults.colors(
                    thumbColor = Color(0xFFF59E0B),
                    activeTrackColor = Color(0xFFF59E0B)
                ),
                modifier = Modifier.width(200.dp)
            )
            
            Text(
                text = "${(settings.sfxVolume * 100).toInt()}%",
                fontSize = 12.sp,
                color = Color(0xFF9CA3AF),
                modifier = Modifier.padding(start = 8.dp)
            )
        }
    }
}

@Composable
private fun GraphicsSettingsSection(
    settings: GameSettings,
    onSettingsChanged: (GameSettings) -> Unit
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Graphics Quality
        SettingRow(
            label = "Graphics Quality",
            description = "Visual detail and performance balance"
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                GraphicsQuality.values().forEach { quality ->
                    FilterChip(
                        selected = settings.graphicsQuality == quality,
                        onClick = {
                            onSettingsChanged(settings.copy(graphicsQuality = quality))
                        },
                        label = {
                            Text(
                                text = quality.displayName,
                                fontSize = 12.sp
                            )
                        },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = Color(0xFF8B5CF6),
                            selectedLabelColor = Color.White
                        )
                    )
                }
            }
        }
        
        // Frame Rate
        SettingRow(
            label = "Target Frame Rate",
            description = "Game performance optimization"
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                FrameRate.values().forEach { frameRate ->
                    FilterChip(
                        selected = settings.targetFrameRate == frameRate,
                        onClick = {
                            onSettingsChanged(settings.copy(targetFrameRate = frameRate))
                        },
                        label = {
                            Text(
                                text = frameRate.displayName,
                                fontSize = 12.sp
                            )
                        },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = Color(0xFF8B5CF6),
                            selectedLabelColor = Color.White
                        )
                    )
                }
            }
        }
        
        // Particle Effects
        SettingRow(
            label = "Particle Effects",
            description = "Show combat and magic effects"
        ) {
            Switch(
                checked = settings.showParticles,
                onCheckedChange = {
                    onSettingsChanged(settings.copy(showParticles = it))
                },
                colors = SwitchDefaults.colors(
                    checkedThumbColor = Color(0xFF8B5CF6),
                    checkedTrackColor = Color(0xFF8B5CF6)
                )
            )
        }
    }
}

@Composable
private fun SettingRow(
    label: String,
    description: String,
    content: @Composable () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = label,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = Color.White
            )
            
            Text(
                text = description,
                fontSize = 12.sp,
                color = Color(0xFF9CA3AF)
            )
        }
        
        content()
    }
}

@Composable
private fun SaveConfirmationDialog(onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "Settings Saved!",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        },
        text = {
            Text(
                text = "Your game settings have been saved successfully.",
                fontSize = 14.sp,
                color = Color(0xFFD1D5DB)
            )
        },
        confirmButton = {
            TextButton(
                onClick = onDismiss,
                colors = ButtonDefaults.textButtonColors(
                    contentColor = Color(0xFF3B82F6)
                )
            ) {
                Text("OK")
            }
        },
        containerColor = Color(0xFF1F2937),
        shape = RoundedCornerShape(16.dp)
    )
}

// GameSettings, Difficulty, GraphicsQuality, and FrameRate are now defined in GameSettings.kt

