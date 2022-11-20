package com.ritesh.snapnews.ui.home

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.ritesh.snapnews.R
import com.ritesh.snapnews.adapter.NewsAdapter
import com.ritesh.snapnews.databinding.FragmentHomeBinding
import com.ritesh.snapnews.util.Resource
import dagger.hilt.android.AndroidEntryPoint

/*Home Fragment contains list of trending news, option to search
news and list of news sorted by category
 */
@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    val adapter: NewsAdapter = NewsAdapter()
    private val viewModel: HomeViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater)

        binding.newsView.adapter = adapter

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.breakingNews.observe(viewLifecycleOwner) { response ->
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

        adapter.setOnNewsClickListener { news ->
            //do something here
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}