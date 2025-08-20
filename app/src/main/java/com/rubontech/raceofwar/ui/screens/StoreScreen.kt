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
 * Store Screen for Race of War
 * Displays in-game store items and purchases
 */
@Composable
fun StoreScreen(
    onBack: () -> Unit
) {
    var selectedCategory by remember { mutableStateOf(StoreCategory.COSMETICS) }
    var playerGold by remember { mutableStateOf(1250) }
    
    val storeItems = remember {
        mapOf(
            StoreCategory.COSMETICS to listOf(
                StoreItem(
                    id = "golden_armor",
                    name = "Golden Armor",
                    description = "Shiny golden armor for your units",
                    price = 500,
                    icon = "🛡️",
                    category = StoreCategory.COSMETICS,
                    isOwned = false
                ),
                StoreItem(
                    id = "crystal_weapon",
                    name = "Crystal Weapon",
                    description = "Magical crystal weapons",
                    price = 750,
                    icon = "⚔️",
                    category = StoreCategory.COSMETICS,
                    isOwned = true
                ),
                StoreItem(
                    id = "dragon_banner",
                    name = "Dragon Banner",
                    description = "Epic dragon banner for your army",
                    price = 1000,
                    icon = "🏴",
                    category = StoreCategory.COSMETICS,
                    isOwned = false
                )
            ),
            StoreCategory.BOOSTERS to listOf(
                StoreItem(
                    id = "xp_boost",
                    name = "XP Booster",
                    description = "Double XP for 24 hours",
                    price = 200,
                    icon = "⭐",
                    category = StoreCategory.BOOSTERS,
                    isOwned = false
                ),
                StoreItem(
                    id = "gold_boost",
                    name = "Gold Booster",
                    description = "Double gold earnings for 24 hours",
                    price = 300,
                    icon = "💰",
                    category = StoreCategory.BOOSTERS,
                    isOwned = false
                ),
                StoreItem(
                    id = "speed_boost",
                    name = "Speed Booster",
                    description = "Faster unit movement for 1 hour",
                    price = 150,
                    icon = "⚡",
                    category = StoreCategory.BOOSTERS,
                    isOwned = false
                )
            ),
            StoreCategory.SPECIAL_UNITS to listOf(
                StoreItem(
                    id = "dragon_rider",
                    name = "Dragon Rider",
                    description = "Powerful dragon riding unit",
                    price = 2000,
                    icon = "🐉",
                    category = StoreCategory.SPECIAL_UNITS,
                    isOwned = false
                ),
                StoreItem(
                    id = "wizard",
                    name = "Wizard",
                    description = "Magical wizard with powerful spells",
                    price = 1500,
                    icon = "🧙‍♂️",
                    category = StoreCategory.SPECIAL_UNITS,
                    isOwned = false
                ),
                StoreItem(
                    id = "giant",
                    name = "Giant",
                    description = "Massive giant unit with high HP",
                    price = 2500,
                    icon = "👹",
                    category = StoreCategory.SPECIAL_UNITS,
                    isOwned = false
                )
            )
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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
        ) {
            // Header
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
                    text = "STORE",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                
                Spacer(modifier = Modifier.width(48.dp))
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Gold Display
            Card(
                modifier = Modifier
                    .fillMaxWidth()
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
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "💰",
                        fontSize = 24.sp
                    )
                    
                    Spacer(modifier = Modifier.width(12.dp))
                    
                    Text(
                        text = "$playerGold",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFF59E0B)
                    )
                    
                    Text(
                        text = " Gold",
                        fontSize = 16.sp,
                        color = Color(0xFFD1D5DB)
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            
            // Category Tabs
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                StoreCategory.values().forEach { category ->
                    CategoryTab(
                        category = category,
                        isSelected = category == selectedCategory,
                        onClick = { selectedCategory = category }
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            
            // Store Items
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(storeItems[selectedCategory] ?: emptyList()) { item ->
                    StoreItemCard(
                        item = item,
                        playerGold = playerGold,
                        onPurchase = { 
                            if (playerGold >= item.price && !item.isOwned) {
                                playerGold -= item.price
                                // Here you would update the item's owned status
                            }
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun CategoryTab(
    category: StoreCategory,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .clickable { onClick() }
            .shadow(if (isSelected) 8.dp else 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) Color(0xFF3B82F6) else Color(0xFF374151)
        ),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = category.icon,
                fontSize = 20.sp
            )
            
            Text(
                text = category.displayName,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }
    }
}

@Composable
private fun StoreItemCard(
    item: StoreItem,
    playerGold: Int,
    onPurchase: () -> Unit
) {
    val canAfford = playerGold >= item.price && !item.isOwned
    
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(4.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (item.isOwned) Color(0xFF10B981) else Color(0xFF1F2937)
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
                    .size(56.dp)
                    .background(
                        color = if (item.isOwned) Color(0xFF059669) else Color(0xFF374151),
                        shape = RoundedCornerShape(8.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = item.icon,
                    fontSize = 28.sp
                )
            }
            
            Spacer(modifier = Modifier.width(16.dp))
            
            // Content
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = item.name,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                
                Text(
                    text = item.description,
                    fontSize = 14.sp,
                    color = Color(0xFFD1D5DB)
                )
                
                if (item.isOwned) {
                    Text(
                        text = "OWNED ✓",
                        fontSize = 12.sp,
                        color = Color(0xFF10B981),
                        fontWeight = FontWeight.Bold
                    )
                }
            }
            
            // Price/Purchase Button
            if (!item.isOwned) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "💰 ${item.price}",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFF59E0B)
                    )
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    Button(
                        onClick = onPurchase,
                        enabled = canAfford,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (canAfford) Color(0xFF3B82F6) else Color(0xFF6B7280)
                        ),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            text = if (canAfford) "BUY" else "CAN'T AFFORD",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}

enum class StoreCategory(val displayName: String, val icon: String) {
    COSMETICS("Cosmetics", "🎨"),
    BOOSTERS("Boosters", "⚡"),
    SPECIAL_UNITS("Special Units", "👥")
}

data class StoreItem(
    val id: String,
    val name: String,
    val description: String,
    val price: Int,
    val icon: String,
    val category: StoreCategory,
    val isOwned: Boolean
)

