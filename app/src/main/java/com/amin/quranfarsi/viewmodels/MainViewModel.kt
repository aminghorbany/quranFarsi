package com.amin.quranfarsi.viewmodels

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.amin.quranfarsi.models.Surah
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    application: Application,
//    private val repo : MainRepository
) : AndroidViewModel(application) {
    val suresLiveData = MutableLiveData<List<Surah>>()

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