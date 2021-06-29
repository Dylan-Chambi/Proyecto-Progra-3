package com.example.proyecto_progra_3

import com.example.proyecto_progra_3.YouTubeResponse.Example
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.Query


object YoutubeClient {
    private var retrofit: Retrofit? = null
    private val BASE_URL = "https://youtube.googleapis.com"

    fun getClient(): youtubeAPI? {
        if (retrofit == null) {
            retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }
        return retrofit?.create(youtubeAPI::class.java)
    }


}

interface youtubeAPI{
//    @Headers("Authorization: Bearer AIzaSyAprtyzLqPPeIJprReQb4vkin1oAoS6vl8")
    @GET("/youtube/v3/search")

    fun obtenerVideos(@Query("part") part: String,
                      @Query("q") q: String,
                      @Query("maxResults") max: Int,
                      @Query("key") key:String): Call<Example>

}