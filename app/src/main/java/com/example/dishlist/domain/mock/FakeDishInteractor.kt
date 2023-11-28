package com.example.dishlist.domain.mock

import com.example.dishlist.data.Dish
import com.example.dishlist.data.DishRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import javax.inject.Inject

class FakeDishInteractor
@Inject constructor(
    private val repository: DishRepository
) : DishInteractor {

    private var shouldThrowError = false

    private val _savedDishes = MutableStateFlow<LinkedHashMap<String, Dish>?>(null)
    private val savedDishes: StateFlow<LinkedHashMap<String, Dish>?> = _savedDishes.asStateFlow()

    private val observableDishes: Flow<List<Dish>?> = savedDishes.map {
        if (shouldThrowError) {
            throw Exception("Test exception")
        } else {
            it?.values?.toList()
        }
    }

    override suspend fun refresh() {
        _savedDishes.update {
            LinkedHashMap<String, Dish>().apply {
                repository.getDishes().forEach { dish -> put(dish.id, dish) }
            }
        }
    }

    // todo: add error handling at presentation layer
    fun setShouldThrowError(value: Boolean) {
        shouldThrowError = value
    }

    override fun getDishesFlow(): Flow<List<Dish>?> {
        return observableDishes
    }

    override suspend fun removeCheckedDishes() {
        //remove from local data
        _savedDishes.update { dishes ->
            dishes?.filterValues { !it.isChecked } as LinkedHashMap<String, Dish>
        }
        //call repository method
        _savedDishes.value
            ?.filter { it.value.isChecked }?.keys
            ?.forEach { id ->
                repository.deleteDish(id)
            }
    }

    override fun checkDish(id: String, isChecked: Boolean) {
        _savedDishes.value?.let { map ->
            val dish = map[id]
            if (dish != null) {
                saveDish(dish.copy(isChecked = isChecked))
            }
        }
    }

    private fun saveDish(dish: Dish) {
        _savedDishes.update { dishes ->
            val newTasks = LinkedHashMap<String, Dish>(dishes)
            newTasks[dish.id] = dish
            newTasks
        }
    }
}