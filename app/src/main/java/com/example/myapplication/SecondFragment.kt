package com.example.myapplication

import android.media.MediaPlayer
import android.os.Bundle
import android.os.CountDownTimer
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CalendarView
import androidx.navigation.fragment.findNavController
import com.example.myapplication.databinding.FragmentSecondBinding

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class SecondFragment : Fragment() {

    private var _binding: FragmentSecondBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    lateinit var mediaPlayer : MediaPlayer


    val timer1 = object : CountDownTimer(30000, 20) {

        override fun onTick(millisUntilFinished: Long) {

            binding.calendarView.rotationX = millisUntilFinished.toFloat() / 10
            binding.calendarView.rotationY = millisUntilFinished.toFloat() / 14
        }
        override fun onFinish() {
            binding.calendarView.rotationX = 0.0F
            binding.calendarView.rotationY = 0.0F
            binding.calendarView.setBackgroundResource(0)
            mediaPlayer.pause()
        }
    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentSecondBinding.inflate(inflater, container, false)
        mediaPlayer = MediaPlayer.create(context, R.raw.calendar)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonSecond.setOnClickListener {
            findNavController().navigate(R.id.action_SecondFragment_to_FirstFragment)
        }

        //binding.textView2.text = Date(binding.calendarView.date).month.toString()
        binding.calendarView.setOnDateChangeListener(object : CalendarView.OnDateChangeListener {
            override fun onSelectedDayChange(view: CalendarView, year: Int, month: Int, dayOfMonth: Int) {
                binding.textView2.text = "$dayOfMonth : ${month + 1} : $year"

                if(dayOfMonth == 3 && month == 8) {
                    binding.textView2.text = "Я календарь"
                    binding.calendarView.setBackgroundResource(R.drawable.mikhail_shufutinsky_small)
                    timer1.start()
                    mediaPlayer.seekTo(0)
                    mediaPlayer.start()
                }
                else {
                    binding.calendarView.rotationX = 0.0F
                    binding.calendarView.rotationY = 0.0F
                    binding.calendarView.setBackgroundResource(0)
                    timer1.cancel()
                    mediaPlayer.pause()
                }
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        timer1.cancel()
        mediaPlayer.stop()
        mediaPlayer.release()
    }
}