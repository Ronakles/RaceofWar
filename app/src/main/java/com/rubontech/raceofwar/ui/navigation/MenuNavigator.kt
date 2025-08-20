/*
 * MIT License
 * 
 * Copyright (c) 2024 Rubontech
 */

package com.rubontech.raceofwar.ui.navigation

import androidx.compose.runtime.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.background
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.rubontech.raceofwar.engine.GameSurface
import com.rubontech.raceofwar.game.entities.UnitEntity
import com.rubontech.raceofwar.game.state.GameState
import com.rubontech.raceofwar.ui.screens.*
import kotlinx.coroutines.delay

/**
 * Navigator for managing menu screen transitions
 */
@Composable
fun MenuNavigator(
    onExitGame: () -> Unit
) {
    var currentScreen by remember { mutableStateOf<MenuScreen>(MenuScreen.MainMenu) }
    var selectedRace by remember { mutableStateOf(UnitEntity.Race.NATURE_TRIBE) }
    var gameSettings by remember { mutableStateOf(GameSettings()) }
    
    when (currentScreen) {
        MenuScreen.MainMenu -> {
            MainMenuScreen(
                onStartGame = {
                    currentScreen = MenuScreen.RaceSelection
                },
                onSettings = {
                    currentScreen = MenuScreen.Settings
                },
                onAchievements = {
                    currentScreen = MenuScreen.Achievements
                },
                onStore = {
                    currentScreen = MenuScreen.Store
                },
                onBookOfRaces = {
                    currentScreen = MenuScreen.BookOfRaces
                },
                onExit = onExitGame,
                onCredits = {
                    currentScreen = MenuScreen.Credits
                }
            )
        }
        
        MenuScreen.RaceSelection -> {
            RaceSelectionScreen(
                selectedRace = selectedRace,
                onRaceSelected = { race ->
                    selectedRace = race
                },
                onNext = {
                    currentScreen = MenuScreen.DifficultySelection
                },
                onBack = {
                    currentScreen = MenuScreen.MainMenu
                }
            )
        }
        
        MenuScreen.DifficultySelection -> {
            DifficultySelectionScreen(
                onDifficultySelected = { difficulty ->
                    gameSettings = gameSettings.copy(difficulty = difficulty)
                },
                onStartGame = {
                    currentScreen = MenuScreen.Game
                },
                onBack = {
                    currentScreen = MenuScreen.RaceSelection
                }
            )
        }
        
        MenuScreen.Settings -> {
            SettingsScreen(
                onBack = {
                    currentScreen = MenuScreen.MainMenu
                },
                onSaveSettings = { settings ->
                    gameSettings = settings
                    // Here you could save settings to SharedPreferences
                    currentScreen = MenuScreen.MainMenu
                }
            )
        }
        
        MenuScreen.Credits -> {
            CreditsScreen(
                onBack = {
                    currentScreen = MenuScreen.MainMenu
                }
            )
        }
        
        MenuScreen.Achievements -> {
            AchievementsScreen(
                onBack = {
                    currentScreen = MenuScreen.MainMenu
                }
            )
        }
        
        MenuScreen.Store -> {
            StoreScreen(
                onBack = {
                    currentScreen = MenuScreen.MainMenu
                }
            )
        }
        
        MenuScreen.BookOfRaces -> {
            BookOfRacesScreen(
                onBack = {
                    currentScreen = MenuScreen.MainMenu
                }
            )
        }
        
        MenuScreen.Game -> {
            // This will be replaced with the actual game screen
            GameScreen(
                selectedRace = selectedRace,
                gameSettings = gameSettings,
                onBackToMenu = {
                    currentScreen = MenuScreen.MainMenu
                }
            )
        }
    }
}

/**
 * Available menu screens
 */
sealed class MenuScreen {
    object MainMenu : MenuScreen()
    object RaceSelection : MenuScreen()
    object DifficultySelection : MenuScreen()
    object Settings : MenuScreen()
    object Credits : MenuScreen()
    object Achievements : MenuScreen()
    object Store : MenuScreen()
    object BookOfRaces : MenuScreen()
    object Game : MenuScreen()
}

/**
 * Actual game screen that launches the game engine with progression system
 */
