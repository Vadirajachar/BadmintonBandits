package com.bandits.badmintonmanager.domain.model

import kotlinx.datetime.LocalDateTime
import kotlin.math.abs

data class Match(
    val id: Long = 0,
    val matchDate: LocalDateTime,
    val team1: Team,
    val team2: Team,
    val winningTeamNumber: Int,  // 1 or 2
    val notes: String? = null
) {
    val scoreDifference: Int
        get() = abs(team1.score - team2.score)

    val winningTeam: Team
        get() = if (winningTeamNumber == 1) team1 else team2

    val losingTeam: Team
        get() = if (winningTeamNumber == 1) team2 else team1

    val allPlayers: List<Player>
        get() = team1.players + team2.players

    val allPlayerIds: List<Long>
        get() = team1.playerIds + team2.playerIds

    fun isPlayerInMatch(playerId: Long): Boolean =
        team1.containsPlayer(playerId) || team2.containsPlayer(playerId)

    fun didPlayerWin(playerId: Long): Boolean =
        winningTeam.containsPlayer(playerId)

    fun getTeamForPlayer(playerId: Long): Team? = when {
        team1.containsPlayer(playerId) -> team1
        team2.containsPlayer(playerId) -> team2
        else -> null
    }

    fun getOpposingTeam(playerId: Long): Team? = when {
        team1.containsPlayer(playerId) -> team2
        team2.containsPlayer(playerId) -> team1
        else -> null
    }

    val matchSummary: String
        get() = "${team1.teamName} (${team1.score}) vs ${team2.teamName} (${team2.score})"
}
