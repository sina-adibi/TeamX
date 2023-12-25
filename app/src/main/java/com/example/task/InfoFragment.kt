package com.example.task

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.example.task.databinding.FragmentInfoBinding
import com.example.task.databinding.FragmentLoginScreenBinding

class InfoFragment : Fragment() {
    private lateinit var binding: FragmentInfoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentInfoBinding.inflate(inflater, container, false)
        val view = binding.root


        val cityArray = resources.getStringArray(R.array.spinner_city)
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, cityArray)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        val spinnerCity = binding.spinnerCity
        spinnerCity.adapter = adapter

        val spinnerCar = binding.spinnerCar
        spinnerCar.adapter = adapter

        val spinnerCarType = binding.spinnerCarType
        spinnerCarType.adapter = adapter

        val spinnerService = binding.spinnerService
        spinnerService.adapter = adapter

        val spinnerCountService = binding.spinnerCountService
        spinnerCountService.adapter = adapter

        val spinnerStop = binding.spinnerStop
        spinnerStop.adapter = adapter
        
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.spinnerCity.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedCity = parent?.getItemAtPosition(position).toString()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }

    }


}