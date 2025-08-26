/*
 * MIT License
 * 
 * Copyright (c) 2024 Rubontech
 */

package com.rubontech.raceofwar.gfx

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Rect
import android.graphics.RectF
import android.graphics.Paint
import android.util.Log
import com.rubontech.raceofwar.game.entities.UnitEntity

/**
 * Animator for Human Knight (Zırhlı Piyade) unit
 * Uses FreeKnight assets for smooth animations
 */
class HumanKnightAnimator(private val context: Context) {
    
    private val tag = "HumanKnightAnimator"
    
    // Animation states
    enum class AnimationState {
        IDLE, WALK, RUN, ATTACK, JUMP, JUMP_ATTACK, DEAD
    }
    
    // Current state
    private var currentState = AnimationState.IDLE
    private var currentFrame = 0
    private var frameTimer = 0f
    private val frameDuration = 1f / 60f // 60 FPS = 16.67ms per frame
    
    // Sprite sheets - individual PNG files for FreeKnight assets
    private var idleSprites = mutableListOf<Bitmap>()
    private var walkSprites = mutableListOf<Bitmap>()
    private var runSprites = mutableListOf<Bitmap>()
    private var attackSprites = mutableListOf<Bitmap>()
    private var jumpSprites = mutableListOf<Bitmap>()
    private var jumpAttackSprites = mutableListOf<Bitmap>()
    private var deadSprites = mutableListOf<Bitmap>()
    
    // Rendering
    private val paint = Paint().apply {
        isAntiAlias = true
        isFilterBitmap = true
    }
    
    init {
        loadSprites()
        Log.d(tag, "HumanKnightAnimator initialized with 60 FPS (${frameDuration * 1000}ms per frame)")
    }
    
    private fun loadSprites() {
        try {
            // Load Idle animation (10 frames)
            for (i in 1..10) {
                val assetName = "human_zirhli_piyade_idle_$i.png"
                val inputStream = context.assets.open(assetName)
                val bitmap = BitmapFactory.decodeStream(inputStream)
                inputStream.close()
                idleSprites.add(bitmap)
            }
            
            // Load Walk animation (10 frames)
            for (i in 1..10) {
                val assetName = "human_zirhli_piyade_walk_$i.png"
                val inputStream = context.assets.open(assetName)
                val bitmap = BitmapFactory.decodeStream(inputStream)
                inputStream.close()
                walkSprites.add(bitmap)
            }
            
            // Load Run animation (10 frames)
            for (i in 1..10) {
                val assetName = "human_zirhli_piyade_run_$i.png"
                val inputStream = context.assets.open(assetName)
                val bitmap = BitmapFactory.decodeStream(inputStream)
                inputStream.close()
                runSprites.add(bitmap)
            }
            
            // Load Attack animation (10 frames)
            for (i in 1..10) {
                val assetName = "human_zirhli_piyade_attack_$i.png"
                val inputStream = context.assets.open(assetName)
                val bitmap = BitmapFactory.decodeStream(inputStream)
                inputStream.close()
                attackSprites.add(bitmap)
            }
            
            // Load Jump animation (10 frames)
            for (i in 1..10) {
                val assetName = "human_zirhli_piyade_jump_$i.png"
                val inputStream = context.assets.open(assetName)
                val bitmap = BitmapFactory.decodeStream(inputStream)
                inputStream.close()
                jumpSprites.add(bitmap)
            }
            
            // Load JumpAttack animation (10 frames)
            for (i in 1..10) {
                val assetName = "human_zirhli_piyade_jumpattack_$i.png"
                val inputStream = context.assets.open(assetName)
                val bitmap = BitmapFactory.decodeStream(inputStream)
                inputStream.close()
                jumpAttackSprites.add(bitmap)
            }
            
            // Load Dead animation (10 frames)
            for (i in 1..10) {
                val assetName = "human_zirhli_piyade_dead_$i.png"
                val inputStream = context.assets.open(assetName)
                val bitmap = BitmapFactory.decodeStream(inputStream)
                inputStream.close()
                deadSprites.add(bitmap)
            }
            
            Log.d(tag, "All sprites loaded successfully: Idle=${idleSprites.size}, Walk=${walkSprites.size}, Run=${runSprites.size}, Attack=${attackSprites.size}, Jump=${jumpSprites.size}, JumpAttack=${jumpAttackSprites.size}, Dead=${deadSprites.size}")
            
        } catch (e: Exception) {
            Log.e(tag, "Error loading sprites: ${e.message}")
            e.printStackTrace()
        }
    }
    
