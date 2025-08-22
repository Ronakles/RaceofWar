/*
 * MIT License
 * 
 * Copyright (c) 2024 Rubontech
 */

package com.rubontech.raceofwar.game.units

import com.rubontech.raceofwar.game.entities.UnitEntity
import com.rubontech.raceofwar.ui.utils.UnitMapping

/**
 * Unit progression system based on XP
 * Players gain XP over time and unlock units at specific XP thresholds
 */
object UnitProgression {
    
    const val STARTING_XP = 0
    const val XP_PER_MINUTE = 200 // XP gained every minute (increased for testing)
    
    // XP thresholds for unit tiers
    const val LIGHT_TIER_XP = 0      // Available immediately
    const val MEDIUM_TIER_XP = 100   // Unlocked at 100 XP (2 minutes)
    const val HEAVY_TIER_XP = 300    // Unlocked at 300 XP (6 minutes)
    
    /**
     * Calculate current XP based on game time
     */
    fun getCurrentXP(gameTimeMinutes: Double): Int {
        return (gameTimeMinutes * XP_PER_MINUTE).toInt()
    }
    
    /**
     * Get available units for a race based on current XP
     */
    fun getAvailableUnits(race: UnitEntity.Race, currentXP: Int): List<UnitType> {
        return when (race) {
            UnitEntity.Race.HUMAN_EMPIRE -> getHumanUnits(currentXP)
            UnitEntity.Race.DARK_CULT -> getDarkCultistUnits(currentXP)
            UnitEntity.Race.NATURE_TRIBE -> getElvenUnits(currentXP)
            UnitEntity.Race.MECHANICAL_LEGION -> getMechanicalUnits(currentXP)
        }
    }
    
    /**
     * Check if a specific unit is unlocked with current XP
     */
    fun isUnitUnlocked(unitType: UnitType, currentXP: Int): Boolean {
        val requiredXP = UnitMapping.getRequiredXP(unitType)
        return currentXP >= requiredXP
    }
    
    private fun getHumanUnits(currentXP: Int): List<UnitType> {
        val units = mutableListOf<UnitType>()
        
        // Light units (0 XP)
        if (currentXP >= LIGHT_TIER_XP) {
            units.add(UnitType.HUMAN_KILICU) // Kılıçu
            units.add(UnitType.HUMAN_OKCU) // Okçu
        }
        
        // Medium units (100 XP)
        if (currentXP >= MEDIUM_TIER_XP) {
            units.add(UnitType.HUMAN_ZIRHLI_PIYADE) // Zırhlı Piyade
            units.add(UnitType.HUMAN_ATLI) // Atlı
            units.add(UnitType.HUMAN_MIZRAKCI) // Mızrakçı
            units.add(UnitType.HUMAN_SIFACI) // Şifacı
        }
        
        // Heavy units (300 XP)
        if (currentXP >= HEAVY_TIER_XP) {
            units.add(UnitType.HUMAN_MANCINIK) // Mancınık
            units.add(UnitType.HUMAN_CIFT_OK_ATAN_OKCU) // Çift Ok Atan Okçu
            units.add(UnitType.HUMAN_KOMUTAN) // Komutan
            units.add(UnitType.HUMAN_LIDER) // Lider
        }
        
        return units
    }
    
    private fun getDarkCultistUnits(currentXP: Int): List<UnitType> {
        val units = mutableListOf<UnitType>()
        
        // Light units (0 XP)
        if (currentXP >= LIGHT_TIER_XP) {
            units.add(UnitType.DARK_GOLGE_DRUID) // Gölge Druid
            units.add(UnitType.DARK_CADI) // Cadı
        }
        
        // Medium units (100 XP)
        if (currentXP >= MEDIUM_TIER_XP) {
            units.add(UnitType.DARK_MIZRAKCI) // Mızrakçı
            units.add(UnitType.DARK_ATLI) // Atlı
            units.add(UnitType.DARK_ATES_CUCESI) // Ateş Cücesi
            units.add(UnitType.DARK_KARANLIK_SOVALYE) // Karanlık Şövalye
        }
        
        // Heavy units (300 XP)
        if (currentXP >= HEAVY_TIER_XP) {
            units.add(UnitType.DARK_KUTSA_SAPAN) // Kutsa Sapan
            units.add(UnitType.DARK_SEYTAN_SAPAN) // Şeytan Sapan
            units.add(UnitType.DARK_CIFT_TIRMIK_SAPAN) // Çift Tırmık Sapan
            units.add(UnitType.DARK_SAPAN_KARAM) // Sapan Karam
        }
        
        return units
    }
    
