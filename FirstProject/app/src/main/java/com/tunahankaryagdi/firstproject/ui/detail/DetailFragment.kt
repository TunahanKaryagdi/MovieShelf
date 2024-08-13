package com.tunahankaryagdi.firstproject.ui.detail

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import coil.load
import com.google.android.material.tabs.TabLayout
import com.tunahankaryagdi.firstproject.R
import com.tunahankaryagdi.firstproject.databinding.FragmentDetailBinding
import com.tunahankaryagdi.firstproject.domain.model.MovieDetail
import com.tunahankaryagdi.firstproject.ui.base.BaseFragment
import com.tunahankaryagdi.firstproject.ui.detail.adapter.DetailReviewsAdapter
import com.tunahankaryagdi.firstproject.utils.DetailTab
import com.tunahankaryagdi.firstproject.utils.ext.getImageUrlFromPath
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DetailFragment : BaseFragment<FragmentDetailBinding, DetailViewModel>() {
    override val viewModel: DetailViewModel by viewModels()
    private lateinit var detailReviewsAdapter: DetailReviewsAdapter


    override fun inflateBinding(
        layoutInflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentDetailBinding {
        return FragmentDetailBinding.inflate(layoutInflater)
    }

    override fun setupViews() {
        detailReviewsAdapter = DetailReviewsAdapter()
        with(binding) {
            DetailTab.entries.forEach { detailTab ->
                tlDetailTabs.addTab(tlDetailTabs.newTab().setText(detailTab.resId))
            }
            rvReviewList.adapter = detailReviewsAdapter
            ivBackToHome.setOnClickListener {
                findNavController().popBackStack()
            }
        }

    }

    override fun observeUiState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.uiState.collect { state ->
                if (state.movieDetail != null) setUiElements(state.movieDetail)
            }
        }
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.uiState.collect { state ->
                detailReviewsAdapter.submitList(state.reviews)
            }
        }
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.uiState.collect { state ->
                with(binding.ivSave){
                    if (state.isFavorite) setImageResource(R.drawable.ic_bookmark) else setImageResource(R.drawable.ic_bookmark_outline)
                }
            }
        }
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.uiState.collect { state ->
                setUiTabElements(state.selectedTab,state.reviews.isEmpty())
            }
        }
    }


    private fun setUiElements(movieDetail: MovieDetail){
        with(binding) {
            tvType.text = movieDetail.genres[0].name
            tvDuration.text = "${movieDetail.runtime.toString()} minutes"
            tvMovieTitle.text = movieDetail.originalTitle
            tvYear.text = movieDetail.releaseDate
            tvAboutMovie.text = movieDetail.overview
            ivPoster.load(movieDetail.posterPath.getImageUrlFromPath())
            ivMovieDetail.load(movieDetail.backdropPath.getImageUrlFromPath())
            ivSave.setOnClickListener {
                viewModel.addToFavorites(movieDetail)
            }
            tlDetailTabs.addOnTabSelectedListener(object :
                TabLayout.OnTabSelectedListener {
                override fun onTabSelected(tab: TabLayout.Tab?) {
                    val position = tab?.position ?: return
                    val selectedTab = DetailTab.entries[position]
                    viewModel.setTab(selectedTab)
                }

                override fun onTabUnselected(tab: TabLayout.Tab?) {}
                override fun onTabReselected(tab: TabLayout.Tab?) {}
            })
            rvReviewList.adapter = detailReviewsAdapter
        }
    }

    private fun setUiTabElements(selectedTab: DetailTab,isEmptyReviews: Boolean){
        with(binding){
            when(selectedTab) {
                DetailTab.ABOUT_MOVIE -> {
                    tvAboutMovie.visibility = View.VISIBLE
                    rvReviewList.visibility = View.INVISIBLE
                    tvEmptyReview.visibility = View.INVISIBLE
                }
                DetailTab.REVIEWS -> {
                    tvAboutMovie.visibility = View.INVISIBLE
                    if (isEmptyReviews) tvEmptyReview.visibility = View.VISIBLE
                    else rvReviewList.visibility = View.VISIBLE
                }
                DetailTab.CAST->{
                   tvAboutMovie.visibility = View.INVISIBLE
                   rvReviewList.visibility = View.INVISIBLE
                   tvEmptyReview.visibility = View.INVISIBLE
                }
            }
        }
    }

}