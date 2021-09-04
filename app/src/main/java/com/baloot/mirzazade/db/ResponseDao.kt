package com.baloot.mirzazade.db

import androidx.room.*
import com.baloot.mirzazade.model.Result
import org.json.JSONObject

@Dao
interface ResponseDao {
    @Query("SELECT * FROM response")
    fun getAll(): List<ResponseDB>

    @Query("SELECT * FROM response WHERE page IN (:page) order by reg_date ")
    fun loadAllByPageNumber(page: Int): List<ResponseDB>

    @Insert
    fun insert(request: ResponseDB)

    fun insertAll(page: Int, result: String) {
        removeOldRequest()
        insert(ResponseDB(page, result, System.currentTimeMillis()))
    }

    @Query("DELETE FROM response where id NOT IN (SELECT id from response ORDER BY id DESC LIMIT $CATCH_MAX_COUNT) ")
    fun removeOldRequest()

    fun loadAllByPageNumberResult(page: Int): Result? {
        return loadAllByPageNumber(page).firstOrNull()?.result
    }

    companion object {
        const val CATCH_MAX_COUNT = 5  // 50 item
    }
}