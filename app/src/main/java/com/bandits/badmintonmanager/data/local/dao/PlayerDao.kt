package com.bandits.badmintonmanager.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.bandits.badmintonmanager.data.local.entities.PlayerEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PlayerDao {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(player: PlayerEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(players: List<PlayerEntity>)

    @Update
    suspend fun update(player: PlayerEntity)

    @Query("SELECT * FROM players ORDER BY currentRating DESC")
    fun getAllPlayers(): Flow<List<PlayerEntity>>

    @Query("SELECT * FROM players WHERE isActive = 1 ORDER BY currentRating DESC")
    fun getActivePlayers(): Flow<List<PlayerEntity>>

    @Query("SELECT * FROM players WHERE id = :playerId")
    fun getPlayerById(playerId: Long): Flow<PlayerEntity?>

    @Query("SELECT * FROM players WHERE id = :playerId LIMIT 1")
    suspend fun getPlayerByIdSuspend(playerId: Long): PlayerEntity?

    @Query("SELECT * FROM players WHERE name LIKE '%' || :query || '%' OR nickname LIKE '%' || :query || '%' ORDER BY currentRating DESC")
    fun searchPlayers(query: String): Flow<List<PlayerEntity>>

    @Query("SELECT * FROM players WHERE skillLevel = :skillLevel AND isActive = 1 ORDER BY currentRating DESC")
    fun getPlayersBySkillLevel(skillLevel: String): Flow<List<PlayerEntity>>

    @Query("SELECT * FROM players WHERE isActive = 1 ORDER BY currentRating DESC LIMIT :limit")
    fun getTopPlayers(limit: Int): Flow<List<PlayerEntity>>

    @Query("UPDATE players SET currentRating = :newRating, updatedAt = :timestamp WHERE id = :playerId")
    suspend fun updatePlayerRating(playerId: Long, newRating: Double, timestamp: Long)

    @Query("UPDATE players SET isActive = :isActive WHERE id = :playerId")
    suspend fun setPlayerActiveStatus(playerId: Long, isActive: Boolean)

    @Query("DELETE FROM players WHERE id = :playerId")
    suspend fun deletePlayer(playerId: Long)

    @Query("SELECT COUNT(*) FROM players")
    suspend fun getPlayerCount(): Int

    @Query("SELECT COUNT(*) FROM players WHERE isActive = 1")
    suspend fun getActivePlayerCount(): Int

    @Query("DELETE FROM players")
    suspend fun deleteAllPlayers()

    @Query("SELECT EXISTS(SELECT 1 FROM players WHERE name = :name LIMIT 1)")
    suspend fun playerNameExists(name: String): Boolean
}
