package com.amin.quranfarsi.repositorys

import com.amin.quranfarsi.local.SurahDao
import com.amin.quranfarsi.models.Surah
import javax.inject.Inject

class MainRepository @Inject constructor(
    private val dao: SurahDao
) {
    suspend fun insertAllSurah(surahList: List<Surah>) = dao.insertAllSurah(surahList)
    suspend fun getAllSurahList() = dao.getAllSurahList()
}