    fun update(deltaTime: Float, unit: UnitEntity? = null) {
        frameTimer += deltaTime
        
        // Update animation state based on unit if provided
        unit?.let { updateAnimationState(it) }
        
        if (frameTimer >= frameDuration) {
            frameTimer = 0f
            currentFrame = (currentFrame + 1) % getFrameCount(currentState)
        }
    }
    
    private fun updateAnimationState(unit: UnitEntity) {
        val newState = when {
            !unit.isAlive -> AnimationState.DEAD
            unit.getUnitState() == UnitEntity.State.ATTACKING -> AnimationState.ATTACK
            unit.getUnitState() == UnitEntity.State.MOVING -> AnimationState.WALK
            else -> AnimationState.IDLE
        }
        
        if (newState != currentState) {
            currentState = newState
            currentFrame = 0
            frameTimer = 0f
            Log.d(tag, "Animation state changed to: $newState")
        }
    }
    
    fun render(canvas: Canvas, x: Float, y: Float, width: Float, height: Float, isFlipped: Boolean = false) {
        val currentSprite = getCurrentSprite()
        if (currentSprite == null) {
            Log.w(tag, "Current sprite is null for state: $currentState")
            // Fallback: draw colored rectangle
            renderFallback(canvas, x, y, width, height)
            return
        }
        
        Log.d(tag, "Rendering state: $currentState, frame: $currentFrame, sprite: ${currentSprite.width}x${currentSprite.height}, targetSize: ${width}x${height}")
        
        // Validate frame dimensions
        val maxFrames = getFrameCount(currentState)
        if (currentFrame >= maxFrames) {
            currentFrame = 0
        }
        
        val dstRect = RectF(x, y, x + width, y + height)
        
        // Handle flipping
        if (isFlipped) {
            canvas.save()
            canvas.scale(-1f, 1f, x + width / 2, y + height / 2)
            canvas.drawBitmap(currentSprite, null, dstRect, paint)
            canvas.restore()
        } else {
            canvas.drawBitmap(currentSprite, null, dstRect, paint)
        }
    }
    
    private fun getCurrentSprite(): Bitmap? {
        return when (currentState) {
            AnimationState.IDLE -> if (idleSprites.isNotEmpty() && currentFrame < idleSprites.size) idleSprites[currentFrame] else null
            AnimationState.WALK -> if (walkSprites.isNotEmpty() && currentFrame < walkSprites.size) walkSprites[currentFrame] else null
            AnimationState.RUN -> if (runSprites.isNotEmpty() && currentFrame < runSprites.size) runSprites[currentFrame] else null
            AnimationState.ATTACK -> if (attackSprites.isNotEmpty() && currentFrame < attackSprites.size) attackSprites[currentFrame] else null
            AnimationState.JUMP -> if (jumpSprites.isNotEmpty() && currentFrame < jumpSprites.size) jumpSprites[currentFrame] else null
            AnimationState.JUMP_ATTACK -> if (jumpAttackSprites.isNotEmpty() && currentFrame < jumpAttackSprites.size) jumpAttackSprites[currentFrame] else null
            AnimationState.DEAD -> if (deadSprites.isNotEmpty() && currentFrame < deadSprites.size) deadSprites[currentFrame] else null
        }
    }
    
