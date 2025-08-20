/*
 * MIT License
 * 
 * Copyright (c) 2024 Rubontech
 */

package com.rubontech.raceofwar.ui.hud

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.rubontech.raceofwar.engine.GameSurface
import com.rubontech.raceofwar.game.BattleScene
import com.rubontech.raceofwar.game.World
import com.rubontech.raceofwar.game.entities.UnitEntity
import com.rubontech.raceofwar.ui.components.FpsCounter
import com.rubontech.raceofwar.ui.components.GoldBar
import com.rubontech.raceofwar.ui.components.LevelBar
import com.rubontech.raceofwar.ui.components.RaceInfo
import com.rubontech.raceofwar.ui.components.RaceButtons
import com.rubontech.raceofwar.ui.components.SpawnButtons
import com.rubontech.raceofwar.ui.components.ProfessionalSpawnButtons
import com.rubontech.raceofwar.ui.theme.RaceOfWarTheme
import kotlinx.coroutines.delay
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.platform.LocalConfiguration
import com.rubontech.raceofwar.ui.utils.ScreenUtils

/**
 * Main HUD screen combining game surface with UI overlay
 */
@Composable
fun HudScreen() {
    val context = LocalContext.current
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp
    val screenHeight = configuration.screenHeightDp
    
    // Calculate responsive sizes
    val controlButtonSize = ScreenUtils.getResponsiveButtonSize(screenWidth, screenHeight) * 0.3f
    val fontSize = ScreenUtils.getResponsiveFontSize(screenWidth, screenHeight, 16f)
    val spacing = ScreenUtils.getResponsiveSpacing(screenWidth, screenHeight)
    
    var gameSurface by remember { mutableStateOf<GameSurface?>(null) }
    var fps by remember { mutableFloatStateOf(0f) }
    var gold by remember { mutableStateOf(100) }
    var currentLevel by remember { mutableStateOf(1) }
    var currentXP by remember { mutableStateOf(0) }
    var selectedRace by remember { mutableStateOf(UnitEntity.Race.MECHANICAL_LEGION) }
    var gameState by remember { mutableStateOf(World.GameState.PLAYING) }
    var isPaused by remember { mutableStateOf(false) }
    
    // Update FPS counter periodically
    LaunchedEffect(gameSurface) {
        while (true) {
            delay(100) // Update every 100ms
            gameSurface?.let { surface ->
                fps = surface.fps
                
                val battleScene = surface.getSceneManager()?.getCurrentScene() as? BattleScene
                battleScene?.let { scene ->
                    val world = scene.getWorld()
                    gold = world.gold
                    currentLevel = world.getCurrentLevel()
                    currentXP = world.getCurrentXP()
                    gameState = world.gameState
                    
                    // Debug XP values
                    if (currentXP > 0 || currentLevel > 1) {
                        println("🎮 HUD Update - Level: $currentLevel, XP: $currentXP")
                    }
                    isPaused = surface.isPaused()
                }
            }
        }
    }
    
    RaceOfWarTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            Box(modifier = Modifier.fillMaxSize()) {
                // Game surface
                AndroidView(
                    factory = { ctx ->
                        GameSurface(ctx).also { surface ->
                            gameSurface = surface
                        }
                    },
                    modifier = Modifier.fillMaxSize()
                )
                
                // Top-left resources (Gold, Mana)
                Column(
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(spacing.dp),
                    verticalArrangement = Arrangement.spacedBy(spacing.dp)
                ) {
                    // Gold and Mana display
                    GoldBar(gold = gold)
                    
                    // FPS Counter
                    FpsCounter(fps = fps)
                    
                    // Debug XP Test Button - Make it bigger and more visible
                    Button(
                        onClick = {
                            val battleScene = gameSurface?.getSceneManager()?.getCurrentScene() as? BattleScene
                            battleScene?.let { scene ->
                                val world = scene.getWorld()
                                world.addXP(10) // Test XP
                                println("🧪 Test XP Added!")
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Red),
                        modifier = Modifier.size(80.dp, 40.dp)
                    ) {
                        Text("TEST\nXP", fontSize = 12.sp, color = Color.White, fontWeight = FontWeight.Bold)
                    }
                }
                
                // Top-center XP display
                LevelBar(
                    currentLevel = currentLevel,
                    currentXP = currentXP,
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .padding(top = spacing.dp)
                )
                
                // Top-right controls (Pause, Settings)
                Row(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(spacing.dp),
                    horizontalArrangement = Arrangement.spacedBy(spacing.dp)
                ) {
                    // Pause button
                    Button(
                        onClick = {
                            gameSurface?.let { surface ->
                                if (surface.isPaused()) {
                                    surface.resumeGame()
                                } else {
                                    surface.pauseGame()
                                }
                            }
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Gray
                        ),
                        modifier = Modifier.size(controlButtonSize.dp),
                        shape = androidx.compose.foundation.shape.CircleShape
                    ) {
                        Text(
                            text = if (isPaused) "▶️" else "⏸️",
                            color = Color.White,
                            fontSize = fontSize.sp
                        )
                    }
                    
                    // Settings button
                    Button(
                        onClick = { /* Settings functionality */ },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Gray
                        ),
                        modifier = Modifier.size(controlButtonSize.dp),
                        shape = androidx.compose.foundation.shape.CircleShape
                    ) {
                        Text(
                            text = "⚙️",
                            color = Color.White,
                            fontSize = fontSize.sp
                        )
                    }
                }
                

                

                
                // Bottom-center race selection and unit spawn controls
                Column(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(bottom = spacing.dp),
                    verticalArrangement = Arrangement.spacedBy(spacing.dp)
                ) {
                    // Race selection buttons
                    RaceButtons(
                        selectedRace = selectedRace,
                        onRaceChanged = { race ->
                            selectedRace = race
                            // Change the race of all existing player units
                            val battleScene = gameSurface?.getSceneManager()?.getCurrentScene() as? BattleScene
                            battleScene?.getWorld()?.changePlayerUnitsRace(race)
                        },
                        modifier = Modifier.fillMaxWidth()
                    )
                    
                    // Professional unit spawn buttons
                    ProfessionalSpawnButtons(
                        gold = gold,
                        currentLevel = currentLevel,
                        selectedRace = selectedRace,
                        isUnitUnlocked = { unitType ->
                            val battleScene = gameSurface?.getSceneManager()?.getCurrentScene() as? BattleScene
                            battleScene?.getWorld()?.isUnitUnlocked(unitType) ?: false
                        },
                        getUnlockLevel = { unitType ->
                            val battleScene = gameSurface?.getSceneManager()?.getCurrentScene() as? BattleScene
                            battleScene?.getWorld()?.getUnlockLevel(unitType) ?: 1
                        },
                        onSpawnUnit = { unitType ->
                            val battleScene = gameSurface?.getSceneManager()?.getCurrentScene() as? BattleScene
                            battleScene?.getWorld()?.spawnPlayerUnit(unitType, selectedRace)
                        },
                        onRaceChanged = { race ->
                            selectedRace = race
                        },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                
                // Game over overlay
                if (gameState != World.GameState.PLAYING) {
                    GameOverOverlay(
                        gameState = gameState,
                        onRestart = {
                            val battleScene = gameSurface?.getSceneManager()?.getCurrentScene() as? BattleScene
                            battleScene?.restartGame()
                        },
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }
        }
    }
}

@Composable
private fun GameOverOverlay(
    gameState: World.GameState,
    onRestart: () -> Unit,
    modifier: Modifier = Modifier
) {
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp
    val screenHeight = configuration.screenHeightDp
    
    // Calculate responsive sizes
    val spacing = ScreenUtils.getResponsiveSpacing(screenWidth, screenHeight)
    val fontSize = ScreenUtils.getResponsiveFontSize(screenWidth, screenHeight, 14f)
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        Surface(
            color = Color.Black.copy(alpha = 0.8f),
            modifier = Modifier.fillMaxSize()
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.padding(spacing.dp)
            ) {
                Text(
                    text = when (gameState) {
                        World.GameState.VICTORY -> "🎉 VICTORY! 🎉"
                        World.GameState.DEFEAT -> "💀 DEFEAT 💀"
                        else -> ""
                    },
                    style = MaterialTheme.typography.headlineLarge,
                    color = when (gameState) {
                        World.GameState.VICTORY -> Color.Green
                        World.GameState.DEFEAT -> Color.Red
                        else -> Color.White
                    }
                )
                
                Button(
                    onClick = onRestart,
                    modifier = Modifier.padding(top = spacing.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Blue
                    )
                ) {
                    Text(
                        text = "🔄 Play Again",
                        color = Color.White,
                        style = MaterialTheme.typography.titleMedium
                    )
                }
            }
        }
    }
}
