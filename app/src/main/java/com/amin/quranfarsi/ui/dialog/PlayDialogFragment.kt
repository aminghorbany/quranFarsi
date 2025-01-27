package com.amin.quranfarsi.ui.dialog

import android.app.Dialog
import android.content.DialogInterface
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.activityViewModels
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import com.amin.quranfarsi.R
import com.amin.quranfarsi.databinding.FragmentDialogPlayBinding
import com.amin.quranfarsi.local.SharedPrefsManager
import com.amin.quranfarsi.models.Surah
import com.amin.quranfarsi.utils.showShortToast
import com.amin.quranfarsi.utils.showSnackBarShort
import com.amin.quranfarsi.viewmodels.MainViewModel
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import java.io.File
import java.util.Locale
import javax.inject.Inject

@AndroidEntryPoint
class PlayDialogFragment : BottomSheetDialogFragment(){

    private lateinit var binding: FragmentDialogPlayBinding
    @Inject lateinit var exoPlayer: ExoPlayer
    private var isUserSeeking = false
    private val viewModel : MainViewModel by activityViewModels()
    @Inject lateinit var sharedPrefsManager: SharedPrefsManager
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState) as BottomSheetDialog
        // Prevent dialog from being dismissed when touching outside
        dialog.setCancelable(false)
        dialog.setCanceledOnTouchOutside(false)
        return dialog
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Add back press callback to allow dismissing with back button
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                dismiss()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentDialogPlayBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val surahData = arguments?.getParcelable<Surah>(ARG_SURAH_DATA)
        binding.apply {
            surahData?.let { data ->
                initializePlayer(data)
                setupUI(data)
            }
        }

    }

    private fun initializePlayer(data: Surah) {
        exoPlayer.stop()
        exoPlayer.clearMediaItems()

        val serverFileName = Uri.parse(data.downloadLink).lastPathSegment
        val filePath = File(
            Environment.getExternalStoragePublicDirectory(
            Environment.DIRECTORY_DOWNLOADS), "QuranFarsi/$serverFileName")
        val mediaItem = MediaItem.fromUri(filePath.path)
        exoPlayer.apply {
            setMediaItem(mediaItem)
            prepare()
            play()
        }

        // Initialize the slider's max value to the duration
        exoPlayer.addListener(object : Player.Listener {
            override fun onIsPlayingChanged(isPlaying: Boolean) {
                if (isPlaying) {
                    binding.sliderMain.valueTo = exoPlayer.duration.toFloat()
                    updateSliderPosition()
                }
            }
        })
        // Update slider in sync with playback position
        exoPlayer.addListener(object : Player.Listener {
            override fun onPlaybackStateChanged(playbackState: Int) {
                if (playbackState == Player.STATE_READY && !isUserSeeking) {
                    binding.sliderMain.value = exoPlayer.currentPosition.toFloat()
                    binding.txtTimerEnd.text = formatTime(exoPlayer.duration)
                }
            }
        })
    }

    // Helper method to update loop UI based on shared preferences
    private fun updateLoopUI() {
        binding.apply {
            if (sharedPrefsManager.getActiveLoop()){
                imgReplayOnOff.setImageResource(R.drawable.ic_replay_on)
                exoPlayer.repeatMode = Player.REPEAT_MODE_ONE
            }else{
                imgReplayOnOff.setImageResource(R.drawable.ic_replay_off)
                exoPlayer.repeatMode = Player.REPEAT_MODE_OFF
            }
        }
    }

    private fun setupUI(data: Surah) {
        binding.apply {
            txtSureName.text = data.name
            // Handle loop state
            updateLoopUI()

            // Toggle loop state when replay icon is clicked
            imgReplayOnOff.setOnClickListener {
                val currentLoopState = sharedPrefsManager.getActiveLoop()
                sharedPrefsManager.setActiveLoop(!currentLoopState)
                updateLoopUI()
                val message = if (!currentLoopState)
                    "تکرار فعال شد"
                else
                    "تکرار غیرفعال شد"
                root.context.showShortToast(message)
            }

            if (data.isFavorite){
                imgHeartInPlayer.setImageResource(R.drawable.ic_heart_fill_red_24)
            } else {
                imgHeartInPlayer.setImageResource(R.drawable.ic_heart_empty_red_24)
            }
            binding.imgHeartInPlayer.setOnClickListener {
                data.isFavorite = !data.isFavorite
                val iconRes = if (data.isFavorite) R.drawable.ic_heart_fill_red_24 else R.drawable.ic_heart_empty_red_24
                imgHeartInPlayer.setImageResource(iconRes)
                val message = if (data.isFavorite)"به علاقه مندی ها اضافه شد" else "از علاقه مندی ها حذف شد"
                val updateSurah = Surah(data.name ,
                    data.transliteration ,
                    data.englishName ,
                    data.persianTranslation ,
                    data.ayahs ,
                    data.place ,
                    data.downloadLink ,
                    data.id ,
                    data.isFavorite)
                viewModel.updateSurah(updateSurah)
                root.context.showShortToast(message)
            }

            imgPlayAndPause.setOnClickListener {
                if (exoPlayer.isPlaying) {
                    exoPlayer.pause()
                    imgPlayAndPause.setImageResource(R.drawable.ic_play)
                } else {
                    exoPlayer.play()
                    imgPlayAndPause.setImageResource(R.drawable.ic_pause)
                }
            }
            imgForward.setOnClickListener {
                exoPlayer.seekTo(exoPlayer.currentPosition + 5000)
            }
            imgBackward.setOnClickListener {
                exoPlayer.seekTo(exoPlayer.currentPosition - 5000)
            }
            // Sync slider with playback progress
            sliderMain.addOnChangeListener { slider, value, fromUser ->
                if (fromUser) {
                    isUserSeeking = true
                    exoPlayer.seekTo(value.toLong())
                    isUserSeeking = false
                }
            }
        }
    }

    // Update slider as playback progresses
    private fun updateSliderPosition() {
        binding.sliderMain.postDelayed({
            if (exoPlayer.isPlaying) {
                binding.sliderMain.value = exoPlayer.currentPosition.toFloat()
                binding.txtTimerStart.text = formatTime(exoPlayer.currentPosition)
                updateSliderPosition() // Recursive call to keep updating
            }
        }, 1000)
    }

    // Format milliseconds to mm:ss
    private fun formatTime(milliseconds: Long): String {
        val minutes = (milliseconds / 1000) / 60
        val seconds = (milliseconds / 1000) % 60
        return String.format(Locale.US, "%02d:%02d", minutes, seconds)
    }

    private fun releasePlayer() {
        exoPlayer.stop()
        exoPlayer.clearMediaItems()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        releasePlayer()
    }

    companion object {
        private const val ARG_SURAH_DATA = "surah_data"

        fun newInstance(data: Surah): PlayDialogFragment {
            val fragment = PlayDialogFragment()
            val bundle = Bundle().apply {
                putParcelable(ARG_SURAH_DATA, data)
            }
            fragment.arguments = bundle
            return fragment
        }
    }

}