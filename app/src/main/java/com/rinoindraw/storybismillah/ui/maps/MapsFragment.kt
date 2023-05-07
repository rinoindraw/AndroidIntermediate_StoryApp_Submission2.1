package com.rinoindraw.storybismillah.ui.maps

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Color
import androidx.fragment.app.Fragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.paging.ExperimentalPagingApi
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.rinoindraw.storybismillah.R
import com.rinoindraw.storybismillah.databinding.FragmentMapsBinding
import com.rinoindraw.storybismillah.utils.SessionManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@Suppress("DEPRECATION")
@ExperimentalPagingApi
@AndroidEntryPoint
class MapsFragment : Fragment() {

    private var _binding: FragmentMapsBinding? = null
    private val binding get() = _binding!!

    private lateinit var mMap: GoogleMap
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private val mapsViewModel: MapsViewModel by viewModels()

    private lateinit var pref: SessionManager
    private lateinit var token: String

    private var mapType: Int = GoogleMap.MAP_TYPE_NORMAL

    private val callback = OnMapReadyCallback { googleMap ->

        mMap = googleMap

        mMap.uiSettings.isZoomControlsEnabled = true
        mMap.uiSettings.isCompassEnabled = true
        mMap.uiSettings.isMapToolbarEnabled = true
        mMap.uiSettings.isIndoorLevelPickerEnabled = true

        val sharedPreferences = activity?.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        mapType = sharedPreferences?.getInt("MapType", GoogleMap.MAP_TYPE_NORMAL) ?: GoogleMap.MAP_TYPE_NORMAL // use default map type if value is not found in SharedPreferences// use default map type if value is not found in SharedPreferences

        mMap.mapType = mapType


        getDeviceStoryLocation()
        initMarkStoryLocation()

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMapsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = false
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)

        pref = SessionManager(requireContext())
        token = pref.fetchAuthToken().toString()

        initAction()

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())

    }


    private fun initAction() {
        binding.apply {
            btnAccount.setOnClickListener(
                Navigation.createNavigateOnClickListener(R.id.action_mapsFragment_to_profileFragment)
            )
            btnSatellite.setOnClickListener {
                mMap.mapType = GoogleMap.MAP_TYPE_SATELLITE
                mapType = GoogleMap.MAP_TYPE_SATELLITE
                val sharedPreferences = activity?.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
                sharedPreferences?.edit()?.putInt("MapType", mapType)?.apply()
            }
            btnNormal.setOnClickListener {
                mMap.mapType = GoogleMap.MAP_TYPE_NORMAL
                mapType = GoogleMap.MAP_TYPE_NORMAL

                val sharedPreferences = activity?.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
                sharedPreferences?.edit()?.putInt("MapType", mapType)?.apply()
            }
        }
    }

    private fun getDeviceStoryLocation() {
        if (ContextCompat.checkSelfPermission(
                requireContext().applicationContext,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            mMap.isMyLocationEnabled = true
            fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                if (location != null) {
                    val latLng = LatLng(location.latitude, location.longitude)
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 8f))
                } else {
                    Toast.makeText(
                        requireContext(),
                        getString(R.string.activate_location),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

        } else {
            requestPermissionLauncher.launch(Manifest.permission.ACCESS_COARSE_LOCATION)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initMarkStoryLocation() {
        viewLifecycleOwner.lifecycleScope.launchWhenResumed {
            launch {
                mapsViewModel.getStoriesLocation(token).collect { result ->
                    result.onSuccess { response ->
                        response.listStory.forEach { story ->

                            if (story.lat != null && story.lon != null) {
                                val latLng = LatLng(story.lat, story.lon)
                                val color = ContextCompat.getColor(requireContext(), R.color.blue_sky)
                                val hsv = FloatArray(3)
                                Color.colorToHSV(color, hsv)
                                val blueSkyMarker = hsv[0]

                                mMap.addMarker(
                                    MarkerOptions()
                                        .position(latLng)
                                        .title(story.name)
                                        .snippet("Lat: ${story.lat}, Lon: ${story.lon}")
                                        .icon(BitmapDescriptorFactory.defaultMarker(blueSkyMarker))
                                )
                            }
                        }
                    }
                }
            }
        }
    }


    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted ->
            if (isGranted) {
                getDeviceStoryLocation()
            }
        }
}
