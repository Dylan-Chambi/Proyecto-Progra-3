package com.example.proyecto_progra_3.YouTubeResponse

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

   
data class Id (

   @Expose var kind : String,
   @Expose var videoId : String

)