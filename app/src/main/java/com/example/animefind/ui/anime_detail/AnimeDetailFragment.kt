package com.example.animefind.ui.anime_detail

import android.os.Bundle
import android.util.Log
import android.util.SparseArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import at.huber.youtubeExtractor.VideoMeta
import at.huber.youtubeExtractor.YouTubeExtractor
import at.huber.youtubeExtractor.YtFile
import com.bumptech.glide.Glide
import com.example.animefind.databinding.FragmentAnimeDetailBinding
import com.example.animefind.domain.util.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.withContext
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class AnimeDetailFragment : Fragment() {

    private lateinit var viewModel: AnimeDetailViewModel
     lateinit var binding: FragmentAnimeDetailBinding
    private var player: ExoPlayer? = null
    private var animeId: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAnimeDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        animeId = arguments?.getInt("animeId") ?: 0
        val factory = AnimeDetailViewModelFactory.provideFactory(requireContext())
        viewModel = ViewModelProvider(this, factory)[AnimeDetailViewModel::class.java]

        observeAnimeDetail()
        viewModel.fetchAnimeDetail(animeId)
    }

    private fun observeAnimeDetail() {
        lifecycleScope.launchWhenStarted {
            viewModel.animeDetail.collectLatest { resource ->
                when (resource) {
                    is Resource.Loading -> {
                        binding.progressBar.visibility = View.VISIBLE
                        binding.playerView.visibility = View.GONE
                        binding.ivPoster.visibility = View.GONE
                    }
                    is Resource.Success -> {
                        binding.progressBar.visibility = View.GONE
                        val anime = resource.data

                        binding.tvTitle.text = anime.title
                        binding.tvGenre.text = "Genre: ${anime.genres ?: "N/A"}"
                        binding.tvCast.text = "Cast: ${anime.mainCast ?: "N/A"}"
                        binding.tvEpisodes.text = "Episodes: ${anime.episodes ?: "N/A"}"
                        binding.tvScore.text = "Rating: ${anime.score ?: "N/A"}"
                        binding.tvSynopsis.text = anime.synopsis

                        playTrailer(anime.trailerUrl, anime.imageUrl)
                    }
                    is Resource.Error -> {
                        binding.progressBar.visibility = View.GONE
                        Toast.makeText(requireContext(), resource.message, Toast.LENGTH_SHORT).show()
                    }
                    else -> {}
                }
            }
        }
    }

    private fun playTrailer(trailerUrl: String?, posterUrl: String?) {
        if (trailerUrl.isNullOrEmpty()) {
            showPoster(posterUrl)
            return
        }
        val videoId = getYoutubeVideoId(trailerUrl)
        if (videoId == null) {
            showPoster(posterUrl)
            return
        }

        val urlToExtract = "https://www.youtube.com/watch?v=$videoId"

        binding.playerView.visibility = View.VISIBLE
        binding.ivPoster.visibility = View.GONE
        player = ExoPlayer.Builder(requireContext()).build()
        binding.playerView.player = player

        // Use coroutine to extract stream URL in background
        lifecycleScope.launchWhenStarted {
            val streamUrl = extractStreamUrl(urlToExtract)
            withContext(Dispatchers.Main) {
                if (streamUrl != null) {
                    val mediaItem = MediaItem.fromUri(streamUrl)
                    player?.setMediaItem(mediaItem)
                    player?.prepare()
                    player?.play()
                } else {
                    showPoster(posterUrl)
                }
            }
        }
    }

    private fun getYoutubeVideoId(url: String): String? {
        // Non-capturing group, matches common YouTube URL patterns
        val regex = "(?:v=|/videos/|embed/|youtu\\.be/|/v/|/e/)([\\w-]{11})".toRegex()
        val match = regex.find(url)
        return match?.groups?.get(1)?.value
    }


    private suspend fun extractStreamUrl(trailerUrl: String): String? {
        return suspendCoroutine { continuation ->
            object : YouTubeExtractor(requireContext()) {
                override fun onExtractionComplete(
                    ytFiles: SparseArray<YtFile>?,
                    videoMeta: VideoMeta?
                ) {
                    if (ytFiles != null && ytFiles.size() > 0) {
                        // Try to get 720p mp4 if available
                        val itag = 22
                        val ytFile = ytFiles[itag] ?: ytFiles.valueAt(0)
                        continuation.resume(ytFile.url)
                    } else {
                        continuation.resume(null)
                    }
                }
            }.extract(trailerUrl, true, true)
        }
    }

    private fun showPoster(imageUrl: String?) {
        binding.playerView.visibility = View.GONE
        binding.ivPoster.visibility = View.VISIBLE
        Glide.with(this)
            .load(imageUrl)
            .placeholder(android.R.color.darker_gray)
            .into(binding.ivPoster)
    }

    override fun onStop() {
        super.onStop()
        player?.release()
        player = null
    }
}
