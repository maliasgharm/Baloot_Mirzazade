package com.baloot.mirzazade.ui.news

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import androidx.room.Room
import com.baloot.mirzazade.R
import com.baloot.mirzazade.databinding.FragmentNewsBinding
import com.baloot.mirzazade.connection.ApiInterface

import com.baloot.mirzazade.connection.ApiClient.getClient
import com.baloot.mirzazade.db.AppDatabase
import com.baloot.mirzazade.model.Result
import com.baloot.mirzazade.ui.details.DetailsFragment
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.Serializable
import java.util.*

@AndroidEntryPoint
class NewsListFragment : Fragment() {

    private var _binding: FragmentNewsBinding? = null
    private var newsAdapter: MarkedAdapter? = null
    private var db: AppDatabase? = null
    private val model: NewsListViewModel by activityViewModels()
    private var typeList: TypeList = TypeList.NEWS

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNewsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        typeList = when (requireArguments().getInt("type")) {
            2 -> TypeList.MARKED
            else -> TypeList.NEWS
        }
        super.onViewCreated(view, savedInstanceState)
        db = Room.databaseBuilder(
            requireContext(),
            AppDatabase::class.java,
            requireContext().packageName
        ).build()
        newsAdapter = MarkedAdapter(binding.rvMain)

        newsAdapter?.setOnItemClickListener { view, position ->
            val args = Bundle()
            args.putSerializable(
                DetailsFragment.KEY_NEWS_ITEM,
                newsAdapter?.listItems?.getOrNull(position) ?: return@setOnItemClickListener
            )
            NavHostFragment.findNavController(this@NewsListFragment)
                .navigate(R.id.action_details_fragment, args)
        }

        if (typeList == TypeList.MARKED) {
            model.newsAdapterModelMarked.observe(viewLifecycleOwner) {
                newsAdapter?.onRestoreState(it)
            }
            loadMarkedList()
            return
        }
        newsAdapter?.setOnLoadMoreListener {
            requestNews(it)
            false
        }

        model.newsAdapterModel.observe(viewLifecycleOwner) {
            newsAdapter?.onRestoreState(it)
            if (it == null) {
                requestNews()
            }
        }

    }

    private fun loadMarkedList() {
        val handler = Handler(Looper.getMainLooper())
        Thread {
            val list = db?.newsItem()?.getAllMarked() ?: return@Thread
            handler.post {
                newsAdapter?.addItem(list, true, 1)
                newsAdapter?.setIsEnd(true)
                binding.tvError.isVisible = list.isEmpty()
                if (list.isEmpty()) {
                    binding.tvError.text = "Bookmarks is empty"
                }
            }
        }.start()
    }

    override fun onPause() {
        super.onPause()
        if (typeList == TypeList.NEWS)
            model.setModelAdapter(newsAdapter?.onSaveState())
        else
            model.setModelMarkedAdapter(newsAdapter?.onSaveState())
    }

    private fun requestNews(page: Int = 1) {
        val apiService = getClient()!!.create(ApiInterface::class.java)
        val newsResponse = apiService.getNews(page = page)
        val handler = Handler(Looper.getMainLooper())
        if (page == 1 && newsAdapter?.listItems?.size ?: 0 == 0) {
            binding.pb.isVisible = true
        }
        binding.tvError.isVisible = false
        binding.tvError.setOnClickListener(null)
        Thread {
            val cacheData = db?.responseDb()?.loadAllByPageNumberResult(page)
            if (cacheData != null && page == 1) {
                handler.post {
                    callbackRequestNews(page, cacheData)
                }
            }
        }.start()
        newsResponse?.enqueue(object : Callback<Result?> {
            override fun onResponse(call: Call<Result?>?, response: Response<Result?>) {
                if (_binding == null)
                    return
                val result = response.body()
                binding.pb.isVisible = false
                binding.tvError.isVisible = false
                binding.tvError.setOnClickListener(null)
                val status = result?.status
                if (status != "ok") {
                    val errorBody = response.errorBody()
                    val jsonErrorResponse = JSONObject(errorBody?.string() ?: "{}")
                    newsAdapter?.setIsEnd(true)
                    newsAdapter?.setIsEnd(false)
                    if (context != null)
                        Toast.makeText(
                            requireContext(),
                            if (jsonErrorResponse.has("message"))
                                jsonErrorResponse.getString("message") else "message not founded",
                            Toast.LENGTH_LONG
                        ).show()
                    return
                }

                result.let {
                    Thread {
                        if (page == 1) {
                            db?.responseDb()?.insertAll(page, Gson().toJson(response.body()))
                        }
                    }.start()

                    callbackRequestNews(page, it)

                }
            }

            override fun onFailure(call: Call<Result?>?, t: Throwable?) {
                if (_binding == null)
                    return
                binding.pb.isVisible = false
                binding.tvError.isVisible = true
                binding.tvError.setOnClickListener {
                    requestNews(page)
                }
                binding.tvError.text = "Connection failure\nfor try again Click here"
            }
        })
    }

    private fun callbackRequestNews(page: Int, result: Result) {
        binding.pb.isVisible = false
        binding.tvError.isVisible = false
        binding.tvError.setOnClickListener(null)

        newsAdapter?.loaded()
        result.articles.let { newsAdapter?.addItem(it, page == 1, loadedPage = page) }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    enum class TypeList : Serializable {
        NEWS, MARKED
    }

    companion object {
        private const val TAG = "NewsFragment"
    }
}