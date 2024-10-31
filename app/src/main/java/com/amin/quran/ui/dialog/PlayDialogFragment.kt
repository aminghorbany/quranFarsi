package com.amin.quran.ui.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.amin.quran.databinding.FragmentDialogPlayBinding
import com.amin.quran.models.Surah
import com.amin.quran.utils.showToast
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PlayDialogFragment(private val data : Surah ) : BottomSheetDialogFragment(){

    private lateinit var binding: FragmentDialogPlayBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentDialogPlayBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            txtSureName.text = data.name
            imgPlayPause.setOnClickListener {
                requireContext().showToast("play")
            }
            imgGoAfter.setOnClickListener {
                requireContext().showToast("imgGoAfter")
            }
            imgGoBefore.setOnClickListener {
                requireContext().showToast("imgGoBefore")
            }

        }
    }
}