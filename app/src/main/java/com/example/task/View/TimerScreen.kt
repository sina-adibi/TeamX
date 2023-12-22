package com.example.task.View

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.ServiceConnection
import android.os.Build
import android.os.Bundle
import android.os.IBinder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.task.R
import com.example.task.databinding.FragmentTimerScreenBinding

class TimerScreen : Fragment() {
    private lateinit var binding: FragmentTimerScreenBinding

    private var isTimerRunning = false
    private lateinit var statusReceiver: BroadcastReceiver
    private lateinit var timeReceiver: BroadcastReceiver

    private var isResetCheck = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTimerScreenBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnStart.setOnClickListener {
            if (isTimerRunning) pauseTimer() else startTimer()
        }

        binding.btnReset.setOnClickListener {
            resetTimer()
        }
    }

    override fun onStart() {
        super.onStart()
        moveToBackground()
    }

    override fun onResume() {
        super.onResume()

        getTimerStatus()

        val statusFilter = IntentFilter().apply {
            addAction(TimerService.TIMER_STATUS)
        }
        statusReceiver = object : BroadcastReceiver() {
            @SuppressLint("SetTextI18n")
            override fun onReceive(context: Context?, intent: Intent?) {
                val isRunning = intent?.getBooleanExtra(TimerService.IS_TIMER_RUNNING, false)!!
                isTimerRunning = isRunning

                val savedTimeElapsed = TimerService.sharedPreferences.getInt("TIME_ELAPSED", 0)

                updateLayout(isTimerRunning)
                updateTimerLayout(savedTimeElapsed)
            }
        }
        requireActivity().registerReceiver(statusReceiver, statusFilter)

        val timeFilter = IntentFilter().apply {
            addAction(TimerService.TIMER_TICK)
        }
        timeReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                val savedTimeElapsed = TimerService.sharedPreferences.getInt("TIME_ELAPSED", 0)

                updateTimerLayout(savedTimeElapsed)
            }
        }
        requireActivity().registerReceiver(timeReceiver, timeFilter)
    }

    override fun onPause() {
        super.onPause()

        requireActivity().unregisterReceiver(statusReceiver)
        requireActivity().unregisterReceiver(timeReceiver)

        moveToForeground()
    }

    override fun onDestroyView() {
        super.onDestroyView()

        // Clean up any references or resources here
    }

    @SuppressLint("SetTextI18n")
    private fun updateTimerLayout(timeElapsed: Int) {
        val hours = timeElapsed % 86400 / 3600
        val minutes = timeElapsed % 86400 % 3600 / 60
        val seconds = timeElapsed % 86400 % 3600 % 60
        binding.tvTimer.text = String.format("%02d:%02d:%02d", hours, minutes, seconds)
    }

    private fun updateLayout(isTimerRunning: Boolean) {
        if (isTimerRunning) {
            binding.btnStart.icon =
                ContextCompat.getDrawable(requireContext(), R.drawable.ic_pause)
            binding.btnReset.visibility = View.INVISIBLE
        } else {
            binding.btnStart.icon =
                ContextCompat.getDrawable(requireContext(), R.drawable.ic_play)
            binding.btnReset.visibility = View.VISIBLE
        }
    }

    private fun getTimerStatus() {
        val timerService = Intent(requireContext(), TimerService::class.java)
        timerService.putExtra(TimerService.TIMER_ACTION, TimerService.GET_STATUS)
        requireActivity().startService(timerService)
    }

    private fun startTimer() {
        isResetCheck = false
        val timerService = Intent(requireContext(), TimerService::class.java)
        timerService.putExtra(TimerService.TIMER_ACTION, TimerService.START)
        requireActivity().startService(timerService)
    }

    private fun pauseTimer() {
        isResetCheck = false
        val timerService = Intent(requireContext(), TimerService::class.java)
        timerService.putExtra(TimerService.TIMER_ACTION, TimerService.PAUSE)
        requireActivity().startService(timerService)
    }

    private fun resetTimer() {
        isResetCheck = true
        val timerService = Intent(requireContext(), TimerService::class.java)
        timerService.putExtra(TimerService.TIMER_ACTION, TimerService.RESET)
        requireActivity().startService(timerService)
    }

    private fun moveToForeground() {
        if (!isResetCheck) {
            val timerService = Intent(requireContext(), TimerService::class.java)
            timerService.putExtra(TimerService.TIMER_ACTION, TimerService.MOVE_TO_FOREGROUND)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                requireActivity().startForegroundService(timerService)
            } else {
                requireActivity().startService(timerService)
            }
        }
    }

    private fun moveToBackground() {
        val timerService = Intent(requireContext(), TimerService::class.java)
        timerService.putExtra(TimerService.TIMER_ACTION, TimerService.MOVE_TO_BACKGROUND)
        requireActivity().startService(timerService)
    }
}

