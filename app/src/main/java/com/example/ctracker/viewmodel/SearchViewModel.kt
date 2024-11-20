package com.example.ctracker.viewmodel

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.ctracker.entity.Food
import com.example.ctracker.repository.mock.MockFoodRepository

class SearchViewModel(val index: Int) : ViewModel() {
    val query: MutableState<String> = mutableStateOf("")
    val results: MutableState<List<Food>> = mutableStateOf(emptyList())
    val hasSearched: MutableState<Boolean> = mutableStateOf(false)

    fun search() {
        hasSearched.value = true
        results.value = MockFoodRepository.search(query.value)
    }

    fun resetSearchState() {
        hasSearched.value = false
        results.value = emptyList()
    }

    fun onQuerySearch(newQuery: String) {
        query.value = newQuery
        if (newQuery.isBlank()) {
            resetSearchState()
        }
    }
}
