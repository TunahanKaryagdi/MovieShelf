package com.tunahankaryagdi.firstproject.ui.search

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import com.tunahankaryagdi.firstproject.databinding.FragmentSearchBinding
import com.tunahankaryagdi.firstproject.ui.base.BaseFragment
import com.tunahankaryagdi.firstproject.ui.search.adapter.SearchMovieListAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SearchFragment : BaseFragment<FragmentSearchBinding, SearchViewModel>() {

    override val viewModel: SearchViewModel by viewModels()
    private lateinit var searchMovieListAdapter: SearchMovieListAdapter


    override fun inflateBinding(
        layoutInflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentSearchBinding {
        return FragmentSearchBinding.inflate(layoutInflater, container, false)
    }

    override fun setupViews() {
        searchMovieListAdapter = SearchMovieListAdapter(
            onClickMovie = { movieId ->
                findNavController().navigate(
                    SearchFragmentDirections.actionSearchFragmentToDetailFragment(movieId)
                )
            }
        )

        searchMovieListAdapter.addLoadStateListener { loadStates ->
            val refreshState = loadStates.refresh
            with(binding) {
                if (refreshState is LoadState.NotLoading && searchMovieListAdapter.itemCount == 0) {
                    rvSearchList.visibility = View.INVISIBLE
                    llSearchEmptyResult.visibility = View.VISIBLE
                } else {
                    rvSearchList.visibility = View.VISIBLE
                    llSearchEmptyResult.visibility = View.INVISIBLE
                }
            }
        }
        with(binding) {
            rvSearchList.adapter = searchMovieListAdapter
            etSearchText.addTextChangedListener { text ->
                viewModel.getMoviesBySearch(text.toString())
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
                with(binding){
                    pbSearch.visibility = if (state.isLoading) View.VISIBLE else View.INVISIBLE
                }
            }
        }
    }

}