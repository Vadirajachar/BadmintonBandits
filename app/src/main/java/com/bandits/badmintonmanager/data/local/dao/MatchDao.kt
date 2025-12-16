package com.bandits.badmintonmanager.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.bandits.badmintonmanager.data.local.entities.MatchEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MatchDao {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(match: MatchEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(matches: List<MatchEntity>)

    @Query("SELECT * FROM matches ORDER BY matchDate DESC")
    fun getAllMatches(): Flow<List<MatchEntity>>

    @Query("SELECT * FROM matches ORDER BY matchDate DESC LIMIT :limit")
    fun getRecentMatches(limit: Int): Flow<List<MatchEntity>>

    @Query("SELECT * FROM matches WHERE id = :matchId LIMIT 1")
    suspend fun getMatchById(matchId: Long): MatchEntity?

    @Query("""
        SELECT * FROM matches
        WHERE matchDate >= :startDate AND matchDate <= :endDate
        ORDER BY matchDate DESC
    """)
    fun getMatchesByDateRange(startDate: Long, endDate: Long): Flow<List<MatchEntity>>

    @Query("""
        SELECT * FROM matches
        WHERE team1Player1Id = :playerId
           OR team1Player2Id = :playerId
           OR team2Player1Id = :playerId
           OR team2Player2Id = :playerId
        ORDER BY matchDate DESC
    """)
    fun getMatchesByPlayer(playerId: Long): Flow<List<MatchEntity>>

    @Query("""
        SELECT * FROM matches
        WHERE (team1Player1Id = :playerId OR team1Player2Id = :playerId
               OR team2Player1Id = :playerId OR team2Player2Id = :playerId)
          AND matchDate >= :startDate AND matchDate <= :endDate
        ORDER BY matchDate DESC
    """)
    fun getMatchesByPlayerAndDateRange(
        playerId: Long,
        startDate: Long,
        endDate: Long
    ): Flow<List<MatchEntity>>

    @Query("""
        SELECT * FROM matches
        WHERE ((team1Player1Id = :player1Id AND team1Player2Id = :player2Id)
            OR (team1Player1Id = :player2Id AND team1Player2Id = :player1Id)
            OR (team2Player1Id = :player1Id AND team2Player2Id = :player2Id)
            OR (team2Player1Id = :player2Id AND team2Player2Id = :player1Id))
        ORDER BY matchDate DESC
    """)
    fun getMatchesByTeamCombination(player1Id: Long, player2Id: Long): Flow<List<MatchEntity>>

    @Query("""
        SELECT COUNT(*) FROM matches
        WHERE team1Player1Id = :playerId
           OR team1Player2Id = :playerId
           OR team2Player1Id = :playerId
           OR team2Player2Id = :playerId
    """)
    suspend fun getMatchCountForPlayer(playerId: Long): Int

    @Query("""
        SELECT COUNT(*) FROM matches
        WHERE (team1Player1Id = :playerId OR team1Player2Id = :playerId) AND winningTeam = 1
           OR (team2Player1Id = :playerId OR team2Player2Id = :playerId) AND winningTeam = 2
    """)
    suspend fun getWinCountForPlayer(playerId: Long): Int

    @Query("""
        SELECT COUNT(*) FROM matches
        WHERE matchDate >= :startTimestamp
    """)
    suspend fun getMatchCountSince(startTimestamp: Long): Int

    @Query("SELECT COUNT(*) FROM matches")
    suspend fun getTotalMatchCount(): Int

    @Query("""
        SELECT EXISTS(
            SELECT 1 FROM matches
            WHERE ABS(matchDate - :matchDate) < :timeWindowMillis
              AND ((team1Player1Id = :t1p1 AND team1Player2Id = :t1p2
                    AND team2Player1Id = :t2p1 AND team2Player2Id = :t2p2)
                OR (team1Player1Id = :t1p2 AND team1Player2Id = :t1p1
                    AND team2Player1Id = :t2p1 AND team2Player2Id = :t2p2)
                OR (team1Player1Id = :t1p1 AND team1Player2Id = :t1p2
                    AND team2Player1Id = :t2p2 AND team2Player2Id = :t2p1)
                OR (team1Player1Id = :t1p2 AND team1Player2Id = :t1p1
                    AND team2Player1Id = :t2p2 AND team2Player2Id = :t2p1)
                OR (team1Player1Id = :t2p1 AND team1Player2Id = :t2p2
                    AND team2Player1Id = :t1p1 AND team2Player2Id = :t1p2)
                OR (team1Player1Id = :t2p2 AND team1Player2Id = :t2p1
                    AND team2Player1Id = :t1p1 AND team2Player2Id = :t1p2)
                OR (team1Player1Id = :t2p1 AND team1Player2Id = :t2p2
                    AND team2Player1Id = :t1p2 AND team2Player2Id = :t1p1)
                OR (team1Player1Id = :t2p2 AND team1Player2Id = :t2p1
                    AND team2Player1Id = :t1p2 AND team2Player2Id = :t1p1))
            LIMIT 1
        )
    """)
    suspend fun isDuplicateMatch(
        matchDate: Long,
        timeWindowMillis: Long,
        t1p1: Long,
        t1p2: Long,
        t2p1: Long,
        t2p2: Long
    ): Boolean

    @Query("DELETE FROM matches WHERE id = :matchId")
    suspend fun deleteMatch(matchId: Long)

    @Query("DELETE FROM matches")
    suspend fun deleteAllMatches()
}
