/*
 * MIT License
 * 
 * Copyright (c) 2024 Rubontech
 */

package com.rubontech.raceofwar.ui.utils

import com.rubontech.raceofwar.game.entities.UnitEntity
import com.rubontech.raceofwar.game.units.UnitType as ProgressionUnitType
import com.rubontech.raceofwar.game.GameConfig

/**
 * Centralized mapping between UI progression units and engine units
 */
object UnitMapping {
    
    /**
     * Convert ProgressionUnitType to Engine UnitType
     */
    fun convertToEngineUnitType(progressionUnitType: ProgressionUnitType): UnitEntity.UnitType {
        return when (progressionUnitType) {
            // Human Empire Units
            ProgressionUnitType.HUMAN_KILICU -> UnitEntity.UnitType.SPEARMAN
            ProgressionUnitType.HUMAN_OKCU -> UnitEntity.UnitType.ARCHER
            ProgressionUnitType.HUMAN_ZIRHLI_PIYADE -> UnitEntity.UnitType.KNIGHT
            ProgressionUnitType.HUMAN_ATLI -> UnitEntity.UnitType.CAVALRY
            ProgressionUnitType.HUMAN_MIZRAKCI -> UnitEntity.UnitType.SPEARMAN
            ProgressionUnitType.HUMAN_SIFACI -> UnitEntity.UnitType.HEAVY_WEAPON
            ProgressionUnitType.HUMAN_MANCINIK -> UnitEntity.UnitType.HEAVY_WEAPON
            ProgressionUnitType.HUMAN_CIFT_OK_ATAN_OKCU -> UnitEntity.UnitType.ARCHER
            ProgressionUnitType.HUMAN_KOMUTAN -> UnitEntity.UnitType.KNIGHT
            ProgressionUnitType.HUMAN_LIDER -> UnitEntity.UnitType.KNIGHT
            
            // Dark Cultist Units
            ProgressionUnitType.DARK_GOLGE_DRUID -> UnitEntity.UnitType.SPEARMAN
            ProgressionUnitType.DARK_CADI -> UnitEntity.UnitType.ARCHER
            ProgressionUnitType.DARK_MIZRAKCI -> UnitEntity.UnitType.SPEARMAN
            ProgressionUnitType.DARK_ATLI -> UnitEntity.UnitType.CAVALRY
            ProgressionUnitType.DARK_ATES_CUCESI -> UnitEntity.UnitType.HEAVY_WEAPON
            ProgressionUnitType.DARK_KARANLIK_SOVALYE -> UnitEntity.UnitType.KNIGHT
            ProgressionUnitType.DARK_KUTSA_SAPAN -> UnitEntity.UnitType.HEAVY_WEAPON
            ProgressionUnitType.DARK_SEYTAN_SAPAN -> UnitEntity.UnitType.HEAVY_WEAPON
            ProgressionUnitType.DARK_CIFT_TIRMIK_SAPAN -> UnitEntity.UnitType.HEAVY_WEAPON
            ProgressionUnitType.DARK_SAPAN_KARAM -> UnitEntity.UnitType.HEAVY_WEAPON
            
            // Elven Units
            ProgressionUnitType.ELF_HAFIF_ELF_ASKER -> UnitEntity.UnitType.SPEARMAN
            ProgressionUnitType.ELF_ELF_OKCUSU -> UnitEntity.UnitType.ARCHER
            ProgressionUnitType.ELF_ELF_ATLI -> UnitEntity.UnitType.CAVALRY
            ProgressionUnitType.ELF_ELF_MIZRAKCI -> UnitEntity.UnitType.SPEARMAN
            ProgressionUnitType.ELF_BUYUCU -> UnitEntity.UnitType.HEAVY_WEAPON
            ProgressionUnitType.ELF_SIFACI_ELF -> UnitEntity.UnitType.HEAVY_WEAPON
            ProgressionUnitType.ELF_ELIT_MANCINIK -> UnitEntity.UnitType.HEAVY_WEAPON
            ProgressionUnitType.ELF_CIFT_OK_SAPNAC_ISI -> UnitEntity.UnitType.ARCHER
            ProgressionUnitType.ELF_ELF_PRENSI -> UnitEntity.UnitType.KNIGHT
            ProgressionUnitType.ELF_ELF_PRENSESI -> UnitEntity.UnitType.KNIGHT
            
            // Mechanical Legion Units
            ProgressionUnitType.MECH_BASIT_DROID -> UnitEntity.UnitType.SPEARMAN
            ProgressionUnitType.MECH_LAZER_TARET -> UnitEntity.UnitType.ARCHER
            ProgressionUnitType.MECH_MIZRAKCI -> UnitEntity.UnitType.SPEARMAN
            ProgressionUnitType.MECH_KALKANLI -> UnitEntity.UnitType.KNIGHT
            ProgressionUnitType.MECH_ZIRHLI_DROID -> UnitEntity.UnitType.HEAVY_WEAPON
            ProgressionUnitType.MECH_HIZLI_DRONE -> UnitEntity.UnitType.CAVALRY
            ProgressionUnitType.MECH_TANK_DROID -> UnitEntity.UnitType.HEAVY_WEAPON
            ProgressionUnitType.MECH_ROKETATAR_DROID -> UnitEntity.UnitType.HEAVY_WEAPON
            ProgressionUnitType.MECH_MECHA_SAVASCI -> UnitEntity.UnitType.KNIGHT
            ProgressionUnitType.MECH_PLAZMA_TOPU -> UnitEntity.UnitType.HEAVY_WEAPON
        }
    }
    
