package com.baloot.mirzazade.ui.news

import android.annotation.SuppressLint
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.baloot.mirzazade.R
import com.baloot.mirzazade.model.NewsItem
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.row_news.view.*
import java.io.Serializable
import java.text.SimpleDateFormat


class MarkedAdapter(val recyclerView: RecyclerView) : RecyclerView.Adapter<MarkedAdapter.ViewHolder>() {
    private var listenerLoadMore: ((newPage : Int) -> Boolean)? = null
    private val handler = Handler(Looper.getMainLooper())

    private var listItemClickListener : ((view: View, position: Int) -> Unit)? = null
    private var newsAdapterModel = NewsAdapterModel(
        arrayListOf(), false, 0,
        loadMore = false,
        loadedPage = 1,
        isAddMoreProgressBar = false,
        firstItemPosition = -1,
    )
    private var isAddMoreProgressBar = false
        set(value) {
            if (value == field) {
                return
            }
            field = value
            newsAdapterModel.isAddMoreProgressBar = value
            if (value) {
                notifyItemInserted(itemCount)
            } else {
                notifyItemRemoved(itemCount)
            }
        }

    var loadMore = false
        private set(value) {
            if (newsAdapterModel.isEndList || value == field)
                return
            newsAdapterModel.loadMore = value
            field = value
        }

    fun setOnLoadMoreListener(listenerLoadMore: ((newPage : Int) -> Boolean)?) {
        this.listenerLoadMore = listenerLoadMore
    }

    fun loaded() {
        handler.post {
            if (loadMore) {
                loadMore = false
//                isAddMoreProgressBar = false
            }
        }
    }

    fun setIsEnd(isEnd: Boolean) {
        newsAdapterModel.isEndList = isEnd
        if (isEnd)
            handler.post {
                isAddMoreProgressBar = false
            }
    }

    private var firstItemPosition = 0

    init {
        recyclerView.apply {
            val linearLayoutManager = LinearLayoutManager(recyclerView.context)
            layoutManager = linearLayoutManager
            adapter = this@MarkedAdapter
            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(
                    recyclerView: RecyclerView,
                    dx: Int,
                    dy: Int
                ) {
                    firstItemPosition = linearLayoutManager.findFirstVisibleItemPosition()
                    val lastVisiblePosition: Int = linearLayoutManager.findLastVisibleItemPosition()
                    if (lastVisiblePosition > itemCount - (newsAdapterModel.lastAddCount / 2)) {
                        if (!loadMore && !newsAdapterModel.isEndList) {
                            loadMore = true
                            try {
                                isAddMoreProgressBar = true
                            }catch (e : Exception){e.printStackTrace()}
                            newsAdapterModel.isEndList = listenerLoadMore?.invoke(newsAdapterModel.loadedPage+1) == true
                        }
                    }
                }
            })
        }

    }


    val listItems: List<NewsItem>
        get() {
            return newsAdapterModel.items
        }


    @SuppressLint("NotifyDataSetChanged")
    fun addItem(items: List<NewsItem>, replace: Boolean = false, loadedPage: Int) {
        if (replace) {
            newsAdapterModel.items.clear()
        }
        val positionStart = newsAdapterModel.items.size
        newsAdapterModel.loadedPage = loadedPage
        newsAdapterModel.lastAddCount = items.size
        newsAdapterModel.items.addAll(items)
        try {
            if (replace) {
                notifyDataSetChanged()
            } else
                notifyItemRangeInserted(
                    positionStart,
                    items.size
                )
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }
    class ViewHolder(val view: View) : RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.row_news, parent, false)!!
        )


    override fun getItemCount(): Int =
        newsAdapterModel.items.size + if (isAddMoreProgressBar) 1 else 0


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val view = holder.view
        if (position == newsAdapterModel.items.size && isAddMoreProgressBar) {
            view.pb.visibility = View.VISIBLE
            view.layerMain.visibility = View.GONE
            return
        } else {
            view.pb.visibility = View.GONE
            view.layerMain.visibility = View.VISIBLE
        }

        val item = newsAdapterModel.items[position]

        view.tvTitle.text = item.title
        view.tvDescription.text = item.description
        view.tvDate.text = SimpleDateFormat("yyyy-MM-dd EEE HH:mm").format(SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").parse(item.publishedAt))

        Glide
            .with(view.context)
            .load(item.urlToImage)
            .centerCrop()
            .error(R.drawable.ic_baseline_broken_image_24)
            .into(view.img)

        holder.view.layerContent.setOnClickListener {
            listItemClickListener?.invoke(holder.view, position)
        }
    }

    fun setOnItemClickListener(listener: (view: View, position: Int) -> Unit) {
        this.listItemClickListener = listener
    }

    class NewsAdapterModel(
        var items: ArrayList<NewsItem>,
        var isEndList: Boolean,
        var lastAddCount: Int,
        var loadMore: Boolean,
        var loadedPage : Int,
        var isAddMoreProgressBar: Boolean,
        var firstItemPosition: Int
    ) : Serializable

    fun onSaveState(): NewsAdapterModel {
        return newsAdapterModel
    }

    @SuppressLint("NotifyDataSetChanged")
    fun onRestoreState(newsAdapterModel: NewsAdapterModel?) {
        this.newsAdapterModel = newsAdapterModel?:return
        notifyDataSetChanged()
    }

    companion object {
        private const val TAG = "NewsAdapter"
    }
}
