package com.bandits.badmintonmanager.data.repository

import com.bandits.badmintonmanager.data.local.dao.MatchDao
import com.bandits.badmintonmanager.data.local.dao.PlayerDao
import com.bandits.badmintonmanager.data.mapper.toDomain
import com.bandits.badmintonmanager.data.mapper.toEntity
import com.bandits.badmintonmanager.domain.model.Match
import com.bandits.badmintonmanager.domain.repository.MatchRepository
import com.bandits.badmintonmanager.util.Constants
import com.bandits.badmintonmanager.util.toEpochMillis
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.datetime.LocalDateTime
import javax.inject.Inject

class MatchRepositoryImpl @Inject constructor(
    private val matchDao: MatchDao,
    private val playerDao: PlayerDao
) : MatchRepository {

    override fun getAllMatches(): Flow<List<Match>> {
        return matchDao.getAllMatches().map { entities ->
            entities.mapNotNull { entity ->
                entity.toDomain { playerId ->
                    playerDao.getPlayerByIdSuspend(playerId)?.toDomain()
                }
            }
        }
    }

    override fun getRecentMatches(limit: Int): Flow<List<Match>> {
        return matchDao.getRecentMatches(limit).map { entities ->
            entities.mapNotNull { entity ->
                entity.toDomain { playerId ->
                    playerDao.getPlayerByIdSuspend(playerId)?.toDomain()
                }
            }
        }
    }

    override suspend fun getMatchById(matchId: Long): Match? {
        val entity = matchDao.getMatchById(matchId) ?: return null
        return entity.toDomain { playerId ->
            playerDao.getPlayerByIdSuspend(playerId)?.toDomain()
        }
    }

    override fun getMatchesByDateRange(
        startDate: LocalDateTime,
        endDate: LocalDateTime
    ): Flow<List<Match>> {
        return matchDao.getMatchesByDateRange(
            startDate.toEpochMillis(),
            endDate.toEpochMillis()
        ).map { entities ->
            entities.mapNotNull { entity ->
                entity.toDomain { playerId ->
                    playerDao.getPlayerByIdSuspend(playerId)?.toDomain()
                }
            }
        }
    }

    override fun getMatchesByPlayer(playerId: Long): Flow<List<Match>> {
        return matchDao.getMatchesByPlayer(playerId).map { entities ->
            entities.mapNotNull { entity ->
                entity.toDomain { id ->
                    playerDao.getPlayerByIdSuspend(id)?.toDomain()
                }
            }
        }
    }

    override fun getMatchesByPlayerAndDateRange(
        playerId: Long,
        startDate: LocalDateTime,
        endDate: LocalDateTime
    ): Flow<List<Match>> {
        return matchDao.getMatchesByPlayerAndDateRange(
            playerId,
            startDate.toEpochMillis(),
            endDate.toEpochMillis()
        ).map { entities ->
            entities.mapNotNull { entity ->
                entity.toDomain { id ->
                    playerDao.getPlayerByIdSuspend(id)?.toDomain()
                }
            }
        }
    }

    override fun getMatchesByTeamCombination(
        player1Id: Long,
        player2Id: Long
    ): Flow<List<Match>> {
        return matchDao.getMatchesByTeamCombination(player1Id, player2Id).map { entities ->
            entities.mapNotNull { entity ->
                entity.toDomain { id ->
                    playerDao.getPlayerByIdSuspend(id)?.toDomain()
                }
            }
        }
    }

    override suspend fun addMatch(match: Match): Long {
        return matchDao.insert(match.toEntity())
    }

    override suspend fun isDuplicateMatch(match: Match): Boolean {
        val timeWindowMillis = Constants.DUPLICATE_TIME_WINDOW_MINUTES * 60 * 1000L
        return matchDao.isDuplicateMatch(
            matchDate = match.matchDate.toEpochMillis(),
            timeWindowMillis = timeWindowMillis,
            t1p1 = match.team1.player1.id,
            t1p2 = match.team1.player2.id,
            t2p1 = match.team2.player1.id,
            t2p2 = match.team2.player2.id
        )
    }

    override suspend fun getMatchCountForPlayer(playerId: Long): Int {
        return matchDao.getMatchCountForPlayer(playerId)
    }

    override suspend fun getWinCountForPlayer(playerId: Long): Int {
        return matchDao.getWinCountForPlayer(playerId)
    }

    override suspend fun getTotalMatchCount(): Int {
        return matchDao.getTotalMatchCount()
    }

    override suspend fun getMatchCountSince(startDate: LocalDateTime): Int {
        return matchDao.getMatchCountSince(startDate.toEpochMillis())
    }

    override suspend fun deleteMatch(matchId: Long) {
        matchDao.deleteMatch(matchId)
    }
}
