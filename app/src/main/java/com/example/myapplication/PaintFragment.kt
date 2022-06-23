package com.example.myapplication

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.myapplication.databinding.FragmentPaintBinding

/**
 * A simple [Fragment] subclass.
 * Use the [PaintFragment.newInstance] factory method to
 * create an instance of this fragment.
 */

public var Dir : Dirs = Dirs.RIGHT
class PaintFragment : Fragment() {

    private var _binding: FragmentPaintBinding? = null
    private val binding get() = _binding!!



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPaintBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.button1.setOnClickListener {
            findNavController().navigate(R.id.action_paintFragment_to_FirstFragment)
        }
        binding.buttonD.setOnClickListener {
            snake.changeDir(Dirs.DOWN)
        }
        binding.buttonL.setOnClickListener {
            snake.changeDir(Dirs.LEFT)
        }
        binding.buttonR.setOnClickListener {
            snake.changeDir(Dirs.RIGHT)
        }
        binding.buttonU.setOnClickListener {
            snake.changeDir(Dirs.UP)
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

enum class Dirs{
    UP, DOWN, RIGHT, LEFT
}