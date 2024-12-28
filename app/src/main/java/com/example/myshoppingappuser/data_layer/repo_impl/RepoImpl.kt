package com.example.myshoppingappuser.data_layer.repo_impl

import android.util.Log
import com.example.myshoppingappuser.common.ADD_TO_CART
import com.example.myshoppingappuser.common.ADD_TO_CART_BY_USER
import com.example.myshoppingappuser.common.CATEGORY
import com.example.myshoppingappuser.common.PRODUCT
import com.example.myshoppingappuser.common.State
import com.example.myshoppingappuser.domain_layer.models.CartModel
import com.example.myshoppingappuser.domain_layer.models.Category
import com.example.myshoppingappuser.domain_layer.models.ProductDataModel
import com.example.myshoppingappuser.domain_layer.models.UserData
import com.example.myshoppingappuser.domain_layer.repo.Repo
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class RepoImpl @Inject constructor(private val firebase: FirebaseFirestore , private val firebaseAuth: FirebaseAuth ) : Repo {

    override fun registerUserWithEmailPassword(userData: UserData): Flow<State<String>> =
        callbackFlow {
            trySend(State.Loading)
            firebaseAuth.createUserWithEmailAndPassword(
                userData.email,
                userData.password
            )
                .addOnSuccessListener { // Basically here we have created a new collection in the firebase named USERS to store the uuid of every user
                    firebase.collection("USERS").document(it.user!!.uid).set(userData)
                        .addOnSuccessListener {
                            trySend(State.Success("User Registered Successfully"))
                        }.addOnFailureListener {
                            trySend(State.Error(it.message.toString()))
                        }
                    updateFcmToken(firebaseAuth.currentUser?.uid.toString())
                }.addOnFailureListener {
                trySend(State.Error(it.message.toString()))
            }
            awaitClose {
                close()
            }
        }

    override fun getAllCategories(): Flow<State<List<Category>>> = callbackFlow {
        trySend(State.Loading)

        firebase.collection(CATEGORY).get().addOnSuccessListener {
            val categories =
                it.documents.mapNotNull {
                    it.toObject(Category::class.java)
                }

            // It will directly fetch the document from the CATEGORY collection
            // and here toObject is used because the data we are fetching is firebase type
            // so we have to convert it

            trySend(State.Success(categories))
        }.addOnFailureListener {
            trySend(State.Error(it.message.toString()))
        }
        awaitClose {
            close()
        }
    }

    override fun getAllProducts(): Flow<State<List<ProductDataModel>>> = callbackFlow {
        trySend(State.Loading)

        firebase.collection(PRODUCT).get().addOnSuccessListener {
            val products =
                it.documents.mapNotNull {
                    it.toObject(ProductDataModel::class.java)
                }

            // It will directly fetch the document from the CATEGORY collection
            // and here toObject is used because the data we are fetching is firebase type
            // so we have to convert it

            trySend(State.Success(products))
        }.addOnFailureListener {
            trySend(State.Error(it.message.toString()))
        }
        awaitClose {
            close()
        }

    }

    override fun logInWithEmailPassword(
        email: String,
        userPassword: String
    ): Flow<State<String>> = callbackFlow {

        trySend(State.Loading)

        firebaseAuth.signInWithEmailAndPassword(email, userPassword).addOnSuccessListener {
            trySend(State.Success("User Logged In Successfully"))
            updateFcmToken(firebaseAuth.currentUser?.uid.toString())
        }.addOnFailureListener {
            trySend(State.Error(it.message.toString()))
        }

        awaitClose {
            close()
        }
    }

    override fun getProductById(productId: String): Flow<State<ProductDataModel>> = callbackFlow {

        if (productId.isBlank()) {
            trySend(State.Error("Invalid product ID"))
            close()
            return@callbackFlow
        }

        trySend(State.Loading)

        firebase.collection(PRODUCT).document(productId).get().addOnSuccessListener { document ->

            val data = document.id
            Log.d("UUID678","UUID : $data")

            val product = document.toObject(ProductDataModel::class.java)

            if (product != null) {
                trySend(State.Success(product))
            } else {
                trySend(State.Error("Product not found"))
            }
        }.addOnFailureListener {
            trySend(State.Error(it.message.toString()))
        }

        awaitClose {
            close()
        }

    }

    override fun getUserById(uid: String): Flow<State<UserData>> = callbackFlow {

        trySend(State.Loading)

        firebase.collection("USERS").document(uid).get().addOnSuccessListener {
            val user = it.toObject(UserData::class.java)!!.apply {
                this.uid = it.id
            }
            trySend(State.Success(user))
        }.addOnFailureListener {
            trySend(State.Error(it.message.toString()))
        }

        awaitClose {
            close()
        }
    }

    override fun updateUserData(userData: UserData): Flow<State<String>> = callbackFlow{

        trySend(State.Loading)

        firebase.collection("USERS").document(userData.uid).set(userData).addOnSuccessListener {
            trySend(State.Success("User Data Updated Successfully"))
        }.addOnFailureListener {
            trySend(State.Error(it.message.toString()))
        }

        awaitClose {
            close()
        }

    }

    override fun addLikedProduct(productId: String, uid: String): Flow<State<String>> = callbackFlow {

        if (productId.isBlank()) {
            trySend(State.Error("Invalid product ID"))
            close()
            return@callbackFlow
        }

        trySend(State.Loading)

        try {
            val userRef = firebase.collection("USERS").document(uid)

            userRef.get().addOnSuccessListener { document ->
                if (document.exists()) {
                    // If the document exists, add the product to the LIKED_PRODUCTS array
                    userRef.update("LIKED_PRODUCTS", FieldValue.arrayUnion(productId))
                        .addOnSuccessListener {
                            Log.d("RepoImpl", "Liked product added: $productId")
                            trySend(State.Success("Liked Product Added Successfully"))
                        }
                        .addOnFailureListener { e ->
                            Log.e("RepoImpl", "Error adding liked product: ${e.message}")
                            trySend(State.Error(e.message ?: "Error adding liked product"))
                        }
                } else {
                    // If the document does not exist, create it with the LIKED_PRODUCTS array
                    val initialData = mapOf("LIKED_PRODUCTS" to listOf(productId))
                    userRef.set(initialData)
                        .addOnSuccessListener {
                            Log.d("RepoImpl", "User document created and product liked: $productId")
                            trySend(State.Success("User document created and liked product added"))
                        }
                        .addOnFailureListener { e ->
                            Log.e("RepoImpl", "Error creating user document: ${e.message}")
                            trySend(State.Error(e.message ?: "Error creating user document"))
                        }
                }
            }.addOnFailureListener { e ->
                Log.e("RepoImpl", "Error fetching user document: ${e.message}")
                trySend(State.Error(e.message ?: "Error fetching user document"))
            }
        } catch (e: Exception) {
            Log.e("RepoImpl", "Unexpected error: ${e.message}")
            trySend(State.Error(e.message ?: "Unexpected error occurred"))
        }

        awaitClose { close() }
    }

    override fun loadLikedProducts(uid: String): Flow<State<List<ProductDataModel>>> = callbackFlow {
        if (uid.isBlank()) {
            trySend(State.Error("Invalid user ID"))
            close()
            return@callbackFlow
        }

        Log.d("RepoImplLoad", "Loading liked products for user: $uid")

        trySend(State.Loading)

        try {
            val userRef = firebase.collection("USERS").document(uid)
            val likedProductsDoc = userRef.get().await()

            if (likedProductsDoc.exists()) {
                val likedProductIds = likedProductsDoc.get("LIKED_PRODUCTS") as? List<String> ?: emptyList()

                if (likedProductIds.isEmpty()) {
                    Log.d("RepoImplLoad", "No liked products found")
                    trySend(State.Success(emptyList()))
                } else {
                    val productDocs = firebase.collection(PRODUCT)
                        .whereIn("productID", likedProductIds)
                        .get()
                        .await()

                    val products = productDocs.documents.mapNotNull { it.toObject(ProductDataModel::class.java) }

                    Log.d("RepoImplLoad", "Loaded liked products: $products")
                    trySend(State.Success(products))
                }
            } else {
                Log.d("RepoImplLoad", "User document does not exist")
                trySend(State.Success(emptyList()))
            }
        } catch (e: Exception) {
            Log.e("RepoImplLoad", "Error loading liked products", e)
            trySend(State.Error(e.message ?: "Unknown error"))
        }

        awaitClose { Log.d("RepoImplLoad", "Flow closed") }
    }

    override fun removeLikedProduct(productId: String, uid: String) = callbackFlow {
        if (productId.isBlank()) {
            trySend(State.Error("Invalid product ID"))
            close()
            return@callbackFlow
        }

        trySend(State.Loading)

        try {
            val userRef = firebase.collection("USERS").document(uid)
            val likedProductsDoc = userRef.get().await()

            if (likedProductsDoc.exists()) {
                val likedProductIds = likedProductsDoc.get("LIKED_PRODUCTS") as? List<String> ?: emptyList()

                if (likedProductIds.contains(productId)) {
                    userRef.update("LIKED_PRODUCTS", FieldValue.arrayRemove(productId)).await()
                    Log.d("RepoImpl", "Product removed successfully: $productId")
                    trySend(State.Success("Product removed successfully."))
                } else {
                    Log.d("RepoImpl", "Product not found in liked products: $productId")
                    trySend(State.Error("Product not found in liked products."))
                }
            } else {
                Log.d("RepoImpl", "User document does not exist")
                trySend(State.Error("User document does not exist."))
            }
        } catch (e: Exception) {
            Log.e("RepoImpl", "Error removing liked product", e)
            trySend(State.Error(e.message ?: "Unknown error occurred"))
        }

        awaitClose { Log.d("RepoImpl", "Flow closed") }
    }

    override fun searchProduct(query: String): Flow<State<List<ProductDataModel>>> = callbackFlow{

        trySend(State.Loading)

        firebase.collection(PRODUCT).orderBy("name")
            .startAt(query).get().addOnSuccessListener {
                val products = it.documents.mapNotNull {
                    it.toObject(ProductDataModel::class.java)?.apply {
                        productID = it.id
                    }
                }
                trySend(State.Success(products))
            }.addOnFailureListener{
                trySend(State.Error(it.message.toString()))
            }

        awaitClose{
            close()
        }
    }

    fun updateFcmToken(userID : String){

        FirebaseMessaging.getInstance().token.addOnCompleteListener{
            if (it.isSuccessful){
                val token = it.result
                firebase.collection("USERS_TOKEN").document(userID).set(
                    mapOf("token" to token)
                )
            }
        }
    }

    override fun removeProductCapacity(productId: String , capacity : Int) : Flow<State<String>> = callbackFlow{
        trySend(State.Loading)

        try {
            firebase.collection(PRODUCT).document(productId).update("availableUnits",capacity).addOnSuccessListener {
                trySend(State.Success("Capacity Updated Successfully"))
            }.addOnFailureListener {
                trySend(State.Error(it.message.toString()))
            }
        }catch (e : Exception){
            trySend(State.Error(e.message.toString()))
        }
        awaitClose{
            close()
        }
    }

    override fun productInCartPerform(cartModel: CartModel): Flow<State<String>> =
        callbackFlow {
            trySend(State.Loading)
            firebase.collection(ADD_TO_CART)
                .document(firebaseAuth.currentUser?.uid.toString()).collection(
                    ADD_TO_CART_BY_USER
                )
                .whereEqualTo("productId", cartModel.productId).get().addOnSuccessListener {
                    if (it.documents.isNotEmpty()) {
                        firebase.collection(ADD_TO_CART)
                            .document(firebaseAuth.currentUser?.uid.toString()).collection(
                                ADD_TO_CART_BY_USER
                            ).document(it.documents[0].id)
                            .delete()
                            .addOnSuccessListener {
                                trySend(State.Success("Product Removed From Cart"))
                                close()
                            }.addOnFailureListener {
                                trySend(State.Error(it.message.toString()))
                                close()
                            }
                        return@addOnSuccessListener
                    } else {
                        firebase.collection(ADD_TO_CART)
                            .document(firebaseAuth.currentUser?.uid.toString())
                            .collection(ADD_TO_CART_BY_USER).document()
                            .set(cartModel).addOnSuccessListener {
                                trySend(State.Success("Product Added In Cart"))
                                close()
                            }.addOnFailureListener {
                                trySend(State.Error(it.message.toString()))
                                close()
                            }
                    }
                }.addOnFailureListener {
                    trySend(State.Error(it.message.toString()))
                    return@addOnFailureListener
                }
            awaitClose {
                close()
            }

        }

    override fun getAllCartProducts(): Flow<State<List<CartModel>>> = callbackFlow {
        trySend(State.Loading)
        firebase.collection(ADD_TO_CART).document(firebaseAuth.currentUser?.uid.toString())
            .collection(
                ADD_TO_CART_BY_USER
            ).get().addOnSuccessListener {
                val cartData = it.documents.mapNotNull { document ->
                    document.toObject(CartModel::class.java)
                }.reversed()
                trySend(State.Success(cartData))
            }.addOnFailureListener {
                trySend(State.Error(it.message.toString()))
            }
        awaitClose {
            close()
        }
    }

    override fun checkProductInCartOrNot(productId: String): Flow<State<Boolean>> =
        callbackFlow {
            trySend(State.Loading)
            firebase.collection(ADD_TO_CART)
                .document(firebaseAuth.currentUser?.uid.toString()).collection(
                    ADD_TO_CART_BY_USER
                ).whereEqualTo("productId", productId).get()
                .addOnSuccessListener {
                    if (it.documents.isNotEmpty()) {
                        trySend(State.Success(true))
                    } else {
                        trySend(State.Success(false))
                    }
                }.addOnFailureListener {
                    trySend(State.Error(it.message.toString()))
                }
            awaitClose {
                close()
            }
        }

}