@Composable
private fun GameScreen(
    selectedRace: UnitEntity.Race,
    gameSettings: GameSettings,
    onBackToMenu: () -> Unit
) {
    val context = LocalContext.current
    val gameState = remember { GameState(selectedRace, gameSettings) }
    var progressionInfo by remember { mutableStateOf(gameState.getProgressionInfo()) }
    
    // Debug: Print selected race and initial units
    LaunchedEffect(Unit) {
        println("🎮 Game started with race: $selectedRace")
        println("🎮 Initial available units: ${gameState.availableUnits.map { it.displayName }}")
    }
    
    // Update game state every second
    LaunchedEffect(Unit) {
        while (true) {
            gameState.update()
            progressionInfo = gameState.getProgressionInfo()
            
            // Debug: Print current state every 5 seconds
            if (System.currentTimeMillis() % 5000 < 1000) {
                println("🎮 Current level: ${gameState.currentLevel}")
                println("🎮 Available units: ${gameState.availableUnits.map { it.displayName }}")
                println("🎮 Total spawned: ${gameState.getTotalSpawnedUnits()}")
            }
            
            delay(1000) // Update every second
        }
    }
    
    Box(modifier = Modifier.fillMaxSize()) {
        AndroidView(
            factory = { ctx ->
                GameSurface(ctx).apply {
                    // Configure game settings here if needed
                    // The game engine will handle the rest
                }
            },
            modifier = Modifier.fillMaxSize(),
            update = { gameSurface ->
                // Update game surface if needed
            }
        )
        
        // Progression UI Overlay (minimalized, top-left)
        ProgressionOverlay(
            progressionInfo = progressionInfo,
            selectedRace = selectedRace,
            availableUnits = gameState.availableUnits,
            gameState = gameState,
            modifier = Modifier.align(Alignment.TopStart)
        )
        
        // Unit Spawn UI Overlay (bottom-center, more visible)
        UnitSpawnOverlay(
            availableUnits = gameState.availableUnits,
            selectedRace = selectedRace,
            gameState = gameState,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 32.dp) // More padding to ensure visibility
        )
        
        // Floating back button overlay (top-right to avoid conflict with progression)
        FloatingActionButton(
            onClick = onBackToMenu,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(16.dp),
            containerColor = Color(0xFF6B7280)
        ) {
            Text(
                text = "←",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }
    }
}

