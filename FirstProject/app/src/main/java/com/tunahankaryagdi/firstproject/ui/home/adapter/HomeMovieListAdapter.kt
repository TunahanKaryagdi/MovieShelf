package com.tunahankaryagdi.firstproject.ui.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.tunahankaryagdi.firstproject.databinding.ItemMovieListBinding

class HomeMovieListAdapter() :
    RecyclerView.Adapter<HomeMovieListAdapter.HomeMovieListViewHolder>() {

    private var movies: List<String> = emptyList()


    class HomeMovieListViewHolder(internal val binding: ItemMovieListBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeMovieListViewHolder {
        val binding =
            ItemMovieListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HomeMovieListViewHolder(binding)
    }

    override fun getItemCount() = movies.size

    override fun onBindViewHolder(holder: HomeMovieListViewHolder, position: Int) {
        val movie = movies[position]
        holder.binding.ivMovie.load("https://image.tmdb.org/t/p/original$movie")
    }

    fun updateMovies(newItems: List<String>) {
        movies = newItems
        notifyDataSetChanged()
    }

}