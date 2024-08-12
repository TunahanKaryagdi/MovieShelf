package com.tunahankaryagdi.firstproject.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.tabs.TabLayout
import com.tunahankaryagdi.firstproject.databinding.FragmentHomeBinding
import com.tunahankaryagdi.firstproject.ui.base.BaseFragment
import com.tunahankaryagdi.firstproject.ui.home.adapter.HomeMovieListAdapter
import com.tunahankaryagdi.firstproject.ui.home.adapter.HomePopularMoviesAdapter
import com.tunahankaryagdi.firstproject.utils.HomeTab
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding, HomeViewModel>() {
    override val viewModel: HomeViewModel by viewModels()
    private lateinit var homeMovieListAdapter: HomeMovieListAdapter
    private lateinit var homePopularMoviesAdapter: HomePopularMoviesAdapter

    override fun inflateBinding(
        layoutInflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentHomeBinding {
        return FragmentHomeBinding.inflate(layoutInflater)
    }

    override fun setupViews() {
        homeMovieListAdapter = HomeMovieListAdapter(
            onClickMovie = { movieId ->
                findNavController().navigate(
                    HomeFragmentDirections.actionHomeFragmentToDetailFragment(movieId)
                )
            }
        )
        homePopularMoviesAdapter = HomePopularMoviesAdapter()

        with(binding.vpPopularMovies) {
            adapter = homePopularMoviesAdapter
            offscreenPageLimit = 3
        }

        with(binding) {
            rvMovies.adapter = homeMovieListAdapter
            HomeTab.entries.forEach { homeTab ->
                binding.tlHomeTabs.addTab(binding.tlHomeTabs.newTab().setText(homeTab.resId))
            }
            tlHomeTabs.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
                override fun onTabSelected(tab: TabLayout.Tab?) {
                    val position = tab?.position ?: return
                    val selectedTab = HomeTab.entries[position]
                    viewModel.setTab(selectedTab)
                }

                override fun onTabUnselected(tab: TabLayout.Tab?) {}
                override fun onTabReselected(tab: TabLayout.Tab?) {}
            })
            btn1x.setOnClickListener {
                rvMovies.layoutManager = LinearLayoutManager(requireContext())
            }
            btn2x.setOnClickListener {
                rvMovies.layoutManager = GridLayoutManager(requireContext(), 2)
            }
            btn3x.setOnClickListener {
                rvMovies.layoutManager = GridLayoutManager(requireContext(), 3)
            }
        }
    }

    override fun observeUiState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.uiState.collect { state ->
                homePopularMoviesAdapter.updateMovies(state.popularMovies)
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.uiState.collectLatest { state ->
                homeMovieListAdapter.submitData(state.movies)
            }
        }
    }

}
