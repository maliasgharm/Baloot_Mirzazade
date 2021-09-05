package com.baloot.mirzazade.ui.details

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.baloot.mirzazade.databinding.FragmentDetailsBinding
import com.baloot.mirzazade.model.NewsItem
import java.text.SimpleDateFormat
import android.content.Intent
import android.net.Uri
import android.os.Handler
import android.os.Looper
import android.view.*
import android.widget.Toast
import com.baloot.mirzazade.MainActivity
import com.baloot.mirzazade.R
import com.bumptech.glide.Glide
import androidx.room.Room
import com.baloot.mirzazade.db.AppDatabase
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.components.FragmentComponent

@AndroidEntryPoint
class DetailsFragment : Fragment() {
    private var db: AppDatabase? = null
    private var _binding: FragmentDetailsBinding? = null
    private val binding get() = _binding!!
    private var newsResponse: NewsItem? = null
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
        db = Room.databaseBuilder(
            requireContext(),
            AppDatabase::class.java,
            requireContext().packageName
        ).build()

        newsResponse = requireArguments().getSerializable(KEY_NEWS_ITEM) as NewsItem
        binding.tvTitle.text = newsResponse?.title ?: return
        binding.tvDescription.text = newsResponse!!.content
        binding.tvSource.text = newsResponse!!.source?.name ?: "Unknown"
        (requireActivity() as MainActivity).supportActionBar?.title = newsResponse!!.title
        binding.tvDate.text = SimpleDateFormat("yyyy-MM-dd EEE HH:mm").format(
            SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").parse(newsResponse!!.publishedAt)
        )

        Glide
            .with(requireContext())
            .load(newsResponse!!.urlToImage)
            .centerCrop()
            .error(R.drawable.ic_baseline_broken_image_24)
            .into(binding.img)
        val handler = Handler(Looper.getMainLooper())
        Thread {
            val isMarked = db?.newsItem()?.checkMarkedItem(newsResponse?.url ?: "") ?: 0
            handler.post {
                newsResponse?.marked = isMarked
                setHasOptionsMenu(true)
            }
        }.start()
        binding.btnLoadMore.setOnClickListener {
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(newsResponse!!.url))
            startActivity(browserIntent)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)

        inflater.inflate(R.menu.details_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }


    override fun onPrepareOptionsMenu(menu: Menu) {
        super.onPrepareOptionsMenu(menu)
        val menuItem = menu.findItem(R.id.nav_mark)
        menuItem.setIcon(
            if ((newsResponse?.marked
                    ?: 0) > 0
            ) R.drawable.ic_baseline_bookmark_24 else R.drawable.ic_baseline_bookmark_border_24
        )
        menuItem.setTitle(
            if ((newsResponse?.marked
                    ?: 0) > 0
            ) R.string.mark else R.string.unmark
        )
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        val handler = Handler(Looper.getMainLooper())
        if (id == R.id.nav_mark) {
            if ((newsResponse?.marked ?: 0) > 0) {
                newsResponse?.marked = 0
                Thread {

                    newsResponse?.url?.let { db?.newsItem()?.delete(it) }
                    handler.post {
                        if (context != null)
                            Toast.makeText(requireContext(), "removed from bookmarks", Toast.LENGTH_SHORT)
                                .show()
                        item.setIcon(R.drawable.ic_baseline_bookmark_border_24)
                        item.setTitle( R.string.unmark)

                    }
                }.start()
            } else {
                newsResponse?.marked = 1
                Thread {
                    newsResponse?.getItemDB()?.let { db?.newsItem()?.insert(it) }
                    handler.post {
                        if (context != null)
                            Toast.makeText(requireContext(), "added to bookmarks", Toast.LENGTH_SHORT)
                                .show()
                        item.setIcon(R.drawable.ic_baseline_bookmark_24)
                        item.setTitle( R.string.mark)
                    }
                }.start()
            }
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    companion object {
        const val KEY_NEWS_ITEM = "news_item"
    }
}