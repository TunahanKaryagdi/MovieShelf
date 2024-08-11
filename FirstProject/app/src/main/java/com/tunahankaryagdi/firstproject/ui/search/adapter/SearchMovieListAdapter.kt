package com.tunahankaryagdi.firstproject.ui.search.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.tunahankaryagdi.firstproject.R
import com.tunahankaryagdi.firstproject.databinding.ItemSearchListBinding
import com.tunahankaryagdi.firstproject.domain.model.Movie
import com.tunahankaryagdi.firstproject.domain.model.SearchMovie

class SearchMovieListAdapter(
    val onClickMovie: (Int) -> Unit
) : PagingDataAdapter<SearchMovie, SearchMovieListAdapter.SearchMovieListViewHolder>(MovieDiffCallback()) {

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
            tvSearchItemDate.text = movie.releaseDate
            if (movie.adult) tvSearchItemAdult.setText(R.string.adult_content) else tvSearchItemAdult.setText(R.string.all_ages)
            tvSearchItemPopularity.text = holder.itemView.context.getString(R.string.popularity,movie.popularity)
            tvSearchItemRank.text = movie.voteAverage.toString()
        }
    }


    class MovieDiffCallback : DiffUtil.ItemCallback<SearchMovie>() {
        override fun areItemsTheSame(oldItem: SearchMovie, newItem: SearchMovie): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: SearchMovie, newItem: SearchMovie): Boolean {
            return oldItem == newItem
        }
    }

}