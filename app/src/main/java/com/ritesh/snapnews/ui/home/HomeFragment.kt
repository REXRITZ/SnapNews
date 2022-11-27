package com.ritesh.snapnews.ui.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.doOnPreDraw
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.tabs.TabLayout
import com.google.android.material.transition.Hold
import com.google.android.material.transition.MaterialElevationScale
import com.google.android.material.transition.MaterialSharedAxis
import com.google.android.material.transition.platform.MaterialContainerTransformSharedElementCallback
import com.ritesh.snapnews.R
import com.ritesh.snapnews.adapters.NewsAdapter
import com.ritesh.snapnews.databinding.FragmentHomeBinding
import com.ritesh.snapnews.model.Message
import com.ritesh.snapnews.model.NewsDetailArg
import com.ritesh.snapnews.util.Constants.Companion.CATEGORY_POS
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

    private var tabPosition = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater)

        binding.newsView.adapter = adapter
        binding.categoryTab.root.getTabAt(tabPosition)?.select()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.breakingNews.observe(viewLifecycleOwner) { response ->
            when(response) {
                is Resource.Error -> {
                    adapter.submitList(response.data)
                    showMessage(Message.ERROR, response.message)
                    binding.loading.visibility = View.GONE
                }
                is Resource.Loading -> {
                    adapter.submitList(response.data)
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

        adapter.setOnNewsClickListener { pos ->
            val dir = HomeFragmentDirections.actionHomeFragmentToNewsDetailFragment(
                NewsDetailArg(
                    data = viewModel.breakingNews.value!!.data!!,
                    scrollPosition = pos,
                    searchMode = false,
                    countryCode = viewModel.countryCode,
                    category = viewModel.category,
                    totalResults = viewModel.totalResults
                )
            )
            findNavController().navigate(dir)
        }

        tabSelectListener()
        menuItemClickListener()
    }

    private fun menuItemClickListener() {
        binding.homeToolbar.setOnMenuItemClickListener {
            when(it.itemId) {
                R.id.search -> {
                    exitTransition = MaterialSharedAxis(MaterialSharedAxis.Z, /* forward= */ true)
                    reenterTransition = MaterialSharedAxis(MaterialSharedAxis.Z, /* forward= */ false)
                    findNavController().navigate(R.id.action_homeFragment_to_searchFragment)
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
                tabPosition = tab.position
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {

            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
                binding.newsView.smoothScrollToPosition(0)
            }
        })
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