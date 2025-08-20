/*
 * MIT License
 * 
 * Copyright (c) 2024 Rubontech
 */

package com.rubontech.raceofwar.core.input

import android.view.MotionEvent
import java.util.concurrent.ConcurrentLinkedQueue

/**
 * Thread-safe input controller for handling touch events
 */
class InputController {
    
    private val touchEvents = ConcurrentLinkedQueue<TouchEvent>()
    
    fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN, MotionEvent.ACTION_UP -> {
                touchEvents.offer(
                    TouchEvent(
                        x = event.x,
                        y = event.y,
                        action = event.action
                    )
                )
            }
        }
        return true
    }
    
    fun pollTouchEvents(): List<TouchEvent> {
        val events = mutableListOf<TouchEvent>()
        while (touchEvents.isNotEmpty()) {
            touchEvents.poll()?.let { events.add(it) }
        }
        return events
    }
    
    data class TouchEvent(
        val x: Float,
        val y: Float,
        val action: Int
    )
}
