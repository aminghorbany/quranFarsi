package com.amin.quranfarsi.ui.sure

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import com.amin.quranfarsi.R
import com.amin.quranfarsi.databinding.FragmentSureBinding
import com.amin.quranfarsi.models.Surah
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
    private val viewModel : MainViewModel by activityViewModels()
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentSureBinding.inflate(layoutInflater)
        viewModel.getAllSurahList()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)
        binding.apply {
            (activity as AppCompatActivity).setSupportActionBar(sureToolbar)
            viewModel.dbSurahLiveData.observe(viewLifecycleOwner){
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

            sureAdapter.onFavoriteItemClickListener {
                val updateSurah = Surah(it.first.name ,
                    it.first.transliteration ,
                    it.first.englishName ,
                    it.first.persianTranslation ,
                    it.first.ayahs ,
                    it.first.place ,
                    it.first.downloadLink ,
                    it.first.id ,
                    it.second)
                viewModel.updateSurah(updateSurah)
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_toolbar , menu)
        val search = menu.findItem(R.id.actionSearch)
        val searchView = search.actionView as SearchView
        searchView.queryHint = "جستجوی سوره..."
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                viewModel.searchSureInfo(newText ?: "")
                return true
            }
        })
        return super.onCreateOptionsMenu(menu , inflater)
    }

}