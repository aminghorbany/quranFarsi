package com.amin.quranfarsi.local

import androidx.room.*
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.amin.quranfarsi.models.Surah

@Dao
interface SurahDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllSurah(data : List<Surah>)

    @Query("select * from surah_table")
    suspend fun getAllSurahList() : List<Surah>

    @Update
    suspend fun updateSurah(surah: Surah)

    @Query("select * from surah_table where isFavorite = 1")
    suspend fun getAllFavoriteSurah() : List<Surah>

}