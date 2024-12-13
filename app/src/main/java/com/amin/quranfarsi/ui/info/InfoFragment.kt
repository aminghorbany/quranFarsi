package com.amin.quranfarsi.ui.info

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import coil.load
import com.amin.quranfarsi.R
import com.amin.quranfarsi.databinding.FragmentInfoBinding
import com.amin.quranfarsi.utils.openWebSite
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class InfoFragment : Fragment() {
    private lateinit var binding: FragmentInfoBinding
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentInfoBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            includeMehr.apply {
                imgSite.load(R.drawable.quranmehr)
                val url = "https://www.quranmehr.ir"
                txtSiteInfo.text = "اصوات قرآنی استفاده شده از  منابع موسسه ی مهر و رحمت (ع)"
                txtSiteURL.text = "www.quranmehr.ir"
                root.setOnClickListener {
                    requireActivity().openWebSite(url)
                }
            }
            includeSSHB.apply {
                imgSite.load(R.drawable.sshb)
                val url = "https://www.sshb.ir"
                txtSiteInfo.text = "با تشکر از مرکز فناوری و نوآوری شهید باقری برای میزبانی سرور اصوات قرآنی"
                txtSiteURL.text = "www.sshb.ir"
                root.setOnClickListener {
                    requireActivity().openWebSite(url)
                }
            }
            includeGithub.apply {
                imgSite.load(R.drawable.github)
                val url = "https://www.github.com/aminghorbany/quranFarsi"
                txtSiteInfo.text = "استفاده از سورس برنامه تنها برای اهداف غیرتجاری و برای توسعه دهندگانی که قصد ترویج فرهنگ اسلامی را دارند مجاز می باشد"
                txtSiteURL.text = "www.github.com/aminghorbany/quranFarsi"
                root.setOnClickListener {
                    requireActivity().openWebSite(url)
                }
            }
        }
    }


}