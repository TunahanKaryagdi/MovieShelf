package com.tunahankaryagdi.firstproject.ui.search.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.tunahankaryagdi.firstproject.databinding.ItemSearchListBinding
import com.tunahankaryagdi.firstproject.domain.model.Movie

class SearchMovieListAdapter(
    val onClickMovie: (Int) -> Unit
) : PagingDataAdapter<Movie, SearchMovieListAdapter.SearchMovieListViewHolder>(MovieDiffCallback()) {

    class SearchMovieListViewHolder(internal val binding: ItemSearchListBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchMovieListViewHolder {
        val binding =
            ItemSearchListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SearchMovieListViewHolder(binding)
    }


    override fun onBindViewHolder(holder: SearchMovieListViewHolder, position: Int) {
        val movie = getItem(position) ?: return
        with(holder.binding){
            ivSearchItemImage.load("https://image.tmdb.org/t/p/original${movie.backdropPath}")
            tvSearchItemTitle.text = movie.title
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