package com.d101.presentation.music

import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.media.MediaPlayer
import android.os.Binder
import android.os.IBinder
import android.util.Log
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.d101.presentation.R
import java.lang.reflect.Field

class BackgroundMusicService : Service() {

    private var mediaPlayer: MediaPlayer? = null
    lateinit var musicList: List<String>
    private val prefix = "music_"
    private val prefixPattern = Regex("^$prefix")

    private lateinit var lastMusicName: String

    private val binder = MusicBinder()

    inner class MusicBinder : Binder() {
        fun getService(): BackgroundMusicService = this@BackgroundMusicService
    }

    private val musicReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if (intent?.action == "BACKGROUND") {
                mediaPlayer?.pause()
            } else if (intent?.action == "FOREGROUND") {
                mediaPlayer?.start()
            }
        }
    }

    override fun onCreate() {
        super.onCreate()
        initMusicList()
        val filter = IntentFilter().apply {
            addAction("FOREGROUND")
            addAction("BACKGROUND")
        }
        LocalBroadcastManager.getInstance(this).registerReceiver(
            musicReceiver,
            filter,
        )
    }

    override fun onBind(intent: Intent): IBinder {
        return binder
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        when (intent.action) {
            PLAY -> {
                var musicName = intent.getStringExtra(MUSIC_NAME)
                if (musicName.isNullOrEmpty()) {
                    musicName = musicList.first()
                }
                playMusic(musicName)
            }

            STOP -> stopMusic()
            PAUSE -> mediaPlayer?.pause()
            START -> mediaPlayer?.start()
        }
        return START_STICKY
    }

    private fun playMusic(musicName: String) {
        if (mediaPlayer?.isPlaying == true && lastMusicName == musicName) {
            return
        }

        try {
            val musicRes = R.raw::class.java.getField(prefix + musicName).getInt(null)

            mediaPlayer?.stop()
            mediaPlayer?.release()
            mediaPlayer = MediaPlayer.create(
                this,
                musicRes,
            )
            lastMusicName = musicName
            mediaPlayer?.isLooping = true
            mediaPlayer?.start()
        } catch (e: Exception) {
            Log.d("Can't play music", "$e")
        }
    }

    private fun stopMusic() {
        mediaPlayer?.stop()
        mediaPlayer?.release()
        mediaPlayer = null
    }

    private fun initMusicList() {
        val fields: Array<Field> = R.raw::class.java.fields
        musicList = fields.mapNotNull { field ->
            try {
                if (field.name.startsWith(prefix)) {
                    prefixPattern.replace(field.name, "")
                } else {
                    null
                }
            } catch (e: Exception) {
                null
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        stopMusic()
        LocalBroadcastManager.getInstance(this).unregisterReceiver(musicReceiver)
    }

    companion object {
        const val PLAY = "PLAY"
        const val STOP = "STOP"
        const val PAUSE = "PAUSE"
        const val START = "START"
        const val MUSIC_NAME = "MUSIC_NAME"
    }
}
