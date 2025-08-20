package com.rubontech.raceofwar.gfx

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Rect
import android.graphics.RectF
import android.graphics.Paint
import android.util.Log


/**
 * Animator for Elf Knight unit using sprite sheets
 */
class ElfKnightAnimator(private val context: Context) {
    
    private val tag = "ElfKnightAnimator"
    
    // Animation states
    enum class AnimationState {
        IDLE, WALK, ATTACK, BLOCK, HIT, DIE
    }
    
    // Current state
    private var currentState = AnimationState.IDLE
    private var currentFrame = 0
    private var frameTimer = 0f
    private val frameDuration = 1f / 60f // 60 FPS = 16.67ms per frame
    
    // Sprite sheets
    private var idleSheet: Bitmap? = null
    private var walkSheet: Bitmap? = null
    private var attackSheet: Bitmap? = null
    private var blockSheet: Bitmap? = null
    private var hitSheet: Bitmap? = null
    private var dieSheet: Bitmap? = null
    
    // Frame dimensions - for 64x64 sprite sheets (from manifest)
    private val frameWidth = 64
    private val frameHeight = 64
    // framesPerRow will be calculated dynamically based on current state
    
    // Rendering
    private val paint = Paint().apply {
        isAntiAlias = true
        isFilterBitmap = true
    }
    
    init {
        loadSpriteSheets()
        Log.d(tag, "ElfKnightAnimator initialized with 60 FPS (${frameDuration * 1000}ms per frame)")
    }
    
    private fun loadSpriteSheets() {
        try {
            // Load idle animation
            val idleStream = context.assets.open("elf_idle_grid_64.png")
            idleSheet = BitmapFactory.decodeStream(idleStream)
            idleStream.close()
            Log.d(tag, "Idle sheet loaded: ${idleSheet?.width}x${idleSheet?.height}")
            
            // Load walk animation
            val walkStream = context.assets.open("elf_walk_grid_64.png")
            walkSheet = BitmapFactory.decodeStream(walkStream)
            walkStream.close()
            Log.d(tag, "Walk sheet loaded: ${walkSheet?.width}x${walkSheet?.height}")
            
            // Load attack animation
            val attackStream = context.assets.open("elf_attack_grid_64.png")
            attackSheet = BitmapFactory.decodeStream(attackStream)
            attackStream.close()
            Log.d(tag, "Attack sheet loaded: ${attackSheet?.width}x${attackSheet?.height}")
            
            // Load block animation
            val blockStream = context.assets.open("elf_block_grid_64.png")
            blockSheet = BitmapFactory.decodeStream(blockStream)
            blockStream.close()
            Log.d(tag, "Block sheet loaded: ${blockSheet?.width}x${blockSheet?.height}")
            
            // Load hit animation
            val hitStream = context.assets.open("elf_hit_grid_64.png")
            hitSheet = BitmapFactory.decodeStream(hitStream)
            hitStream.close()
            Log.d(tag, "Hit sheet loaded: ${hitSheet?.width}x${hitSheet?.height}")
            
            // Load die animation
            val dieStream = context.assets.open("elf_die_grid_64.png")
            dieSheet = BitmapFactory.decodeStream(dieStream)
            dieStream.close()
            Log.d(tag, "Die sheet loaded: ${dieSheet?.width}x${dieSheet?.height}")
            
            Log.d(tag, "All sprite sheets loaded successfully")
            
        } catch (e: Exception) {
            Log.e(tag, "Error loading sprite sheets: ${e.message}")
            e.printStackTrace()
        }
    }
    
    fun update(deltaTime: Float) {
        frameTimer += deltaTime
        
        if (frameTimer >= frameDuration) {
            frameTimer = 0f
            currentFrame = (currentFrame + 1) % getFrameCount(currentState)
        }
    }
    