    /**
     * Get unit cost for progression unit type
     */
    fun getUnitCost(progressionUnitType: ProgressionUnitType): Int {
        val engineType = convertToEngineUnitType(progressionUnitType)
        return when (engineType) {
            UnitEntity.UnitType.CAVALRY -> GameConfig.CAVALRY_COST
            UnitEntity.UnitType.SPEARMAN -> GameConfig.SPEARMAN_COST
            UnitEntity.UnitType.ARCHER -> GameConfig.ARCHER_COST
            UnitEntity.UnitType.KNIGHT -> GameConfig.KNIGHT_COST
            UnitEntity.UnitType.HEAVY_WEAPON -> GameConfig.HEAVY_WEAPON_COST
            UnitEntity.UnitType.ELF_KNIGHT -> GameConfig.KNIGHT_COST
        }
    }
    
    /**
     * Get placeholder emoji for unit type
     */
    fun getUnitEmoji(progressionUnitType: ProgressionUnitType): String {
        val engineType = convertToEngineUnitType(progressionUnitType)
        return when (engineType) {
            UnitEntity.UnitType.SPEARMAN -> "🗡️"
            UnitEntity.UnitType.ARCHER -> "🏹"
            UnitEntity.UnitType.CAVALRY -> "🐎"
            UnitEntity.UnitType.KNIGHT -> "⚔️"
            UnitEntity.UnitType.HEAVY_WEAPON -> "🔨"
            UnitEntity.UnitType.ELF_KNIGHT -> "🛡️"
        }
    }
    
    /**
     * Get unit category for grouping similar units
     */
    fun getUnitCategory(progressionUnitType: ProgressionUnitType): UnitCategory {
        val engineType = convertToEngineUnitType(progressionUnitType)
        return when (engineType) {
            UnitEntity.UnitType.SPEARMAN -> UnitCategory.INFANTRY
            UnitEntity.UnitType.ARCHER -> UnitCategory.RANGED
            UnitEntity.UnitType.CAVALRY -> UnitCategory.CAVALRY
            UnitEntity.UnitType.KNIGHT -> UnitCategory.ELITE
            UnitEntity.UnitType.HEAVY_WEAPON -> UnitCategory.SIEGE
            UnitEntity.UnitType.ELF_KNIGHT -> UnitCategory.ELITE
        }
    }
    
