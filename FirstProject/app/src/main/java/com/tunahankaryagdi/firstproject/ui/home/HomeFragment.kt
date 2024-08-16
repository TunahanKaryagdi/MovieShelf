package com.tunahankaryagdi.firstproject.ui.home

import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupWindow
import android.widget.TextView
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.tunahankaryagdi.firstproject.R
import com.tunahankaryagdi.firstproject.databinding.FragmentHomeBinding
import com.tunahankaryagdi.firstproject.ui.base.BaseFragment
import com.tunahankaryagdi.firstproject.ui.components.ViewPagerTransform
import com.tunahankaryagdi.firstproject.ui.home.adapter.HomeMovieListAdapter
import com.tunahankaryagdi.firstproject.ui.home.adapter.HomePopularMoviesAdapter
import com.tunahankaryagdi.firstproject.ui.search.adapter.SearchMovieListAdapter
import com.tunahankaryagdi.firstproject.utils.enums.HomeTab
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding, HomeViewModel>() {
    override val viewModel: HomeViewModel by viewModels()
    private lateinit var homeMovieListAdapter: HomeMovieListAdapter
    private lateinit var homePopularMoviesAdapter: HomePopularMoviesAdapter

    private val handler = Handler(Looper.getMainLooper())
    private val slideTime = 5000L

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


        with(binding.vpPopularMovies) {
            adapter = homePopularMoviesAdapter
            offscreenPageLimit = 3
            getChildAt(0).overScrollMode = RecyclerView.OVER_SCROLL_NEVER
            setPageTransformer(ViewPagerTransform())
        }

        with(binding) {
            rvMovies.adapter = homeMovieListAdapter
            rvMovies.setHasFixedSize(false)
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

            etHomeText.setOnFocusChangeListener { _, hasFocus->
                if (hasFocus){
                    findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToHomeSearchFragment())
                }
            }

            ivOptions.setOnClickListener {
                setupPopupMenu()
            }
            vpPopularMovies.registerOnPageChangeCallback(object :
                ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    handler.removeCallbacks(sliderRunnable)
                    handler.postDelayed(sliderRunnable, slideTime)
                }
            })

            srlRefreshLayout.setOnRefreshListener {
                viewModel.init()
                srlRefreshLayout.isRefreshing = false
            }

        }
    }

    private val sliderRunnable = Runnable {
        val currentItem = binding.vpPopularMovies.currentItem
        val itemCount = binding.vpPopularMovies.adapter?.itemCount ?: 1
        val nextItem = if (currentItem + 1 >= itemCount) 0 else currentItem + 1
        binding.vpPopularMovies.setCurrentItem(nextItem, true)
    }

    override fun observeUiState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.uiState.collect { state ->
                with(binding) {
                    pbHome.visibility = if (state.isLoading) View.VISIBLE else View.INVISIBLE
                }
                homePopularMoviesAdapter.submitList(state.popularMovies)
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.uiState.collectLatest { state ->
                homeMovieListAdapter.submitData(state.movies)
            }
        }

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