    private fun getElvenUnits(currentXP: Int): List<UnitType> {
        val units = mutableListOf<UnitType>()
        
        // Light units (0 XP)
        if (currentXP >= LIGHT_TIER_XP) {
            units.add(UnitType.ELF_HAFIF_ELF_ASKER) // Hafif Elf Asker
            units.add(UnitType.ELF_ELF_OKCUSU) // Elf Okçusu
        }
        
        // Medium units (100 XP)
        if (currentXP >= MEDIUM_TIER_XP) {
            units.add(UnitType.ELF_ELF_ATLI) // Elf Atlısı
            units.add(UnitType.ELF_ELF_MIZRAKCI) // Elf Mızrakçı
            units.add(UnitType.ELF_BUYUCU) // Büyücü
            units.add(UnitType.ELF_SIFACI_ELF) // Şifacı Elf
        }
        
        // Heavy units (300 XP)
        if (currentXP >= HEAVY_TIER_XP) {
            units.add(UnitType.ELF_ELIT_MANCINIK) // Elit Mancınık
            units.add(UnitType.ELF_CIFT_OK_SAPNAC_ISI) // Çift Ok Sapnaç ısı
            units.add(UnitType.ELF_ELF_PRENSI) // Elf Prensi
            units.add(UnitType.ELF_ELF_PRENSESI) // Elf Prensesi
        }
        
        return units
    }
    
    private fun getMechanicalUnits(currentXP: Int): List<UnitType> {
        val units = mutableListOf<UnitType>()
        
        // Light units (0 XP)
        if (currentXP >= LIGHT_TIER_XP) {
            units.add(UnitType.MECH_BASIT_DROID) // Basit Droid
            units.add(UnitType.MECH_LAZER_TARET) // Lazer Taret
        }
        
        // Medium units (100 XP)
        if (currentXP >= MEDIUM_TIER_XP) {
            units.add(UnitType.MECH_MIZRAKCI) // Mızrakçı
            units.add(UnitType.MECH_KALKANLI) // Kalkanlı
            units.add(UnitType.MECH_ZIRHLI_DROID) // Zırhlı Droid
            units.add(UnitType.MECH_HIZLI_DRONE) // Hızlı Drone
        }
        
        // Heavy units (300 XP)
        if (currentXP >= HEAVY_TIER_XP) {
            units.add(UnitType.MECH_TANK_DROID) // Tank Droid
            units.add(UnitType.MECH_ROKETATAR_DROID) // Roketatar Droid
            units.add(UnitType.MECH_MECHA_SAVASCI) // Mecha Savaşçı
            units.add(UnitType.MECH_PLAZMA_TOPU) // Plazma Topu
        }
        
        return units
    }
    
    /**
     * Get next XP threshold for unlocking more units
     */
    fun getNextXPThreshold(currentXP: Int): Int? {
        return when {
            currentXP < MEDIUM_TIER_XP -> MEDIUM_TIER_XP
            currentXP < HEAVY_TIER_XP -> HEAVY_TIER_XP
            else -> null // All units unlocked
        }
    }
    
    /**
     * Get unit count at specific XP for a race
     */
    fun getUnitCountAtXP(race: UnitEntity.Race, currentXP: Int): Int {
        return getAvailableUnits(race, currentXP).size
    }
    
    /**
     * Get all units for a race (for max count)
     */
    fun getAllUnitsForRace(race: UnitEntity.Race): List<UnitType> {
        return getAvailableUnits(race, HEAVY_TIER_XP) // Return all units
    }
}

/**
 * All unit types in the game
 */
