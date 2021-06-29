package com.example.proyecto_progra_3.YouTubeResponse

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

   
data class High (

   @Expose var url : String,
   @Expose var width : Int,
   @Expose var height : Int

)