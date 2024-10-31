package com.amin.quran.viewmodels

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.amin.quran.models.SureModel
import com.amin.quran.repositorys.MainRepository
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    application: Application,
    private val repo : MainRepository
) : AndroidViewModel(application) {
    val suresLiveData = MutableLiveData<SureModel>()
    val loading = MutableLiveData<Boolean>()

    //get from assets
    fun getSuresDataFromAssets() = viewModelScope.launch {
        val result = readSuresFromAsset(getApplication())
        suresLiveData.postValue(result)
    }

    private fun readSuresFromAsset(context: Context): SureModel {
        val jsonFile = context.assets.open("sures.json").bufferedReader().use { it.readText() }
        val quranSures = Gson().fromJson(jsonFile, SureModel::class.java)
        return quranSures
    }


}