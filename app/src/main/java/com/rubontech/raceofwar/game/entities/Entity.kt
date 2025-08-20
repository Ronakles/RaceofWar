/*
 * MIT License
 * 
 * Copyright (c) 2024 Rubontech
 */

package com.rubontech.raceofwar.game.entities

import android.graphics.Canvas
import android.graphics.RectF
import com.rubontech.raceofwar.gfx.Sprite

/**
 * Base entity class for all game objects
 */
abstract class Entity(
    x: Float,
    y: Float,
    width: Float,
    height: Float,
    color: Int
) {
    
    protected val sprite = Sprite(x, y, width, height, color)
    
    var isAlive = true
        protected set
    
    open val x: Float get() = sprite.x
    open val y: Float get() = sprite.y
    open val width: Float get() = sprite.width
    open val height: Float get() = sprite.height
    
    abstract fun update(deltaTime: Float)
    
    open fun render(canvas: Canvas) {
        if (isAlive) {
            sprite.render(canvas)
        }
    }
    
    fun getBounds(): RectF = sprite.getBounds()
    
    open fun destroy() {
        isAlive = false
    }
    
    fun intersects(other: Entity): Boolean {
        return if (!isAlive || !other.isAlive) {
            false
        } else {
            RectF.intersects(getBounds(), other.getBounds())
        }
    }
    
    fun setPosition(x: Float, y: Float) {
        sprite.setPosition(x, y)
    }
}
