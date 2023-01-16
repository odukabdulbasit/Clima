package com.odukabdulbasit.clima.location.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.odukabdulbasit.clima.R
import com.odukabdulbasit.clima.databinding.FragmentLocationBinding
import com.odukabdulbasit.clima.location.viewmodel.LocationViewModel


class LocationFragment : Fragment() {

    private val viewModel = LocationViewModel()
    private var cityName: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_location, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val view = FragmentLocationBinding.bind(view)

        cityName = arguments?.getString(GET_CITY_NAME)

        getWeather(cityName)

        view.cityButton.setOnClickListener {
            viewModel.getWeatherByCityName(cityName)
            findNavController().navigate(R.id.openCityFragment)
        }

        view.locationButton.setOnClickListener {
            viewModel.getMyLocationWeather()
        }

        viewModel.message.observe(this.viewLifecycleOwner, {
            view.messageTextView.text = it
        })

        viewModel.tempature.observe(this.viewLifecycleOwner, {
            view.tempetureTextView.text = it
        })
    }

    private fun getWeather(cityName : String?) {
        if (cityName.isNullOrEmpty()){
            viewModel.getMyLocationWeather()
        }else viewModel.getWeatherByCityName(cityName)
    }

    companion object {
        const val GET_CITY_NAME = "get_city_name"
    }

}