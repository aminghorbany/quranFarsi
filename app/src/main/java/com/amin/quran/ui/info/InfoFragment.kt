package com.amin.quran.ui.info

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.amin.quran.databinding.FragmentInfoBinding
import com.amin.quran.databinding.FragmentSuresBinding
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class InfoFragment : Fragment() {

    private lateinit var binding: FragmentInfoBinding
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentInfoBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

//    private fun convertMillisToString(duration: Long): String {
//        val second = duration / 1000 % 60
//        val minute = duration / (1000 * 60) % 60
//        return java.lang.String.format(Locale.US, "%02d:%02d", minute, second)
//    }
//
//    private fun readSuresFromAsset() : QuranSuresDC{
//        val fileInRead = requireContext().assets.open("quranSures.json")
//            .bufferedReader()
//            .use {
//                it.readText()
//            }
//        val dataFromJson = Gson().fromJson(fileInRead , QuranSuresDC::class.java)!!
//        return dataFromJson
//    }
}