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
import com.tunahankaryagdi.firstproject.R
import com.tunahankaryagdi.firstproject.databinding.FragmentDetailBinding
import com.tunahankaryagdi.firstproject.domain.model.MovieDetail
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DetailFragment : Fragment(R.layout.fragment_detail) {
    private lateinit var binding: FragmentDetailBinding
    private val viewModel: DetailViewModel by viewModels()
    private val args: DetailFragmentArgs by navArgs()


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
        observeUiState()
        viewModel.getDetailByMovieId(args.movieId)
    }

    private fun observeUiState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.uiState.collect { state ->
                if (state.movieDetail != null) setUi(state.movieDetail)
            }
        }
    }

    private fun setUi(movieDetail: MovieDetail) {
        with(binding){
            tvType.text = movieDetail.genres[0].name
            tvDuration.text = movieDetail.runtime.toString()
            tvDetail.text = movieDetail.originalTitle
            tvYear.text = movieDetail.releaseDate
            ivPoster.load("https://image.tmdb.org/t/p/original/${movieDetail.backdropPath}")
            ivMovieDetail.load("https://image.tmdb.org/t/p/original/${movieDetail.posterPath}")
            ivSave.setOnClickListener {
                viewModel.addToFavorites(movieDetail)
            }
        }
    }
}
