package com.amin.quranfarsi.repositorys

import com.amin.quranfarsi.local.SurahDao
import com.amin.quranfarsi.models.Surah
import javax.inject.Inject

class MainRepository @Inject constructor(
    private val dao: SurahDao
) {
    suspend fun insertAllSurah(surahList: List<Surah>) = dao.insertAllSurah(surahList)
    suspend fun getAllSurahList() = dao.getAllSurahList()
    suspend fun updateSurah(surah: Surah) = dao.updateSurah(surah)
    suspend fun getAllFavoriteSurah() = dao.getAllFavoriteSurah()
    suspend fun searchSureInfo(query: String) = dao.searchSureInfo(query)
    suspend fun searchFavoriteSureInfo(query: String) = dao.searchFavoriteSureInfo(query)
}