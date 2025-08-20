/*
 * MIT License
 * 
 * Copyright (c) 2024 Rubontech
 */

package com.rubontech.raceofwar.ui.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rubontech.raceofwar.game.entities.UnitEntity

/**
 * Book of Races Screen for Race of War
 * Displays detailed lore and information about each race
 */
@Composable
fun BookOfRacesScreen(
    onBack: () -> Unit
) {
    var selectedRace by remember { mutableStateOf<UnitEntity.Race?>(null) }
    
    val races = remember {
        listOf(
            UnitEntity.Race.HUMAN_EMPIRE,
            UnitEntity.Race.DARK_CULT,
            UnitEntity.Race.NATURE_TRIBE,
            UnitEntity.Race.MECHANICAL_LEGION
        )
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF0D1B2A),
                        Color(0xFF1B263B),
                        Color(0xFF2C3E50)
                    )
                )
            )
    ) {
        if (selectedRace == null) {
            // Race Selection View
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Header
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(
                            onClick = onBack,
                            colors = IconButtonDefaults.iconButtonColors(
                                contentColor = Color.White
                            )
                        ) {
                            Text(
                                text = "←",
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        
                        Text(
                            text = "BOOK OF RACES",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        
                        Spacer(modifier = Modifier.width(48.dp))
                    }
                }
                
                // Title Section
                item {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "📖 Chronicles of the Realm",
                            fontSize = 28.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            textAlign = TextAlign.Center
                        )
                        
                        Text(
                            text = "Discover the rich history and lore of each race",
                            fontSize = 16.sp,
                            color = Color(0xFFB8C5D6),
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(top = 8.dp)
                        )
                    }
                }
                
                // Race Cards
                items(races) { race ->
                    RaceChapterCard(
                        race = race,
                        onClick = { selectedRace = race }
                    )
                }
            }
        } else {
            // Race Detail View
            RaceLoreDetailScreen(
                race = selectedRace!!,
                onBack = { selectedRace = null }
            )
        }
    }
}

@Composable
private fun RaceChapterCard(
    race: UnitEntity.Race,
    onClick: () -> Unit
) {
    val raceInfo = getRaceChapterInfo(race)
    
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .shadow(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF1F2937)
        ),
        shape = RoundedCornerShape(16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Race Icon
            Box(
                modifier = Modifier
                    .size(64.dp)
                    .background(
                        color = raceInfo.primaryColor,
                        shape = RoundedCornerShape(12.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = raceInfo.emoji,
                    fontSize = 32.sp
                )
            }
            
            Spacer(modifier = Modifier.width(16.dp))
            
            // Content
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = raceInfo.title,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                
                Text(
                    text = raceInfo.subtitle,
                    fontSize = 14.sp,
                    color = Color(0xFFD1D5DB),
                    modifier = Modifier.padding(top = 4.dp)
                )
                
                Text(
                    text = raceInfo.preview,
                    fontSize = 12.sp,
                    color = Color(0xFFB8C5D6),
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
            
            // Arrow
            Text(
                text = "→",
                fontSize = 24.sp,
                color = raceInfo.primaryColor,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
private fun RaceLoreDetailScreen(
    race: UnitEntity.Race,
    onBack: () -> Unit
) {
    val raceLore = getRaceFullLore(race)
    
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Header
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = onBack,
                    colors = IconButtonDefaults.iconButtonColors(
                        contentColor = Color.White
                    )
                ) {
                    Text(
                        text = "←",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                
                Text(
                    text = raceLore.title,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                
                Spacer(modifier = Modifier.width(48.dp))
            }
        }
        
        // Race Icon and Title
        item {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .background(
                            color = raceLore.primaryColor,
                            shape = RoundedCornerShape(16.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = raceLore.emoji,
                        fontSize = 40.sp
                    )
                }
                
                Spacer(modifier = Modifier.height(16.dp))
                
                Text(
                    text = raceLore.title,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    textAlign = TextAlign.Center
                )
                
                Text(
                    text = raceLore.subtitle,
                    fontSize = 16.sp,
                    color = raceLore.primaryColor,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
        }
        
        // Lore Content
        items(raceLore.loreChapters) { chapter ->
            LoreChapterCard(chapter = chapter)
        }
    }
}

@Composable
private fun LoreChapterCard(chapter: LoreChapter) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(4.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF1F2937)
        ),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = chapter.title,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                text = chapter.content,
                fontSize = 14.sp,
                color = Color(0xFFD1D5DB),
                lineHeight = 20.sp
            )
        }
    }
}

