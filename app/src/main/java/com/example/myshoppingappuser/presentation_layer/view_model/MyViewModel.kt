package com.example.myshoppingappuser.presentation_layer.view_model


import android.system.Os.remove
import android.util.Log
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myshoppingappuser.common.State
import com.example.myshoppingappuser.domain_layer.models.CartModel
import com.example.myshoppingappuser.domain_layer.models.Category
import com.example.myshoppingappuser.domain_layer.models.ProductDataModel
import com.example.myshoppingappuser.domain_layer.models.UserData
import com.example.myshoppingappuser.domain_layer.repo.Repo
import com.example.myshoppingappuser.domain_layer.use_case.FetchProductUuidUsecase
import com.example.myshoppingappuser.domain_layer.use_case.GetAllCategoryUseCase
import com.example.myshoppingappuser.domain_layer.use_case.GetAllProductUseCase
import com.example.myshoppingappuser.domain_layer.use_case.GetProductByIdUseCase
import com.example.myshoppingappuser.domain_layer.use_case.GetProductBySearchUseCase
import com.example.myshoppingappuser.domain_layer.use_case.GetUserByIdUseCase
import com.example.myshoppingappuser.domain_layer.use_case.LogInWithEmailPassUseCase
import com.example.myshoppingappuser.domain_layer.use_case.RegisterWithUserUseCase
import com.example.myshoppingappuser.domain_layer.use_case.UpdateUserDataUseCase
import com.example.myshoppingappuser.user_pref.UserPreferenceManager
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.collections.toMutableList

