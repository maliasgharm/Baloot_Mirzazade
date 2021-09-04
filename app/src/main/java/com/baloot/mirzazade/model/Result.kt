package com.baloot.mirzazade.model

import com.baloot.mirzazade.db.ResponseDB
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName

class Result {

    @SerializedName("status")
    var status: String = ""

    @SerializedName("totalResults")
    var totalResults: Int = 0

    @SerializedName("articles")
    var articles: List<NewsResponse> = ArrayList()

    constructor(
        status: String = "",
        totalResults: Int = 0,
        articles: List<NewsResponse> = ArrayList()
    ) {
        this.articles = articles
        this.totalResults = totalResults
        this.status = status
    }

    constructor(responseDB: ResponseDB) {
        val result = Gson().fromJson(responseDB.response, Result::class.java)
        this.status = result.status
        this.totalResults = result.totalResults
        this.articles = result.articles
    }
}