package com.example.gomommy

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import com.example.gomommy.api.NearbyPlacesResponse
import com.example.gomommy.api.PlacesService
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CircleOptions
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NearestHospitalFragment : Fragment(), OnMapReadyCallback {

    private val tag = "NearestHospitalFragment"
    private val locationPermissionRequestCode = 1
    private var isMapReady = false
    private var nearbyHospitalsCall: Call<NearbyPlacesResponse>? = null

    private lateinit var placesService: PlacesService
    private lateinit var mMap: GoogleMap
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_nearest_hospital, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        placesService = PlacesService.create()
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())

        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        isMapReady = true

        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            mMap.isMyLocationEnabled = true
            getCurrentLocationAndNearbyHospitals()
        } else {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                locationPermissionRequestCode
            )
        }
    }

    private fun getCurrentLocationAndNearbyHospitals() {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // Ensure that the fragment is attached to the activity before requesting permissions
            if (isAdded) {
                ActivityCompat.requestPermissions(
                    requireActivity(),
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    locationPermissionRequestCode
                )
            }
        } else {
            fusedLocationClient.lastLocation
                .addOnSuccessListener { location: Location? ->
                    location?.let {
                        if (!isAdded || !isVisible) {
                            // Fragment is not in a valid state, return early.
                            return@addOnSuccessListener
                        }

                        val currentLatLng = LatLng(location.latitude, location.longitude)
                        mMap.addMarker(
                            MarkerOptions()
                                .position(currentLatLng)
                                .title("My Location")
                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_current_location))
                        )
                        mMap.addCircle(
                            CircleOptions()
                                .center(currentLatLng)
                                .radius(1250.0) // Set the radius in meters (you can adjust this value as needed)
                                .strokeWidth(2f)
                                .strokeColor(
                                    ContextCompat.getColor(
                                        requireContext(),
                                        R.color.gomommy_primary
                                    )
                                )
                                .fillColor(
                                    ContextCompat.getColor(
                                        requireContext(),
                                        R.color.gomommy_secondary_low_op
                                    )
                                )
                        )

                        // Move the camera to show the circle and nearby hospitals
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, DEFAULT_ZOOM))

                        // Call getNearbyHospitals only when the map is ready and the fragment is in the active view lifecycle state
                        if (isMapReady && viewLifecycleOwner.lifecycle.currentState.isAtLeast(
                                Lifecycle.State.STARTED
                            )
                        ) {
                            getNearbyHospitals(location)
                        }
                    }
                }
        }
    }

    private fun getNearbyHospitals(location: Location) {
        val apiKey = getString(R.string.google_maps_key)
        // Cancel any previous ongoing call to prevent concurrent requests.
        nearbyHospitalsCall?.cancel()
        nearbyHospitalsCall = placesService.nearbyPlaces(
            apiKey = apiKey,
            location = "${location.latitude},${location.longitude}",
            radiusInMeters = 2000,
            placeType = "hospital"
        )
        nearbyHospitalsCall?.enqueue(object : Callback<NearbyPlacesResponse> {
            override fun onResponse(
                call: Call<NearbyPlacesResponse>,
                response: Response<NearbyPlacesResponse>
            ) {
                if (response.isSuccessful) {
                    val places = response.body()?.results ?: emptyList()
                    addHospitalMarkers(places)
                } else {
                    Log.e(tag, "Failed to get nearby hospitals")
                }
            }

            override fun onFailure(call: Call<NearbyPlacesResponse>, t: Throwable) {
                Log.e(tag, "Failed to get nearby hospitals", t)
            }
        })
    }

    private fun addHospitalMarkers(places: List<com.example.gomommy.model.Place>) {
        for (place in places) {
            val markerOptions = MarkerOptions()
                .position(place.geometry.location.latLng)
                .title(place.name)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_hospital_location))
            mMap.addMarker(markerOptions)
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        if (requestCode == locationPermissionRequestCode) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, handle the case when permission is granted.
                // For example, call getCurrentLocationAndNearbyHospitals().
                getCurrentLocationAndNearbyHospitals()
            } else {
                // Permission denied, handle the case when permission is denied.
                // For example, show a message or fallback behavior.
                Log.e(tag, "Location permission denied.")
            }
        }
    }

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 100
        private const val DEFAULT_ZOOM = 15f
    }
}