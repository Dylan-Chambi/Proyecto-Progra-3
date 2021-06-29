package com.example.proyecto_progra_3.YouTubeResponse

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

   
data class Example (

   @Expose var kind : String,
   @Expose var etag : String,
   @Expose var nextPageToken : String,
   @Expose var regionCode : String,
   @Expose var pageInfo : PageInfo,
   @Expose var items : List<Items>

)