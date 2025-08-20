/*
 * MIT License
 * 
 * Copyright (c) 2024 Rubontech
 */

package com.rubontech.raceofwar.core.time

/**
 * Fixed timestep clock for consistent game simulation
 * Uses accumulator pattern to ensure fixed update intervals
 */
class FixedTimestepClock(private val fixedDeltaMs: Long = 16L) { // 60 FPS target
    
    private var lastTime = 0L
    private var accumulator = 0.0
    private var maxFrameTime = 250.0 // Prevent spiral of death
    
    val fixedDelta: Float = fixedDeltaMs / 1000f
    
    fun update(currentTime: Long, onFixedUpdate: () -> Unit, onRender: () -> Unit) {
        if (lastTime == 0L) {
            lastTime = currentTime
            return
        }
        
        var frameTime = (currentTime - lastTime).toDouble()
        if (frameTime > maxFrameTime) frameTime = maxFrameTime
        lastTime = currentTime
        
        accumulator += frameTime
        
        while (accumulator >= fixedDeltaMs) {
            onFixedUpdate()
            accumulator -= fixedDeltaMs
        }
        
        onRender()
    }
    
    fun reset() {
        lastTime = 0L
        accumulator = 0.0
    }
}
