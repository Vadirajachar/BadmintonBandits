package com.bandits.badmintonmanager.data.mapper

import com.bandits.badmintonmanager.data.local.entities.MatchEntity
import com.bandits.badmintonmanager.domain.model.Match
import com.bandits.badmintonmanager.domain.model.Player
import com.bandits.badmintonmanager.domain.model.Team
import com.bandits.badmintonmanager.util.toEpochMillis
import com.bandits.badmintonmanager.util.toLocalDateTime
import kotlin.math.abs

suspend fun MatchEntity.toDomain(
    getPlayer: suspend (Long) -> Player?
): Match? {
    val team1Player1 = getPlayer(team1Player1Id) ?: return null
    val team1Player2 = getPlayer(team1Player2Id) ?: return null
    val team2Player1 = getPlayer(team2Player1Id) ?: return null
    val team2Player2 = getPlayer(team2Player2Id) ?: return null

    return Match(
        id = id,
        matchDate = matchDate.toLocalDateTime(),
        team1 = Team(team1Player1, team1Player2, team1Score),
        team2 = Team(team2Player1, team2Player2, team2Score),
        winningTeamNumber = winningTeam,
        notes = notes
    )
}

fun Match.toEntity(): MatchEntity {
    return MatchEntity(
        id = id,
        matchDate = matchDate.toEpochMillis(),
        team1Player1Id = team1.player1.id,
        team1Player2Id = team1.player2.id,
        team1Score = team1.score,
        team2Player1Id = team2.player1.id,
        team2Player2Id = team2.player2.id,
        team2Score = team2.score,
        winningTeam = winningTeamNumber,
        scoreDifference = abs(team1.score - team2.score),
        notes = notes
    )
}
