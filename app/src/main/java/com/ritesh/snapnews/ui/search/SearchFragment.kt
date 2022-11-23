package com.ritesh.snapnews.ui.search

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.transition.MaterialSharedAxis
import com.ritesh.snapnews.adapters.NewsAdapter
import com.ritesh.snapnews.databinding.FragmentSearchBinding
import com.ritesh.snapnews.model.NewsDetailArg
import com.ritesh.snapnews.util.Resource
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchFragment : Fragment() {

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

    private val adapter = NewsAdapter()
    private val viewModel: SearchViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enterTransition = MaterialSharedAxis(MaterialSharedAxis.Z, /* forward= */ true)
        returnTransition = MaterialSharedAxis(MaterialSharedAxis.Z, /* forward= */ false)
    }

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

        binding.searchInput.requestFocus()
        val imm = requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)

        viewModel.searchNews.observe(viewLifecycleOwner) { response ->
            when(response) {
                is Resource.Error -> {
                    Toast.makeText(requireContext(), response.message, Toast.LENGTH_LONG).show()
                    binding.loading.visibility = View.GONE
                }
                is Resource.Loading -> {
                    binding.loading.visibility = View.VISIBLE
                }
                is Resource.Success -> {
                    adapter.submitList(response.data)
                    binding.loading.visibility = View.GONE
                }
            }
        }

        binding.searchToolbar.setNavigationOnClickListener {
            //hide keyboard
            val imm = requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(requireActivity().window.currentFocus?.windowToken,0)
            requireActivity().onBackPressed()
        }

        binding.searchInput.addTextChangedListener { editable ->
            editable?.let {
                viewModel.searchNews(it.toString())
            }
        }

        adapter.setOnNewsClickListener { pos ->
            val dir = SearchFragmentDirections.actionSearchFragmentToNewsDetailFragment(
                NewsDetailArg(
                    data = viewModel.searchNews.value!!.data!!,
                    scrollPosition = pos,
                    searchMode = true,
                    query = viewModel.query,
                    totalResults = viewModel.totalResults
                )
            )
            findNavController().navigate(dir)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null

    }
}