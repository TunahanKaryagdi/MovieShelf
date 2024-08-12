package com.tunahankaryagdi.firstproject.ui.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import coil.load
import com.google.android.material.tabs.TabLayout
import com.tunahankaryagdi.firstproject.R
import com.tunahankaryagdi.firstproject.databinding.FragmentDetailBinding
import com.tunahankaryagdi.firstproject.domain.model.MovieDetail
import com.tunahankaryagdi.firstproject.ui.detail.adapter.DetailReviewsAdapter
import com.tunahankaryagdi.firstproject.utils.DetailTab
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DetailFragment : Fragment(R.layout.fragment_detail) {
    private lateinit var binding: FragmentDetailBinding
    private val viewModel: DetailViewModel by viewModels()
    private val args: DetailFragmentArgs by navArgs()
    private lateinit var detailReviewsAdapter: DetailReviewsAdapter


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentDetailBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setInitialValues()
        observeUiState()
        viewModel.getDetailByMovieId(args.movieId)
        viewModel.getReviewsByMovieId(args.movieId)
        viewModel.checkIsFavorite(args.movieId)
    }

    private fun observeUiState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.uiState.collect { state ->
                if (state.movieDetail != null) setUi(state.movieDetail)
            }
        }
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.uiState.collect { state ->
                detailReviewsAdapter.updateReviews(state.reviews)
            }
        }
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.uiState.collect { state ->
                when(state.selectedTab){
                    DetailTab.ABOUT_MOVIE->{
                        with(binding){
                            tvAboutMovie.visibility = View.VISIBLE
                            rvReviewList.visibility = View.INVISIBLE
                            tvEmptyReview.visibility = View.INVISIBLE
                        }
                    }
                    DetailTab.REVIEWS->{
                        with(binding){
                            tvAboutMovie.visibility = View.INVISIBLE
                            if (state.reviews.isEmpty()) tvEmptyReview.visibility = View.VISIBLE
                            else rvReviewList.visibility = View.VISIBLE

                        }
                    }
                    DetailTab.CAST->{
                        with(binding){
                            tvAboutMovie.visibility = View.INVISIBLE
                            rvReviewList.visibility = View.INVISIBLE
                            tvEmptyReview.visibility = View.INVISIBLE
                        }
                    }
                }
            }
        }
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.uiState.collect { state ->
                with(binding.ivSave){
                    if (state.isFavorite) setImageResource(R.drawable.ic_bookmark) else setImageResource(R.drawable.ic_bookmark_outline)
                }
            }
        }
    }

    private fun setUi(movieDetail: MovieDetail) {

        with(binding) {
            tvType.text = movieDetail.genres[0].name
            tvDuration.text = "${movieDetail.runtime.toString()} minutes"
            tvMovieTitle.text = movieDetail.originalTitle
            tvYear.text = movieDetail.releaseDate
            tvAboutMovie.text = movieDetail.overview
            ivPoster.load("https://image.tmdb.org/t/p/original/${movieDetail.backdropPath}")
            ivMovieDetail.load("https://image.tmdb.org/t/p/original/${movieDetail.posterPath}")
            ivSave.setOnClickListener {
                viewModel.addToFavorites(movieDetail)
            }
            tlDetailTabs.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
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

    private fun setInitialValues(){
        detailReviewsAdapter = DetailReviewsAdapter()
        with(binding){
            DetailTab.entries.forEach { detailTab ->
                tlDetailTabs.addTab(tlDetailTabs.newTab().setText(detailTab.resId))
            }
            rvReviewList.adapter = detailReviewsAdapter
        }

    }

}
