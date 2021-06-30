package com.example.proyecto_progra_3.googleMapsPlacesResponse

import com.google.android.gms.maps.model.LatLng
import com.google.gson.annotations.Expose
import java.io.Serializable

class Location : Serializable {
    @Expose
    var lat: Double? = null

    @Expose
    var lng: Double? = null

    @JvmName("getLat1")
    fun getLat(): Double? {
        return lat
    }

    @JvmName("setLat1")
    fun setLat(lat: Double) {
        this.lat = lat
    }

    @JvmName("getLng1")
    fun getLng(): Double? {
        return lng
    }

    @JvmName("setLng1")
    fun setLng(lng: Double) {
        this.lng = lng
    }
    fun getLatLng(): LatLng{
        return LatLng(this.lat!!, this.lng!!)
    }
}