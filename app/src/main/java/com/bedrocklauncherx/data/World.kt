package com.bedrocklauncherx.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "worlds")
data class World(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val seed: String = "",
    val gameMode: String = "survival",
    val difficulty: String = "normal",
    val createdAt: Long = System.currentTimeMillis()
)