    /**
     * Get unit tier based on strength and XP requirements
     */
    fun getUnitTier(progressionUnitType: ProgressionUnitType): UnitTier {
        return when (progressionUnitType) {
            // Human Empire - Light (0 XP)
            ProgressionUnitType.HUMAN_KILICU -> UnitTier.LIGHT
            ProgressionUnitType.HUMAN_OKCU -> UnitTier.LIGHT
            
            // Human Empire - Medium (100 XP)
            ProgressionUnitType.HUMAN_ZIRHLI_PIYADE -> UnitTier.MEDIUM
            ProgressionUnitType.HUMAN_ATLI -> UnitTier.MEDIUM
            ProgressionUnitType.HUMAN_MIZRAKCI -> UnitTier.MEDIUM
            ProgressionUnitType.HUMAN_SIFACI -> UnitTier.MEDIUM
            
            // Human Empire - Heavy (300 XP)
            ProgressionUnitType.HUMAN_MANCINIK -> UnitTier.HEAVY
            ProgressionUnitType.HUMAN_CIFT_OK_ATAN_OKCU -> UnitTier.HEAVY
            ProgressionUnitType.HUMAN_KOMUTAN -> UnitTier.HEAVY
            ProgressionUnitType.HUMAN_LIDER -> UnitTier.HEAVY
            
            // Dark Cult - Light (0 XP)
            ProgressionUnitType.DARK_GOLGE_DRUID -> UnitTier.LIGHT
            ProgressionUnitType.DARK_CADI -> UnitTier.LIGHT
            
            // Dark Cult - Medium (100 XP)
            ProgressionUnitType.DARK_MIZRAKCI -> UnitTier.MEDIUM
            ProgressionUnitType.DARK_ATLI -> UnitTier.MEDIUM
            ProgressionUnitType.DARK_ATES_CUCESI -> UnitTier.MEDIUM
            ProgressionUnitType.DARK_KARANLIK_SOVALYE -> UnitTier.MEDIUM
            
            // Dark Cult - Heavy (300 XP)
            ProgressionUnitType.DARK_KUTSA_SAPAN -> UnitTier.HEAVY
            ProgressionUnitType.DARK_SEYTAN_SAPAN -> UnitTier.HEAVY
            ProgressionUnitType.DARK_CIFT_TIRMIK_SAPAN -> UnitTier.HEAVY
            ProgressionUnitType.DARK_SAPAN_KARAM -> UnitTier.HEAVY
            
            // Elven - Light (0 XP)
            ProgressionUnitType.ELF_HAFIF_ELF_ASKER -> UnitTier.LIGHT
            ProgressionUnitType.ELF_ELF_OKCUSU -> UnitTier.LIGHT
            
            // Elven - Medium (100 XP)
            ProgressionUnitType.ELF_ELF_ATLI -> UnitTier.MEDIUM
            ProgressionUnitType.ELF_ELF_MIZRAKCI -> UnitTier.MEDIUM
            ProgressionUnitType.ELF_BUYUCU -> UnitTier.MEDIUM
            ProgressionUnitType.ELF_SIFACI_ELF -> UnitTier.MEDIUM
            
            // Elven - Heavy (300 XP)
            ProgressionUnitType.ELF_ELIT_MANCINIK -> UnitTier.HEAVY
            ProgressionUnitType.ELF_CIFT_OK_SAPNAC_ISI -> UnitTier.HEAVY
            ProgressionUnitType.ELF_ELF_PRENSI -> UnitTier.HEAVY
            ProgressionUnitType.ELF_ELF_PRENSESI -> UnitTier.HEAVY
            
            // Mechanical - Light (0 XP)
            ProgressionUnitType.MECH_BASIT_DROID -> UnitTier.LIGHT
            ProgressionUnitType.MECH_LAZER_TARET -> UnitTier.LIGHT
            
            // Mechanical - Medium (100 XP)
            ProgressionUnitType.MECH_MIZRAKCI -> UnitTier.MEDIUM
            ProgressionUnitType.MECH_KALKANLI -> UnitTier.MEDIUM
            ProgressionUnitType.MECH_ZIRHLI_DROID -> UnitTier.MEDIUM
            ProgressionUnitType.MECH_HIZLI_DRONE -> UnitTier.MEDIUM
            
            // Mechanical - Heavy (300 XP)
            ProgressionUnitType.MECH_TANK_DROID -> UnitTier.HEAVY
            ProgressionUnitType.MECH_ROKETATAR_DROID -> UnitTier.HEAVY
            ProgressionUnitType.MECH_MECHA_SAVASCI -> UnitTier.HEAVY
            ProgressionUnitType.MECH_PLAZMA_TOPU -> UnitTier.HEAVY
        }
    }
    
    /**
     * Get required XP to unlock a unit
     */
    fun getRequiredXP(progressionUnitType: ProgressionUnitType): Int {
        return getUnitTier(progressionUnitType).xpRequired
    }
    
