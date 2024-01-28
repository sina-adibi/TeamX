package com.example.task.View

import android.Manifest
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.LocationManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.task.databinding.FragmentMapsBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import android.provider.Settings
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.example.task.R
import java.io.IOException

class MapsScreen : Fragment() {

    private lateinit var googleMap: GoogleMap
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private lateinit var locationManager: LocationManager
    private var locationEnabled = false

    private lateinit var binding: FragmentMapsBinding

    private val callback = OnMapReadyCallback { map ->
        googleMap = map
        enableMyLocation()
    }
    private lateinit var searchEditText: EditText

    private val searchListener = TextView.OnEditorActionListener { _, actionId, _ ->
        if (actionId == EditorInfo.IME_ACTION_SEARCH) {
            val query = searchEditText.text.toString()
            searchLocation(query)
            return@OnEditorActionListener true
        }
        false
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMapsBinding.inflate(inflater, container, false)
        val view = binding.root

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        locationManager = requireActivity().getSystemService(Context.LOCATION_SERVICE) as LocationManager
        checkLocationEnabled()
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(callback)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())

        searchEditText = binding.searchBar
        searchEditText.setOnEditorActionListener(searchListener)

        val addMarkerButton = binding.addMarkerButton
        addMarkerButton.setOnClickListener {
            addCustomMarker()
        }


    }
    private fun addCustomMarker() {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                location?.let {
                    val currentLatLng = LatLng(location.latitude, location.longitude)
                    val markerOptions = MarkerOptions().position(currentLatLng).title("Custom Marker")
                    googleMap.addMarker(markerOptions)
                }
            }
        } else {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION_REQUEST_CODE
            )
        }
    }
    private fun searchLocation(query: String) {
        val geocoder = Geocoder(requireContext())
        try {
            val addresses = geocoder.getFromLocationName(query, 1)
            if (addresses.isNullOrEmpty()) {
                Toast.makeText(requireContext(), "Location not found", Toast.LENGTH_SHORT).show()
                return
            }

            val address = addresses[0]
            val location = LatLng(address?.latitude ?: 0.0, address?.longitude ?: 0.0)
            val markerOptions = MarkerOptions().position(location).title(address?.getAddressLine(0))
            googleMap.addMarker(markerOptions)
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 15f))
        } catch (e: IOException) {
            Toast.makeText(requireContext(), "Error searching location", Toast.LENGTH_SHORT).show()
            e.printStackTrace()
        }
    }    private fun checkLocationEnabled() {
        locationEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
        if (!locationEnabled) {
            showLocationAlertDialog()
        }
    }

    private fun showLocationAlertDialog() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setMessage("Location is disabled")
            .setCancelable(false)
            .setPositiveButton("Yes") { _, _ ->
                startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
            }
            .setNegativeButton("No") { dialog, _ ->
                dialog.dismiss()
            }
        val alert = builder.create()
        alert.show()
    }

    private fun enableMyLocation() {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            googleMap.isMyLocationEnabled = true
            fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                location?.let {
                    val currentLatLng = LatLng(location.latitude, location.longitude)
                    val markerOptions = MarkerOptions().position(currentLatLng).title("My Location")
                    googleMap.addMarker(markerOptions)
                    googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 15f))
                }
            }
        } else {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION_REQUEST_CODE
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                enableMyLocation()
            }
        }
    }


    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 123
    }
}