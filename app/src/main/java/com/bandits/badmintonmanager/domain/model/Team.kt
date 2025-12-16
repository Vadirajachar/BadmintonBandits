package com.bandits.badmintonmanager.domain.model

data class Team(
    val player1: Player,
    val player2: Player,
    val score: Int
) {
    val players: List<Player>
        get() = listOf(player1, player2)

    val playerIds: List<Long>
        get() = listOf(player1.id, player2.id)

    val teamName: String
        get() = "${player1.shortDisplayName} & ${player2.shortDisplayName}"

    val averageRating: Double
        get() = (player1.currentRating + player2.currentRating) / 2.0

    fun containsPlayer(playerId: Long): Boolean =
        player1.id == playerId || player2.id == playerId

    fun getPartner(playerId: Long): Player? = when (playerId) {
        player1.id -> player2
        player2.id -> player1
        else -> null
    }
}
