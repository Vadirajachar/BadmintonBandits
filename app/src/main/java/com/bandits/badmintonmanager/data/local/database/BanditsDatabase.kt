package com.bandits.badmintonmanager.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.bandits.badmintonmanager.data.local.dao.MatchDao
import com.bandits.badmintonmanager.data.local.dao.PlayerDao
import com.bandits.badmintonmanager.data.local.dao.RatingHistoryDao
import com.bandits.badmintonmanager.data.local.entities.MatchEntity
import com.bandits.badmintonmanager.data.local.entities.PlayerEntity
import com.bandits.badmintonmanager.data.local.entities.RatingHistoryEntity

@Database(
    entities = [
        PlayerEntity::class,
        MatchEntity::class,
        RatingHistoryEntity::class
    ],
    version = 1,
    exportSchema = true
)
@TypeConverters(Converters::class)
abstract class BanditsDatabase : RoomDatabase() {

    abstract fun playerDao(): PlayerDao
    abstract fun matchDao(): MatchDao
    abstract fun ratingHistoryDao(): RatingHistoryDao

    companion object {
        const val DATABASE_NAME = "bandits_database"
    }
}
