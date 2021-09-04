package com.baloot.mirzazade.ui.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.baloot.mirzazade.databinding.FragmentDetailsBinding
import com.baloot.mirzazade.model.NewsResponse
import kotlinx.android.synthetic.main.row_news.view.*
import java.text.SimpleDateFormat
import android.content.Intent
import android.net.Uri
import com.baloot.mirzazade.MainActivity
import com.baloot.mirzazade.R
import com.bumptech.glide.Glide


class DetailsFragment : Fragment() {
    private var _binding: FragmentDetailsBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val newsResponse = requireArguments().getSerializable(KEY_NEWS_ITEM) as NewsResponse
        binding.tvTitle.text = newsResponse.title
        binding.tvDescription.text = newsResponse.content
        binding.tvSource.text = newsResponse.source?.name?:"Unknown"
        (requireActivity() as MainActivity).supportActionBar?.title = newsResponse.title
        binding.tvDate.text = SimpleDateFormat("yyyy-MM-dd EEE HH:mm").format(
            SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").parse(newsResponse.publishedAt)
        )

        Glide
            .with(requireContext())
            .load(newsResponse.urlToImage)
            .centerCrop()
            .error(R.drawable.ic_baseline_broken_image_24)
            .into(binding.img)

        binding.btnLoadMore.setOnClickListener {
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(newsResponse.url))
            startActivity(browserIntent)
        }
    }

    companion object {
        const val KEY_NEWS_ITEM = "news_item"
    }
}