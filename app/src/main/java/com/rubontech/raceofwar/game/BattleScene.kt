/*
 * MIT License
 * 
 * Copyright (c) 2024 Rubontech
 */

package com.rubontech.raceofwar.game

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.Log
import com.rubontech.raceofwar.BuildConfig
import com.rubontech.raceofwar.core.input.InputController
import com.rubontech.raceofwar.engine.Scene
import com.rubontech.raceofwar.gfx.ParallaxBackground
import com.rubontech.raceofwar.gfx.RaceBackground

/**
 * Main battle scene where the gameplay happens
 */
class BattleScene(
    private val context: android.content.Context,
    private val gameSettings: com.rubontech.raceofwar.ui.screens.GameSettings? = null
) : Scene() {
    
    private val world = World(context, gameSettings)
    private val background = ParallaxBackground()
    private val raceBackground = RaceBackground(context)
    private var debugMode = false // Debug mode disabled to remove placeholder lines
    
    // Debug rendering
    private val debugPaint = Paint().apply {
        color = Color.RED
        style = Paint.Style.STROKE
        strokeWidth = 2f
    }
    
    private val linePaint = Paint().apply {
        color = Color.parseColor("#8B0000") // Dark red - blood line
        strokeWidth = 6f
    }
    
    override fun update(touchEvents: List<InputController.TouchEvent>) {
        // Handle touch events for debug mode toggle (if needed)
        world.update(1/60f) // Fixed timestep
    }
    
    override fun render(canvas: Canvas) {
        val width = canvas.width
        val height = canvas.height
        
        // Render background
        background.render(canvas, width, height)
        
        // Render race-themed background (overlay)
        raceBackground.render(canvas)
        
        // Draw battle lane line (blood red battlefield line)
        linePaint.color = Color.parseColor("#8B0000")
        canvas.drawLine(0f, GameConfig.laneY, width.toFloat(), GameConfig.laneY, linePaint)
        
        // Render all entities
        world.getPlayerBase().render(canvas)
        world.getEnemyBase().render(canvas)
        
        world.getPlayerUnits().forEach { it.render(canvas) }
        world.getEnemyUnits().forEach { it.render(canvas) }
        world.getBullets().forEach { it.render(canvas) }
        
        // Debug rendering
        if (debugMode) {
            renderDebugInfo(canvas)
        }
        
        // Game over overlay
        if (world.gameState != World.GameState.PLAYING) {
            renderGameOverOverlay(canvas)
        }
    }
    
    private fun renderDebugInfo(canvas: Canvas) {
        // Draw hitboxes
        world.getPlayerUnits().forEach { unit ->
            if (unit.isAlive) {
                val bounds = unit.getBounds()
                canvas.drawRect(bounds, debugPaint)
            }
        }
        
        world.getEnemyUnits().forEach { unit ->
            if (unit.isAlive) {
                val bounds = unit.getBounds()
                canvas.drawRect(bounds, debugPaint)
            }
        }
    }
    
    private fun renderGameOverOverlay(canvas: Canvas) {
        val overlayPaint = Paint().apply {
            color = Color.parseColor("#80000000") // Semi-transparent black
            style = Paint.Style.FILL
        }
        
        val textPaint = Paint().apply {
            color = Color.WHITE
            textSize = 64f
            textAlign = Paint.Align.CENTER
            isAntiAlias = true
        }
        
        // Draw overlay
        canvas.drawRect(0f, 0f, canvas.width.toFloat(), canvas.height.toFloat(), overlayPaint)
        
        // Draw game over text
        val message = when (world.gameState) {
            World.GameState.VICTORY -> "VICTORY!"
            World.GameState.DEFEAT -> "DEFEAT!"
            else -> ""
        }
        
        canvas.drawText(
            message,
            canvas.width / 2f,
            canvas.height / 2f,
            textPaint
        )
        
        // Draw restart instruction
        textPaint.textSize = 32f
        canvas.drawText(
            "Tap to restart",
            canvas.width / 2f,
            canvas.height / 2f + 100f,
            textPaint
        )
    }
    
    override fun resize(width: Int, height: Int) {
        world.initialize(width, height)
        background.update(0f) // Initialize background
        raceBackground.resize(width, height) // Initialize race background
    }
    
    override fun onEnter() {
        super.onEnter()
    }
    
    override fun onExit() {
        super.onExit()
    }
    
    fun getWorld(): World = world
    
    fun toggleDebugMode() {
        debugMode = !debugMode
    }
    
    fun restartGame() {
        world.reset()
    }
    
    fun setRace(race: com.rubontech.raceofwar.game.entities.UnitEntity.Race) {
        raceBackground.setRace(race)
        Log.d("BattleScene", "Race changed to: $race")
    }
}
