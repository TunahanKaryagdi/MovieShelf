package com.tunahankaryagdi.firstproject.ui.favorite

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.tunahankaryagdi.firstproject.R
import com.tunahankaryagdi.firstproject.databinding.FragmentFavoriteBinding
import com.tunahankaryagdi.firstproject.ui.favorite.adapter.FavoriteListAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FavoriteFragment : Fragment(R.layout.fragment_favorite) {
    private val viewModel: FavoriteViewModel by viewModels()
    private lateinit var binding: FragmentFavoriteBinding
    private lateinit var favoriteListAdapter: FavoriteListAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFavoriteBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setAdapters()
        observeUiState()
    }

    private fun observeUiState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.uiState.collectLatest { state ->
                favoriteListAdapter.updateMovies(state.filteredMovies)

            }
        }
    }

    private fun setAdapters() {
        favoriteListAdapter = FavoriteListAdapter()
        binding.rvFavoriteList.adapter = favoriteListAdapter
        with(binding) {
            rvFavoriteList.adapter = favoriteListAdapter
            etFavoriteText.addTextChangedListener { text ->
                viewModel.filterMovies(text.toString())
            }
        }
    }
}