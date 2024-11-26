package com.amin.quran.viewmodels

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.amin.quran.models.Surah
import com.amin.quran.repositorys.MainRepository
import com.google.common.reflect.TypeToken
import com.google.gson.Gson
import com.google.gson.JsonObject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    application: Application,
    private val repo : MainRepository
) : AndroidViewModel(application) {
    val suresLiveData = MutableLiveData<List<Surah>>()
    val loading = MutableLiveData<Boolean>()

    //get from assets
    fun getSuresDataFromAssets() = viewModelScope.launch {
        val result = readSuresFromAsset(getApplication())
        suresLiveData.postValue(result)
    }

    private fun readSuresFromAsset(context: Context): List<Surah> {
        val jsonFile = context.assets.open("sures.json").bufferedReader().use { it.readText() }
        val jsonObject = Gson().fromJson(jsonFile, JsonObject::class.java)
        val surahArray = jsonObject.getAsJsonArray("surahs")
        val type = object : TypeToken<List<Surah>>() {}.type
        return Gson().fromJson(surahArray, type)
    }

}