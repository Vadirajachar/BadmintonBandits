package com.bandits.badmintonmanager.data.repository

import com.bandits.badmintonmanager.data.local.dao.PlayerDao
import com.bandits.badmintonmanager.data.mapper.toDomain
import com.bandits.badmintonmanager.data.mapper.toEntity
import com.bandits.badmintonmanager.domain.model.Player
import com.bandits.badmintonmanager.domain.model.SkillLevel
import com.bandits.badmintonmanager.domain.repository.PlayerRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class PlayerRepositoryImpl @Inject constructor(
    private val playerDao: PlayerDao
) : PlayerRepository {

    override fun getAllPlayers(): Flow<List<Player>> {
        return playerDao.getAllPlayers().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override fun getActivePlayers(): Flow<List<Player>> {
        return playerDao.getActivePlayers().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override fun getPlayerById(id: Long): Flow<Player?> {
        return playerDao.getPlayerById(id).map { it?.toDomain() }
    }

    override suspend fun getPlayerByIdSuspend(id: Long): Player? {
        return playerDao.getPlayerByIdSuspend(id)?.toDomain()
    }

    override fun searchPlayers(query: String): Flow<List<Player>> {
        return playerDao.searchPlayers(query).map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override fun getPlayersBySkillLevel(skillLevel: SkillLevel): Flow<List<Player>> {
        return playerDao.getPlayersBySkillLevel(skillLevel.name).map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override fun getTopPlayers(limit: Int): Flow<List<Player>> {
        return playerDao.getTopPlayers(limit).map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override suspend fun addPlayer(player: Player): Long {
        return playerDao.insert(player.toEntity())
    }

    override suspend fun updatePlayer(player: Player) {
        playerDao.update(player.toEntity())
    }

    override suspend fun updatePlayerRating(playerId: Long, newRating: Double) {
        playerDao.updatePlayerRating(playerId, newRating, System.currentTimeMillis())
    }

    override suspend fun setPlayerActiveStatus(playerId: Long, isActive: Boolean) {
        playerDao.setPlayerActiveStatus(playerId, isActive)
    }

    override suspend fun deletePlayer(playerId: Long) {
        playerDao.deletePlayer(playerId)
    }

    override suspend fun getPlayerCount(): Int {
        return playerDao.getPlayerCount()
    }

    override suspend fun getActivePlayerCount(): Int {
        return playerDao.getActivePlayerCount()
    }

    override suspend fun playerNameExists(name: String): Boolean {
        return playerDao.playerNameExists(name)
    }
}
