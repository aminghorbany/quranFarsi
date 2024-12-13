package com.amin.quranfarsi.ui.sure

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import com.amin.quranfarsi.databinding.FragmentSureBinding
import com.amin.quranfarsi.ui.dialog.PlayDialogFragment
import com.amin.quranfarsi.ui.sure.adapter.SureAdapter
import com.amin.quranfarsi.utils.showShortToast
import com.amin.quranfarsi.viewmodels.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SureFragment : Fragment() {
    private lateinit var binding: FragmentSureBinding
    @Inject lateinit var sureAdapter: SureAdapter
    private val viewModel : MainViewModel by viewModels()
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentSureBinding.inflate(layoutInflater)
        viewModel.getSuresDataFromAssets()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            viewModel.suresLiveData.observe(viewLifecycleOwner){
                sureAdapter.setData(it)
            }
            recyclerSure.apply {
                adapter = sureAdapter
                layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
            }
            //onClick
            sureAdapter.onItemClickListener {
                val dialog = PlayDialogFragment.newInstance(it)
                dialog.show(childFragmentManager, PlayDialogFragment().tag)
            }
        }
    }

}