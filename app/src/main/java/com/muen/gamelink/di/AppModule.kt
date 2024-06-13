package com.muen.gamelink.di

import android.content.Context
import com.muen.gamelink.source.local.dao.ItemDao
import com.muen.gamelink.source.local.dao.LevelDao
import com.muen.gamelink.source.local.dao.UserDao
import com.muen.gamelink.source.local.db.GameDB
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideDataBase(@ApplicationContext context: Context): GameDB {
        return GameDB.getDatabase(context)
    }

    @Provides
    fun provideItemDao(database: GameDB): ItemDao = database.itemDao()

    @Provides
    fun provideLevelDao(database: GameDB): LevelDao = database.levelDao()

    @Provides
    fun provideUserDao(database: GameDB): UserDao = database.userDao()
}