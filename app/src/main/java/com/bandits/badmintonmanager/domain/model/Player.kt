package com.bandits.badmintonmanager.domain.model

import kotlinx.datetime.LocalDateTime

data class Player(
    val id: Long = 0,
    val name: String,
    val nickname: String? = null,
    val skillLevel: SkillLevel,
    val joiningDate: LocalDateTime,
    val currentRating: Double = 1000.0,
    val isActive: Boolean = true
) {
    val displayName: String
        get() = nickname?.let { "$name ($it)" } ?: name

    val shortDisplayName: String
        get() = nickname ?: name
}
