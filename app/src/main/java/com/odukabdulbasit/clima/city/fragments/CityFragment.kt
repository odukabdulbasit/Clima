package com.odukabdulbasit.clima.city.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import com.odukabdulbasit.clima.R
import com.odukabdulbasit.clima.databinding.FragmentCityBinding
import com.odukabdulbasit.clima.location.fragments.LocationFragment

class CityFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_city, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val view = FragmentCityBinding.bind(view)


        view.tvGetWeather.setOnClickListener {
            findNavController().navigate(R.id.openLocationFragment,
            bundleOf(LocationFragment.GET_CITY_NAME to view.etCityName.text.toString()))
        }
    }

}