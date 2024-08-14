package com.tunahankaryagdi.firstproject.ui.detail.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.tunahankaryagdi.firstproject.R
import com.tunahankaryagdi.firstproject.databinding.ItemReviewListBinding
import com.tunahankaryagdi.firstproject.databinding.ItemSimilarMoviesBinding
import com.tunahankaryagdi.firstproject.domain.model.Movie
import com.tunahankaryagdi.firstproject.domain.model.Review
import com.tunahankaryagdi.firstproject.ui.base.BaseDiffUtil
import com.tunahankaryagdi.firstproject.ui.base.calculateAndDispatch
import com.tunahankaryagdi.firstproject.utils.ext.getImageUrlFromPath

class DetailSimilarMoviesAdapter(
    val onClickSimilarMovie: (Int) -> Unit
) :
    RecyclerView.Adapter<DetailSimilarMoviesAdapter.DetailSimilarMoviesViewHolder>() {

    private var movies: List<Movie> = emptyList()


    class DetailSimilarMoviesViewHolder(internal val binding: ItemSimilarMoviesBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DetailSimilarMoviesViewHolder {
        val binding =
            ItemSimilarMoviesBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DetailSimilarMoviesViewHolder(binding)
    }

    override fun getItemCount() = movies.size

    override fun onBindViewHolder(holder: DetailSimilarMoviesViewHolder, position: Int) {
        val movie = movies[position]
        with(holder.binding){
            ivSimilarMovie.load(movie.backdropPath?.getImageUrlFromPath())
            tvSimilarMovie.text = movie.title
            llSimilarItem.setOnClickListener {
                onClickSimilarMovie(movie.id)
            }
        }
    }

    fun submitList(newMovies: List<Movie>) {
        val diffCallback = BaseDiffUtil(movies, newMovies)
        diffCallback.calculateAndDispatch(this)
        movies = newMovies
    }


}