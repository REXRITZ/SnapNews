package com.ritesh.snapnews.ui.detail

import android.util.Log
import androidx.lifecycle.*
import com.ritesh.snapnews.model.News
import com.ritesh.snapnews.model.NewsDetailArg
import com.ritesh.snapnews.repository.NewsRepository
import com.ritesh.snapnews.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class NewsDetailViewModel @Inject constructor(
    private val newsRepository: NewsRepository,
    savedStateHandle: SavedStateHandle
): ViewModel() {

    val TAG = "NewsDetailViewModel: "

    private val _news = MutableLiveData<List<News>>()
    val news: LiveData<List<News>> = _news

    var scrollPos: Int = 0
    private var query: String? = null
    private var category: String? = null
    private var countryCode: String? = null
    private var fromSearch: Boolean = false
    var pageNumber: Int = 1
    private var _totalResults: Int = 0
    val totalResults get() = _totalResults

    init {
        savedStateHandle.get<NewsDetailArg>("newsinfo")?.let {
            _news.value = it.data
            scrollPos = it.scrollPosition
            _totalResults = it.totalResults
            if (it.searchMode) {
                fromSearch = true
                query = it.query
            } else {
                category = it.category
                countryCode = it.countryCode
            }
        }
    }

    fun fetchNews() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response =
                    if (fromSearch)
                        newsRepository.getBreakingNews("", pageNumber, "", query!!)
                    else
                        newsRepository.getBreakingNews(countryCode!!, pageNumber, category!!)

                if (response.isSuccessful) {
                    response.body()?.let { newsResponse ->
                        pageNumber = newsResponse.nextPage
                        _totalResults = newsResponse.totalResults
                        Log.d(TAG, "Total: $totalResults")
                        val oldNews = _news.value as MutableList
                        val news = newsResponse.results.map {
                            it.toNews()
                        }
                        oldNews.addAll(news)
                        _news.postValue(oldNews)
                    }
                }
                println(response.code())
            } catch (e : HttpException) {
                //todo
                println("EEEEE")
            } catch (e : IOException) {
                //todo
                println("WWWWWWW")
            }
        }
    }
}