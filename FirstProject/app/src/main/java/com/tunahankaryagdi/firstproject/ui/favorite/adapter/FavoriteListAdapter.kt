package com.tunahankaryagdi.firstproject.ui.favorite.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.tunahankaryagdi.firstproject.databinding.ItemFavoriteListBinding
import com.tunahankaryagdi.firstproject.databinding.ItemSearchListBinding
import com.tunahankaryagdi.firstproject.domain.model.Movie

class FavoriteListAdapter(
    val onClickDeleteItem: (Movie) -> Unit
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
            ivFavoriteItemImage.load("https://image.tmdb.org/t/p/original${movie.backdropPath}")
            tvFavoriteItemTitle.text = movie.title
            ivFavoriteItemDelete.setOnClickListener {
                onClickDeleteItem(movie)
            }
        }
    }

    fun updateMovies(newItems: List<Movie>) {
        movies = newItems
        notifyDataSetChanged()
    }
}