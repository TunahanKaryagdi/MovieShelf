package com.tunahankaryagdi.firstproject.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.google.android.material.tabs.TabLayout
import com.tunahankaryagdi.firstproject.R
import com.tunahankaryagdi.firstproject.databinding.FragmentHomeBinding
import com.tunahankaryagdi.firstproject.ui.home.adapter.HomeMovieListAdapter
import com.tunahankaryagdi.firstproject.ui.home.adapter.HomePopularMoviesAdapter
import com.tunahankaryagdi.firstproject.utils.HomeTab
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : Fragment(R.layout.fragment_home) {
    private val viewModel: HomeViewModel by viewModels()
    private lateinit var binding: FragmentHomeBinding
    private lateinit var homeMovieListAdapter: HomeMovieListAdapter
    private lateinit var homePopularMoviesAdapter: HomePopularMoviesAdapter


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentHomeBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)
        observeUiState()
        setAdapters()




    }


    private fun setAdapters() {

        homeMovieListAdapter = HomeMovieListAdapter(movies = viewModel.uiState.value.popularMovies)
        homePopularMoviesAdapter = HomePopularMoviesAdapter()

        with(binding.vpPopularMovies) {
            adapter = homePopularMoviesAdapter
            offscreenPageLimit = 3
        }

        binding.rvMovies.adapter = homeMovieListAdapter

        HomeTab.entries.forEach { homeTab ->
            binding.tlTabs.addTab(binding.tlTabs.newTab().setText(homeTab.resId))
        }

        binding.tlTabs.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                val position = tab?.position ?: return
                val selectedTab = HomeTab.entries[position]
                viewModel.setTab(selectedTab)
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {}

            override fun onTabReselected(tab: TabLayout.Tab?) {}

        })

    }

    private fun observeUiState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.uiState.collect { state ->
                if (state.popularMovies.isNotEmpty()) {
                    homePopularMoviesAdapter.updateMovies(state.popularMovies)
                }
                if (state.movies.isNotEmpty()) {
                    homeMovieListAdapter.updateMovies(state.movies)
                }
            }

        }
    }
}
