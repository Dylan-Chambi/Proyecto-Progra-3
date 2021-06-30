package com.example.proyecto_progra_3.googleMapsPlacesResponse

import com.google.android.libraries.places.api.model.OpeningHours
import com.google.gson.annotations.Expose
import java.util.*

class Result {
    @Expose
    var geometry: Geometry? = null

    @Expose
    var icon: String? = null

    @Expose
    var id: String? = null

    @Expose
    var name: String? = null

    @Expose
    var openingHours: OpeningHours? = null

    @Expose
    var photos: List<Photo> = ArrayList<Photo>()

    @Expose
    var placeId: String? = null

    @Expose
    var rating: Double? = null

    @Expose
    var reference: String? = null

    @Expose
    var scope: String? = null

    @Expose
    var types: List<String> = ArrayList()

    @Expose
    var vicinity: String? = null

    @Expose
    var priceLevel: Int? = null

    @JvmName("getGeometry1")
    fun getGeometry(): Geometry? {
        return geometry
    }

    /**
     * @param geometry The geometry
     */
    @JvmName("setGeometry1")
    fun setGeometry(geometry: Geometry?) {
        this.geometry = geometry
    }

    /**
     * @return The icon
     */
    @JvmName("getIcon1")
    fun getIcon(): String? {
        return icon
    }

    /**
     * @param icon The icon
     */
    @JvmName("setIcon1")
    fun setIcon(icon: String?) {
        this.icon = icon
    }

    /**
     * @return The id
     */
    @JvmName("getId1")
    fun getId(): String? {
        return id
    }

    /**
     * @param id The id
     */
    @JvmName("setId1")
    fun setId(id: String?) {
        this.id = id
    }

    /**
     * @return The name
     */
    @JvmName("getName1")
    fun getName(): String? {
        return name
    }

    /**
     * @param name The name
     */
    @JvmName("setName1")
    fun setName(name: String?) {
        this.name = name
    }

    /**
     * @return The openingHours
     */
    @JvmName("getOpeningHours1")
    fun getOpeningHours(): OpeningHours? {
        return openingHours
    }

    /**
     * @param openingHours The opening_hours
     */
    @JvmName("setOpeningHours1")
    fun setOpeningHours(openingHours: OpeningHours?) {
        this.openingHours = openingHours
    }

    /**
     * @return The photos
     */
    @JvmName("getPhotos1")
    fun getPhotos(): List<Photo?>? {
        return photos
    }

    /**
     * @param photos The photos
     */
    @JvmName("setPhotos1")
    fun setPhotos(photos: List<Photo>) {
        this.photos = photos
    }

    /**
     * @return The placeId
     */
    @JvmName("getPlaceId1")
    fun getPlaceId(): String? {
        return placeId
    }

    /**
     * @param placeId The place_id
     */
    @JvmName("setPlaceId1")
    fun setPlaceId(placeId: String?) {
        this.placeId = placeId
    }

    /**
     * @return The rating
     */
    @JvmName("getRating1")
    fun getRating(): Double? {
        return rating
    }

    /**
     * @param rating The rating
     */
    @JvmName("setRating1")
    fun setRating(rating: Double?) {
        this.rating = rating
    }

    /**
     * @return The reference
     */
    @JvmName("getReference1")
    fun getReference(): String? {
        return reference
    }

    /**
     * @param reference The reference
     */
    @JvmName("setReference1")
    fun setReference(reference: String?) {
        this.reference = reference
    }

    /**
     * @return The scope
     */
    @JvmName("getScope1")
    fun getScope(): String? {
        return scope
    }

    /**
     * @param scope The scope
     */
    @JvmName("setScope1")
    fun setScope(scope: String?) {
        this.scope = scope
    }

    /**
     * @return The types
     */
    @JvmName("getTypes1")
    fun getTypes(): List<String?>? {
        return types
    }

    /**
     * @param types The types
     */
    @JvmName("setTypes1")
    fun setTypes(types: List<String>) {
        this.types = types
    }

    /**
     * @return The vicinity
     */
    @JvmName("getVicinity1")
    fun getVicinity(): String? {
        return vicinity
    }

    /**
     * @param vicinity The vicinity
     */
    @JvmName("setVicinity1")
    fun setVicinity(vicinity: String?) {
        this.vicinity = vicinity
    }

    /**
     * @return The priceLevel
     */
    @JvmName("getPriceLevel1")
    fun getPriceLevel(): Int? {
        return priceLevel
    }

    /**
     * @param priceLevel The price_level
     */
    @JvmName("setPriceLevel1")
    fun setPriceLevel(priceLevel: Int?) {
        this.priceLevel = priceLevel
    }

}