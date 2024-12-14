package com.amin.quranfarsi.viewmodels

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.amin.quranfarsi.models.Surah
import com.amin.quranfarsi.repositorys.MainRepository
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    application: Application,
    private val repo : MainRepository
) : AndroidViewModel(application) {
    val assetSurahLiveData = MutableLiveData<List<Surah>>()
    val dbSurahLiveData = MutableLiveData<List<Surah>>()
    val favoriteSurahLiveData = MutableLiveData<List<Surah>>()

    fun getSuresDataFromAssets() = viewModelScope.launch {
        val result = readSuresFromAsset(getApplication())
        assetSurahLiveData.postValue(result)
    }

    private fun readSuresFromAsset(context: Context): List<Surah> {
        val jsonFile = context.assets.open("sures.json").bufferedReader().use { it.readText() }
        val jsonObject = Gson().fromJson(jsonFile, JsonObject::class.java)
        val surahArray = jsonObject.getAsJsonArray("surahs")
        val type = object : TypeToken<List<Surah>>() {}.type
        return Gson().fromJson(surahArray, type)
    }

    fun insertAllSurah(surahList: List<Surah>) = viewModelScope.launch {
        repo.insertAllSurah(surahList)
    }

    fun getAllSurahList() = viewModelScope.launch {
        val res = repo.getAllSurahList()
        dbSurahLiveData.postValue(res)
    }

    fun updateSurah(surah: Surah) = viewModelScope.launch {
        repo.updateSurah(surah)
    }

    fun getAllFavoriteSurah() = viewModelScope.launch {
        val res = repo.getAllFavoriteSurah()
        favoriteSurahLiveData.postValue(res)
    }

    fun searchSureInfo(query: String) = viewModelScope.launch {
        val res = repo.searchSureInfo(query)
        dbSurahLiveData.postValue(res)
    }
}