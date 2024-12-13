package com.amin.quranfarsi.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.amin.quranfarsi.R
import com.amin.quranfarsi.databinding.ActivityMainBinding
import com.amin.quranfarsi.local.SharedPrefsManager
import com.amin.quranfarsi.utils.showShortToast
import com.amin.quranfarsi.viewmodels.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private var _binding : ActivityMainBinding?= null
    private val binding get() = _binding!!
    private lateinit var navController: NavController
    private val viewModel : MainViewModel by viewModels()
    @Inject lateinit var sharedPrefsManager: SharedPrefsManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.apply {
            navController = findNavController(R.id.navHost)
            bottomNav.setupWithNavController(navController)
        }

        //first run
        if (sharedPrefsManager.getIsFirstRun()) {
            viewModel.getSuresDataFromAssets()
            sharedPrefsManager.setIsFirstRun(false)
            binding.bottomNav.selectedItemId = R.id.infoFragment
        }else{
            binding.bottomNav.selectedItemId = R.id.sureFragment
        }

        //observe and insert all surah in database
        viewModel.assetSurahLiveData.observe(this){
            viewModel.insertAllSurah(it)
        }
    }

    override fun onNavigateUp(): Boolean {
        return navController.navigateUp() || super.onNavigateUp()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}