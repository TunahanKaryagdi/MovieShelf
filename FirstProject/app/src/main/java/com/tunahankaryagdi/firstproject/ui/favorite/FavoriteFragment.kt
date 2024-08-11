package com.tunahankaryagdi.firstproject.ui.favorite

import android.app.AlertDialog
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
import com.tunahankaryagdi.firstproject.domain.model.Movie
import com.tunahankaryagdi.firstproject.ui.components.CustomDialog
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
                with(binding){
                    if (state.filteredMovies.isEmpty()){
                        rvFavoriteList.visibility = View.INVISIBLE
                        llFavoriteEmptyResult.visibility = View.VISIBLE
                    }
                    else{
                        rvFavoriteList.visibility = View.VISIBLE
                        llFavoriteEmptyResult.visibility = View.INVISIBLE
                    }
                }
            }
        }
    }

    private fun setAdapters() {
        favoriteListAdapter = FavoriteListAdapter(::onClickDeleteItem)
        binding.rvFavoriteList.adapter = favoriteListAdapter
        with(binding) {
            rvFavoriteList.adapter = favoriteListAdapter
            etFavoriteText.addTextChangedListener { text ->
                viewModel.filterMovies(text.toString())
            }
        }
    }

    private fun onClickDeleteItem(movie: Movie){
        val dialog = CustomDialog(
            onConfirm = {
                viewModel.deleteFavoriteMovie(movie)
            },
        )
        dialog.show(parentFragmentManager,"Custom Dialog")
    }

}