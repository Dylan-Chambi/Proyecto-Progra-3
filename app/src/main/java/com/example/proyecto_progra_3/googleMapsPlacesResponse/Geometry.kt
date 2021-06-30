package com.example.proyecto_progra_3.googleMapsPlacesResponse

import com.google.gson.annotations.Expose


data class Geometry (
    @Expose
    var location: Location? = null
)