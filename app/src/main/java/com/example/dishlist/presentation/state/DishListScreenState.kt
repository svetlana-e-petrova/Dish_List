package com.example.dishlist.presentation.state

import com.example.dishlist.data.Dish

sealed class DishListScreenState(
    open val shouldShowProgress: Boolean = false
) {

    data object Loading : DishListScreenState(shouldShowProgress = true)

    data class Error(
        val messageRes: Int,
        override val shouldShowProgress: Boolean = false
    ) : DishListScreenState(shouldShowProgress)

    data class SuccessScreenLoad(
        val items: List<Dish>,
        val shouldEnableButton: Boolean = false,
        override val shouldShowProgress: Boolean = false
    ) : DishListScreenState(shouldShowProgress)

    data class EmptyScreen(
        override val shouldShowProgress: Boolean = false
    ) : DishListScreenState(shouldShowProgress)
}