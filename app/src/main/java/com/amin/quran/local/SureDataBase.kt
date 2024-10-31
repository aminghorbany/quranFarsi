package com.example.panel.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.amin.quran.models.Surah

@Database(entities = [Surah::class] , version = 1 , exportSchema = false)
abstract class SureDataBase : RoomDatabase(){
    abstract fun sureDao() : SureDao
}