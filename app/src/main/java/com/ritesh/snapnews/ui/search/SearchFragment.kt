package com.ritesh.snapnews.ui.search

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.transition.MaterialSharedAxis
import com.ritesh.snapnews.R
import com.ritesh.snapnews.adapters.NewsAdapter
import com.ritesh.snapnews.databinding.FragmentSearchBinding
import com.ritesh.snapnews.model.Message
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
                    adapter.submitList(response.data)
                    showMessage(Message.ERROR,response.message)
                    binding.loading.visibility = View.GONE
                }
                is Resource.Loading -> {
                    binding.messageLayout.root.visibility = View.GONE
                    binding.loading.visibility = View.VISIBLE
                }
                is Resource.Success -> {
                    adapter.submitList(response.data)
                    binding.loading.visibility = View.GONE
                    if (response.data.isNullOrEmpty()) {
                        showMessage(Message.SEARCH_EMPTY,"")
                    }
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
        binding.searchInput.setOnEditorActionListener { textView, id, keyEvent ->
            if(id == EditorInfo.IME_ACTION_SEARCH) {
                val query = textView.text.toString().toString()
                if(query.isNotEmpty())
                    viewModel.searchNews(query)
            }
            return@setOnEditorActionListener false
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

    private fun showMessage(error: Message, errorMsg: String?) {
        val messageBinding = binding.messageLayout
        when(error) {
            Message.SEARCH_EMPTY -> {
                messageBinding.messageIcon.setImageResource(R.drawable.ic_search_empty)
                messageBinding.messageTitle.text = getString(R.string.search_empty_title)
                messageBinding.messageSubtitle.text = getString(R.string.empty_search_subtitle)
            }
            Message.ERROR -> {
                messageBinding.messageIcon.setImageResource(R.drawable.ic_network)
                messageBinding.messageTitle.text = errorMsg
                messageBinding.messageSubtitle.text = getString(R.string.network_subtitle)
            }
        }
        messageBinding.root.visibility = View.VISIBLE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null

    }
}