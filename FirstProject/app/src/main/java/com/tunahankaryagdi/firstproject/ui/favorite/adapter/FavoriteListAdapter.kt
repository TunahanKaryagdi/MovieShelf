package com.tunahankaryagdi.firstproject.ui.favorite.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.tunahankaryagdi.firstproject.databinding.ItemFavoriteListBinding
import com.tunahankaryagdi.firstproject.domain.model.Movie
import com.tunahankaryagdi.firstproject.ui.base.BaseDiffUtil
import com.tunahankaryagdi.firstproject.ui.base.calculateAndDispatch
import com.tunahankaryagdi.firstproject.utils.ext.getImageUrlFromPath

class FavoriteListAdapter(
    val onClickDeleteItem: (Movie) -> Unit,
    val onClickItem: (Int) -> Unit,
) : RecyclerView.Adapter<FavoriteListAdapter.FavoriteListViewHolder>() {

    private var movies: List<Movie> = emptyList()

    class FavoriteListViewHolder(internal val binding: ItemFavoriteListBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteListViewHolder {
        val binding = ItemFavoriteListBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return FavoriteListViewHolder(binding)
    }

    override fun getItemCount() = movies.size

    override fun onBindViewHolder(holder: FavoriteListViewHolder, position: Int) {
        val movie = movies[position]
        with(holder.binding){
            ivFavoriteItemImage.load(movie.backdropPath?.getImageUrlFromPath())
            tvFavoriteItemTitle.text = movie.title
            ivFavoriteItemDelete.setOnClickListener {
                onClickDeleteItem(movie)
            }
            llFavoriteMovieItem.setOnClickListener {
                onClickItem(movie.id)
            }
        }
    }


    fun submitList(newMovies: List<Movie>) {
        val diffCallback = BaseDiffUtil(movies,newMovies)
        diffCallback.calculateAndDispatch(this)
        movies = newMovies
    }


}