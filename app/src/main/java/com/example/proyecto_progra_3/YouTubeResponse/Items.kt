package com.example.proyecto_progra_3.YouTubeResponse
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

   
data class Items (

   @Expose var kind : String,
   @Expose var etag : String,
   @Expose var id : Id,
   @Expose var snippet : Snippet

)