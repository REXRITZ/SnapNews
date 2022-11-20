package com.ritesh.snapnews.ui.home

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.tabs.TabLayout
import com.ritesh.snapnews.R
import com.ritesh.snapnews.adapter.NewsAdapter
import com.ritesh.snapnews.databinding.FragmentHomeBinding
import com.ritesh.snapnews.util.Resource
import com.ritesh.snapnews.util.Utils
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
                    adapter.submitList(response.data)
                }
                is Resource.Success -> {
                    adapter.submitList(response.data)
                }
            }
        }

        adapter.setOnNewsClickListener { news ->
            //do something here
        }

        tabSelectListener()
        menuItemClickListener()
    }

    private fun menuItemClickListener() {
        binding.homeToolbar.setOnMenuItemClickListener {
            when(it.itemId) {
                R.id.search -> {
                    true
                }
                R.id.country -> {
                    inflateCountrySelector()
                    true
                }
                else -> false
            }
        }
    }

    private fun inflateCountrySelector() {
        val countries = resources.getStringArray(R.array.country_names)
        val countryCodes = resources.getStringArray(R.array.country_iso)
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Select country")
            .setItems(countries) { dialog, pos ->
                viewModel.setCountry(countryCodes[pos])
                dialog.dismiss()
            }
            .show()
    }

    private fun tabSelectListener() {
        binding.categoryTab.root.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                val selectedCategory = Utils.getNewsCategory(tab.position)
                viewModel.setCategory(selectedCategory)
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {

            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
                binding.newsView.smoothScrollToPosition(0)
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}