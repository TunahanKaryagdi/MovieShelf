package com.tunahankaryagdi.firstproject.ui.home

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.tunahankaryagdi.firstproject.R
import com.tunahankaryagdi.firstproject.databinding.FragmentHomeBinding
import com.tunahankaryagdi.firstproject.ui.base.BaseFragment
import com.tunahankaryagdi.firstproject.ui.components.ViewPagerTransform
import com.tunahankaryagdi.firstproject.ui.home.adapter.HomeMovieListAdapter
import com.tunahankaryagdi.firstproject.ui.home.adapter.HomePopularMoviesAdapter
import com.tunahankaryagdi.firstproject.utils.HomeTab
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlin.math.abs

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding, HomeViewModel>() {
    override val viewModel: HomeViewModel by viewModels()
    private lateinit var homeMovieListAdapter: HomeMovieListAdapter
    private lateinit var homePopularMoviesAdapter: HomePopularMoviesAdapter

    private val handler = Handler(Looper.getMainLooper())


    override fun inflateBinding(
        layoutInflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentHomeBinding {
        return FragmentHomeBinding.inflate(layoutInflater)
    }

    override fun setupViews() {
        homeMovieListAdapter = HomeMovieListAdapter(onClickMovie = ::navigateToDetail)
        homePopularMoviesAdapter = HomePopularMoviesAdapter(onClickPopularMovie = ::navigateToDetail)


        with(binding.vpPopularMovies) {
            adapter = homePopularMoviesAdapter
            offscreenPageLimit = 3
            getChildAt(0).overScrollMode = RecyclerView.OVER_SCROLL_NEVER
            setPageTransformer(ViewPagerTransform())
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
            etHomeSearchText.setOnFocusChangeListener { _, hasFocus ->
                if (hasFocus) {
                    clDefaultLayout.visibility = View.GONE
                    llHomeSearchLayout.visibility = View.VISIBLE
                }
            }
            etHomeSearchLayoutText.setOnFocusChangeListener { _, hasFocus ->
                if (!hasFocus) {
                    clDefaultLayout.visibility = View.VISIBLE
                    llHomeSearchLayout.visibility = View.GONE
                }
            }

        }
    }

    override fun observeUiState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.uiState.collect { state ->
                homePopularMoviesAdapter.submitList(state.popularMovies)
                startAutoScroll()
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.uiState.collectLatest { state ->
                homeMovieListAdapter.submitData(state.movies)
            }
        }
    }


    private fun startAutoScroll() {
        val runnable = object : Runnable {
            override fun run() {
                if (homePopularMoviesAdapter.itemCount > 0) {
                    val nextItem =
                        (binding.vpPopularMovies.currentItem + 1) % homePopularMoviesAdapter.itemCount
                    binding.vpPopularMovies.setCurrentItem(nextItem, true)
                    handler.postDelayed(this, 5000)
                }
            }
        }
        handler.post(runnable)
    }


    private fun navigateToDetail(movieId: Int) {
        findNavController().navigate(
            HomeFragmentDirections.actionHomeFragmentToDetailFragment(movieId)
        )
    }
}
