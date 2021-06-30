package com.example.proyecto_progra_3.googleMapsPlacesResponse

import com.google.gson.annotations.SerializedName
import java.io.Serializable
import java.util.*


class PlacesResults {

    private var htmlAttributions: List<Any> = ArrayList()


    private var nextPageToken: String? = null


    private var results: List<Result> = ArrayList<Result>()


    private var status: String? = null

    fun getHtmlAttributions(): List<Any> {
        return htmlAttributions
    }

    fun setHtmlAttributions(htmlAttributions: List<Any>) {
        this.htmlAttributions = htmlAttributions
    }

    fun getNextPageToken(): String? {
        return nextPageToken
    }

    fun setNextPageToken(nextPageToken: String) {
        this.nextPageToken = nextPageToken
    }

    fun getResults(): List<Result> {
        return results
    }

    fun setResults(results: List<Result>) {
        this.results = results
    }

    fun getStatus(): String? {
        return status
    }

    fun setStatus(status: String) {
        this.status = status
    }
}