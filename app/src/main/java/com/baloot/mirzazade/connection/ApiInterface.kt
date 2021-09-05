package com.baloot.mirzazade.connection

import retrofit2.http.GET
import retrofit2.Call
import com.baloot.mirzazade.model.Result
import retrofit2.http.Query


interface ApiInterface {
    //    https://newsapi.org/v2/everything?     q=Persian&    from=2021-09-03T08:00:00Z   &sortBy=popularity    &apiKey=9732dedabcea49039073ebe3d80eedc5
    @GET("everything")
    fun getNews(
        @Query("q") q: String = "Iran",
        @Query("page") page: Int,
        @Query("sortBy") sort: String = "popularity",
//        @Query("apiKey") apiKey: String = "9732dedabcea49039073ebe3d80eedc5"
        @Query("apiKey") apiKey: String = "eb44a5c2fd6e4ffcbb4f5270a3d1125e"
    ): Call<Result?>?
}