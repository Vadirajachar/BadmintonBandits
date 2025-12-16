package com.bandits.badmintonmanager.domain.model

import kotlinx.datetime.LocalDateTime

data class RatingInfo(
    val playerId: Long,
    val currentRating: Double,
    val ratingChange: Double,
    val previousRating: Double,
    val matchId: Long,
    val timestamp: LocalDateTime
) {
    val trend: RatingTrend
        get() = RatingTrend.fromChange(ratingChange)

    val isImprovement: Boolean
        get() = ratingChange > 0
}
