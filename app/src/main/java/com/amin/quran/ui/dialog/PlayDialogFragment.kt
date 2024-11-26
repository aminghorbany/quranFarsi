package com.amin.quran.ui.dialog

import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import com.amin.quran.R
import com.amin.quran.databinding.FragmentDialogPlayBinding
import com.amin.quran.models.Surah
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import java.io.File
import java.util.Locale
import javax.inject.Inject

@AndroidEntryPoint
class PlayDialogFragment(private val data : Surah ) : BottomSheetDialogFragment(){

    private lateinit var binding: FragmentDialogPlayBinding
    @Inject lateinit var exoPlayer: ExoPlayer
    private var isUserSeeking = false
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentDialogPlayBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initializePlayer()
        setupUI()

        dialog?.setOnDismissListener {
                exoPlayer.release()
        }
    }

    private fun initializePlayer() {
        val serverFileName = Uri.parse(data.downloadLink).lastPathSegment
        val filePath = File(Environment.getExternalStoragePublicDirectory(
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

    private fun setupUI() {
        binding.apply {
            txtSureName.text = data.name

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

}