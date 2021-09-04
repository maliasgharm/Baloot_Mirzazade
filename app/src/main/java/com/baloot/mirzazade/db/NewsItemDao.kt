package com.baloot.mirzazade.db

import androidx.room.*
import com.baloot.mirzazade.model.NewsItem
import com.baloot.mirzazade.model.Result
import org.json.JSONObject

@Dao
interface NewsItemDao {
    @Query("SELECT * FROM news_item")
    fun getAll(): List<NewsItemDB>

    @Query("SELECT * FROM news_item WHERE marked = 1 ")
    fun getAllMarkedDB(): List<NewsItemDB>

    @Query("SELECT count(*) FROM news_item WHERE url = (:url) and marked = 1 ")
    fun checkMarkedItem(url:String): Int

    fun getAllMarked():List<NewsItem>{
        return getAllMarkedDB().map { it.getNewsItem() }
    }
    @Insert
    fun insert(newsItem: NewsItemDB)

    @Query("delete FROM news_item WHERE url = (:url) and marked = 1")
    fun delete(url :String )
}