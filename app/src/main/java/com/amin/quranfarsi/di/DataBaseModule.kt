package com.amin.quranfarsi.di

import android.content.Context
import androidx.room.Room
import com.amin.quranfarsi.local.SurahDao
import com.amin.quranfarsi.local.SurahDataBase
import com.amin.quranfarsi.utils.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataBaseModule {

    @Provides
    @Singleton
    fun provideDataBase(@ApplicationContext context: Context) : SurahDataBase =
        Room.databaseBuilder(context , SurahDataBase::class.java , Constants.SURAH_DB)
            .fallbackToDestructiveMigration()
            .allowMainThreadQueries()
            .build()

    @Provides
    @Singleton
    fun provideDao(db : SurahDataBase) : SurahDao = db.surahDao()

}