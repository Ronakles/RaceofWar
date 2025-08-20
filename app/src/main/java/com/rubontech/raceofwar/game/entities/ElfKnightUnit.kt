package com.rubontech.raceofwar.game.entities

import android.content.Context
import android.graphics.Canvas
import com.rubontech.raceofwar.game.GameConfig
import com.rubontech.raceofwar.gfx.ElfKnightAnimator

/**
 * Elf Knight unit with animated sprites
 */
class ElfKnightUnit(
    x: Float,
    y: Float,
    team: UnitEntity.Team,
    race: UnitEntity.Race = UnitEntity.Race.NATURE_TRIBE,
    private val context: Context
) : UnitEntity(x, y, team, UnitEntity.UnitType.KNIGHT, race) {
    
    // Animation system
    private val animator = ElfKnightAnimator(context)
    
    // Animation state mapping
    private var lastState = UnitEntity.State.MOVING
    
    override fun update(deltaTime: Float) {
        super.update(deltaTime)
        
        // Update animation state
        updateAnimationState()
        
        // Update animator
        animator.update(deltaTime)
    }
    
    private fun updateAnimationState() {
        val newState = when {
            !isAlive -> ElfKnightAnimator.AnimationState.DIE
            state == UnitEntity.State.ATTACKING -> ElfKnightAnimator.AnimationState.ATTACK
            state == UnitEntity.State.MOVING -> ElfKnightAnimator.AnimationState.WALK
            else -> ElfKnightAnimator.AnimationState.IDLE
        }
        
        animator.setState(newState)
        lastState = state
    }
    
    override fun render(canvas: Canvas) {
        if (!isAlive) {
            // Render death animation
            animator.render(canvas, x, y, width, height, team == UnitEntity.Team.ENEMY)
            return
        }
        
        // Render animated sprite
        animator.render(canvas, x, y, width, height, team == UnitEntity.Team.ENEMY)
        
        // Render health bar
        renderHealthBar(canvas)
    }
    
    private fun renderHealthBar(canvas: Canvas) {
        val barWidth = width
        val barHeight = 6f
        val barY = y - 10f
        
        // Background (red)
        val bgPaint = android.graphics.Paint().apply {
            color = android.graphics.Color.RED
            style = android.graphics.Paint.Style.FILL
        }
        canvas.drawRect(x, barY, x + barWidth, barY + barHeight, bgPaint)
        
        // Health fill (green)
        val healthRatio = hp / maxHp
        val healthPaint = android.graphics.Paint().apply {
            color = android.graphics.Color.GREEN
            style = android.graphics.Paint.Style.FILL
        }
        canvas.drawRect(x, barY, x + barWidth * healthRatio, barY + barHeight, healthPaint)
    }
    
    override fun performAttack(target: Entity) {
        if (target is UnitEntity) {
            target.takeDamage(damage)
        } else if (target is BaseEntity) {
            target.takeDamage(damage)
        }
    }
    
    override fun destroy() {
        super.destroy()
        animator.cleanup()
    }
}


