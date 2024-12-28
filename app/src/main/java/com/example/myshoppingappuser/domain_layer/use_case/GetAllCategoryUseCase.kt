package com.example.myshoppingappuser.domain_layer.use_case

import com.example.myshoppingappuser.domain_layer.repo.Repo
import javax.inject.Inject

class GetAllCategoryUseCase @Inject constructor(private val repo: Repo) {

    fun getAllCategoryUseCase() = repo.getAllCategories()

    fun getAllProductsUseCase() = repo.getAllProducts()
}