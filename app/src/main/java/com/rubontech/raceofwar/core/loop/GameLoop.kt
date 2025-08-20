/*
 * MIT License
 * 
 * Copyright (c) 2024 Rubontech
 */

package com.rubontech.raceofwar.core.loop

import com.rubontech.raceofwar.core.time.FixedTimestepClock
import kotlin.math.min

/**
 * Main game loop running on a separate thread
 * Handles fixed timestep updates and variable framerate rendering
 */
class GameLoop(
    private val onUpdate: () -> Unit,
    private val onRender: () -> Unit
) : Thread() {
    
    @Volatile
    private var running = false
    
    @Volatile
    private var paused = false
    
    private val clock = FixedTimestepClock()
    private var fpsCounter = FpsCounter()
    
    val fps: Float get() = fpsCounter.fps
    
    override fun run() {
        running = true
        clock.reset()
        
        while (running) {
            val startTime = System.currentTimeMillis()
            
            if (!paused) {
                clock.update(startTime, onUpdate, onRender)
                fpsCounter.frame()
            } else {
                // When paused, just sleep to avoid busy waiting
                sleep(16)
            }
            
            // Maintain roughly 60 FPS cap for rendering
            val frameTime = System.currentTimeMillis() - startTime
            val sleepTime = 16 - frameTime
            if (sleepTime > 0) {
                sleep(sleepTime)
            }
        }
    }
    
    fun pauseLoop() {
        paused = true
    }
    
    fun resumeLoop() {
        paused = false
        clock.reset() // Reset to avoid large accumulation
    }
    
    fun stopLoop() {
        running = false
        try {
            join()
        } catch (e: InterruptedException) {
            currentThread().interrupt()
        }
    }
    
    fun isPaused(): Boolean = paused
}

/**
 * Simple FPS counter
 */
private class FpsCounter {
    private var frameCount = 0
    private var lastTime = System.currentTimeMillis()
    var fps = 0f
        private set
    
    fun frame() {
        frameCount++
        val currentTime = System.currentTimeMillis()
        
        if (currentTime - lastTime >= 1000) {
            fps = frameCount * 1000f / (currentTime - lastTime)
            frameCount = 0
            lastTime = currentTime
        }
    }
}
