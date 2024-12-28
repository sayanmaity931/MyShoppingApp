package com.example.myshoppingappuser.domain_layer.use_case

import com.example.myshoppingappuser.domain_layer.repo.Repo
import javax.inject.Inject

class GetUserByIdUseCase @Inject constructor(private val repo: Repo)  {

    fun getUserById (uid : String) = repo.getUserById(uid)

}