package com.example.myshoppingappuser.domain_layer.use_case

import com.example.myshoppingappuser.domain_layer.repo.Repo
import javax.inject.Inject

class LogInWithEmailPassUseCase @Inject constructor(private val repo: Repo)  {

    fun logInWithEmailPassword(email: String, userPassword: String) = repo.logInWithEmailPassword(email,userPassword)

}