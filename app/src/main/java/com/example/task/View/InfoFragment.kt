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
import com.example.task.utils.CustomDialog

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
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
//                val isEmpty = phone.text.isBlank() && selfphone.text.isBlank()
                val selfPhoneText = selfphone.text.toString()
                val isSelfPhoneValid = selfPhoneText.startsWith("09") && selfPhoneText.length == 4

                binding.EtName.isEnabled = isSelfPhoneValid
                binding.spinnerCar.isEnabled = isSelfPhoneValid
                binding.EtName.isEnabled = isSelfPhoneValid
                binding.EtInfo.isEnabled = isSelfPhoneValid
                binding.EtAddress.isEnabled = isSelfPhoneValid
                binding.EtAddress2.isEnabled = isSelfPhoneValid
                binding.EtOffer.isEnabled = isSelfPhoneValid
                binding.spinnerCar.isEnabled = isSelfPhoneValid
                binding.spinnerCity.isEnabled = isSelfPhoneValid
                binding.spinnerCarType.isEnabled = isSelfPhoneValid
                binding.spinnerService.isEnabled = isSelfPhoneValid
                binding.spinnerCountService.isEnabled = isSelfPhoneValid
                binding.spinnerStop.isEnabled = isSelfPhoneValid
            }
        }

        phone.addTextChangedListener(textWatcher)
        selfphone.addTextChangedListener(textWatcher)


        binding.BtnAddress1.setOnClickListener {
            val addresses1 = listOf("Address 1", "Address 2", "Address 3")
            val dialog = CustomDialog.newInstance(addresses1, binding.EtAddress, binding.EtAddress2, true)
            dialog.show(childFragmentManager, "MyCustomFragment")
        }

        binding.BtnAddress2.setOnClickListener {
            val addresses2 = listOf("Address 4", "Address 5", "Address 6")
            val dialog = CustomDialog.newInstance(addresses2, binding.EtAddress, binding.EtAddress2,false)
            dialog.show(childFragmentManager, "MyCustomFragment")
        }




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
    fun onLogin(view: View) {
        CustomDialog().show(childFragmentManager, "MyCustomFragment")
    }
}