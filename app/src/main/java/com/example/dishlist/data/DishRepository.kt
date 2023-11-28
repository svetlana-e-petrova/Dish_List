package com.example.dishlist.data

interface DishRepository {

    suspend fun getDishes(): List<Dish>

    suspend fun deleteDish(id: String)

    suspend fun getDishById(id: String): Dish?
}