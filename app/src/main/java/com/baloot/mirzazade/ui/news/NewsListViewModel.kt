package com.baloot.mirzazade.ui.news

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class NewsListViewModel : ViewModel() {

    private val _newsAdapterModel = MutableLiveData<MarkedAdapter.NewsAdapterModel?>().apply {
        value = null
    }
    val newsAdapterModel: LiveData<MarkedAdapter.NewsAdapterModel?> = _newsAdapterModel


    fun setModelAdapter(onSaveState: MarkedAdapter.NewsAdapterModel?) {
        _newsAdapterModel.value = onSaveState
    }

    private val _newsAdapterModelMarked = MutableLiveData<MarkedAdapter.NewsAdapterModel?>().apply {
        value = null
    }
    val newsAdapterModelMarked: LiveData<MarkedAdapter.NewsAdapterModel?> = _newsAdapterModelMarked


    fun setModelMarkedAdapter(onSaveState: MarkedAdapter.NewsAdapterModel?) {
        _newsAdapterModelMarked.value = onSaveState
    }
}