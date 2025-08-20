/*
 * MIT License
 * 
 * Copyright (c) 2024 Rubontech
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 */

package com.rubontech.raceofwar

import android.app.Application
import com.rubontech.raceofwar.game.entities.HeavyWeaponUnit
import com.rubontech.raceofwar.game.entities.KnightUnit
import com.rubontech.raceofwar.game.entities.SpearmanUnit
import com.rubontech.raceofwar.game.entities.ArcherUnit
import com.rubontech.raceofwar.game.entities.CavalryUnit

/**
 * Main Application class for Race of War game
 */
class App : Application() {
    
    override fun onCreate() {
        super.onCreate()
        instance = this
        
        // Load game assets
        loadGameAssets()
    }
    
    private fun loadGameAssets() {
        try {
            // Load Heavy Weapon Unit bitmap
            HeavyWeaponUnit.loadBitmap(this)
            
            // Load Knight Unit bitmap
            KnightUnit.loadBitmap(this)
            
            // Load Spearman Unit bitmap
            SpearmanUnit.loadBitmap(this)
            
            // Load Archer Unit bitmap
            ArcherUnit.loadBitmap(this)
            
            // Load Cavalry Unit bitmap
            CavalryUnit.loadBitmap(this)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
    
    companion object {
        lateinit var instance: App
            private set
    }
}
