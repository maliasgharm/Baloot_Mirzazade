package com.baloot.mirzazade.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.baloot.mirzazade.model.Result

@Entity(tableName = "response")
class ResponseDB(
    @ColumnInfo val page: Int,
    @ColumnInfo val response: String,
    @ColumnInfo(name = "reg_date") val regDate : Long ,
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
) {
    val result get() = Result(this)
}