package com.example.myshoppingappuser.domain_layer.use_case

import com.example.myshoppingappuser.domain_layer.repo.Repo
import javax.inject.Inject

class GetProductByIdUseCase @Inject constructor(private val repo: Repo) {

    fun getProductById(productId: String) = repo.getProductById(productId)

}