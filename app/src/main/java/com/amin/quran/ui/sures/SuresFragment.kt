package com.amin.quran.ui.sures

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.amin.quran.databinding.FragmentSuresBinding
import com.amin.quran.ui.dialog.PlayDialogFragment
import com.amin.quran.viewmodels.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SuresFragment : Fragment() {

    private lateinit var binding: FragmentSuresBinding
    private val viewModel : MainViewModel by viewModels()
    @Inject lateinit var suresAdapter: SuresAdapter
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentSuresBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            viewModel.getSuresDataFromAssets()
            viewModel.suresLiveData.observe(viewLifecycleOwner){
                suresAdapter.setData(it)
                recyclerSure.apply {
                    adapter = suresAdapter
                    layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
                }
            }
            suresAdapter.onItemClickListener { selectedItem ->
                PlayDialogFragment(selectedItem).show(childFragmentManager , PlayDialogFragment(selectedItem).tag)
            }
        }
    }
}