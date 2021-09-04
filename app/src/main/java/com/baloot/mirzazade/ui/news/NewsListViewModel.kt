package com.baloot.mirzazade.ui.news

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class NewsListViewModel : ViewModel() {

    private val _newsAdapterModel = MutableLiveData<NewsAdapter.NewsAdapterModel?>().apply {
        value = null
    }
    val newsAdapterModel: LiveData<NewsAdapter.NewsAdapterModel?> = _newsAdapterModel


    fun setModelAdapter(onSaveState: NewsAdapter.NewsAdapterModel?) {
        _newsAdapterModel.value = onSaveState
    }
}