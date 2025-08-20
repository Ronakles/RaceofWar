/*
 * MIT License
 * 
 * Copyright (c) 2024 Rubontech
 */

package com.rubontech.raceofwar.gfx

/**
 * Simple frame-based animator
 */
class Animator(
    private val frameCount: Int,
    private val frameDuration: Float = 0.1f, // seconds per frame
    private val loop: Boolean = true
) {
    
    private var currentFrame = 0
    private var timeAccumulator = 0f
    private var isPlaying = true
    
    fun update(deltaTime: Float) {
        if (!isPlaying || frameCount <= 1) return
        
        timeAccumulator += deltaTime
        
        while (timeAccumulator >= frameDuration) {
            timeAccumulator -= frameDuration
            currentFrame++
            
            if (currentFrame >= frameCount) {
                if (loop) {
                    currentFrame = 0
                } else {
                    currentFrame = frameCount - 1
                    isPlaying = false
                }
            }
        }
    }
    
    fun getCurrentFrame(): Int = currentFrame
    
    fun play() {
        isPlaying = true
    }
    
    fun pause() {
        isPlaying = false
    }
    
    fun reset() {
        currentFrame = 0
        timeAccumulator = 0f
        isPlaying = true
    }
    
    fun isFinished(): Boolean = !loop && currentFrame >= frameCount - 1
}
