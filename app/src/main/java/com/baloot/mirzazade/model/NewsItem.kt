package com.baloot.mirzazade.model

import com.baloot.mirzazade.db.NewsItemDB
import com.google.gson.Gson
import javax.annotation.Generated
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable


@Generated("jsonschema2pojo")
class NewsItem : Serializable {
    @SerializedName("source")
    @Expose
    var source: Source? = null

    @SerializedName("author")
    @Expose
    var author: String? = null

    @SerializedName("title")
    @Expose
    var title: String? = null

    @SerializedName("description")
    @Expose
    var description: String? = null

    @SerializedName("url")
    @Expose
    var url: String? = null

    @SerializedName("urlToImage")
    @Expose
    var urlToImage: String? = null

    @SerializedName("publishedAt")
    @Expose
    var publishedAt: String? = null

    var content: String? = null

    @SerializedName("marked")
    @Expose
    var marked: Int? = 1


    fun getItemDB(): NewsItemDB {
        return NewsItemDB(
            Gson().toJson(source),
            author,
            title,
            description,
            url,
            urlToImage,
            publishedAt,
            content,
            marked ?: 0,
        )
    }
}