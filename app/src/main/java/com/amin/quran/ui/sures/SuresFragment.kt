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
import com.amin.quran.models.Surah
import com.amin.quran.ui.dialog.PlayDialogFragment
import com.amin.quran.viewmodels.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SuresFragment : Fragment() {

    private lateinit var binding: FragmentSuresBinding
    private val viewModel : MainViewModel by viewModels()
    @Inject lateinit var suresAdapter: SuresAdapter
    private var playDialog: PlayDialogFragment? = null

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
                showPlayDialog(selectedItem)
            }
        }
    }

    private fun showPlayDialog(data: Surah) {
        //delete previous dialog is exist
        playDialog?.dismiss()
        playDialog = null

        //create new dialog
        playDialog = PlayDialogFragment.newInstance(data)
        playDialog?.show(childFragmentManager, PlayDialogFragment::class.java.simpleName)
    }
}