    /**
     * Get unit description based on type and race
     */
    fun getUnitDescription(progressionUnitType: ProgressionUnitType): String {
        return when (progressionUnitType) {
            // Human Empire
            ProgressionUnitType.HUMAN_KILICU -> "Temel savaşçı, yakın mesafe saldırısı"
            ProgressionUnitType.HUMAN_OKCU -> "Uzak mesafe saldırısı, düşük zırh"
            ProgressionUnitType.HUMAN_ZIRHLI_PIYADE -> "Zırhlı piyade, yüksek savunma"
            ProgressionUnitType.HUMAN_ATLI -> "Hızlı atlı birim, yüksek hasar"
            ProgressionUnitType.HUMAN_MIZRAKCI -> "Uzun menzilli yakın savaş"
            ProgressionUnitType.HUMAN_SIFACI -> "Destek birimi, iyileştirme"
            ProgressionUnitType.HUMAN_MANCINIK -> "Kuşatma silahı, yüksek hasar"
            ProgressionUnitType.HUMAN_CIFT_OK_ATAN_OKCU -> "Gelişmiş okçu, çift saldırı"
            ProgressionUnitType.HUMAN_KOMUTAN -> "Lider birim, güçlü savaşçı"
            ProgressionUnitType.HUMAN_LIDER -> "En güçlü insan birimi"
            
            // Dark Cult
            ProgressionUnitType.DARK_GOLGE_DRUID -> "Karanlık büyücü, gölge saldırıları"
            ProgressionUnitType.DARK_CADI -> "Büyülü saldırılar, uzak mesafe"
            ProgressionUnitType.DARK_MIZRAKCI -> "Karanlık mızrakçı"
            ProgressionUnitType.DARK_ATLI -> "Karanlık atlı birim"
            ProgressionUnitType.DARK_ATES_CUCESI -> "Ateş büyüleri, yüksek hasar"
            ProgressionUnitType.DARK_KARANLIK_SOVALYE -> "Karanlık şövalye, güçlü zırh"
            ProgressionUnitType.DARK_KUTSA_SAPAN -> "Büyülü kuşatma silahı"
            ProgressionUnitType.DARK_SEYTAN_SAPAN -> "Şeytani güçlerle destekli"
            ProgressionUnitType.DARK_CIFT_TIRMIK_SAPAN -> "Çift saldırılı kuşatma"
            ProgressionUnitType.DARK_SAPAN_KARAM -> "En güçlü karanlık kuşatma"
            
            // Elven
            ProgressionUnitType.ELF_HAFIF_ELF_ASKER -> "Hafif elf savaşçısı"
            ProgressionUnitType.ELF_ELF_OKCUSU -> "Elf okçusu, yüksek hassasiyet"
            ProgressionUnitType.ELF_ELF_ATLI -> "Elf atlısı, hızlı ve çevik"
            ProgressionUnitType.ELF_ELF_MIZRAKCI -> "Elf mızrakçısı"
            ProgressionUnitType.ELF_BUYUCU -> "Elf büyücüsü, doğa büyüleri"
            ProgressionUnitType.ELF_SIFACI_ELF -> "Şifacı elf, destek birimi"
            ProgressionUnitType.ELF_ELIT_MANCINIK -> "Elit elf kuşatma silahı"
            ProgressionUnitType.ELF_CIFT_OK_SAPNAC_ISI -> "Gelişmiş elf okçusu"
            ProgressionUnitType.ELF_ELF_PRENSI -> "Elf prensi, lider birim"
            ProgressionUnitType.ELF_ELF_PRENSESI -> "Elf prensesi, en güçlü elf"
            
            // Mechanical
            ProgressionUnitType.MECH_BASIT_DROID -> "Temel robot savaşçı"
            ProgressionUnitType.MECH_LAZER_TARET -> "Lazer silahı, uzak saldırı"
            ProgressionUnitType.MECH_MIZRAKCI -> "Robot mızrakçı"
            ProgressionUnitType.MECH_KALKANLI -> "Kalkanlı robot, yüksek savunma"
            ProgressionUnitType.MECH_ZIRHLI_DROID -> "Zırhlı savaş robotu"
            ProgressionUnitType.MECH_HIZLI_DRONE -> "Hızlı saldırı drone'u"
            ProgressionUnitType.MECH_TANK_DROID -> "Tank robot, ağır zırh"
            ProgressionUnitType.MECH_ROKETATAR_DROID -> "Roketatar robot"
            ProgressionUnitType.MECH_MECHA_SAVASCI -> "Mecha savaşçı, elit robot"
            ProgressionUnitType.MECH_PLAZMA_TOPU -> "Plazma silahı, en güçlü robot"
        }
    }
}

/**
 * Unit categories for grouping
 */
enum class UnitCategory {
    INFANTRY,   // Piyade
    RANGED,     // Uzak mesafe
    CAVALRY,    // Atlı
    SIEGE,      // Kuşatma
    ELITE       // Elit
}

/**
 * Unit tiers based on strength and XP requirements
 */
enum class UnitTier(val displayName: String, val xpRequired: Int) {
    LIGHT("Light", 0),      // Level 1 - başlangıç
    MEDIUM("Medium", 100),  // Level 2-4 - orta seviye
    HEAVY("Heavy", 300)     // Level 5+ - ileri seviye
}
