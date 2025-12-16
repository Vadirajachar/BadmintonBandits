package com.bandits.badmintonmanager.domain.model

data class PlayerStatistics(
    val player: Player,
    val totalMatches: Int,
    val wins: Int,
    val losses: Int,
    val pointsScored: Int,
    val pointsConceded: Int,
    val recentMatches: List<Match> = emptyList()
) {
    val winPercentage: Double
        get() = if (totalMatches > 0) (wins.toDouble() / totalMatches) * 100.0 else 0.0

    val averagePointsScored: Double
        get() = if (totalMatches > 0) pointsScored.toDouble() / totalMatches else 0.0

    val averagePointsConceded: Double
        get() = if (totalMatches > 0) pointsConceded.toDouble() / totalMatches else 0.0

    val pointsDifference: Int
        get() = pointsScored - pointsConceded

    val hasPlayedMatches: Boolean
        get() = totalMatches > 0
}
