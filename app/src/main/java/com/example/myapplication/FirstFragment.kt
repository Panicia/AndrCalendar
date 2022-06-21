package com.example.myapplication

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.navigation.fragment.findNavController
import com.example.myapplication.databinding.FragmentFirstBinding
import com.google.android.material.slider.Slider

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class FirstFragment : Fragment() {

    private var _binding: FragmentFirstBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentFirstBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonFirst.setOnClickListener {
            findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)
        }
        binding.button1.setOnClickListener {
            Log.d("TAG1", "Button 1 has been touched")
            binding.textView.text = howMuch(binding.editTextNumberSigned.text?.toString() ?: "0")
        }
        binding.button2.setOnClickListener {
            Log.d("TAG1", "Button 2 has been touched")
        }
        binding.seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                Log.d("Tag1", "SeekBar 1 has been changed")
                binding.textviewFirst.text = progress.toString()
                binding.textView.text = howMuch(progress.toString())
            }
            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }



    private fun howMuch(x:String? = null):String {
        val a:Int
        a = if(x == "") 0
        else x?.toInt() ?: 0
        return when {
            a < 0 -> "subzero"
            a == 0 -> "zero"
            a == 1 || a == 2 -> "couple"
            a in 3..12 -> "several"
            a in 13..80 -> "many"
            a in 80..1000 -> "a lot"
            else -> "very much"
        }
    }
}