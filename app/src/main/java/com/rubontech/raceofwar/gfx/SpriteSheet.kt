/*
 * MIT License
 * 
 * Copyright (c) 2024 Rubontech
 */

package com.rubontech.raceofwar.gfx

import android.graphics.Rect

/**
 * SpriteSheet management for animation frames
 * Currently placeholder for future bitmap atlas support
 */
class SpriteSheet(
    private val frameWidth: Int,
    private val frameHeight: Int,
    private val framesPerRow: Int,
    private val totalFrames: Int
) {
    
    private val frames = mutableListOf<Rect>()
    
    init {
        generateFrames()
    }
    
    private fun generateFrames() {
        for (i in 0 until totalFrames) {
            val row = i / framesPerRow
            val col = i % framesPerRow
            
            frames.add(
                Rect(
                    col * frameWidth,
                    row * frameHeight,
                    (col + 1) * frameWidth,
                    (row + 1) * frameHeight
                )
            )
        }
    }
    
    fun getFrame(index: Int): Rect? {
        return if (index >= 0 && index < frames.size) {
            frames[index]
        } else null
    }
    
    fun getFrameCount(): Int = frames.size
}
