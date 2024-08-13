package com.tunahankaryagdi.firstproject.ui.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.tunahankaryagdi.firstproject.databinding.ItemPopularMoviesBinding
import com.tunahankaryagdi.firstproject.domain.model.Movie
import com.tunahankaryagdi.firstproject.ui.base.BaseDiffUtil
import com.tunahankaryagdi.firstproject.ui.base.calculateAndDispatch
import com.tunahankaryagdi.firstproject.utils.ext.getImageUrlFromPath

class HomePopularMoviesAdapter(
    val onClickPopularMovie: (Int) -> Unit
) :
    RecyclerView.Adapter<HomePopularMoviesAdapter.HomePopularMoviesViewHolder>() {

    private var movies: List<Movie> = emptyList()


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
        with(holder.binding){
            ivPopularMovie.load(movie.backdropPath?.getImageUrlFromPath())
            llPopularMovieItem.setOnClickListener {
                onClickPopularMovie(movie.id)
            }
        }
    }

    fun submitList(newMovies: List<Movie>) {
        val diffCallback = BaseDiffUtil(movies,newMovies)
        diffCallback.calculateAndDispatch(this)
        movies = newMovies
    }

}