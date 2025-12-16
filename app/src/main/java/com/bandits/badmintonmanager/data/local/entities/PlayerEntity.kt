package com.bandits.badmintonmanager.data.local.entities

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "players",
    indices = [
        Index(value = ["name"], unique = true),
        Index(value = ["currentRating"]),
        Index(value = ["isActive"])
    ]
)
data class PlayerEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val nickname: String? = null,
    val skillLevel: String,  // BEGINNER, INTERMEDIATE, ADVANCED
    val joiningDate: Long,   // Timestamp in milliseconds
    val currentRating: Double = 1000.0,
    val isActive: Boolean = true,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)