    fun render(canvas: Canvas, x: Float, y: Float, width: Float, height: Float, isFlipped: Boolean = false) {
        val currentSheet = getCurrentSheet()
        if (currentSheet == null) {
            Log.w(tag, "Current sheet is null for state: $currentState")
            // Fallback: draw colored rectangle
            renderFallback(canvas, x, y, width, height)
            return
        }
        
        Log.d(tag, "Rendering state: $currentState, frame: $currentFrame, sheet: ${currentSheet.width}x${currentSheet.height}, targetSize: ${width}x${height}")
        
        // Validate frame dimensions
        val maxFrames = getFrameCount(currentState)
        if (currentFrame >= maxFrames) {
            currentFrame = 0
        }
        
        // Calculate source rectangle for 64x64 frames using dynamic grid
        val gridColumns = getGridColumns(currentState)
        val gridRows = getGridRows(currentState)
        
        val frameRow = currentFrame / gridColumns
        val frameCol = currentFrame % gridColumns
        
        // Ensure we don't go out of bounds
        val maxCols = currentSheet.width / frameWidth
        val maxRows = currentSheet.height / frameHeight
        
        Log.d(tag, "64x64 Dynamic Grid calc: state=$currentState, frame=$currentFrame, row=$frameRow, col=$frameCol, grid=${gridColumns}x${gridRows}, sheet=${maxCols}x${maxRows}, frameSize=${frameWidth}x${frameHeight}")
        
        if (frameCol >= maxCols || frameRow >= maxRows) {
            Log.w(tag, "Frame out of bounds: col=$frameCol, row=$frameRow, maxCols=$maxCols, maxRows=$maxRows, grid=${gridColumns}x${gridRows}")
            renderFallback(canvas, x, y, width, height)
            return
        }
        
        val srcRect = Rect(
            frameCol * frameWidth,
            frameRow * frameHeight,
            (frameCol + 1) * frameWidth,
            (frameRow + 1) * frameHeight
        )
        
        val dstRect = RectF(x, y, x + width, y + height)
        
        // Handle flipping
        if (isFlipped) {
            canvas.save()
            canvas.scale(-1f, 1f, x + width / 2, y + height / 2)
            canvas.drawBitmap(currentSheet, srcRect, dstRect, paint)
            canvas.restore()
        } else {
            canvas.drawBitmap(currentSheet, srcRect, dstRect, paint)
        }
    }
    
    private fun getCurrentSheet(): Bitmap? {
        return when (currentState) {
            AnimationState.IDLE -> idleSheet
            AnimationState.WALK -> walkSheet
            AnimationState.ATTACK -> attackSheet
            AnimationState.BLOCK -> blockSheet
            AnimationState.HIT -> hitSheet
            AnimationState.DIE -> dieSheet
        }
    }
    
    private fun getFrameCount(state: AnimationState): Int {
        return when (state) {
            AnimationState.IDLE -> 16   // 16 frames for idle (from new HF manifest)
            AnimationState.WALK -> 12   // 12 frames for walk (from new HF manifest)
            AnimationState.ATTACK -> 8  // 8 frames for attack (from new HF manifest)
            AnimationState.BLOCK -> 6   // 6 frames for block (from new HF manifest)
            AnimationState.HIT -> 4     // 4 frames for hit (from new HF manifest)
            AnimationState.DIE -> 8     // 8 frames for die (from new HF manifest)
        }
    }
    
    private fun getGridColumns(state: AnimationState): Int {
        return when (state) {
            AnimationState.IDLE -> 4    // 4 columns for idle (4x4 grid)
            AnimationState.WALK -> 4    // 4 columns for walk (4x3 grid)
            AnimationState.ATTACK -> 4  // 4 columns for attack (4x2 grid)
            AnimationState.BLOCK -> 4   // 4 columns for block (4x2 grid)
            AnimationState.HIT -> 4     // 4 columns for hit (4x1 grid)
            AnimationState.DIE -> 4     // 4 columns for die (4x2 grid)
        }
    }
    
    private fun getGridRows(state: AnimationState): Int {
        return when (state) {
            AnimationState.IDLE -> 4    // 4 rows for idle (4x4 grid)
            AnimationState.WALK -> 3    // 3 rows for walk (4x3 grid)
            AnimationState.ATTACK -> 2  // 2 rows for attack (4x2 grid)
            AnimationState.BLOCK -> 2   // 2 rows for block (4x2 grid)
            AnimationState.HIT -> 1     // 1 row for hit (4x1 grid)
            AnimationState.DIE -> 2     // 2 rows for die (4x2 grid)
        }
    }
    
    // 60 FPS sabit model - tüm animasyonlar aynı hızda
    
    private fun renderFallback(canvas: Canvas, x: Float, y: Float, width: Float, height: Float) {
        val color = when (currentState) {
            AnimationState.IDLE -> android.graphics.Color.GREEN
            AnimationState.WALK -> android.graphics.Color.BLUE
            AnimationState.ATTACK -> android.graphics.Color.RED
            AnimationState.BLOCK -> android.graphics.Color.YELLOW
            AnimationState.HIT -> android.graphics.Color.rgb(255, 165, 0) // ORANGE equivalent
            AnimationState.DIE -> android.graphics.Color.GRAY
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
        idleSheet?.recycle()
        walkSheet?.recycle()
        attackSheet?.recycle()
        blockSheet?.recycle()
        hitSheet?.recycle()
        dieSheet?.recycle()
        
        idleSheet = null
        walkSheet = null
        attackSheet = null
        blockSheet = null
        hitSheet = null
        dieSheet = null
    }
}
