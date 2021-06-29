package com.example.proyecto_progra_3.YouTubeResponse

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

   
data class PageInfo (

   @Expose var totalResults : Int,
   @Expose var resultsPerPage : Int

)