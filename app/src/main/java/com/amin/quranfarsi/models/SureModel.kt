package com.amin.quranfarsi.models

import android.os.Parcelable
import androidx.annotation.Keep
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.amin.quranfarsi.utils.Constants
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize


@Entity(tableName = Constants.SURAH_TABLE)
@Keep
@Parcelize
data class Surah(
    @SerializedName("name")
    val name: String, // الفاتحة
    @SerializedName("transliteration")
    val transliteration: String, // Al-Fatiha
    @SerializedName("englishName")
    val englishName: String, // The Opening
    @SerializedName("persianTranslation")
    val persianTranslation: String, // آغاز
    @SerializedName("ayahs")
    val ayahs: Int, // 7
    @SerializedName("place")
    val place: String, // Mecca
    @SerializedName("downloadLink")
    val downloadLink: String?, // https://sshb.ir/quran/1.mp3
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
) : Parcelable
