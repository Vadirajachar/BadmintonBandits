package com.bandits.badmintonmanager.di

import com.bandits.badmintonmanager.data.repository.ExportRepositoryImpl
import com.bandits.badmintonmanager.data.repository.MatchRepositoryImpl
import com.bandits.badmintonmanager.data.repository.PlayerRepositoryImpl
import com.bandits.badmintonmanager.data.repository.StatisticsRepositoryImpl
import com.bandits.badmintonmanager.domain.repository.ExportRepository
import com.bandits.badmintonmanager.domain.repository.MatchRepository
import com.bandits.badmintonmanager.domain.repository.PlayerRepository
import com.bandits.badmintonmanager.domain.repository.StatisticsRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindPlayerRepository(
        impl: PlayerRepositoryImpl
    ): PlayerRepository

    @Binds
    @Singleton
    abstract fun bindMatchRepository(
        impl: MatchRepositoryImpl
    ): MatchRepository

    @Binds
    @Singleton
    abstract fun bindStatisticsRepository(
        impl: StatisticsRepositoryImpl
    ): StatisticsRepository

    @Binds
    @Singleton
    abstract fun bindExportRepository(
        impl: ExportRepositoryImpl
    ): ExportRepository
}
