package com.ritesh.snapnews.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ritesh.snapnews.model.News
import com.ritesh.snapnews.network.dto.NewsResponse
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
class HomeViewModel @Inject constructor(
    private val newsRepository: NewsRepository
) : ViewModel() {

    private val _breakingNews: MutableLiveData<Resource<List<News>>> = MutableLiveData()
    val breakingNews: LiveData<Resource<List<News>>> = _breakingNews

    private var country = "us"
    private var _category = ""
    private var totalResults: Int = 0
    private var pageNumber: Int = 1

    private var job: Job? = null

    init {
        getBreakingNews()
    }

    private fun getBreakingNews() {
        job?.cancel()
        job = viewModelScope.launch(Dispatchers.IO) {
            _breakingNews.postValue(Resource.Loading())
            delay(200) // adding delay in case of rapidly switching between tabs
            try {
                val response = newsRepository.getBreakingNews(country, pageNumber, _category)
                if (response.isSuccessful) {
                    response.body()?.let { newsResponse ->
                        pageNumber++
                        totalResults = newsResponse.totalResults
                        val news = newsResponse.articles.map {
                            it.toNews()
                        }
                        _breakingNews.postValue(Resource.Success(news))
                    }
                }
            } catch (e : HttpException) {
                _breakingNews.postValue(Resource.Error("Oops, something went wrong!"))
            } catch (e : IOException) {
                _breakingNews.postValue(Resource.Error("Couldn't reach server, please check your internet connection!"))
            }
        }
    }

    fun setCategory(value: String) {
        _category = value
        pageNumber = 1
        getBreakingNews()
    }

    fun setCountry(value: String) {
        if(value == country) return
        country = value
        pageNumber = 1
        getBreakingNews()
    }
}