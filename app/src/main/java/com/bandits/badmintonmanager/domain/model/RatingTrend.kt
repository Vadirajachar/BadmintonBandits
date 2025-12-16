package com.bandits.badmintonmanager.domain.model

enum class RatingTrend {
    UP,
    DOWN,
    STABLE;

    companion object {
        fun fromChange(ratingChange: Double, threshold: Double = 5.0): RatingTrend {
            return when {
                ratingChange > threshold -> UP
                ratingChange < -threshold -> DOWN
                else -> STABLE
            }
        }
    }
}
