package com.example.task.View

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Binder
import android.os.Build
import android.os.IBinder
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.example.task.MainActivity
import com.example.task.MainApplication
import com.example.task.R
import java.util.Timer
import java.util.TimerTask

class TimerService : Service() {

    companion object {
        const val CHANNEL_ID = "Timer_Notifications"

        const val START = "START"
        const val PAUSE = "PAUSE"
        const val RESET = "RESET"
        const val GET_STATUS = "GET_STATUS"
        const val MOVE_TO_FOREGROUND = "MOVE_TO_FOREGROUND"
        const val MOVE_TO_BACKGROUND = "MOVE_TO_BACKGROUND"

        const val TIMER_ACTION = "TIMER_ACTION"
        const val TIME_ELAPSED = "TIME_ELAPSED"
        const val IS_TIMER_RUNNING = "IS_TIMER_RUNNING"

        const val TIMER_TICK = "TIMER_TICK"
        const val TIMER_STATUS = "TIMER_STATUS"

        lateinit var sharedPreferences: SharedPreferences

    }
    lateinit var context:Context
    private var timeElapsed: Int = 0
    private var isTimerRunning = false

    private var updateTimer = Timer()
    private var timer = Timer()


    private lateinit var notificationManager: NotificationManager
    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        createChannel()
        getNotificationManager()

        when (intent?.getStringExtra(TIMER_ACTION)!!) {
            START -> startTimer()
            PAUSE -> pauseTimer()
            RESET -> resetTimer()
            GET_STATUS -> sendStatus()
            MOVE_TO_FOREGROUND -> moveToForeground()
            MOVE_TO_BACKGROUND -> moveToBackground()
        }
        return START_STICKY
    }

    override fun onCreate() {
        super.onCreate()
        sharedPreferences = getSharedPreferences("TimerPrefs", Context.MODE_PRIVATE)
    }

    private fun moveToForeground() {
        startForeground(1, buildNotification())

        if (isTimerRunning) {
            updateTimer = Timer()
            updateTimer.scheduleAtFixedRate(
                object : TimerTask() {
                    override fun run() {
                        updateNotification()
                    }
                },
                0,
                1000
            )
        }

    }

    private fun moveToBackground() {
        updateTimer.cancel()
        stopForeground(true)
    }
    private fun startTimer() {
        isTimerRunning = true

        val savedTimeElapsed = sharedPreferences.getInt("TIME_ELAPSED", 0)
        timeElapsed = savedTimeElapsed

            timer = Timer()
            timer.scheduleAtFixedRate(
                object : TimerTask() {
                    override fun run() {
                        val timerIntent = Intent()
                        timerIntent.action = TIMER_TICK

                        timeElapsed++

                        sharedPreferences.edit().putInt("TIME_ELAPSED", timeElapsed).apply()

                        timerIntent.putExtra(TIME_ELAPSED, timeElapsed)
                        sendBroadcast(timerIntent)

                        if (timeElapsed == 60) {
                            val activityIntent = Intent(applicationContext, MainActivity::class.java)
                            activityIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                            startActivity(activityIntent)
                        }
                    }
                },
                0,
                1000
            )
        sendStatus()
    }
    private fun pauseTimer() {
        timer.cancel()
        isTimerRunning = false
        sendStatus()
    }

    private fun resetTimer() {
        pauseTimer()
        timeElapsed = 0

        sharedPreferences.edit().remove("TIME_ELAPSED").apply()

        sendStatus()
    }

    private fun sendStatus() {
        val statusIntent = Intent()
        statusIntent.action = TIMER_STATUS
        statusIntent.putExtra(IS_TIMER_RUNNING, isTimerRunning)
        statusIntent.putExtra(TIME_ELAPSED, timeElapsed)
        sendBroadcast(statusIntent)
    }

    private fun createChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                CHANNEL_ID,
                "TIMER",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationChannel.setSound(null, null)
            notificationChannel.setShowBadge(true)
            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(notificationChannel)
        }
    }

    private fun getNotificationManager() {
        notificationManager = ContextCompat.getSystemService(
            this,
            NotificationManager::class.java
        ) as NotificationManager
    }


    private fun buildNotification(): Notification {
        val title = if (isTimerRunning) {
            "Timer is running!"
        } else {
            "Timer is paused!"
        }


        val savedTimeElapsed = sharedPreferences.getInt("TIME_ELAPSED", -1)


        val hours: Int
        val minutes: Int
        val seconds: Int

        if (savedTimeElapsed != -1) {
            hours = savedTimeElapsed % 86400 / 3600
            minutes = savedTimeElapsed % 86400 % 3600 / 60
            seconds = savedTimeElapsed % 86400 % 3600 % 60
        } else {
            hours = 0
            minutes = 0
            seconds = 0
        }

        val time = String.format("%02d:%02d:%02d", hours, minutes, seconds)

        val intent = Intent(this, TimerScreen::class.java)
        val pIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)

        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle(title)
            .setOngoing(true)
            .setContentText(time)
            .setColorized(true)
            .setColor(Color.parseColor("#c2e1ff"))
            .setSmallIcon(R.drawable.ic_timer)
            .setOnlyAlertOnce(true)
            .setContentIntent(pIntent)
            .setAutoCancel(true)
            .build()
    }

    private fun updateNotification() {
        notificationManager.notify(
            1,
            buildNotification()
        )
    }

    override fun onDestroy() {
        timer.cancel()
        updateTimer.cancel()
        stopForeground(true)
        stopSelf()
        super.onDestroy()
    }

    override fun onTaskRemoved(rootIntent: Intent?) {
        timer.cancel()
        updateTimer.cancel()
        stopForeground(true)
        stopSelf()
        super.onTaskRemoved(rootIntent)
    }
}
