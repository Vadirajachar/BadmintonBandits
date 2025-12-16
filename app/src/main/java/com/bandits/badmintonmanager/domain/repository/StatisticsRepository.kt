package com.bandits.badmintonmanager.domain.repository

import com.bandits.badmintonmanager.domain.model.PlayerStatistics
import com.bandits.badmintonmanager.domain.model.RatingInfo
import com.bandits.badmintonmanager.domain.model.SkillLevel
import com.bandits.badmintonmanager.domain.model.TeamCombinationStats
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.LocalDateTime

interface StatisticsRepository {

    suspend fun getPlayerStatistics(
        playerId: Long,
        startDate: LocalDateTime? = null,
        endDate: LocalDateTime? = null
    ): PlayerStatistics

    suspend fun getTeamCombinationStats(
        player1Id: Long,
        player2Id: Long,
        startDate: LocalDateTime? = null,
        endDate: LocalDateTime? = null
    ): TeamCombinationStats

    suspend fun getAllTeamCombinations(): List<TeamCombinationStats>

    fun getRatingHistoryForPlayer(playerId: Long, limit: Int = 10): Flow<List<RatingInfo>>

    suspend fun getLeaderboard(
        skillLevel: SkillLevel? = null,
        limit: Int = 50
    ): List<PlayerStatistics>
}
