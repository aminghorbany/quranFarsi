package com.amin.quranfarsi.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.amin.quranfarsi.models.Surah

@Database(entities = [Surah::class] , version = 1 , exportSchema = false)
abstract class SurahDataBase : RoomDatabase(){
    abstract fun surahDao() : SurahDao
}