package com.baloot.mirzazade.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.baloot.mirzazade.model.NewsItem
import com.baloot.mirzazade.model.Source
import com.google.gson.Gson
import javax.annotation.Generated
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.components.SingletonComponent
import java.io.Serializable


@InstallIn(SingletonComponent::class)
@Module
@Entity(tableName = "news_item")
class NewsItemDB(
    @ColumnInfo
    var source: String? = null,
    @ColumnInfo
    var author: String? = null,
    @ColumnInfo
    var title: String? = null,
    @ColumnInfo
    var description: String? = null,
    @ColumnInfo
    var url: String? = null,
    @ColumnInfo
    var urlToImage: String? = null,
    @ColumnInfo
    var publishedAt: String? = null,
    @ColumnInfo
    var content: String? = null,
    @ColumnInfo
    var marked: Int = 0,
    @PrimaryKey(autoGenerate = true) val id: Long = 0
) {
    fun getNewsItem(): NewsItem {
        val newsItem =  NewsItem()
        newsItem.content = content
        newsItem.description = description
        newsItem.author = author
        newsItem.publishedAt = publishedAt
        newsItem.source = Gson().fromJson(source,Source::class.java)
        newsItem.title = title
        newsItem.url = url
        newsItem.urlToImage = urlToImage
        newsItem.marked = marked
        return newsItem
    }
}