    private fun getFrameCount(state: AnimationState): Int {
        return when (state) {
            AnimationState.IDLE -> 10
            AnimationState.WALK -> 10
            AnimationState.RUN -> 10
            AnimationState.ATTACK -> 10
            AnimationState.JUMP -> 10
            AnimationState.JUMP_ATTACK -> 10
            AnimationState.DEAD -> 10
        }
    }
    
    private fun renderFallback(canvas: Canvas, x: Float, y: Float, width: Float, height: Float) {
        val color = when (currentState) {
            AnimationState.IDLE -> android.graphics.Color.GREEN
            AnimationState.WALK -> android.graphics.Color.BLUE
            AnimationState.RUN -> android.graphics.Color.CYAN
            AnimationState.ATTACK -> android.graphics.Color.RED
            AnimationState.JUMP -> android.graphics.Color.MAGENTA
            AnimationState.JUMP_ATTACK -> android.graphics.Color.parseColor("#FF4500") // Orange Red
            AnimationState.DEAD -> android.graphics.Color.GRAY
        }
        
        // Draw main rectangle
        val fallbackPaint = Paint().apply {
            this.color = color
            style = Paint.Style.FILL
        }
        canvas.drawRect(x, y, x + width, y + height, fallbackPaint)
        
        // Draw border
        val borderPaint = Paint().apply {
            this.color = android.graphics.Color.BLACK
            style = Paint.Style.STROKE
            strokeWidth = 8f
        }
        canvas.drawRect(x, y, x + width, y + height, borderPaint)
        
        // Draw size indicator
        val sizeText = "${width.toInt()}x${height.toInt()}"
        val sizeTextPaint = Paint().apply {
            this.color = android.graphics.Color.WHITE
            textSize = 12f
            isAntiAlias = true
        }
        canvas.drawText(sizeText, x + 5f, y + 15f, sizeTextPaint)
        
        // Draw debug info
        Log.d(tag, "Rendering fallback for state: $currentState at ($x, $y) with size ($width, $height)")
        
        // Draw state text with background
        val text = currentState.name
        val textPaint = Paint().apply {
            this.color = android.graphics.Color.WHITE
            textSize = 14f
            isAntiAlias = true
            isFakeBoldText = true
        }
        
        val textBounds = Rect()
        textPaint.getTextBounds(text, 0, text.length, textBounds)
        
        val textX = x + (width - textBounds.width()) / 2
        val textY = y + height / 2 + textBounds.height() / 2
        
        // Draw text background
        val textBgPaint = Paint().apply {
            this.color = android.graphics.Color.parseColor("#80000000") // Semi-transparent black
            style = Paint.Style.FILL
        }
        val padding = 4f
        canvas.drawRect(
            textX - padding,
            textY - textBounds.height() - padding,
            textX + textBounds.width() + padding,
            textY + padding,
            textBgPaint
        )
        
        canvas.drawText(text, textX, textY, textPaint)
        
        // Draw frame indicator
        val frameText = "F:${currentFrame + 1}/${getFrameCount(currentState)}"
        val frameTextPaint = Paint().apply {
            this.color = android.graphics.Color.WHITE
            textSize = 10f
            isAntiAlias = true
        }
        val frameTextBounds = Rect()
        frameTextPaint.getTextBounds(frameText, 0, frameText.length, frameTextBounds)
        
        canvas.drawText(frameText, x + 5f, y + height - 5f, frameTextPaint)
    }
    
    fun setState(newState: AnimationState) {
        if (currentState != newState) {
            currentState = newState
            currentFrame = 0
            frameTimer = 0f
            Log.d(tag, "Animation state changed to: $newState")
        }
    }
    
    fun cleanup() {
        // Clean up all bitmaps
        (idleSprites + walkSprites + runSprites + attackSprites + 
         jumpSprites + jumpAttackSprites + deadSprites).forEach { it.recycle() }
        
        idleSprites.clear()
        walkSprites.clear()
        runSprites.clear()
        attackSprites.clear()
        jumpSprites.clear()
        jumpAttackSprites.clear()
        deadSprites.clear()
        
        Log.d(tag, "HumanKnightAnimator cleaned up")
    }
}
