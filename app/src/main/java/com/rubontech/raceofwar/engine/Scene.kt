/*
 * MIT License
 * 
 * Copyright (c) 2024 Rubontech
 */

package com.rubontech.raceofwar.engine

import android.graphics.Canvas
import com.rubontech.raceofwar.core.input.InputController

/**
 * Abstract base scene class
 */
abstract class Scene {
    
    abstract fun update(touchEvents: List<InputController.TouchEvent>)
    abstract fun render(canvas: Canvas)
    abstract fun resize(width: Int, height: Int)
    
    open fun onEnter() {}
    open fun onExit() {}
}
