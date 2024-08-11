package com.tunahankaryagdi.firstproject.ui.detail.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.tunahankaryagdi.firstproject.R
import com.tunahankaryagdi.firstproject.databinding.ItemReviewListBinding
import com.tunahankaryagdi.firstproject.domain.model.Review

class DetailReviewsAdapter() :
    RecyclerView.Adapter<DetailReviewsAdapter.DetailReviewsViewHolder>() {

    private var reviews: List<Review> = emptyList()


    class DetailReviewsViewHolder(internal val binding: ItemReviewListBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DetailReviewsViewHolder {
        val binding =
            ItemReviewListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DetailReviewsViewHolder(binding)
    }

    override fun getItemCount() = reviews.size

    override fun onBindViewHolder(holder: DetailReviewsViewHolder, position: Int) {
        val review = reviews[position]
        with(holder.binding) {
            tvReviewItemRate.text = review.authorDetails.rating.toString()
            tvReviewItemContent.text = review.content
            tvReviewItemAuthorUsername.text = review.authorDetails.username
            if (review.authorDetails.avatarPath == null) ivReviewItemAuthorImage.setImageResource(R.drawable.ic_default_user)
            else ivReviewItemAuthorImage.load("https://image.tmdb.org/t/p/original${review.authorDetails.avatarPath}")
        }
    }

    fun updateReviews(newItems: List<Review>) {
        reviews = newItems
        notifyDataSetChanged()
    }

}