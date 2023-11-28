package com.example.dishlist.di

import com.example.dishlist.data.DishRepository
import com.example.dishlist.data.FakeDishRepository
import com.example.dishlist.domain.mock.DishInteractor
import com.example.dishlist.domain.mock.FakeDishInteractor
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class DishModule {

    @Binds
    abstract fun bindDishRepository(
        repository: FakeDishRepository
    ): DishRepository

    @Binds
    abstract fun bindDishInteractor(
        interactor: FakeDishInteractor
    ): DishInteractor
}