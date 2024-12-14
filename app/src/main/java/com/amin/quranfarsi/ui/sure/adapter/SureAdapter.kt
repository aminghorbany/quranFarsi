package com.amin.quranfarsi.ui.sure.adapter

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.DownloadManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.Settings
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.amin.quranfarsi.R
import com.amin.quranfarsi.databinding.ItemRecyclerSureBinding
import com.amin.quranfarsi.models.Surah
import com.amin.quranfarsi.utils.goneWidget
import com.amin.quranfarsi.utils.showShortToast
import com.amin.quranfarsi.utils.showSnackBarLong
import com.amin.quranfarsi.utils.showSnackBarShort
import com.amin.quranfarsi.utils.showWidget
import com.amin.quranfarsi.viewmodels.MainViewModel
import java.io.File
import javax.inject.Inject

class SureAdapter @Inject constructor() : RecyclerView.Adapter<SureAdapter.SureViewHolder>() {

    private var sureList = emptyList<Surah>()
    private var onItemClick: ((Surah) -> Unit)? = null
    private var onFavoriteItemClick: ((Pair<Surah , Boolean>) -> Unit)? = null

    fun setData(data: List<Surah>) {
        val diffUtil = DiffUtil.calculateDiff(WordsDiffUtils(sureList, data))
        sureList = data
        diffUtil.dispatchUpdatesTo(this)
    }

    fun onItemClickListener(listener: (Surah) -> Unit) {
        onItemClick = listener
    }

    fun onFavoriteItemClickListener(listener: (Pair<Surah , Boolean>) -> Unit) {
        onFavoriteItemClick = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SureViewHolder {
        val binding = ItemRecyclerSureBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SureViewHolder(binding)
    }

    override fun getItemCount() = sureList.size

    override fun onBindViewHolder(holder: SureViewHolder, position: Int) {
        holder.bindData(sureList[position])
    }

    inner class SureViewHolder(private val binding: ItemRecyclerSureBinding) : RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("SetTextI18n")
        fun bindData(item: Surah) {
            binding.apply {
                txtCounter.text = (adapterPosition + 1).toString()
                txtSure.text = item.name
                txtAyeCount.text = item.ayahs.toString()
                // Check if the audio file exists locally
                val serverFileName = Uri.parse(item.downloadLink).lastPathSegment // e.g., "1.mp3"
                val filePath = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "QuranFarsi/$serverFileName")
                if (filePath.exists()){
                    root.context.goneWidget(imgDownload)
                    root.setBackgroundColor(ContextCompat.getColor(root.context , R.color.green_50))
                } else {
                    root.context.showWidget(imgDownload)
                    root.setBackgroundColor(ContextCompat.getColor(root.context , R.color.white))
                }
                var pos = 0
                txtSure.setOnClickListener {
                    when(pos){
                        0 ->{
                            txtSure.text = item.persianTranslation
                            pos++
                        }
                        1 -> {
                            txtSure.text = item.englishName
                            pos++
                        }
                        2 -> {
                            txtSure.text = item.name
                            pos = 0
                        }
                        else -> {
                            txtSure.text = item.name
                        }
                    }
                }
                if (item.isFavorite){
                    imgAddToFavorite.setBackgroundResource(R.drawable.ic_heart_fill_red_24)
                } else {
                    imgAddToFavorite.setBackgroundResource(R.drawable.ic_heart_empty_red_24)
                }
                binding.imgAddToFavorite.setOnClickListener {
                    item.isFavorite = !item.isFavorite
                    val iconRes = if (item.isFavorite) R.drawable.ic_heart_fill_red_24 else R.drawable.ic_heart_empty_red_24
                    it.setBackgroundResource(iconRes)
                    onFavoriteItemClick?.invoke(Pair(item , item.isFavorite))
                    val message = if (item.isFavorite)"به علاقه مندی ها اضافه شد" else "از علاقه مندی ها حذف شد"
                    root.context.showSnackBarShort(message, binding.root, "باشه")
                }
                if (item.place == "Mecca"){
                    imgPlace.load(R.drawable.img_mecca1){
                        crossfade(true)
                        crossfade(100)
                    }
                }else{
                    imgPlace.load(R.drawable.img_medina){
                        crossfade(true)
                        crossfade(100)
                    }
                }
                imgDownload.setOnClickListener {
                    item.downloadLink?.let {
                        if (!filePath.exists()){
                            downloadFile(root.context , item.name , item.downloadLink)
                        }else{
                            root.context.showSnackBarShort("این سوره از قبل دریافت شده است" , root , "باشه")
                        }
                    }
                }
                root.setOnClickListener {
                    if (filePath.exists()){
                        onItemClick?.invoke(item)
                    }else{
                        root.context.showSnackBarLong("ابتدا سوره را دانلود نمایید" , root , "باشه")
                    }
                }
            }
        }
    }

    private class WordsDiffUtils(private val oldItems: List<Surah>, private val newItems: List<Surah>) : DiffUtil.Callback() {

        override fun getOldListSize(): Int = oldItems.size

        override fun getNewListSize(): Int = newItems.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldItems[oldItemPosition] === newItems[newItemPosition]
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldItems[oldItemPosition] == newItems[newItemPosition]
        }
    }

    private fun downloadFile(context: Context, fileName: String, fileUrl: String) {
        // Check permission for Android 10 and below
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q &&
            ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            // Request permissions if not granted
            ActivityCompat.requestPermissions((context as Activity), arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), 1)
            return
        }

        // Check for MANAGE_EXTERNAL_STORAGE permission on Android 11+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R && !Environment.isExternalStorageManager()) {
            val intent = Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION)
            intent.data = Uri.parse("package:${context.packageName}")
            context.startActivity(intent)
            return
        }

        // Extract the original filename from the URL
        val serverFileName = Uri.parse(fileUrl).lastPathSegment ?: "default_name.mp3"

        // Create download folder in Downloads directory
        val downloadFolder = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "QuranFarsi")
        if (!downloadFolder.exists()) {
            downloadFolder.mkdirs()
        }

        // Setup the download request
        val request = DownloadManager.Request(Uri.parse(fileUrl))
            .setTitle("دانلود سوره ی $fileName")
            .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
            .setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "QuranFarsi/$serverFileName")
            .setAllowedOverMetered(true)
            .setAllowedOverRoaming(true)

        // Enqueue the request
        context.showShortToast("دریافت شروع شد...")
        val downloadManager = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        downloadManager.enqueue(request)
    }
}
