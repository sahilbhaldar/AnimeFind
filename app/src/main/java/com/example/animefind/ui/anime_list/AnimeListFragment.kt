package com.example.animefind.ui.anime_list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.animefind.R
import com.example.animefind.data.local.db.AnimeDatabase
import com.example.animefind.databinding.FragmentAnimeListBinding
import com.example.animefind.domain.util.Resource
import com.example.animefind.ui.anime_detail.AnimeDetailFragment
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.launch

class AnimeListFragment : Fragment() {

    private lateinit var viewModel: AnimeListViewModel
    private var _binding: FragmentAnimeListBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: AnimeListAdapter

    private val searchQuery = MutableStateFlow("")
    private var searchJob: Job? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAnimeListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val factory = AnimeListViewModelFactory(
            database = AnimeDatabase.getDatabase(requireContext())
        )
        viewModel = ViewModelProvider(this, factory)[AnimeListViewModel::class.java]

        adapter = AnimeListAdapter { anime ->
            // Navigate to AnimeDetailFragment
            val fragment = AnimeDetailFragment().apply {
                arguments = Bundle().apply {
                    putInt("animeId", anime.id)
                }
            }
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, fragment) // Make sure this is your container ID in activity
                .addToBackStack(null)
                .commit()
        }


        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = adapter

        // Pagination buttons
        binding.btnNext.setOnClickListener {resetSearch()
            viewModel.nextPage() }
        binding.btnPrevious.setOnClickListener { resetSearch()
            viewModel.previousPage() }

        observeAnimeList()
        observePageNumber()
        setupSearch()
    }

    private fun setupSearch() {
        binding.etSearch.addTextChangedListener { editable ->
            searchQuery.value = editable.toString()
        }

        lifecycleScope.launchWhenStarted {
            searchQuery
                .debounce(300) // wait for 300ms after typing
                .collectLatest { query ->
                    if (query.isEmpty()) {
                        // Show full list
                        observeAnimeList()
                    } else {
                        // Filter list based on query
                        val currentList = (viewModel.animeList.value as? Resource.Success)?.data ?: emptyList()

                        val filtered = currentList.filter { anime ->
                            val q = query.trim().lowercase()
                            anime.title.lowercase().contains(q) ||
                                    (anime.genres?.lowercase()?.contains(q) == true) ||
                                    (anime.mainCast?.lowercase()?.contains(q) == true) ||
                                    (anime.synopsis?.lowercase()?.contains(q) == true)
                        }

                        adapter.submitList(filtered)
                        animateRecyclerView()
                    }
                }
        }
    }

    private fun resetSearch(){
        binding.etSearch.setText("")
    }
    private fun animateRecyclerView() {
        binding.recyclerView.apply {
            alpha = 0f
            translationY = -50f
            animate()
                .alpha(1f)
                .translationY(0f)
                .setDuration(300)
                .start()
        }
    }

    private fun observeAnimeList() {
        lifecycleScope.launch {
            viewModel.animeList.collectLatest { resource ->
                when (resource) {
                    is Resource.Idle -> {}
                    is Resource.Loading -> binding.progressBar.visibility = View.VISIBLE
                    is Resource.Success -> {
                        binding.progressBar.visibility = View.GONE
                        adapter.submitList(resource.data)
                        // Optional: scroll to top when page changes
                        binding.recyclerView.scrollToPosition(0)
                    }
                    is Resource.Error -> {
                        binding.progressBar.visibility = View.GONE
                        Toast.makeText(requireContext(), resource.message, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    private fun observePageNumber() {
        lifecycleScope.launch {
            viewModel.currentPage.collect { page ->
                binding.tvPageNumber.text = page.toString()
                binding.btnPrevious.isEnabled = page > 1
            }
        }
    }
}
