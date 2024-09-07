package com.tunahankaryagdi.firstproject.ui.home_search

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import com.tunahankaryagdi.firstproject.databinding.FragmentHomeSearchBinding
import com.tunahankaryagdi.firstproject.databinding.FragmentSearchBinding
import com.tunahankaryagdi.firstproject.ui.base.BaseFragment
import com.tunahankaryagdi.firstproject.ui.home.HomeFragmentDirections
import com.tunahankaryagdi.firstproject.ui.search.SearchViewModel
import com.tunahankaryagdi.firstproject.ui.search.adapter.SearchMovieListAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


@AndroidEntryPoint
class HomeSearchFragment : BaseFragment<FragmentHomeSearchBinding, HomeSearchViewModel>() {
    override val viewModel: HomeSearchViewModel by viewModels()

    private lateinit var searchMovieListAdapter: SearchMovieListAdapter


    override fun inflateBinding(
        layoutInflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentHomeSearchBinding {
        return FragmentHomeSearchBinding.inflate(layoutInflater, container, false)
    }

    override fun setupViews() {
        searchMovieListAdapter = SearchMovieListAdapter(onClickMovie = ::navigateToDetail)
        with(binding){
            rvHomeSearch.adapter = searchMovieListAdapter
            searchMovieListAdapter.addLoadStateListener { loadStates ->
                val refreshState = loadStates.refresh
                with(binding) {
                    if (refreshState is LoadState.NotLoading && searchMovieListAdapter.itemCount == 0) {
                        rvHomeSearch.visibility = View.GONE
                        llHomeEmptyResult.visibility = View.VISIBLE
                    } else {
                        rvHomeSearch.visibility = View.VISIBLE
                        llHomeEmptyResult.visibility = View.GONE
                    }
                }
            }
            etHomeSearchText.addTextChangedListener { text ->
                if (text?.toString()?.isNotBlank() == true){
                    viewModel.getMoviesBySearch(text.toString())
                }
            }
        }
    }

    override fun observeUiState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.uiState.collectLatest { state ->
                searchMovieListAdapter.submitData(state.movies)
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.uiState.collectLatest { state ->
                with(binding) {
                    pbHomeSearch.visibility = if (state.isLoading) View.VISIBLE else View.INVISIBLE
                }
            }
        }
    }

    private fun navigateToDetail(movieId: Int) {
        findNavController().navigate(
            HomeSearchFragmentDirections.actionHomeSearchFragmentToDetailFragment(movieId)
        )
    }
}