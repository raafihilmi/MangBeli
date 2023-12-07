package com.capstone.mangbeli.ui.profile

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.capstone.mangbeli.R
import com.capstone.mangbeli.databinding.FragmentProfileBinding
import com.capstone.mangbeli.utils.LocationHelper
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class ProfileFragment : Fragment(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private var currentLat: Float? = null
    private var currentLog: Float? = null
    private val profileViewModel by viewModels<ProfileViewModel> {
        ProfileViewModelFactory.getInstance(requireActivity())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {

        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.tvNameUser
        profileViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())

        initMap()


        return root
    }


    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        mMap.uiSettings.isZoomControlsEnabled = true
        mMap.uiSettings.isMapToolbarEnabled = true
        mMap.uiSettings.isCompassEnabled = true

        profileViewModel.currentLocation.observe(viewLifecycleOwner) {
            updateMapLocation(it.first, it.second)
        }

    }

    private fun updateMapLocation(latitude: Float, longitude: Float) {
        val currentLocation = LatLng(latitude.toDouble(), longitude.toDouble())
        mMap.addMarker(
            MarkerOptions().position(currentLocation).title(binding.tvNameUser.text.toString())
        )
        Log.d("Profile", "onMapReady: $currentLat, $currentLog")
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 18f))
    }

    private fun onPermissionDenied() {
        Toast.makeText(
            requireContext(), "Location permission denied", Toast.LENGTH_SHORT
        ).show()
    }

    private fun initMap() {
        // Initialize map
        val mapFragment =
            childFragmentManager.findFragmentById(R.id.google_map_profile) as SupportMapFragment
        mapFragment.getMapAsync(this)

        // Request location permissions and get user location
        LocationHelper.requestLocationPermissions(this, {
            getUserLocation()
        }, {
            onPermissionDenied()
        })
    }

    private fun getUserLocation() {
        LocationHelper.getLastKnownLocation(fusedLocationClient, { location ->
            currentLat = location.latitude.toFloat()
            currentLog = location.longitude.toFloat()

            currentLat?.let { lat ->
                currentLog?.let { log ->
                    profileViewModel.updateCurrentLocation(lat, log)
                }
            }
        }, {
            Toast.makeText(
                requireContext(), "Location not available, please try again", Toast.LENGTH_SHORT
            ).show()
        })
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}