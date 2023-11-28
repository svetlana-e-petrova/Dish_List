package com.example.dishlist.data

import java.io.Serializable

data class Dish(
    val id: String,
    val name: String,
    val description: String?,
    val image: String?,
    val price: Int,
    val isChecked: Boolean = false
) : Serializable