@HiltViewModel
class MyViewModel @Inject constructor(
    private val getAllCategory : GetAllCategoryUseCase,
    private val getAllProducts : GetAllProductUseCase,
    private val registerWithUserUseCase: RegisterWithUserUseCase,
    private val logInWithEmailPassUseCase: LogInWithEmailPassUseCase,
    private val getProductByIdUseCase: GetProductByIdUseCase,
    private val getUserByIdUseCase: GetUserByIdUseCase,
    private val updateUserDataUseCase: UpdateUserDataUseCase,
    private val getProductBySearchUseCase : GetProductBySearchUseCase,
    private val repo : Repo,
    val firebaseAuth: FirebaseAuth
) : ViewModel() {

    private val _getAllCategoryState = MutableStateFlow(GetAllCategoryState())
    val getAllCategoryState = _getAllCategoryState.asStateFlow()

    private val _getAllProductsState = MutableStateFlow(GetAllProductsState())
    val getAllProductsState = _getAllProductsState.asStateFlow()

    private val _registerUserState = MutableStateFlow(UserDataState())
    val registerUserState = _registerUserState.asStateFlow()

    private val _logInUserState = MutableStateFlow(LogInUserState())
    val logInUserState = _logInUserState.asStateFlow()

    private val _getProductByIdState = MutableStateFlow(GetAllProductByIdState())
    val getProductByIdState = _getProductByIdState.asStateFlow()

    private val _getUserByIdState = MutableStateFlow(GetUserByIdState())
    val getUserByIdState = _getUserByIdState.asStateFlow()

    private val _recentlyViewedProducts = MutableStateFlow<List<String>>(emptyList())
    val recentlyViewedProducts: StateFlow<List<String>> = _recentlyViewedProducts.asStateFlow()

    private val _updateUserResponse = MutableStateFlow(UserDataState())
    val updateUserResponse: StateFlow<UserDataState> = _updateUserResponse.asStateFlow()

    private val _likeAddedResponse = MutableStateFlow(GetAllProductsState())
    val likeAddedResponse: StateFlow<GetAllProductsState> = _likeAddedResponse.asStateFlow()

    //     Manage liked products locally as a Set of product IDs
    private val _likedProducts = MutableStateFlow<Set<String>>(emptySet())
    val likedProducts: StateFlow<Set<String>> = _likedProducts.asStateFlow()

    private val _getProductBySearchState = MutableStateFlow(GetProductBySearchState())
    val getProductBySearchState = _getProductBySearchState.asStateFlow()

    private val _addToCart = MutableStateFlow(AddCartState())
    val addToCart = _addToCart.asStateFlow()

    private val _cartProducts = MutableStateFlow(GetCartState())
    val cartProducts = _cartProducts.asStateFlow()

    private val _isAdded = MutableStateFlow(false)
    val isAdded = _isAdded.asStateFlow()

    val _removeCapacity = MutableStateFlow("")

    val _searchQuery = MutableStateFlow("")


    fun onSearchQueryChange(query: String) {
        _searchQuery.value = query
    }

    fun searchQuery() {
        viewModelScope.launch(Dispatchers.IO) {
            _searchQuery.debounce(500).distinctUntilChanged()
                .collect {
                    if (it.isNotEmpty()) {
                        searchProduct(query = it)
                    }
                }
        }
    }

    fun searchProduct(query: String) {
        viewModelScope.launch(Dispatchers.IO) {

            getProductBySearchUseCase.getProductBySearchUseCase(query).collect {
                when (it) {
                    is State.Success -> {
                        _getProductBySearchState.value =
                            GetProductBySearchState(data = it.data, isLoading = false)
                    }

                    is State.Error -> {
                        _getProductBySearchState.value =
                            GetProductBySearchState(error = it.exception, isLoading = false)
                    }

                    is State.Loading -> {
                        _getProductBySearchState.value = GetProductBySearchState(isLoading = true)
                    }
                }
            }
        }
    }

    fun getUserById(uid: String) {
        viewModelScope.launch(Dispatchers.IO) {
            getUserByIdUseCase.getUserById(uid).collect {
                when (it) {
                    is State.Success -> {
                        _getUserByIdState.value =
                            GetUserByIdState(data = it.data, isLoading = false)
                    }

                    is State.Error -> {
                        _getUserByIdState.value =
                            GetUserByIdState(error = it.exception, isLoading = false)
                    }

                    is State.Loading -> {
                        _getUserByIdState.value = GetUserByIdState(isLoading = true)
                    }
                }
            }
        }
    }

    fun getProductById(productId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            getProductByIdUseCase.getProductById(productId).collect {
                when (it) {
                    is State.Success -> {
                        _getProductByIdState.value =
                            GetAllProductByIdState(data = it.data, isLoading = false)
                    }

                    is State.Error -> {
                        _getProductByIdState.value =
                            GetAllProductByIdState(error = it.exception, isLoading = false)
                    }

                    is State.Loading -> {
                        _getProductByIdState.value = GetAllProductByIdState(isLoading = true)
                    }
                }
            }
        }
    }

    fun logInUser(email: String, userPassword: String) {
        viewModelScope.launch(Dispatchers.IO) {
            logInWithEmailPassUseCase.logInWithEmailPassword(email, userPassword).collect {
                when (it) {
                    is State.Success -> {
                        _logInUserState.value =
                            LogInUserState(userData = it.data, isLoading = false)
                    }

                    is State.Error -> {
                        _logInUserState.value =
                            LogInUserState(error = it.exception, isLoading = false)
                    }

                    is State.Loading -> {
                        _logInUserState.value =
                            LogInUserState(isLoading = true)
                    }
                }
            }
        }
    }

    fun getAllCategories() {
        viewModelScope.launch(Dispatchers.IO) {
            getAllCategory.getAllCategoryUseCase().collect {
                when (it) {
                    is State.Success -> {
                        _getAllCategoryState.value =
                            GetAllCategoryState(data = it.data, isLoading = false)
                    }

                    is State.Error -> {
                        _getAllCategoryState.value =
                            GetAllCategoryState(error = it.exception, isLoading = false)
                    }

                    is State.Loading -> {
                        _getAllCategoryState.value =
                            GetAllCategoryState(isLoading = true)
                    }
                }
            }
        }
    }

    fun getAllProducts() {
        viewModelScope.launch(Dispatchers.IO) {
            getAllProducts.getAllProductsUseCase().collectLatest {
                when (it) {
                    is State.Success -> {
                        _getAllProductsState.value =
                            GetAllProductsState(data = it.data, isLoading = false)
                    }

                    is State.Error -> {
                        _getAllProductsState.value =
                            GetAllProductsState(error = it.exception, isLoading = false)
                    }

                    is State.Loading -> {
                        _getAllProductsState.value = GetAllProductsState(isLoading = true)
                    }
                }
            }
        }
    }


    fun updateUserData(userData: UserData) {
        viewModelScope.launch {
            updateUserDataUseCase.updateUserData(userData).collectLatest {
                when (it) {
                    is State.Success -> {
                        _updateUserResponse.value =
                            UserDataState(data = it.data, isLoading = false)
                    }

                    is State.Error -> {
                        _updateUserResponse.value =
                            UserDataState(error = it.exception, isLoading = false)
                    }

                    is State.Loading -> {
                        _updateUserResponse.value =
                            UserDataState(isLoading = true)
                    }
                }
            }
        }
    }


    // Load liked products from the backend (Firestore or similar)
    fun loadLikedProducts(uid: String) {
        viewModelScope.launch(Dispatchers.IO) {
            repo.loadLikedProducts(uid).collect { likedProductsState ->
                when (likedProductsState) {
                    is State.Loading -> {
                        _likeAddedResponse.value = GetAllProductsState(isLoading = true)
                    }

                    is State.Success -> {
                        val likedIds = likedProductsState.data.map { it.productID }.toSet()
                        _likedProducts.value = likedIds as Set<String>
                        _likeAddedResponse.value =
                            GetAllProductsState(data = likedProductsState.data, isLoading = false)
                    }

                    is State.Error -> {
                        _likeAddedResponse.value = GetAllProductsState(
                            error = likedProductsState.exception,
                            isLoading = false
                        )
                    }
                }
            }
        }
    }

    // Add product to liked list
    fun addLikedProduct(productId: String, uid: String) {
        viewModelScope.launch(Dispatchers.IO) {
            repo.addLikedProduct(productId, uid).collect { result ->
                when (result) {
                    is State.Success -> {
                        _likedProducts.value = _likedProducts.value + productId
                        _likeAddedResponse.value = GetAllProductsState(
                            data = _likedProducts.value.toList()
                                .map { productId -> ProductDataModel(productID = productId.toString()) },
                            isLoading = false
                        )
                    }

                    is State.Error -> Log.e(
                        "ViewModel",
                        "Error adding liked product: ${result.exception}"
                    )

                    State.Loading -> {
                        _likeAddedResponse.value = GetAllProductsState(isLoading = true)
                    }
                }
            }
        }
    }

    // Remove product from liked list
    fun removeLikedProduct(productId: String, uid: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _likeAddedResponse.value = GetAllProductsState(isLoading = true) // Show loading state

            repo.removeLikedProduct(productId, uid).collect { result ->
                when (result) {
                    is State.Success -> {

                        _likedProducts.value = _likedProducts.value - productId
                        // Update the response state
                        _likeAddedResponse.value = GetAllProductsState(
                            data = _likedProducts.value.toList()
                                .map { productId -> ProductDataModel(productID = productId.toString()) },
                            isLoading = false
                        )
                    }

                    is State.Error -> {
                        // Handle error case
                        _likeAddedResponse.value =
                            GetAllProductsState(error = result.exception, isLoading = false)
                    }

                    is State.Loading -> {
                        _likeAddedResponse.value = GetAllProductsState(isLoading = true)
                    }
                }
            }
        }
    }



    fun registerUser(userData: UserData) {
        viewModelScope.launch(Dispatchers.IO) {
            registerWithUserUseCase.registerUserWithEmailPasswordUseCase(userData = userData)
                .collect {
                    when (it) {
                        is State.Success -> {
                            _registerUserState.value =
                                UserDataState(data = it.data, isLoading = false)
                        }

                        is State.Error -> {
                            _registerUserState.value =
                                UserDataState(error = it.exception, isLoading = false)
                        }

                        is State.Loading -> {
                            _registerUserState.value =
                                UserDataState(isLoading = true)
                        }
                    }
                }
        }
    }

    fun removeCapacity(productId: String, capacity: Int) {
        viewModelScope.launch {
            repo.removeProductCapacity(productId, capacity).collect {
                _removeCapacity.value = it.toString()
            }
        }
    }

    fun addToCart(cartModel: CartModel) {
        viewModelScope.launch {
           repo.productInCartPerform(cartModel).collect{
               when(it){
                   is State.Success -> {
                       _addToCart.value = AddCartState(data = CartModel(it.data), isLoading = false)
                   }
                    is State.Error -> {
                        _addToCart.value = AddCartState(error = it.exception, isLoading = false)
                    }
                    is State.Loading -> {
                        _addToCart.value = AddCartState(isLoading = true)
                    }
               }
           }
        }
    }

    fun getAllCartProducts() {
        viewModelScope.launch {
            repo.getAllCartProducts().collect{

                when(it){
                    is State.Success -> {
                        _cartProducts.value = GetCartState(data = it.data, isLoading = false)
                    }
                    is State.Error -> {
                        _cartProducts.value = GetCartState(error = it.exception, isLoading = false)
                    }
                    is State.Loading -> {
                        _cartProducts.value = GetCartState(isLoading = true)
                    }
                }
            }
        }
    }

    fun isAddedToCart(productId: String) {
        viewModelScope.launch {
            repo.checkProductInCartOrNot(productId).collectLatest{
                _isAdded.value = it.toString().toBooleanStrict()
                Log.d("value", it.toString())
                Log.d("value2", _isAdded.value.toString())
            }
        }
    }

    init {
        getAllCategories()
        getAllProducts()
        loadLikedProducts(firebaseAuth.currentUser?.uid ?: "")
        getAllCartProducts()
    }
}
    data class GetAllCategoryState(

        val isLoading: Boolean = false,
        val error: String = "",
        val data: List<Category> = emptyList()

    )

    data class GetAllProductsState(

        val isLoading: Boolean = false,
        val error: String = "",
        val data: List<ProductDataModel> = emptyList()

    )

    data class UserDataState(

        val isLoading: Boolean = false,
        val error: String = "",
        val data: String = ""
    )

    data class LogInUserState(

        val isLoading: Boolean = false,
        val error: String = "",
        val userData: String = ""

    )

    data class GetAllProductByIdState(

        val isLoading: Boolean = false,
        val error: String = "",
        val data: ProductDataModel? = null

    )

    data class GetUserByIdState(

        val isLoading: Boolean = false,
        val error: String = "",
        val data: UserData? = null

    )

    data class GetProductBySearchState(

        val isLoading: Boolean = false,
        val error: String = "",
        val data: List<ProductDataModel> = emptyList()

    )

    data class AddCartState(
         val isLoading: Boolean = false,
        val error: String = "",
        val data: CartModel = CartModel()
        )

    data class GetCartState(
        val isLoading: Boolean = false,
        val error: String = "",
        val data: List<CartModel> = emptyList()
    )



