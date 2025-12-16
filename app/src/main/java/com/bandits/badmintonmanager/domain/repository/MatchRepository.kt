package com.bandits.badmintonmanager.domain.repository

import com.bandits.badmintonmanager.domain.model.Match
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.LocalDateTime

interface MatchRepository {

    fun getAllMatches(): Flow<List<Match>>

    fun getRecentMatches(limit: Int): Flow<List<Match>>

    suspend fun getMatchById(matchId: Long): Match?

    fun getMatchesByDateRange(
        startDate: LocalDateTime,
        endDate: LocalDateTime
    ): Flow<List<Match>>

    fun getMatchesByPlayer(playerId: Long): Flow<List<Match>>

    fun getMatchesByPlayerAndDateRange(
        playerId: Long,
        startDate: LocalDateTime,
        endDate: LocalDateTime
    ): Flow<List<Match>>

    fun getMatchesByTeamCombination(
        player1Id: Long,
        player2Id: Long
    ): Flow<List<Match>>

    suspend fun addMatch(match: Match): Long

    suspend fun isDuplicateMatch(match: Match): Boolean

    suspend fun getMatchCountForPlayer(playerId: Long): Int

    suspend fun getWinCountForPlayer(playerId: Long): Int

    suspend fun getTotalMatchCount(): Int

    suspend fun getMatchCountSince(startDate: LocalDateTime): Int

    suspend fun deleteMatch(matchId: Long)
}
