package com.example.gomommy.model

import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.ktx.utils.sphericalHeading

import kotlin.math.cos
import kotlin.math.sin

/**
 * A model describing details about a Place (location, name, type, etc.).
 */
data class Place(
    val id: String,
    val icon: String,
    val name: String,
    val geometry: Geometry
) {
    override fun equals(other: Any?): Boolean {
        if (other !is Place) {
            return false
        }
        return this.id == other.id
    }

    override fun hashCode(): Int {
        return this.id.hashCode()
    }
}

data class Geometry(
    val location: GeometryLocation
)

data class GeometryLocation(
    val lat: Double,
    val lng: Double
) {
    val latLng: LatLng
        get() = LatLng(lat, lng)
}