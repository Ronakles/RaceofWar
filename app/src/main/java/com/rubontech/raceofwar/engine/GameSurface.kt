/*
 * MIT License
 * 
 * Copyright (c) 2024 Rubontech
 */

package com.rubontech.raceofwar.engine

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.SurfaceHolder
import android.view.SurfaceView
import com.rubontech.raceofwar.core.input.InputController
import com.rubontech.raceofwar.core.loop.GameLoop

/**
 * Main game surface handling rendering and input
 * Runs game loop on separate thread
 */
class GameSurface @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : SurfaceView(context, attrs, defStyleAttr), SurfaceHolder.Callback {
    
    private var gameLoop: GameLoop? = null
    private var renderer: GameRenderer? = null
    private var sceneManager: SceneManager? = null
    private val inputController = InputController()
    
    val fps: Float get() = gameLoop?.fps ?: 0f
    
    init {
        holder.addCallback(this)
        isFocusable = true
    }
    
    override fun surfaceCreated(holder: SurfaceHolder) {
        // Initialize game components
        sceneManager = SceneManager(context)
        renderer = GameRenderer(sceneManager!!)
        
        // Start game loop
        gameLoop = GameLoop(
            onUpdate = { update() },
            onRender = { render() }
        )
        gameLoop?.start()
    }
    
    override fun surfaceDestroyed(holder: SurfaceHolder) {
        var retry = true
        gameLoop?.stopLoop()
        while (retry) {
            try {
                gameLoop?.join()
                retry = false
            } catch (e: InterruptedException) {
                // Will try again
            }
        }
    }
    
    override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {
        renderer?.resize(width, height)
        sceneManager?.resize(width, height)
    }
    
    override fun onTouchEvent(event: MotionEvent): Boolean {
        return inputController.onTouchEvent(event)
    }
    
    private fun update() {
        val touchEvents = inputController.pollTouchEvents()
        sceneManager?.update(touchEvents)
    }
    
    private fun render() {
        var canvas: Canvas? = null
        try {
            canvas = holder.lockCanvas()
            canvas?.let { c ->
                renderer?.render(c)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            canvas?.let { c ->
                try {
                    holder.unlockCanvasAndPost(c)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }
    
    fun pauseGame() {
        gameLoop?.pauseLoop()
    }
    
    fun resumeGame() {
        gameLoop?.resumeLoop()
    }
    
    fun isPaused(): Boolean = gameLoop?.isPaused() ?: true
    
    fun getSceneManager(): SceneManager? = sceneManager
}
