package com.bandits.badmintonmanager.di

import android.content.Context
import androidx.room.Room
import com.bandits.badmintonmanager.data.local.dao.MatchDao
import com.bandits.badmintonmanager.data.local.dao.PlayerDao
import com.bandits.badmintonmanager.data.local.dao.RatingHistoryDao
import com.bandits.badmintonmanager.data.local.database.BanditsDatabase
import com.bandits.badmintonmanager.util.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext context: Context
    ): BanditsDatabase {
        return Room.databaseBuilder(
            context,
            BanditsDatabase::class.java,
            Constants.DATABASE_NAME
        )
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    @Singleton
    fun providePlayerDao(database: BanditsDatabase): PlayerDao {
        return database.playerDao()
    }

    @Provides
    @Singleton
    fun provideMatchDao(database: BanditsDatabase): MatchDao {
        return database.matchDao()
    }

    @Provides
    @Singleton
    fun provideRatingHistoryDao(database: BanditsDatabase): RatingHistoryDao {
        return database.ratingHistoryDao()
    }
}
