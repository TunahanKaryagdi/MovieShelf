package com.tunahankaryagdi.firstproject.ui.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.tunahankaryagdi.firstproject.databinding.ItemMovieListBinding
import com.tunahankaryagdi.firstproject.domain.model.Movie
import com.tunahankaryagdi.firstproject.utils.ext.getImageUrlFromPath

class HomeMovieListAdapter(
    val onClickMovie: (Int) -> Unit
) :
    PagingDataAdapter<Movie, HomeMovieListAdapter.HomeMovieListViewHolder>(MovieDiffCallback()) {

    class HomeMovieListViewHolder(internal val binding: ItemMovieListBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeMovieListViewHolder {
        val binding =
            ItemMovieListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HomeMovieListViewHolder(binding)
    }


    override fun onBindViewHolder(holder: HomeMovieListViewHolder, position: Int) {
        val movie = getItem(position) ?: return
        with(holder.binding){
            ivMovie.load(movie.backdropPath?.getImageUrlFromPath())
            tvMovie.text = movie.title
            llMovieListItem.setOnClickListener {
                onClickMovie(movie.id)
            }
        }

    }


    class MovieDiffCallback : DiffUtil.ItemCallback<Movie>() {
        override fun areItemsTheSame(oldItem: Movie, newItem: Movie): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Movie, newItem: Movie): Boolean {
            return oldItem == newItem
        }
    }

}

