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

/**
 * Achievements Screen for Race of War
 * Displays player achievements and progress
 */
@Composable
fun AchievementsScreen(
    onBack: () -> Unit
) {
    val achievements = remember {
        listOf(
            Achievement(
                id = "first_win",
                title = "First Victory",
                description = "Herhangi bir zorlukta bir oyun kazan",
                icon = "🏆",
                isUnlocked = true,
                progress = 100
            ),
            Achievement(
                id = "elf_expert_master",
                title = "Elf Expert Master",
                description = "Elf olarak en zor seviye botları yen",
                icon = "🌿",
                isUnlocked = false,
                progress = 0
            ),
            Achievement(
                id = "human_expert_master",
                title = "Human Expert Master",
                description = "Human olarak en zor seviye botları yen",
                icon = "👑",
                isUnlocked = false,
                progress = 0
            ),
            Achievement(
                id = "dark_cult_expert_master",
                title = "Dark Cult Expert Master",
                description = "Dark Cult olarak en zor seviye botları yen",
                icon = "☠️",
                isUnlocked = false,
                progress = 0
            ),
            Achievement(
                id = "mech_expert_master",
                title = "Mechanical Legion Expert Master",
                description = "Mechanical Legion olarak en zor seviye botları yen",
                icon = "⚙️",
                isUnlocked = false,
                progress = 0
            ),
            Achievement(
                id = "knight_slayer",
                title = "Knight Slayer",
                description = "10 tane düşman şövalye birimini öldür",
                icon = "⚔️",
                isUnlocked = false,
                progress = 30
            ),
            Achievement(
                id = "race_master",
                title = "Race Master",
                description = "Tüm ırklarla kazanım elde et",
                icon = "🎖️",
                isUnlocked = false,
                progress = 25
            ),
            Achievement(
                id = "battle_veteran",
                title = "Battle Veteran",
                description = "100 savaş tamamla",
                icon = "🗡️",
                isUnlocked = false,
                progress = 15
            ),
            Achievement(
                id = "perfect_strategy",
                title = "Perfect Strategy",
                description = "Hiç birim kaybetmeden kazan",
                icon = "🎯",
                isUnlocked = false,
                progress = 0
            ),
            Achievement(
                id = "speed_demon",
                title = "Speed Demon",
                description = "2 dakikada bir savaş kazan",
                icon = "⚡",
                isUnlocked = true,
                progress = 100
            )
        )
    }
    
    val unlockedCount = achievements.count { it.isUnlocked }
    val totalCount = achievements.size
    
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
                        text = "ACHIEVEMENTS",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    
                    Spacer(modifier = Modifier.width(48.dp))
                }
            }
            
            // Progress Summary
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .shadow(8.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFF1F2937)
                    ),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "$unlockedCount / $totalCount",
                            fontSize = 32.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF10B981)
                        )
                        
                        Text(
                            text = "Achievements Unlocked",
                            fontSize = 16.sp,
                            color = Color(0xFFD1D5DB)
                        )
                        
                        Spacer(modifier = Modifier.height(16.dp))
                        
                        LinearProgressIndicator(
                            progress = unlockedCount.toFloat() / totalCount,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(8.dp)
                                .clip(RoundedCornerShape(4.dp)),
                            color = Color(0xFF10B981),
                            trackColor = Color(0xFF374151)
                        )
                    }
                }
            }
            
            // Achievements List Header
            item {
                Text(
                    text = "All Achievements",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    modifier = Modifier.padding(horizontal = 4.dp)
                )
            }
            
            // Individual Achievements
            items(achievements) { achievement ->
                AchievementCard(achievement = achievement)
            }
            
            // Bottom spacing for last item
            item {
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

@Composable
private fun AchievementCard(achievement: Achievement) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(4.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (achievement.isUnlocked) 
                Color(0xFF1F2937) else Color(0xFF111827)
        ),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Icon
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .background(
                        color = if (achievement.isUnlocked) 
                            Color(0xFF10B981) else Color(0xFF6B7280),
                        shape = RoundedCornerShape(8.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = achievement.icon,
                    fontSize = 24.sp
                )
            }
            
            Spacer(modifier = Modifier.width(16.dp))
            
            // Content
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = achievement.title,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (achievement.isUnlocked) Color.White else Color(0xFF9CA3AF)
                )
                
                Text(
                    text = achievement.description,
                    fontSize = 14.sp,
                    color = if (achievement.isUnlocked) Color(0xFFD1D5DB) else Color(0xFF6B7280)
                )
                
                if (!achievement.isUnlocked && achievement.progress > 0) {
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    LinearProgressIndicator(
                        progress = achievement.progress / 100f,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(4.dp)
                            .clip(RoundedCornerShape(2.dp)),
                        color = Color(0xFFF59E0B),
                        trackColor = Color(0xFF374151)
                    )
                    
                    Text(
                        text = "${achievement.progress}%",
                        fontSize = 12.sp,
                        color = Color(0xFFF59E0B),
                        fontWeight = FontWeight.Bold
                    )
                }
            }
            
            // Status
            if (achievement.isUnlocked) {
                Text(
                    text = "✓",
                    fontSize = 20.sp,
                    color = Color(0xFF10B981),
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

data class Achievement(
    val id: String,
    val title: String,
    val description: String,
    val icon: String,
    val isUnlocked: Boolean,
    val progress: Int
)