enum class UnitType(val displayName: String, val race: UnitEntity.Race) {
    // Human Empire Units
    HUMAN_KILICU("Kılıçu", UnitEntity.Race.HUMAN_EMPIRE),
    HUMAN_OKCU("Okçu", UnitEntity.Race.HUMAN_EMPIRE),
    HUMAN_ZIRHLI_PIYADE("Zırhlı Piyade", UnitEntity.Race.HUMAN_EMPIRE),
    HUMAN_ATLI("Atlı", UnitEntity.Race.HUMAN_EMPIRE),
    HUMAN_MIZRAKCI("Mızrakçı", UnitEntity.Race.HUMAN_EMPIRE),
    HUMAN_SIFACI("Şifacı", UnitEntity.Race.HUMAN_EMPIRE),
    HUMAN_MANCINIK("Mancınık", UnitEntity.Race.HUMAN_EMPIRE),
    HUMAN_CIFT_OK_ATAN_OKCU("Çift Ok Atan Okçu", UnitEntity.Race.HUMAN_EMPIRE),
    HUMAN_KOMUTAN("Komutan", UnitEntity.Race.HUMAN_EMPIRE),
    HUMAN_LIDER("Lider", UnitEntity.Race.HUMAN_EMPIRE),
    
    // Dark Cultist Units
    DARK_GOLGE_DRUID("Gölge Druid", UnitEntity.Race.DARK_CULT),
    DARK_CADI("Cadı", UnitEntity.Race.DARK_CULT),
    DARK_MIZRAKCI("Mızrakçı", UnitEntity.Race.DARK_CULT),
    DARK_ATLI("Atlı", UnitEntity.Race.DARK_CULT),
    DARK_ATES_CUCESI("Ateş Cücesi", UnitEntity.Race.DARK_CULT),
    DARK_KARANLIK_SOVALYE("Karanlık Şövalye", UnitEntity.Race.DARK_CULT),
    DARK_KUTSA_SAPAN("Kutsa Sapan", UnitEntity.Race.DARK_CULT),
    DARK_SEYTAN_SAPAN("Şeytan Sapan", UnitEntity.Race.DARK_CULT),
    DARK_CIFT_TIRMIK_SAPAN("Çift Tırmık Sapan", UnitEntity.Race.DARK_CULT),
    DARK_SAPAN_KARAM("Sapan Karam", UnitEntity.Race.DARK_CULT),
    
    // Elven Units
    ELF_HAFIF_ELF_ASKER("Hafif Elf Asker", UnitEntity.Race.NATURE_TRIBE),
    ELF_ELF_OKCUSU("Elf Okçusu", UnitEntity.Race.NATURE_TRIBE),
    ELF_ELF_ATLI("Elf Atlısı", UnitEntity.Race.NATURE_TRIBE),
    ELF_ELF_MIZRAKCI("Elf Mızrakçı", UnitEntity.Race.NATURE_TRIBE),
    ELF_BUYUCU("Büyücü", UnitEntity.Race.NATURE_TRIBE),
    ELF_SIFACI_ELF("Şifacı Elf", UnitEntity.Race.NATURE_TRIBE),
    ELF_ELIT_MANCINIK("Elit Mancınık", UnitEntity.Race.NATURE_TRIBE),
    ELF_CIFT_OK_SAPNAC_ISI("Çift Ok Sapnaç ısı", UnitEntity.Race.NATURE_TRIBE),
    ELF_ELF_PRENSI("Elf Prensi", UnitEntity.Race.NATURE_TRIBE),
    ELF_ELF_PRENSESI("Elf Prensesi", UnitEntity.Race.NATURE_TRIBE),
    
    // Mechanical Legion Units
    MECH_BASIT_DROID("Basit Droid", UnitEntity.Race.MECHANICAL_LEGION),
    MECH_LAZER_TARET("Lazer Taret", UnitEntity.Race.MECHANICAL_LEGION),
    MECH_MIZRAKCI("Mızrakçı", UnitEntity.Race.MECHANICAL_LEGION),
    MECH_KALKANLI("Kalkanlı", UnitEntity.Race.MECHANICAL_LEGION),
    MECH_ZIRHLI_DROID("Zırhlı Droid", UnitEntity.Race.MECHANICAL_LEGION),
    MECH_HIZLI_DRONE("Hızlı Drone", UnitEntity.Race.MECHANICAL_LEGION),
    MECH_TANK_DROID("Tank Droid", UnitEntity.Race.MECHANICAL_LEGION),
    MECH_ROKETATAR_DROID("Roketatar Droid", UnitEntity.Race.MECHANICAL_LEGION),
    MECH_MECHA_SAVASCI("Mecha Savaşçı", UnitEntity.Race.MECHANICAL_LEGION),
    MECH_PLAZMA_TOPU("Plazma Topu", UnitEntity.Race.MECHANICAL_LEGION)
}
