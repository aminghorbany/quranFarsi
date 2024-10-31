package com.amin.quran.ui.sures

import com.amin.quran.models.SureModel
import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.amin.quran.R
import com.amin.quran.databinding.ItemRecyclerSureBinding
import com.amin.quran.models.Surah
import com.amin.quran.utils.setMyBackground
import com.amin.quran.utils.showSnackBarShort
import javax.inject.Inject

class SuresAdapter @Inject constructor() : RecyclerView.Adapter<SuresAdapter.SureViewHolder>() {

    private var sureList = emptyList<Surah>()

    fun setData(data: List<Surah>) {
        val diffUtil = DiffUtil.calculateDiff(SuresDiffUtils(sureList, data))
        sureList = data
        diffUtil.dispatchUpdatesTo(this)
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
                txtSure.text = item.name
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
                var isFavorite = false
                binding.imgAddToFavorite.setOnClickListener {
                    isFavorite = !isFavorite
                    val iconRes = if (isFavorite) R.drawable.ic_heart_fill_red_24 else R.drawable.ic_heart_empty_red_24
                    it.setBackgroundResource(iconRes)
                    val message = if (isFavorite)"به علاقه مندی ها اضافه شد" else "از علاقه مندی ها حذف شد"
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
            }
        }
    }

    private class SuresDiffUtils(private val oldItems: List<Surah>, private val newItems: List<Surah>) : DiffUtil.Callback() {

        override fun getOldListSize(): Int = oldItems.size

        override fun getNewListSize(): Int = newItems.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldItems[oldItemPosition] === newItems[newItemPosition]
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldItems[oldItemPosition] == newItems[newItemPosition]
        }
    }
}