// Data classes
private data class RaceChapterInfo(
    val title: String,
    val subtitle: String,
    val preview: String,
    val emoji: String,
    val primaryColor: Color
)

private data class RaceFullLore(
    val title: String,
    val subtitle: String,
    val emoji: String,
    val primaryColor: Color,
    val loreChapters: List<LoreChapter>
)

private data class LoreChapter(
    val title: String,
    val content: String
)

// Race information functions
private fun getRaceChapterInfo(race: UnitEntity.Race): RaceChapterInfo {
    return when (race) {
        UnitEntity.Race.HUMAN_EMPIRE -> RaceChapterInfo(
            title = "👑 The Human Empire",
            subtitle = "Chapter I: Rise of the Kingdoms",
            preview = "From scattered tribes to mighty empire...",
            emoji = "👑",
            primaryColor = Color(0xFF3B82F6)
        )
        UnitEntity.Race.DARK_CULT -> RaceChapterInfo(
            title = "☠️ The Dark Cultists",
            subtitle = "Chapter II: Shadows and Heresy",
            preview = "Born from forbidden rites and ancient darkness...",
            emoji = "☠️",
            primaryColor = Color(0xFF8B5CF6)
        )
        UnitEntity.Race.NATURE_TRIBE -> RaceChapterInfo(
            title = "🌿 The Elven Nations",
            subtitle = "Chapter III: Guardians of the Forest",
            preview = "Ancient wisdom flows through the eternal woodlands...",
            emoji = "🌿",
            primaryColor = Color(0xFF10B981)
        )
        UnitEntity.Race.MECHANICAL_LEGION -> RaceChapterInfo(
            title = "⚙️ The Mechanical Legion",
            subtitle = "Chapter IV: Steel and Steam",
            preview = "Forged in the fires of industry and war...",
            emoji = "⚙️",
            primaryColor = Color(0xFFF59E0B)
        )
    }
}

