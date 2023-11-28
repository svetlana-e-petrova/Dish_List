package com.example.dishlist.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dishlist.R
import com.example.dishlist.domain.mock.DishInteractor
import com.example.dishlist.presentation.state.DishListScreenState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DishListViewModel
@Inject constructor(
    private val interactor: DishInteractor
) : ViewModel() {

    init {
        viewModelScope.launch { interactor.refresh() }
    }

    private val _isLoading = MutableStateFlow(false)

    private val _dishListScreenState = interactor.getDishesFlow()
        .map { dishList ->
            when {
                dishList == null -> DishListScreenState.Loading
                dishList.isEmpty() -> DishListScreenState.EmptyScreen()
                else -> DishListScreenState.SuccessScreenLoad(dishList)
            }
        }
        .catch { emit(DishListScreenState.Error(R.string.loading_dish_list_error)) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(3000), DishListScreenState.Loading)

    val screenState: Flow<DishListScreenState> =
        combine(_dishListScreenState, _isLoading) { state, isLoading ->
            when (state) {
                is DishListScreenState.EmptyScreen -> state.copy(shouldShowProgress = isLoading)
                is DishListScreenState.Error -> state.copy(shouldShowProgress = isLoading)
                is DishListScreenState.SuccessScreenLoad -> {
                    val hasCheckedItems = state.items.any { it.isChecked }
                    state.copy(
                        shouldEnableButton = hasCheckedItems && !isLoading,
                        shouldShowProgress = isLoading
                    )
                }
                else -> state
            }
        }

    fun checkDish(id: String, isChecked: Boolean) {

        viewModelScope.launch {
            interactor.checkDish(id, isChecked)
        }
    }

    fun removeCheckedDishes() {
        _isLoading.value = true
        viewModelScope.launch {
            interactor.removeCheckedDishes()
            _isLoading.value = false
        }
    }

    fun refresh() {
        _isLoading.value = true
        viewModelScope.launch {
            interactor.refresh()
            _isLoading.value = false
        }
    }
}