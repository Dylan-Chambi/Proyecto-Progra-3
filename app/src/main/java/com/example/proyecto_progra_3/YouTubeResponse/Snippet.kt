package com.example.proyecto_progra_3.YouTubeResponse

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

   
data class Snippet (

   @Expose var publishedAt : String,
   @Expose var channelId : String,
   @Expose var title : String,
   @Expose var description : String,
   @Expose var thumbnails : Thumbnails,
   @Expose var channelTitle : String,
   @Expose var liveBroadcastContent : String,
   @Expose var publishTime : String

)