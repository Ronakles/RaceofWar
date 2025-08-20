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
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.rubontech.raceofwar.engine.GameSurface
import com.rubontech.raceofwar.game.entities.UnitEntity
import com.rubontech.raceofwar.game.BattleScene
import com.rubontech.raceofwar.game.state.GameState
import com.rubontech.raceofwar.game.units.UnitType as ProgressionUnitType
import com.rubontech.raceofwar.ui.screens.*
import com.rubontech.raceofwar.ui.components.FloatingUnitButtons
import com.rubontech.raceofwar.ui.components.DifficultyIndicator
import com.rubontech.raceofwar.ui.components.GoldBar
import com.rubontech.raceofwar.ui.components.LevelBar
import com.rubontech.raceofwar.ui.utils.ScreenUtils
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
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp
    val screenHeight = configuration.screenHeightDp
    
    val gameState = remember { GameState(selectedRace, gameSettings) }
    var progressionInfo by remember { mutableStateOf(gameState.getProgressionInfo()) }
    var gameSurface by remember { mutableStateOf<GameSurface?>(null) }
    
    // Real-time game data from engine
    var engineGold by remember { mutableStateOf(1000) } // Starting gold
    var engineLevel by remember { mutableStateOf(1) }
    var engineXP by remember { mutableStateOf(0) }
    
    // Debug: Print selected race and initial units
    LaunchedEffect(Unit) {
        println("🎮 Game started with race: $selectedRace")
        println("🎮 Initial available units: ${gameState.availableUnits.map { it.displayName }}")
        println("🎮 GameState current level: ${gameState.currentLevel}")
        println("🎮 GameState progression info: ${gameState.getProgressionInfo()}")
    }
    
    // Force update GameState to get initial units
    LaunchedEffect(Unit) {
        println("🎮 Before update - GameState level: ${gameState.currentLevel}")
        println("🎮 Before update - GameState race: $selectedRace")
        
        gameState.update()
        
        println("🎮 After update - GameState level: ${gameState.currentLevel}")
        println("🎮 After update - Available units: ${gameState.availableUnits.map { it.displayName }}")
        println("🎮 After update - Units count: ${gameState.availableUnits.size}")
        
        // Debug UnitProgression directly
        val directUnits = com.rubontech.raceofwar.game.units.UnitProgression.getAvailableUnits(selectedRace, gameState.currentLevel)
        println("🎮 Direct UnitProgression - Race: $selectedRace, Level: ${gameState.currentLevel}")
        println("🎮 Direct UnitProgression - Units: ${directUnits.map { it.displayName }}")
        println("🎮 Direct UnitProgression - Count: ${directUnits.size}")
    }
    
    // Update both UI state and real game engine data
    LaunchedEffect(gameSurface) {
        gameSurface?.let { surface ->
            val sceneManager = surface.getSceneManager()
            val currentScene = sceneManager?.getCurrentScene()
            
            if (currentScene is BattleScene) {
                val world = currentScene.getWorld()
                
                // Collect real-time data from game engine
                engineGold = world.gold
                engineLevel = world.getCurrentLevel()
                engineXP = world.getCurrentXP()
                
                println("🎯 Engine data updated - Gold: $engineGold, Level: $engineLevel, XP: $engineXP")
            }
        }
    }
    
    // Continuous update of engine data
    LaunchedEffect(Unit) {
        while (true) {
            kotlinx.coroutines.delay(500) // Update every 500ms
            
            gameSurface?.let { surface ->
                val sceneManager = surface.getSceneManager()
                val currentScene = sceneManager?.getCurrentScene()
                
                if (currentScene is BattleScene) {
                    val world = currentScene.getWorld()
                    
                    // Update real-time data
                    engineGold = world.gold
                    engineLevel = world.getCurrentLevel()
                    engineXP = world.getCurrentXP()
                }
            }
        }
    }
    
    // Calculate responsive sizes
    val spacing = ScreenUtils.getResponsiveSpacing(screenWidth, screenHeight)
    
    Box(modifier = Modifier.fillMaxSize()) {
        // Game surface
        AndroidView(
            factory = { ctx ->
                GameSurface(ctx).apply {
                    // Configure game settings here if needed
                    // The game engine will handle the rest
                }.also { surface ->
                    gameSurface = surface // Store reference for spawn callbacks
                    
                    // Set race for background
                    surface.getSceneManager()?.getCurrentScene()?.let { scene ->
                        if (scene is BattleScene) {
                            scene.setRace(selectedRace)
                        }
                    }
                }
            },
            modifier = Modifier.fillMaxSize(),
            update = { surface ->
                // Update game surface if needed
                gameSurface = surface
            }
        )
        
        // Top-left: Gold display
        GoldBar(
            gold = engineGold,
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(8.dp)
        )
        
        // Top-center: Level and XP display
        LevelBar(
            currentLevel = engineLevel,
            currentXP = engineXP,
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 8.dp)
        )
        
        // Difficulty indicator (below XP bar)
        DifficultyIndicator(
            currentLevel = engineLevel,
            progress = engineXP.toFloat() / 100f,
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 60.dp) // Below the level bar
        )
        
        // Unit spawn buttons (at bottom with higher z-index)
        Surface(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 16.dp)
                .fillMaxWidth(),
            color = Color.Transparent,
            shadowElevation = 8.dp // Higher z-index
        ) {
            FloatingUnitButtons(
                availableUnits = gameState.availableUnits,
                selectedRace = selectedRace,
                gameState = gameState,
                onSpawnUnit = { unitType ->
                    // Convert UI unit type to engine unit type
                    val engineUnitType = convertToEngineUnitType(unitType)
                    if (engineUnitType != null) {
                        // Try to spawn unit in game engine
                        val success = gameSurface?.getSceneManager()?.getCurrentScene()?.let { scene ->
                            if (scene is BattleScene) {
                                scene.getWorld().spawnPlayerUnit(engineUnitType, selectedRace)
                            } else false
                        } ?: false
                        
                        if (success) {
                            // Update UI state
                            gameState.spawnUnit(unitType)
                            println("✅ Successfully spawned unit: $unitType")
                        } else {
                            println("❌ Failed to spawn unit: $unitType")
                        }
                    }
                },
                realGold = engineGold
            )
        }
        
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
 * Convert GameState UnitType to GameEngine UnitType
 */
private fun convertToEngineUnitType(gameStateUnitType: ProgressionUnitType): UnitEntity.UnitType? {
    return when (gameStateUnitType) {
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
        
        else -> UnitEntity.UnitType.SPEARMAN // Default fallback
    }
}
