package com.tunahankaryagdi.firstproject.ui.detail

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import coil.load
import com.google.android.material.tabs.TabLayout
import com.tunahankaryagdi.firstproject.R
import com.tunahankaryagdi.firstproject.databinding.FragmentDetailBinding
import com.tunahankaryagdi.firstproject.domain.model.MovieDetail
import com.tunahankaryagdi.firstproject.ui.base.BaseFragment
import com.tunahankaryagdi.firstproject.ui.components.CustomToast
import com.tunahankaryagdi.firstproject.ui.detail.adapter.DetailReviewsAdapter
import com.tunahankaryagdi.firstproject.ui.detail.adapter.DetailSimilarMoviesAdapter
import com.tunahankaryagdi.firstproject.utils.DetailTab
import com.tunahankaryagdi.firstproject.utils.ext.getImageUrlFromPath
import com.tunahankaryagdi.firstproject.utils.ext.toFormattedString
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DetailFragment : BaseFragment<FragmentDetailBinding, DetailViewModel>() {
    override val viewModel: DetailViewModel by viewModels()

    private val args: DetailFragmentArgs by navArgs()
    private lateinit var detailReviewsAdapter: DetailReviewsAdapter
    private lateinit var detailSimilarMoviesAdapter: DetailSimilarMoviesAdapter


    override fun inflateBinding(
        layoutInflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentDetailBinding {
        return FragmentDetailBinding.inflate(layoutInflater)
    }

    override fun setupViews() {
        viewModel.init(
            movieId = args.movieId,
            onError = {
                val customToast = CustomToast(requireContext())
                customToast.show(it)
            }
        )
        detailReviewsAdapter = DetailReviewsAdapter()
        detailSimilarMoviesAdapter = DetailSimilarMoviesAdapter(
            onClickSimilarMovie = {
                val action = DetailFragmentDirections.actionDetailFragmentSelf(it)
                findNavController().navigate(action)
            }
        )
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
                with(binding.ivSave) {
                    if (state.isFavorite) setImageResource(R.drawable.ic_bookmark) else setImageResource(
                        R.drawable.ic_bookmark_outline
                    )
                }
            }
        }
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.uiState.collect { state ->
                setUiTabElements(state.selectedTab, state.reviews.isEmpty())
            }
        }
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.uiState.collect { state ->
                detailSimilarMoviesAdapter.submitList(state.similarMovies)
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.uiState.collect { state ->
                binding.pbDetail.visibility = if (state.isLoading) View.VISIBLE else View.INVISIBLE
            }
        }
    }


    private fun setUiElements(movieDetail: MovieDetail) {
        with(binding) {
            tvType.text = movieDetail.genres[0].name
            tvDuration.text = getString(R.string.minutes,movieDetail.runtime.toString())
            tvMovieTitle.text = movieDetail.originalTitle
            tvYear.text = movieDetail.releaseDate
            tvAboutMovie.text = movieDetail.overview
            tvMovieRate.text = movieDetail.voteAverage.toFormattedString()
            ivPoster.load(movieDetail.posterPath?.getImageUrlFromPath())
            ivMovieDetail.load(movieDetail.backdropPath?.getImageUrlFromPath())
            ivSave.setOnClickListener {
                if (viewModel.uiState.value.isFavorite) {
                    viewModel.deleteFavorite(
                        movieDetail = movieDetail,
                        showToast = {
                            val customToast = CustomToast(requireContext())
                            customToast.show(getString(R.string.successfully_deleted))
                        }
                    )
                }
                else {
                    viewModel.addToFavorites(
                        movieDetail = movieDetail,
                        showToast = {
                            val customToast = CustomToast(requireContext())
                            customToast.show(getString(R.string.successfully_saved))
                        }
                    )
                }

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
            rvSimilarMovieList.adapter = detailSimilarMoviesAdapter
        }
    }

    private fun setUiTabElements(selectedTab: DetailTab, isEmptyReviews: Boolean) {
        with(binding) {
            when (selectedTab) {
                DetailTab.ABOUT_MOVIE -> {
                    tvAboutMovie.visibility = View.VISIBLE
                    rvReviewList.visibility = View.INVISIBLE
                    tvEmptyReview.visibility = View.INVISIBLE
                    rvSimilarMovieList.visibility = View.INVISIBLE
                }

                DetailTab.REVIEWS -> {
                    tvAboutMovie.visibility = View.INVISIBLE
                    if (isEmptyReviews) tvEmptyReview.visibility = View.VISIBLE
                    else rvReviewList.visibility = View.VISIBLE
                    rvSimilarMovieList.visibility = View.INVISIBLE
                }

                DetailTab.SIMILARS -> {
                    tvAboutMovie.visibility = View.INVISIBLE
                    rvReviewList.visibility = View.INVISIBLE
                    tvEmptyReview.visibility = View.INVISIBLE
                    rvSimilarMovieList.visibility = View.VISIBLE
                }
            }
        }
    }

}