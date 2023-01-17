package com.odukabdulbasit.clima.location.fragments

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import com.odukabdulbasit.clima.R
import com.odukabdulbasit.clima.databinding.FragmentLocationBinding
import com.odukabdulbasit.clima.location.viewmodel.LocationViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.CancellationToken
import com.google.android.gms.tasks.CancellationTokenSource
import com.google.android.gms.tasks.OnTokenCanceledListener


class LocationFragment : Fragment() {

    private val viewModel = LocationViewModel()
    private var cityName: String? = null

    private lateinit var fusedLocationClient: FusedLocationProviderClient

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_location, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val view = FragmentLocationBinding.bind(view)


        cityName = arguments?.getString(GET_CITY_NAME)

        fusedLocationClient =
            LocationServices.getFusedLocationProviderClient(this.requireActivity())

        getWeather(cityName)

        view.cityButton.setOnClickListener {
            findNavController().navigate(R.id.openCityFragment)
        }

        view.locationButton.setOnClickListener {
            getLocation()
        }

        viewModel.message.observe(this.viewLifecycleOwner, {
            view.messageTextView.text = it
        })

        viewModel.temperature.observe(this.viewLifecycleOwner, {
            view.tempetureTextView.text = it
        })

        viewModel.isUploading.observe(this.viewLifecycleOwner, {
            if (it) {
                view.progressBar.visibility = View.VISIBLE
            } else view.progressBar.visibility = View.GONE

        })
    }

    private fun getWeather(cityName: String?) {
        if (cityName.isNullOrEmpty()) {
            getLocation()
        } else viewModel.getWeatherByCityName(cityName)
    }


    private fun getLocation() {
        if ((ContextCompat.checkSelfPermission(
                this.requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED)
        ) {
            ActivityCompat.requestPermissions(
                this.requireActivity(),
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                locationPermissionCode
            )
        } else {

            fusedLocationClient.getCurrentLocation(
                LocationRequest.PRIORITY_HIGH_ACCURACY,
                object : CancellationToken() {
                    override fun onCanceledRequested(p0: OnTokenCanceledListener) =
                        CancellationTokenSource().token

                    override fun isCancellationRequested() = false
                })
                .addOnSuccessListener { location: Location? ->
                    if (location == null)
                        Toast.makeText(
                            this.requireContext(),
                            "Cannot get location.",
                            Toast.LENGTH_SHORT
                        ).show()
                    else {
                        val lat = location.latitude
                        val lon = location.longitude

                        Log.e(
                            "location result : ",
                            "Latitude: " + location.latitude + " , Longitude: " + location.longitude
                        )

                        viewModel.getMyLocationWeather(lat, lon)
                    }

                }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == locationPermissionCode) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this.requireContext(), "Permission Granted", Toast.LENGTH_SHORT)
                    .show()
                getLocation()
            } else {
                Toast.makeText(this.requireContext(), "Permission Denied", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }


    companion object {
        const val GET_CITY_NAME = "get_city_name"
        const val locationPermissionCode = 1010
    }
}