package com.example.myshoppingappuser.domain_layer.repo

import com.example.myshoppingappuser.common.State
import com.example.myshoppingappuser.domain_layer.models.CartModel
import com.example.myshoppingappuser.domain_layer.models.Category
import com.example.myshoppingappuser.domain_layer.models.ProductDataModel
import com.example.myshoppingappuser.domain_layer.models.UserData
import kotlinx.coroutines.flow.Flow

interface Repo {

    fun registerUserWithEmailPassword(userData: UserData) : Flow<State<String>>

    fun getAllCategories(): Flow<State<List<Category>>>

    fun getAllProducts(): Flow<State<List<ProductDataModel>>>

    fun logInWithEmailPassword(email: String, userPassword: String): Flow<State<String>>

    fun getProductById(productId : String) : Flow<State<ProductDataModel>>

    fun getUserById(uid : String) : Flow<State<UserData>>

    fun updateUserData(userData: UserData) : Flow<State<String>>

    fun addLikedProduct(productId : String , uid : String) : Flow<State<String>>

    fun loadLikedProducts(uid : String) : Flow<State<List<ProductDataModel>>>

    fun removeLikedProduct(productId : String , uid : String) : Flow<State<String>>

    fun searchProduct(query : String) : Flow<State<List<ProductDataModel>>>

    fun removeProductCapacity(productId: String, capacity: Int): Flow<State<String>>
    fun productInCartPerform(cartModel: CartModel): Flow<State<String>>
    fun getAllCartProducts(): Flow<State<List<CartModel>>>
    fun checkProductInCartOrNot(productId: String): Flow<State<Boolean>>
}