private fun getRaceFullLore(race: UnitEntity.Race): RaceFullLore {
    return when (race) {
        UnitEntity.Race.HUMAN_EMPIRE -> RaceFullLore(
            title = "👑 The Human Empire",
            subtitle = "Lords of Unity and Steel",
            emoji = "👑",
            primaryColor = Color(0xFF3B82F6),
            loreChapters = listOf(
                LoreChapter(
                    title = "Origins of the Empire",
                    content = "Humans are the embodiment of order and discipline, forging kingdoms and armies that dominate the battlefield. Throughout history, they have built civilizations across vast lands, excelling in both melee and ranged combat.\n\nThey begin with simple footmen — swordsmen, archers, and spearmen — but soon reinforce their armies with healers, catapults, and deadly double-shot archers. In the late stages, under the command of a mighty Commander / Leader, the human host becomes a near-unstoppable force.\n\nThe greatest strength of the Humans is unity; when they march as one, they can challenge any race."
                ),
                LoreChapter(
                    title = "The Art of War",
                    content = "Human commanders are renowned for their strategic brilliance. Where others rely on raw power or mystical forces, humans excel through discipline, coordination, and tactical superiority. Their armies move as one, each soldier knowing their role in the greater strategy."
                ),
                LoreChapter(
                    title = "Legacy of Leadership",
                    content = "The Empire's strength comes from its ability to unite diverse peoples under one banner. From the mountain clans to the coastal merchants, all find purpose in service to the crown. This unity has allowed them to stand against forces that would consume lesser civilizations."
                )
            )
        )
        UnitEntity.Race.DARK_CULT -> RaceFullLore(
            title = "☠️ The Dark Cultists",
            subtitle = "Harbingers of Chaos",
            emoji = "☠️",
            primaryColor = Color(0xFF8B5CF6),
            loreChapters = listOf(
                LoreChapter(
                    title = "Birth of Heresy",
                    content = "Dark Cultists are born of forgotten faiths and twisted rituals. They wield unholy powers, demonic entities, and cursed sorcery to spread chaos across the land.\n\nShadow druids and witches weaken their foes, while fire dwarves and dark knights strike with brutal force. As the war escalates, cursed slings rain destruction, and clawed war-slingers tear through defenses. At their height, the dreaded Sling Karam emerges as a harbinger of doom.\n\nThe Cultists' greatest strength is chaos; they thrive on fear, corruption, and the slow rot of their enemies' resolve."
                ),
                LoreChapter(
                    title = "Pacts with Darkness",
                    content = "The Cultists draw their power from demonic bargains and twisted rituals. Each victory comes at a price, but they pay it willingly, for they have seen what lies beyond the veil and know that only through embracing darkness can they achieve true power."
                ),
                LoreChapter(
                    title = "Spreading Chaos",
                    content = "Wherever the Dark Cult marches, chaos follows. Reality bends around their presence, and those who oppose them find their certainties crumbling. They fight not for conquest but for the eventual unraveling of order itself."
                )
            )
        )
        UnitEntity.Race.NATURE_TRIBE -> RaceFullLore(
            title = "🌿 The Elven Nations",
            subtitle = "Keepers of Ancient Wisdom",
            emoji = "🌿",
            primaryColor = Color(0xFF10B981),
            loreChapters = listOf(
                LoreChapter(
                    title = "Children of the Forest",
                    content = "Elves are a people of elegance and harmony, bound to the ancient wisdom of nature. Their grace and mastery of both the bow and magic make them deadly opponents.\n\nThey begin with light elf soldiers and swift archers, then advance to spearmen and mages. Healer elves sustain their ranks, while elite catapults and double-shot slingers dominate at long range. In the final stages, noble Elf Princes and Princesses lead their kin with radiant aura and command.\n\nThe strength of the Elves lies in grace and balance; they blend nature and sorcery to control the flow of battle."
                ),
                LoreChapter(
                    title = "Swift and Graceful",
                    content = "Elven warriors move like the wind through leaves, striking with unmatched precision before vanishing back into the wilderness. Their mastery of both bow and blade is legendary, but it is their connection to the natural world that truly sets them apart."
                ),
                LoreChapter(
                    title = "Guardians of Balance",
                    content = "The Elves defend their sacred groves not out of aggression, but from necessity. They have witnessed the rise and fall of countless civilizations, and know that some secrets of the forest must remain hidden for the good of all."
                )
            )
        )
        UnitEntity.Race.MECHANICAL_LEGION -> RaceFullLore(
            title = "⚙️ The Mechanical Legion",
            subtitle = "Perfection in Steel",
            emoji = "⚙️",
            primaryColor = Color(0xFFF59E0B),
            loreChapters = listOf(
                LoreChapter(
                    title = "Forged for War",
                    content = "The Mechanical Legion is a cold, merciless army of machines. They feel no pity, show no mercy, and exist only to advance and annihilate.\n\nThey begin with basic droids and laser turrets, quickly escalating to armored droids and fast-moving drones. Tank droids hold the lines while rocket artillery and towering mecha warriors spearhead assaults. At their peak, the devastating Plasma Cannon brings ruin to all who oppose them.\n\nThe Legion's true strength is technology; devoid of emotion, yet flawless in design, they crush their enemies with relentless efficiency."
                ),
                LoreChapter(
                    title = "No Fear, No Mercy",
                    content = "The Legion knows neither fear nor mercy, only the cold logic of warfare. They analyze every battlefield with mechanical precision, calculating the most efficient path to victory. Emotions are weaknesses they have long since discarded."
                ),
                LoreChapter(
                    title = "Cold Steel Perfection",
                    content = "Where flesh fails, steel endures. The Legion embodies this principle, replacing the fallible nature of organic life with the perfection of technology. They see themselves not as conquerors, but as the inevitable evolution of warfare itself."
                )
            )
        )
    }
}
