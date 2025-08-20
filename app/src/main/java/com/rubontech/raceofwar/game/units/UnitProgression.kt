/*
 * MIT License
 * 
 * Copyright (c) 2024 Rubontech
 */

package com.rubontech.raceofwar.game.units

import com.rubontech.raceofwar.game.entities.UnitEntity

/**
 * Unit progression system based on game time
 * Players start at level 1 and progress to level 10
 */
object UnitProgression {
    
    const val MAX_LEVEL = 10
    const val STARTING_LEVEL = 1
    
    /**
     * Time-based progression for each level (in minutes)
     */
    private val progressionTimes = mapOf(
        1 to 0.0,    // 0:00 - Starting units
        2 to 1.0,    // 1:00 
        3 to 2.0,    // 2:00
        4 to 3.0,    // 3:00
        5 to 4.0,    // 4:00
        6 to 5.0,    // 5:00
        7 to 6.5,    // 6:30
        8 to 9.0,    // 9:00
        9 to 10.0,   // 10:00
        10 to 10.0   // 10:00 (max level)
    )
    
    /**
     * Get current level based on game time
     */
    fun getCurrentLevel(gameTimeMinutes: Double): Int {
        var currentLevel = STARTING_LEVEL
        for ((level, timeRequired) in progressionTimes) {
            if (gameTimeMinutes >= timeRequired) {
                currentLevel = level
            } else {
                break
            }
        }
        return currentLevel.coerceAtMost(MAX_LEVEL)
    }
    
    /**
     * Get available units for a race at specific level
     */
    fun getAvailableUnits(race: UnitEntity.Race, level: Int): List<UnitType> {
        return when (race) {
            UnitEntity.Race.HUMAN_EMPIRE -> getHumanUnits(level)
            UnitEntity.Race.DARK_CULT -> getDarkCultistUnits(level)
            UnitEntity.Race.NATURE_TRIBE -> getElvenUnits(level)
            UnitEntity.Race.MECHANICAL_LEGION -> getMechanicalUnits(level)
        }
    }
    
    private fun getHumanUnits(level: Int): List<UnitType> {
        val units = mutableListOf<UnitType>()
        
        if (level >= 1) {
            units.add(UnitType.HUMAN_KILICU) // Kılıçu
            units.add(UnitType.HUMAN_OKCU) // Okçu - TEST: Level 1'de de açık
        }
        if (level >= 2) units.add(UnitType.HUMAN_ZIRHLI_PIYADE) // Zırhlı Piyade
        if (level >= 3) units.add(UnitType.HUMAN_ATLI) // Atlı
        if (level >= 4) units.add(UnitType.HUMAN_MIZRAKCI) // Mızrakçı
        if (level >= 5) units.add(UnitType.HUMAN_SIFACI) // Şifacı
        if (level >= 6) units.add(UnitType.HUMAN_MANCINIK) // Mancınık
        if (level >= 7) units.add(UnitType.HUMAN_CIFT_OK_ATAN_OKCU) // Çift Ok Atan Okçu
        if (level >= 8) units.add(UnitType.HUMAN_KOMUTAN) // Komutan
        if (level >= 9) units.add(UnitType.HUMAN_LIDER) // Lider
        
        return units
    }
    
    private fun getDarkCultistUnits(level: Int): List<UnitType> {
        val units = mutableListOf<UnitType>()
        
        if (level >= 1) {
            units.add(UnitType.DARK_GOLGE_DRUID) // Gölge Druid
            units.add(UnitType.DARK_CADI) // Cadı - TEST: Level 1'de de açık
        }
        if (level >= 2) units.add(UnitType.DARK_MIZRAKCI) // Mızrakçı
        if (level >= 3) units.add(UnitType.DARK_ATLI) // Atlı
        if (level >= 4) units.add(UnitType.DARK_ATES_CUCESI) // Ateş Cücesi
        if (level >= 5) units.add(UnitType.DARK_KARANLIK_SOVALYE) // Karanlık Şövalye
        if (level >= 6) units.add(UnitType.DARK_KUTSA_SAPAN) // Kutsa Sapan
        if (level >= 7) units.add(UnitType.DARK_SEYTAN_SAPAN) // Şeytan Sapan
        if (level >= 8) units.add(UnitType.DARK_CIFT_TIRMIK_SAPAN) // Çift Tırmık Sapan
        if (level >= 9) units.add(UnitType.DARK_SAPAN_KARAM) // Sapan Karam
        
        return units
    }
    
    private fun getElvenUnits(level: Int): List<UnitType> {
        val units = mutableListOf<UnitType>()
        
        if (level >= 1) {
            units.add(UnitType.ELF_HAFIF_ELF_ASKER) // Hafif Elf Asker
            units.add(UnitType.ELF_ELF_OKCUSU) // Elf Okçusu - TEST: Level 1'de de açık
        }
        if (level >= 2) units.add(UnitType.ELF_ELF_ATLI) // Elf Atlısı
        if (level >= 3) units.add(UnitType.ELF_ELF_MIZRAKCI) // Elf Mızrakçı
        if (level >= 4) units.add(UnitType.ELF_BUYUCU) // Büyücü
        if (level >= 5) units.add(UnitType.ELF_SIFACI_ELF) // Şifacı Elf
        if (level >= 6) units.add(UnitType.ELF_ELIT_MANCINIK) // Elit Mancınık
        if (level >= 7) units.add(UnitType.ELF_CIFT_OK_SAPNAC_ISI) // Çift Ok Sapnaç ısı
        if (level >= 8) units.add(UnitType.ELF_ELF_PRENSI) // Elf Prensi
        if (level >= 9) units.add(UnitType.ELF_ELF_PRENSESI) // Elf Prensesi
        
        return units
    }
    
    private fun getMechanicalUnits(level: Int): List<UnitType> {
        val units = mutableListOf<UnitType>()
        
        if (level >= 1) {
            units.add(UnitType.MECH_BASIT_DROID) // Basit Droid
            units.add(UnitType.MECH_LAZER_TARET) // Lazer Taret - TEST: Level 1'de de açık
        }
        if (level >= 2) units.add(UnitType.MECH_MIZRAKCI) // Mızrakçı
        if (level >= 3) units.add(UnitType.MECH_KALKANLI) // Kalkanlı
        if (level >= 4) units.add(UnitType.MECH_ZIRHLI_DROID) // Zırhlı Droid
        if (level >= 5) units.add(UnitType.MECH_HIZLI_DRONE) // Hızlı Drone
        if (level >= 6) units.add(UnitType.MECH_TANK_DROID) // Tank Droid
        if (level >= 7) units.add(UnitType.MECH_ROKETATAR_DROID) // Roketatar Droid
        if (level >= 8) units.add(UnitType.MECH_MECHA_SAVASCI) // Mecha Savaşçı
        if (level >= 9) units.add(UnitType.MECH_PLAZMA_TOPU) // Plazma Topu
        
        return units
    }
    
    /**
     * Get next unlock time for a race
     */
    fun getNextUnlockTime(race: UnitEntity.Race, currentLevel: Int): Double? {
        if (currentLevel >= MAX_LEVEL) return null
        
        val nextLevel = currentLevel + 1
        return progressionTimes[nextLevel]
    }
    
    /**
     * Get unit count at specific level for a race
     */
    fun getUnitCountAtLevel(race: UnitEntity.Race, level: Int): Int {
        return getAvailableUnits(race, level).size
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
