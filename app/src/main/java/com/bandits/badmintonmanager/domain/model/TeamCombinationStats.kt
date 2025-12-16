package com.bandits.badmintonmanager.domain.model

data class TeamCombinationStats(
    val player1: Player,
    val player2: Player,
    val matchesPlayed: Int,
    val wins: Int,
    val losses: Int,
    val pointsScored: Int,
    val pointsConceded: Int
) {
    val teamName: String
        get() = "${player1.shortDisplayName} & ${player2.shortDisplayName}"

    val winPercentage: Double
        get() = if (matchesPlayed > 0) (wins.toDouble() / matchesPlayed) * 100.0 else 0.0

    val averagePointsScored: Double
        get() = if (matchesPlayed > 0) pointsScored.toDouble() / matchesPlayed else 0.0

    val averagePointsConceded: Double
        get() = if (matchesPlayed > 0) pointsConceded.toDouble() / matchesPlayed else 0.0

    val pointsDifference: Int
        get() = pointsScored - pointsConceded

    val combinedRating: Double
        get() = (player1.currentRating + player2.currentRating) / 2.0

    fun containsPlayer(playerId: Long): Boolean =
        player1.id == playerId || player2.id == playerId
}
