package com.example.myshoppingappuser.domain_layer.use_case

import com.example.myshoppingappuser.domain_layer.repo.Repo
import javax.inject.Inject

class GetProductBySearchUseCase @Inject constructor(private val repo: Repo) {

    fun getProductBySearchUseCase(query: String) = repo.searchProduct(query)

}