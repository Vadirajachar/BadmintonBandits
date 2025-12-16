package com.bandits.badmintonmanager.data.local.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "matches",
    foreignKeys = [
        ForeignKey(
            entity = PlayerEntity::class,
            parentColumns = ["id"],
            childColumns = ["team1Player1Id"],
            onDelete = ForeignKey.RESTRICT
        ),
        ForeignKey(
            entity = PlayerEntity::class,
            parentColumns = ["id"],
            childColumns = ["team1Player2Id"],
            onDelete = ForeignKey.RESTRICT
        ),
        ForeignKey(
            entity = PlayerEntity::class,
            parentColumns = ["id"],
            childColumns = ["team2Player1Id"],
            onDelete = ForeignKey.RESTRICT
        ),
        ForeignKey(
            entity = PlayerEntity::class,
            parentColumns = ["id"],
            childColumns = ["team2Player2Id"],
            onDelete = ForeignKey.RESTRICT
        )
    ],
    indices = [
        Index(value = ["matchDate"]),
        Index(value = ["team1Player1Id"]),
        Index(value = ["team1Player2Id"]),
        Index(value = ["team2Player1Id"]),
        Index(value = ["team2Player2Id"]),
        // Composite index for duplicate detection
        Index(
            value = ["matchDate", "team1Player1Id", "team1Player2Id", "team2Player1Id", "team2Player2Id"],
            name = "idx_match_composite"
        )
    ]
)
data class MatchEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val matchDate: Long,     // Timestamp in milliseconds

    // Team 1
    val team1Player1Id: Long,
    val team1Player2Id: Long,
    val team1Score: Int,

    // Team 2
    val team2Player1Id: Long,
    val team2Player2Id: Long,
    val team2Score: Int,

    val winningTeam: Int,    // 1 or 2
    val scoreDifference: Int, // Absolute difference

    val notes: String? = null,
    val createdAt: Long = System.currentTimeMillis()
)
