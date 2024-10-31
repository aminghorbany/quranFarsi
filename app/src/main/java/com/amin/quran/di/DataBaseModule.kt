package com.amin.quran.di

import android.content.Context
import androidx.room.Room
import com.amin.quran.utils.Constants
import com.example.panel.local.db.SureDao
import com.example.panel.local.db.SureDataBase
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
    fun provideDataBase(@ApplicationContext context: Context) : SureDataBase =
        Room.databaseBuilder(context , SureDataBase::class.java , Constants.SURE_DB)
            .fallbackToDestructiveMigration()
            .allowMainThreadQueries()
            .build()

    @Provides
    @Singleton
    fun provideDao(db : SureDataBase) : SureDao = db.sureDao()

}