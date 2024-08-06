package com.tunahankaryagdi.firstproject.ui.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.tunahankaryagdi.firstproject.R
import com.tunahankaryagdi.firstproject.databinding.FragmentHomeBinding
import com.tunahankaryagdi.firstproject.databinding.FragmentSearchBinding
import com.tunahankaryagdi.firstproject.ui.search.adapter.SearchMovieListAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SearchFragment : Fragment(R.layout.fragment_search) {
    private val viewModel: SearchViewModel by viewModels()
    private lateinit var binding: FragmentSearchBinding
    private lateinit var searchMovieListAdapter: SearchMovieListAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentSearchBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)
        setAdapters()
        observeUiState()
    }

    private fun observeUiState() {

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.uiState.collectLatest { state ->
                searchMovieListAdapter.submitData(state.movies)
            }
        }
    }

    private fun setAdapters() {

        searchMovieListAdapter = SearchMovieListAdapter(
            onClickMovie = {

            }
        )
        with(binding) {
            rvSearchList.adapter = searchMovieListAdapter
//            btnSearch.setOnClickListener {
//                viewModel.getMoviesBySearch(etSearchText.text.toString())
//            }
            etSearchText.addTextChangedListener { text ->
                viewModel.getMoviesBySearch(text.toString())
            }
        }
    }

}