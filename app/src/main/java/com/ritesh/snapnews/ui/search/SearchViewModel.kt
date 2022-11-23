package com.ritesh.snapnews.ui.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ritesh.snapnews.model.News
import com.ritesh.snapnews.repository.NewsRepository
import com.ritesh.snapnews.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val newsRepository: NewsRepository
) : ViewModel() {

    private val _searchNews: MutableLiveData<Resource<List<News>>> = MutableLiveData()
    val searchNews: LiveData<Resource<List<News>>> = _searchNews

    private var searchJob: Job? = null

    private var prevQuery: String = ""
    val query get() = prevQuery
    private var _totalResults = 0
    val totalResults get() = _totalResults

    fun searchNews(query: String) {
        if(query.trim().isEmpty() || query == prevQuery) return
        searchJob?.cancel()
        searchJob = viewModelScope.launch(Dispatchers.IO) {
            _searchNews.postValue(Resource.Loading())
            delay(200) // adding delay to wait for user input completion
            try {
                val response = newsRepository.getBreakingNews("",1,"",query)
                if (response.isSuccessful) {
                    response.body()?.let { newsResponse ->
                        prevQuery = query
                        _totalResults = newsResponse.totalResults
                        val news = newsResponse.results.map {
                            it.toNews()
                        }
                        _searchNews.postValue(Resource.Success(news))
                    }
                }
            } catch (e : HttpException) {
                _searchNews.postValue(Resource.Error("Oops, something went wrong!"))
            } catch (e : IOException) {
                _searchNews.postValue(Resource.Error("Couldn't reach server, please check your internet connection!"))
            }
        }
    }
}