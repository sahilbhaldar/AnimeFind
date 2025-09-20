package com.example.animefind.ui.anime_list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.example.animefind.R
import com.example.animefind.data.local.model.AnimeEntity
import com.example.animefind.databinding.ItemAnimeBinding
import com.example.animefind.domain.model.Anime

class AnimeListAdapter(
    private val onItemClick: (Anime) -> Unit
) : ListAdapter<Anime, AnimeListAdapter.AnimeViewHolder>(AnimeDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AnimeViewHolder {
        val binding = ItemAnimeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AnimeViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AnimeViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class AnimeViewHolder(private val binding: ItemAnimeBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(anime: Anime) {
            binding.tvTitle.text = anime.title
            binding.tvEpisodes.text = "Episodes: ${anime.episodes ?: "N/A"}"
            binding.tvScore.text = "Score: ${anime.score ?: "N/A"}"

            Glide.with(binding.ivPoster.context)
                .load(anime.imageUrl)
                .placeholder(R.drawable.ic_placeholder)
                .apply(RequestOptions().transform(RoundedCorners(30)))
                .error(R.drawable.ic_placeholder)
                .into(binding.ivPoster)

            binding.root.setOnClickListener {
                onItemClick(anime)
            }
        }
    }
}

class AnimeDiffCallback : DiffUtil.ItemCallback<Anime>() {
    override fun areItemsTheSame(oldItem: Anime, newItem: Anime): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Anime, newItem: Anime): Boolean {
        return oldItem == newItem
    }
}
