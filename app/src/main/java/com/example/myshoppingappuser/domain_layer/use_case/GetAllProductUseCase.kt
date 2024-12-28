package com.example.myshoppingappuser.domain_layer.use_case

import com.example.myshoppingappuser.domain_layer.repo.Repo
import javax.inject.Inject

class GetAllProductUseCase @Inject constructor(private val repo: Repo)  {

    fun getAllProductsUseCase() = repo.getAllProducts()

}