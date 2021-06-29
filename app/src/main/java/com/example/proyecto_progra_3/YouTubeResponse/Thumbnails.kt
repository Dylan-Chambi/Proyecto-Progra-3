package com.example.proyecto_progra_3.YouTubeResponse

import com.google.gson.annotations.Expose


data class Thumbnails (

   @Expose var default : Default,
   @Expose var medium : Medium,
   @Expose var high : High

)