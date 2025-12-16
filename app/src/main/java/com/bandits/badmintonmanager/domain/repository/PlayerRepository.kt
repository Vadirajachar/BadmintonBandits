package com.bandits.badmintonmanager.domain.repository

import com.bandits.badmintonmanager.domain.model.Player
import com.bandits.badmintonmanager.domain.model.SkillLevel
import kotlinx.coroutines.flow.Flow

interface PlayerRepository {

    fun getAllPlayers(): Flow<List<Player>>

    fun getActivePlayers(): Flow<List<Player>>

    fun getPlayerById(id: Long): Flow<Player?>

    suspend fun getPlayerByIdSuspend(id: Long): Player?

    fun searchPlayers(query: String): Flow<List<Player>>

    fun getPlayersBySkillLevel(skillLevel: SkillLevel): Flow<List<Player>>

    fun getTopPlayers(limit: Int): Flow<List<Player>>

    suspend fun addPlayer(player: Player): Long

    suspend fun updatePlayer(player: Player)

    suspend fun updatePlayerRating(playerId: Long, newRating: Double)

    suspend fun setPlayerActiveStatus(playerId: Long, isActive: Boolean)

    suspend fun deletePlayer(playerId: Long)

    suspend fun getPlayerCount(): Int

    suspend fun getActivePlayerCount(): Int

    suspend fun playerNameExists(name: String): Boolean
}
