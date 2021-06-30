package com.example.proyecto_progra_3

import com.example.proyecto_progra_3.YouTubeResponse.Example
import com.example.proyecto_progra_3.googleMapsPlacesResponse.PlacesResults
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query


object APIClient {
    private var retrofitYoutube: Retrofit? = null
    private var retrofitMedicalCenters: Retrofit? = null
    private val BASE_URL = "https://youtube.googleapis.com"
    private val BASE_URL_PLACES = "https://maps.googleapis.com/maps/api/"

    fun getClientYoutube(): YoutubeAPI? {
        if (retrofitYoutube == null) {
            retrofitYoutube = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }
        return retrofitYoutube?.create(YoutubeAPI::class.java)
    }

    fun getClientMedicalCenters(): GoogleMapsAPI?{
        if(retrofitMedicalCenters == null) {
            retrofitMedicalCenters = Retrofit.Builder()
                .baseUrl(BASE_URL_PLACES)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }
        return retrofitMedicalCenters?.create(GoogleMapsAPI::class.java)
    }

}

interface YoutubeAPI{
    @GET("/youtube/v3/search")

    fun obtenerVideos(@Query("part") part: String,
                      @Query("q") q: String,
                      @Query("maxResults") max: Int,
                      @Query("key") key:String): Call<Example>
}

interface  GoogleMapsAPI{
    @GET("place/nearbysearch/json")
    fun getNearBy(
        @Query("location") location: String,
        @Query("rankBy") rankBy: String,
        @Query("radius") radius: Int,
        @Query("type") type: String,
        @Query("keyword") keyword: String,
        @Query("key") key: String): Call<PlacesResults>
}