@Composable
private fun ProgressionOverlay(
    progressionInfo: com.rubontech.raceofwar.game.state.ProgressionInfo,
    selectedRace: UnitEntity.Race,
    availableUnits: List<com.rubontech.raceofwar.game.units.UnitType>,
    gameState: GameState,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.padding(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xCC1F2937) // Semi-transparent
        ),
        shape = RoundedCornerShape(8.dp)
    ) {
        Row(
            modifier = Modifier.padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Race icon
            Text(
                text = when (selectedRace) {
                    UnitEntity.Race.HUMAN_EMPIRE -> "👑"
                    UnitEntity.Race.DARK_CULT -> "☠️"
                    UnitEntity.Race.NATURE_TRIBE -> "🌿"
                    UnitEntity.Race.MECHANICAL_LEGION -> "⚙️"
                },
                fontSize = 16.sp
            )
            
            Spacer(modifier = Modifier.width(6.dp))
            
            // Level and time
            Column {
                Text(
                    text = "Lvl ${progressionInfo.currentLevel}",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Text(
                    text = progressionInfo.formattedGameTime,
                    fontSize = 12.sp,
                    color = Color(0xFFB8C5D6)
                )
            }
            
            Spacer(modifier = Modifier.width(8.dp))
            
            // Progress bar
            LinearProgressIndicator(
                progress = progressionInfo.progressPercentage,
                modifier = Modifier
                    .width(60.dp)
                    .height(4.dp),
                color = Color(0xFF10B981),
                trackColor = Color(0xFF374151)
            )
            
            Spacer(modifier = Modifier.width(6.dp))
            
            // Total spawned units
            val totalSpawned = gameState.getTotalSpawnedUnits()
            if (totalSpawned > 0) {
                Text(
                    text = "$totalSpawned",
                    fontSize = 12.sp,
                    color = Color(0xFFFFD700),
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

/**
 * Spawn a unit in the game
 */
private fun spawnUnit(unitType: com.rubontech.raceofwar.game.units.UnitType, gameState: GameState) {
    val success = gameState.spawnUnit(unitType)
    
    if (success) {
        val count = gameState.getSpawnedUnitCount(unitType)
        println("🎯 Spawning unit: ${unitType.displayName} (Total: $count)")
        
        // TODO: Integrate with actual game engine
        // This would typically:
        // 1. Create unit entity
        // 2. Add to game world
        // 3. Play spawn animation
        // 4. Update UI
        
        println("✅ Unit spawned successfully: ${unitType.displayName}")
    } else {
        println("❌ Unit not available yet: ${unitType.displayName}")
    }
}

@Composable
private fun UnitSpawnOverlay(
    availableUnits: List<com.rubontech.raceofwar.game.units.UnitType>,
    selectedRace: UnitEntity.Race,
    gameState: GameState,
    modifier: Modifier = Modifier
) {
    // Debug: Print overlay state
    LaunchedEffect(availableUnits) {
        println("🎯 UnitSpawnOverlay - Available units: ${availableUnits.map { it.displayName }}")
        println("🎯 UnitSpawnOverlay - Selected race: $selectedRace")
    }
    
    // TEST: Always show overlay even if no units
    Card(
        modifier = modifier
            .padding(16.dp)
            .fillMaxWidth() // Make it full width
            .height(200.dp), // Fixed height to ensure visibility
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF2D3748) // More opaque, easier to see
        ),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier.padding(12.dp)
        ) {
            // Header with race info - BIGGER
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(bottom = 12.dp)
            ) {
                Text(
                    text = when (selectedRace) {
                        UnitEntity.Race.HUMAN_EMPIRE -> "👑"
                        UnitEntity.Race.DARK_CULT -> "☠️"
                        UnitEntity.Race.NATURE_TRIBE -> "🌿"
                        UnitEntity.Race.MECHANICAL_LEGION -> "⚙️"
                    },
                    fontSize = 24.sp // BIGGER
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = "Available Units (${availableUnits.size})",
                    fontSize = 20.sp, // BIGGER
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
            
            // Unit grid
            if (availableUnits.isNotEmpty()) {
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(availableUnits) { unitType ->
                        UnitSpawnButton(
                            unitType = unitType,
                            gameState = gameState,
                            onClick = {
                                // Spawn unit logic
                                spawnUnit(unitType, gameState)
                            }
                        )
                    }
                }
            } else {
                // Show message when no units available - BIGGER
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 32.dp)
                ) {
                    Text(
                        text = "❌",
                        fontSize = 48.sp,
                        color = Color(0xFFEF4444)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "No units available yet...",
                        fontSize = 18.sp,
                        color = Color(0xFFEF4444),
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Wait for level progression",
                        fontSize = 14.sp,
                        color = Color(0xFF6B7280)
                    )
                }
            }
        }
    }
}

@Composable
private fun UnitSpawnButton(
    unitType: com.rubontech.raceofwar.game.units.UnitType,
    gameState: GameState,
    onClick: () -> Unit
) {
    val unlockLevel = gameState.getUnitUnlockLevel(unitType)
    val isAvailable = gameState.isUnitAvailable(unitType)
    val currentLevel = gameState.currentLevel
    val spawnCount = gameState.getSpawnedUnitCount(unitType)
    
    Button(
        onClick = onClick,
        enabled = isAvailable,
        colors = ButtonDefaults.buttonColors(
            containerColor = if (isAvailable) Color(0xFF374151) else Color(0xFF1F2937),
            disabledContainerColor = Color(0xFF1F2937)
        ),
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier.width(90.dp).height(70.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Unit icon (using first letter for now)
            Text(
                text = unitType.displayName.first().toString(),
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = if (isAvailable) Color.White else Color(0xFF6B7280)
            )
            
            // Unit name (shortened)
            Text(
                text = unitType.displayName.split(" ").first(),
                fontSize = 11.sp,
                color = if (isAvailable) Color(0xFFB8C5D6) else Color(0xFF6B7280),
                maxLines = 1
            )
            
            // Spawn count and level
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (spawnCount > 0) {
                    Text(
                        text = "$spawnCount",
                        fontSize = 10.sp,
                        color = Color(0xFFFFD700),
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.width(2.dp))
                }
                
                Text(
                    text = if (isAvailable) "Lv$unlockLevel ✓" else "Lv$unlockLevel 🔒",
                    fontSize = 9.sp,
                    color = if (isAvailable) Color(0xFF10B981) else Color(0xFFEF4444),
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}
