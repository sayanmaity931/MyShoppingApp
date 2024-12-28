package com.example.myshoppingappuser.domain_layer.use_case

import com.example.myshoppingappuser.domain_layer.models.UserData
import com.example.myshoppingappuser.domain_layer.repo.Repo
import javax.inject.Inject

class UpdateUserDataUseCase @Inject constructor(private val repo : Repo) {

    fun updateUserData(userData: UserData) = repo.updateUserData(userData)

}