package com.bandits.badmintonmanager.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.bandits.badmintonmanager.data.local.entities.RatingHistoryEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface RatingHistoryDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(ratingHistory: RatingHistoryEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(ratingHistories: List<RatingHistoryEntity>)

    @Query("SELECT * FROM rating_history WHERE playerId = :playerId ORDER BY timestamp DESC")
    fun getRatingHistoryForPlayer(playerId: Long): Flow<List<RatingHistoryEntity>>

    @Query("""
        SELECT * FROM rating_history
        WHERE playerId = :playerId
        ORDER BY timestamp DESC
        LIMIT :limit
    """)
    fun getRecentRatingHistoryForPlayer(playerId: Long, limit: Int): Flow<List<RatingHistoryEntity>>

    @Query("""
        SELECT * FROM rating_history
        WHERE playerId = :playerId
          AND timestamp >= :startDate
          AND timestamp <= :endDate
        ORDER BY timestamp DESC
    """)
    fun getRatingHistoryByDateRange(
        playerId: Long,
        startDate: Long,
        endDate: Long
    ): Flow<List<RatingHistoryEntity>>

    @Query("""
        SELECT * FROM rating_history
        WHERE matchId = :matchId
        ORDER BY playerId
    """)
    suspend fun getRatingHistoryForMatch(matchId: Long): List<RatingHistoryEntity>

    @Query("""
        SELECT COUNT(*) FROM rating_history
        WHERE playerId = :playerId
    """)
    suspend fun getRatingHistoryCountForPlayer(playerId: Long): Int

    @Query("DELETE FROM rating_history WHERE playerId = :playerId")
    suspend fun deleteRatingHistoryForPlayer(playerId: Long)

    @Query("DELETE FROM rating_history WHERE matchId = :matchId")
    suspend fun deleteRatingHistoryForMatch(matchId: Long)

    @Query("DELETE FROM rating_history")
    suspend fun deleteAllRatingHistory()

    @Query("SELECT * FROM rating_history ORDER BY timestamp DESC")
    fun getAllRatingHistory(): Flow<List<RatingHistoryEntity>>
}
