package com.example.myapplication

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import androidx.navigation.fragment.findNavController
import com.example.myapplication.databinding.FragmentPaintBinding

/**
 * A simple [Fragment] subclass.
 * Use the [PaintFragment.newInstance] factory method to
 * create an instance of this fragment.
 */

class PaintFragment : Fragment() {

    var canvas_height = 0
    var canwas_width = 0

    private var _binding: FragmentPaintBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPaintBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        canvas_height = binding.canvasView.height
        canwas_width = binding.canvasView.width
        Log.d("TAG1", "$canvas_height $canwas_width")

        binding.button1.setOnClickListener {
            findNavController().navigate(R.id.action_paintFragment_to_FirstFragment)
        }
        binding.canvasView.setOnTouchListener {
            _, event ->
            val x = event.x
            val y = event.y
            val width = binding.canvasView.width
            val height = binding.canvasView.height
            val dir_s = checkPos(width.toFloat(), height.toFloat(), x, y)
            if(dir_s != null) snake.changeDir(dir_s)
            true
        }
        val func123 = { i: Int, _: (Int) -> Int ->
            val a = i + 1523
            a
        }

        func123(123) {it + 5}
        func123(123, func1234)
        func1234(123)
    }

    private val func1234 =
    {
        i : Int ->
        i + 1523
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    private fun checkPos(width : Float, height : Float, in_x : Float, in_y : Float) : Dirs? {
        val k1 = (0 - width) / (0 - height)
        val k2 = (width - 0) / (0 - height)
        val b1 = 0
        val b2 = width
        val sol1 = k1 * in_y + b1
        val sol2 = k2 * in_y + b2
        return when {
            in_x > sol1 && in_x > sol2 -> Dirs.RIGHT
            in_x > sol1 && in_x < sol2 -> Dirs.UP
            in_x < sol1 && in_x < sol2 -> Dirs.LEFT
            in_x < sol1 && in_x > sol2 -> Dirs.DOWN
            else -> null
        }
    }
}

enum class Dirs{
    UP, DOWN, RIGHT, LEFT
}