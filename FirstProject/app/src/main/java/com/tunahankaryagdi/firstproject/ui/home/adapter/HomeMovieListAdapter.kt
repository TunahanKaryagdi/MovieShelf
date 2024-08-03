package com.tunahankaryagdi.firstproject.ui.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.tunahankaryagdi.firstproject.databinding.ItemMovieListBinding
import com.tunahankaryagdi.firstproject.domain.model.PopularMovie

class HomeMovieListAdapter() :
    PagingDataAdapter<PopularMovie, HomeMovieListAdapter.HomeMovieListViewHolder>(MovieDiffCallback()) {

    class HomeMovieListViewHolder(internal val binding: ItemMovieListBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeMovieListViewHolder {
        val binding =
            ItemMovieListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HomeMovieListViewHolder(binding)
    }


    override fun onBindViewHolder(holder: HomeMovieListViewHolder, position: Int) {
        val movie = getItem(position) ?: return
        holder.binding.ivMovie.load("https://image.tmdb.org/t/p/original${movie.backdropPath}")
    }


    class MovieDiffCallback : DiffUtil.ItemCallback<PopularMovie>() {
        override fun areItemsTheSame(oldItem: PopularMovie, newItem: PopularMovie): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: PopularMovie, newItem: PopularMovie): Boolean {
            return oldItem == newItem
        }
    }

}