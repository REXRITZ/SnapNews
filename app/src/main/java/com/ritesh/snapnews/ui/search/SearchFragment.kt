package com.ritesh.snapnews.ui.search

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import com.ritesh.snapnews.R
import com.ritesh.snapnews.adapter.NewsAdapter
import com.ritesh.snapnews.databinding.FragmentSearchBinding
import com.ritesh.snapnews.util.Resource
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchFragment : Fragment() {

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

    private val adapter = NewsAdapter()

    private val viewModel: SearchViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(layoutInflater)

        binding.searchNewsList.adapter = adapter

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.searchNews.observe(viewLifecycleOwner) { response ->
            when(response) {
                is Resource.Error -> {

                }
                is Resource.Loading -> {
                }
                is Resource.Success -> {
                    adapter.submitList(response.data)
                }
            }
        }

        binding.searchToolbar.setNavigationOnClickListener {
            requireActivity().onBackPressed()
        }


        binding.searchInput.addTextChangedListener { editable ->
            editable?.let {
                viewModel.searchNews(it.toString())
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null

    }
}