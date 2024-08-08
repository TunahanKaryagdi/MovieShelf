package com.tunahankaryagdi.firstproject.ui.favorite.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.tunahankaryagdi.firstproject.databinding.ItemSearchListBinding
import com.tunahankaryagdi.firstproject.domain.model.Movie

class FavoriteListAdapter() : RecyclerView.Adapter<FavoriteListAdapter.FavoriteListViewHolder>() {

    private var movies: List<Movie> = emptyList()

    class FavoriteListViewHolder(internal val binding: ItemSearchListBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteListViewHolder {
        val binding = ItemSearchListBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return FavoriteListViewHolder(binding)
    }

    override fun getItemCount() = movies.size

    override fun onBindViewHolder(holder: FavoriteListViewHolder, position: Int) {
        val movie = movies[position]
        with(holder.binding){
            ivSearchItemImage.load("https://image.tmdb.org/t/p/original${movie.backdropPath}")
            tvSearchItemTitle.text = movie.title
        }
    }

    fun updateMovies(newItems: List<Movie>) {
        movies = newItems
        notifyDataSetChanged()
    }
}