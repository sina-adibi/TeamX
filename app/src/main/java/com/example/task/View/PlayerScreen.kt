package com.example.task.View

import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.task.databinding.FragmentPlayerBinding
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.ui.PlayerView
import android.app.DownloadManager
import android.content.Context
import android.os.Environment
import java.io.File

class PlayerScreen : Fragment() {
    private val exoPlayer by lazy { SimpleExoPlayer.Builder(requireContext()).build() }
    private lateinit var binding: FragmentPlayerBinding

    private lateinit var downloadManager: DownloadManager
    private var downloadId: Long = -1

    private lateinit var exoPlayer2: SimpleExoPlayer
    private lateinit var playerView2: PlayerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPlayerBinding.inflate(inflater, container, false)
        val view = binding.root

        val audioUri = Uri.parse("http://simotel.transport-x.ir:1884/api/voice/caldX:23V3moshnee2/1704269292.6308518/2024-01-03")
        val mediaItem = MediaItem.fromUri(audioUri)
        exoPlayer.setMediaItem(mediaItem)
        exoPlayer.prepare()

        binding.playerView.player = exoPlayer

        playerView2 = binding.playerView2
        exoPlayer2 = SimpleExoPlayer.Builder(requireContext()).build()

        val downloadedFileUri = Uri.parse(requireContext().getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS)?.path + "/audio_file.mp3")
        val mediaItem2 = MediaItem.fromUri(downloadedFileUri)
        exoPlayer2.setMediaItem(mediaItem2)
        exoPlayer2.prepare()

        playerView2.player = exoPlayer2

        downloadManager = requireContext().getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        startDownload(audioUri)

        return view
    }

    override fun onStart() {
        super.onStart()
        exoPlayer.playWhenReady = true
    }

    override fun onStop() {
        super.onStop()
        exoPlayer.playWhenReady = false
    }

    private fun startDownload(uri: Uri) {
        val downloadedFile = File(requireContext().getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), "audio_file.mp3")
        if (downloadedFile.exists()) {
            downloadedFile.delete()
        }

        val request = DownloadManager.Request(uri)
            .setTitle("Audio Download")
            .setDescription("Downloading")
            .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE)
            .setAllowedOverMetered(true)
            .setAllowedOverRoaming(true)

        request.setDestinationInExternalFilesDir(
            requireContext(),
            Environment.DIRECTORY_DOWNLOADS,
            "audio_file.mp3"
        )
        downloadId = downloadManager.enqueue(request)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        exoPlayer.release()

        if (downloadId != -1L) {
            downloadManager.remove(downloadId)
        }
    }
}
