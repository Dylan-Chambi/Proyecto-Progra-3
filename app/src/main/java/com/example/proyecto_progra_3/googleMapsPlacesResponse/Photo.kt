package com.example.proyecto_progra_3.googleMapsPlacesResponse


import com.google.gson.annotations.Expose
import java.io.Serializable
import java.util.*


data class Photo(

    @Expose
    var height: Int? = null,

    @Expose
    var htmlAttributions: List<String> = ArrayList(),

    @Expose
    var photoReference: String? = null,

    @Expose
    var width: Int? = null,
)