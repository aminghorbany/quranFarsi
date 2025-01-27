package com.amin.quranfarsi.local

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SharedPrefsManager @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE)

    fun setIsFirstRun(value: Boolean?) {
        sharedPreferences.edit().apply {
            putBoolean("isFirstRun", value ?: true).apply()
        }
    }

    fun getIsFirstRun(): Boolean {
        return (sharedPreferences.getBoolean("isFirstRun", true))
    }

    fun setActiveLoop(value: Boolean?) {
        sharedPreferences.edit().apply {
            putBoolean("activeLoop", value ?: true).apply()
        }
    }

    fun getActiveLoop(): Boolean {
        return (sharedPreferences.getBoolean("activeLoop", true))
    }
}