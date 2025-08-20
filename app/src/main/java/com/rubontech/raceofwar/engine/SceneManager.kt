/*
 * MIT License
 * 
 * Copyright (c) 2024 Rubontech
 */

package com.rubontech.raceofwar.engine

import android.graphics.Canvas
import com.rubontech.raceofwar.core.input.InputController
import com.rubontech.raceofwar.game.BattleScene

/**
 * Manages different game scenes (menu, battle, etc.)
 */
class SceneManager(private val context: android.content.Context) {
    
    private var currentScene: Scene? = null
    private var width = 0
    private var height = 0
    
    init {
        // Start with battle scene for demo
        setScene(BattleScene(context))
    }
    
    fun setScene(scene: Scene) {
        currentScene?.onExit()
        currentScene = scene
        scene.onEnter()
        if (width > 0 && height > 0) {
            scene.resize(width, height)
        }
    }
    
    fun update(touchEvents: List<InputController.TouchEvent>) {
        currentScene?.update(touchEvents)
    }
    
    fun render(canvas: Canvas) {
        currentScene?.render(canvas)
    }
    
    fun resize(width: Int, height: Int) {
        this.width = width
        this.height = height
        currentScene?.resize(width, height)
    }
    
    fun getCurrentScene(): Scene? = currentScene
}
