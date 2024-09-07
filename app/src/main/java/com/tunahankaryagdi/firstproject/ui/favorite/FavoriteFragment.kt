package com.tunahankaryagdi.firstproject.ui.favorite

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.tunahankaryagdi.firstproject.R
import com.tunahankaryagdi.firstproject.databinding.FragmentFavoriteBinding
import com.tunahankaryagdi.firstproject.domain.model.Movie
import com.tunahankaryagdi.firstproject.ui.base.BaseFragment
import com.tunahankaryagdi.firstproject.ui.components.CustomDialog
import com.tunahankaryagdi.firstproject.ui.components.CustomToast
import com.tunahankaryagdi.firstproject.ui.favorite.adapter.FavoriteListAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FavoriteFragment : BaseFragment<FragmentFavoriteBinding, FavoriteViewModel>() {
    override val viewModel: FavoriteViewModel by viewModels()
    private lateinit var favoriteListAdapter: FavoriteListAdapter

    override fun inflateBinding(
        layoutInflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentFavoriteBinding {
        return FragmentFavoriteBinding.inflate(layoutInflater)
    }


    override fun setupViews() {
        favoriteListAdapter = FavoriteListAdapter(
            onClickDeleteItem = ::onClickDeleteItem,
            onClickItem = { movieId ->
                findNavController().navigate(
                    FavoriteFragmentDirections.actionFavoriteFragmentToDetailFragment(movieId)
                )
            })
        binding.rvFavoriteList.adapter = favoriteListAdapter
        with(binding) {
            rvFavoriteList.adapter = favoriteListAdapter
            etFavoriteText.addTextChangedListener { text ->
                viewModel.filterMovies(text.toString())
            }
        }
        viewModel.getFavoriteMovies()
    }

    override fun observeUiState() {

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.uiState.collect { state ->
                favoriteListAdapter.submitList(state.filteredMovies)
                with(binding) {
                    if (state.filteredMovies.isEmpty()) {
                        rvFavoriteList.visibility = View.INVISIBLE
                        llFavoriteEmptyResult.visibility = View.VISIBLE
                    } else {
                        rvFavoriteList.visibility = View.VISIBLE
                        llFavoriteEmptyResult.visibility = View.INVISIBLE
                    }
                }
            }

        }
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.uiState.collect { state ->
                binding.pbFavorite.visibility = if (state.isLoading) View.VISIBLE else View.INVISIBLE
            }
        }
    }

    private fun onClickDeleteItem(movie: Movie) {
        val dialog = CustomDialog(
            onConfirm = {
                viewModel.deleteFavoriteMovie(
                    movie = movie,
                    showToast = {
                        val customToast = CustomToast(requireContext())
                        customToast.show(getString(R.string.successfully_deleted))
                    }
                )
            },
        )
        dialog.show(parentFragmentManager, "Custom Dialog")
    }

}