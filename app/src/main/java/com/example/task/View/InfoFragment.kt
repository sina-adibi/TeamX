package com.example.task.View

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.example.task.R
import com.example.task.databinding.FragmentInfoBinding

class InfoFragment : Fragment() {
    private lateinit var binding: FragmentInfoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentInfoBinding.inflate(inflater, container, false)
        val view = binding.root


        val cityArray = resources.getStringArray(R.array.spinner_city)
        val adapter =
            ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, cityArray)
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


        val phone = binding.EtPhone
        val selfphone = binding.EtSelfPhone


        val textWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                binding.spinnerCar.setEnabled(false);
                binding.spinnerCity.setEnabled(false);
                binding.spinnerCarType.setEnabled(false);
                binding.spinnerService.setEnabled(false);
                binding.spinnerCountService.setEnabled(false);
                binding.spinnerStop.setEnabled(false);
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                val isEmpty = phone.text.isNullOrEmpty() && selfphone.text.isNullOrEmpty()
                val selfPhoneText = selfphone.text.toString()
                val isSelfPhoneValid = selfPhoneText.startsWith("09") && selfPhoneText.length == 4

                binding.EtName.isEnabled = !isEmpty && isSelfPhoneValid
                binding.spinnerCar.isEnabled = !isEmpty && isSelfPhoneValid
                binding.EtName.isEnabled = !isEmpty && isSelfPhoneValid
                binding.EtInfo.isEnabled = !isEmpty && isSelfPhoneValid
                binding.EtAddress.isEnabled = !isEmpty && isSelfPhoneValid
                binding.EtAddress2.isEnabled = !isEmpty && isSelfPhoneValid
                binding.EtOffer.isEnabled = !isEmpty && isSelfPhoneValid
                binding.spinnerCar.isEnabled = !isEmpty && isSelfPhoneValid
                binding.spinnerCity.isEnabled = !isEmpty && isSelfPhoneValid
                binding.spinnerCarType.isEnabled = !isEmpty && isSelfPhoneValid
                binding.spinnerService.isEnabled = !isEmpty && isSelfPhoneValid
                binding.spinnerCountService.isEnabled = !isEmpty && isSelfPhoneValid
                binding.spinnerStop.isEnabled = !isEmpty && isSelfPhoneValid
            }
        }

        phone.addTextChangedListener(textWatcher)
        selfphone.addTextChangedListener(textWatcher)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.spinnerCity.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val selectedCity = parent?.getItemAtPosition(position).toString()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }


    }

}

