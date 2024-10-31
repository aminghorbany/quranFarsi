package com.amin.quran.models

import androidx.annotation.Keep
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import com.amin.quran.utils.Constants
import kotlinx.parcelize.Parcelize
import android.os.Parcelable

@Keep
@Parcelize
data class SureModel(
    @SerializedName("surahs")
    val surahs: List<Surah>
) : Parcelable

@Keep
@Parcelize
@Entity(tableName = Constants.SURE_TABLE)
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
