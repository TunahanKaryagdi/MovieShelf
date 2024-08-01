package com.tunahankaryagdi.firstproject.ui.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import coil.load
import com.tunahankaryagdi.firstproject.databinding.ItemPopularMoviesBinding

class HomePopularMoviesAdapter(
    viewPager2: ViewPager2
) :
    RecyclerView.Adapter<HomePopularMoviesAdapter.HomePopularMoviesViewHolder>() {

    private var movies: List<String> = emptyList()


    class HomePopularMoviesViewHolder(internal val binding: ItemPopularMoviesBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomePopularMoviesViewHolder {
        val binding =
            ItemPopularMoviesBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HomePopularMoviesViewHolder(binding)
    }

    override fun getItemCount() = movies.size

    override fun onBindViewHolder(holder: HomePopularMoviesViewHolder, position: Int) {
        val movie = movies[position]
        holder.binding.ivPopularMovie.load("https://image.tmdb.org/t/p/original$movie")
    }

    fun updateMovies(newItems: List<String>) {
        movies = newItems
        notifyDataSetChanged()
    }

}