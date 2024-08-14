package com.tunahankaryagdi.firstproject.ui.home

import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupWindow
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.tabs.TabLayout
import com.tunahankaryagdi.firstproject.R
import com.tunahankaryagdi.firstproject.databinding.FragmentHomeBinding
import com.tunahankaryagdi.firstproject.ui.base.BaseFragment
import com.tunahankaryagdi.firstproject.ui.components.ViewPagerTransform
import com.tunahankaryagdi.firstproject.ui.home.adapter.HomeMovieListAdapter
import com.tunahankaryagdi.firstproject.ui.home.adapter.HomePopularMoviesAdapter
import com.tunahankaryagdi.firstproject.ui.search.adapter.SearchMovieListAdapter
import com.tunahankaryagdi.firstproject.utils.HomeTab
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding, HomeViewModel>() {
    override val viewModel: HomeViewModel by viewModels()
    private lateinit var homeMovieListAdapter: HomeMovieListAdapter
    private lateinit var homePopularMoviesAdapter: HomePopularMoviesAdapter
    private lateinit var searchMovieListAdapter: SearchMovieListAdapter

    private val handler = Handler(Looper.getMainLooper())


    override fun inflateBinding(
        layoutInflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentHomeBinding {
        return FragmentHomeBinding.inflate(layoutInflater)
    }

    override fun setupViews() {
        homeMovieListAdapter = HomeMovieListAdapter(onClickMovie = ::navigateToDetail)
        homePopularMoviesAdapter =
            HomePopularMoviesAdapter(onClickPopularMovie = ::navigateToDetail)
        searchMovieListAdapter = SearchMovieListAdapter(onClickMovie = ::navigateToDetail)

        with(binding.vpPopularMovies) {
            adapter = homePopularMoviesAdapter
            offscreenPageLimit = 3
            getChildAt(0).overScrollMode = RecyclerView.OVER_SCROLL_NEVER
            setPageTransformer(ViewPagerTransform())
        }

        with(binding) {
            rvMovies.adapter = homeMovieListAdapter
            rvHomeSearch.adapter = searchMovieListAdapter
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

//            etHomeSearchText.setOnFocusChangeListener { _, hasFocus ->
//                if (hasFocus) {
//                    llHomeDefaultLayout.visibility = View.GONE
//                    clHomeSearchLayout.visibility = View.VISIBLE
//                } else {
//                    llHomeDefaultLayout.visibility = View.VISIBLE
//                    clHomeSearchLayout.visibility = View.GONE
//                }
//            }
            searchMovieListAdapter.addLoadStateListener { loadStates ->
                val refreshState = loadStates.refresh
                with(binding) {
                    if (refreshState is LoadState.NotLoading && searchMovieListAdapter.itemCount == 0) {
                        rvHomeSearch.visibility = View.INVISIBLE
                        llHomeEmptyResult.visibility = View.VISIBLE
                    } else {
                        rvHomeSearch.visibility = View.VISIBLE
                        llHomeEmptyResult.visibility = View.INVISIBLE
                    }
                }
            }
            etHomeSearchText.addTextChangedListener { text ->

                if (text.toString().isBlank()) {
                    if (etHomeSearchText.hasFocus()){
                        rvHomeSearch.visibility = View.INVISIBLE
                        clHomeSearchLayout.visibility = View.INVISIBLE
                    }
                    llHomeDefaultLayout.visibility = View.VISIBLE
                    clHomeSearchLayout.visibility = View.GONE
                }
                else {
                    llHomeDefaultLayout.visibility = View.INVISIBLE
                    clHomeSearchLayout.visibility = View.VISIBLE
                }
                viewModel.getMoviesBySearch(text.toString())
            }

            ivOptions.setOnClickListener {
                setupPopupMenu()
            }

        }
    }

    override fun observeUiState() {

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.uiState.collect { state ->
                with(binding) {
                    pbHome.visibility = if (state.isLoading) View.VISIBLE else View.INVISIBLE
                }
                homePopularMoviesAdapter.submitList(state.popularMovies)
                startAutoScroll()
            }
        }


//        viewLifecycleOwner.lifecycleScope.launch {
//            viewModel.uiState.collect { state ->
//                homePopularMoviesAdapter.submitList(state.popularMovies)
//                startAutoScroll()
//            }
//        }
//
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.uiState.collectLatest { state ->
                homeMovieListAdapter.submitData(state.movies)
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.uiState.collectLatest { state ->
                searchMovieListAdapter.submitData(state.searchMovies)
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

    private fun setupPopupMenu() {
        val inflater = requireActivity().layoutInflater
        val popupView = inflater.inflate(R.layout.custom_popup, null)

        val popupWindow = PopupWindow(
            popupView,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            true
        )

        popupWindow.setBackgroundDrawable(null)
        popupWindow.isOutsideTouchable = true
        popupWindow.isFocusable = true
        popupWindow.showAsDropDown(binding.ivOptions, 0, 0)

        popupView.findViewById<TextView>(R.id.linear).setOnClickListener {
            binding.rvMovies.layoutManager = LinearLayoutManager(requireContext())
            popupWindow.dismiss()
        }

        popupView.findViewById<TextView>(R.id.grid2).setOnClickListener {
            binding.rvMovies.layoutManager = GridLayoutManager(requireContext(), 2)
            popupWindow.dismiss()
        }

        popupView.findViewById<TextView>(R.id.grid3).setOnClickListener {
            binding.rvMovies.layoutManager = GridLayoutManager(requireContext(), 3)
            popupWindow.dismiss()
        }

    }
}
