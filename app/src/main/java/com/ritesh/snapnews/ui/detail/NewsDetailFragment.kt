package com.ritesh.snapnews.ui.detail

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.SharedElementCallback
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.transition.MaterialContainerTransform
import com.google.android.material.transition.MaterialSharedAxis
import com.ritesh.snapnews.R
import com.ritesh.snapnews.adapters.VerticalNewsAdapter
import com.ritesh.snapnews.databinding.FragmentNewsDetailBinding
import com.ritesh.snapnews.util.Constants.Companion.PAGE_SIZE
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NewsDetailFragment : Fragment() {

    val PAGINATION_TAG = "Pagination"

    private var _binding: FragmentNewsDetailBinding? = null
    private val binding get() = _binding!!

    private val adapter = VerticalNewsAdapter()
    private val viewModel: NewsDetailViewModel by viewModels()

    private var isLoading = false
    private var isLastPage = false

    private val scrollListener = object : RecyclerView.OnScrollListener() {

        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)

            val layoutManager = recyclerView.layoutManager as LinearLayoutManager
            val visibleItemCount = layoutManager.childCount
            val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
            val totalItemCount = layoutManager.itemCount

            val isOnLastPage = firstVisibleItemPosition + visibleItemCount >= totalItemCount
            val isNotLoadingAndNotLastPage = !isLoading && !isLastPage

            val shouldLoadMoreItems = isOnLastPage && isNotLoadingAndNotLastPage

            Log.d(PAGINATION_TAG, "shouldLoadMore: $shouldLoadMoreItems")
            Log.d(PAGINATION_TAG, "isOnLastPage: $isOnLastPage")
            Log.d(PAGINATION_TAG, "firstVisibleItemPosition: $firstVisibleItemPosition")
            Log.d(PAGINATION_TAG, "visibleItemCount: $visibleItemCount")
            Log.d(PAGINATION_TAG, "isLoading: $isLoading")
            Log.d(PAGINATION_TAG, "isLastPage: $isLastPage")
            Log.d(PAGINATION_TAG, "")
            if (shouldLoadMoreItems) {
                Log.d(PAGINATION_TAG, "Inside load news")
                viewModel.fetchNews()
                isLoading = true
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNewsDetailBinding.inflate(inflater)

        binding.verticalNewsList.adapter = adapter
        binding.verticalNewsList.addOnScrollListener(scrollListener)
        PagerSnapHelper().apply {
            attachToRecyclerView(binding.verticalNewsList)
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.news.observe(viewLifecycleOwner) { news ->
            isLoading = false
            val totalPages = viewModel.totalResults / PAGE_SIZE + 1
            isLastPage = viewModel.pageNumber == totalPages
            // TODO submitList() only works when converted to mutable list
            adapter.submitList(news.toMutableList())
            if (viewModel.scrollPos != -1) {
                binding.verticalNewsList.scrollToPosition(viewModel.scrollPos)
                viewModel.scrollPos = -1
            }
        }

        binding.goBack.setOnClickListener {
            requireActivity().onBackPressed()
        }

        adapter.setDisplayWebViewClickListener { url ->
            val dir = NewsDetailFragmentDirections.actionNewsDetailFragmentToWebViewFragment(url)
            findNavController().navigate(dir)
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        binding.verticalNewsList.clearOnScrollListeners()
        _binding = null
    }

}