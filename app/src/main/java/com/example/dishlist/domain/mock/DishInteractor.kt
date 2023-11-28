package com.example.dishlist.domain.mock

import com.example.dishlist.data.Dish
import kotlinx.coroutines.flow.Flow

interface DishInteractor {

    fun getDishesFlow(): Flow<List<Dish>?>

    suspend fun removeCheckedDishes()

    fun checkDish(id: String, isChecked: Boolean)

    suspend fun refresh()
}