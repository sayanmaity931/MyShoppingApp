package com.example.myshoppingappuser.user_pref

import android.content.Context
import android.util.Log
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlin.text.split

private val Context.dataStore by preferencesDataStore(name = "user_preferences")
class UserPreferenceManager (val context: Context){


    companion object {
        private val PRODUCT_ID_KEY = stringPreferencesKey("product_id")
        private val CART_ID_KEY = stringPreferencesKey("cart_id")
        private val LIKE_ID_KEY = stringPreferencesKey("like_id")
    }

    suspend fun saveProductID(productId: String){
        if (productId.isNotEmpty()) {
            val existingIds = getProductIDs().first()
            if (!existingIds.contains(productId)) {
                val updatedIds = (existingIds + productId).joinToString(",")
                context.dataStore.edit { it[PRODUCT_ID_KEY] = updatedIds }
            }
        } else {
            Log.d("TAG", "addProductID: Invalid product ID")
        }
    }

    suspend fun saveProductIdForCart(productId: String){
        if (productId.isNotEmpty()) {
            val existingIds = getProductID1s().first()
            if (!existingIds.contains(productId)) {
                val updatedIds = (existingIds + productId).joinToString(",")
                context.dataStore.edit { it[CART_ID_KEY] = updatedIds }
                Log.d("UserPreferenceManager", "Cart updated: $updatedIds")
            }
        } else {
            Log.d("TAG", "addProductID: Invalid cart ID")
        }
    }

    suspend fun saveProductIdForLike(productId: String){
        if (productId.isNotEmpty()) {
            val existingIds = getProductID2s().first()
            if (!existingIds.contains(productId)) {
                val updatedIds = (existingIds + productId).joinToString(",")
                context.dataStore.edit { it[LIKE_ID_KEY] = updatedIds }
                Log.d("UserPreferenceManager", "Likes updated: $updatedIds")
            }
        } else {
            Log.d("TAG", "addProductID: Invalid like ID")
        }
    }

    fun getProductID2s(): Flow<List<String>> {
        return context.dataStore.data.map { preferences ->
            preferences[LIKE_ID_KEY]?.split(",")?.filter { it.isNotEmpty() } ?: emptyList()
        }.also {
            Log.d("UserPreferenceManager", "Retrieved like product IDs")
        }
    }

    suspend fun removeLikedProduct(productId: String){
        if (productId.isNotEmpty()) {
            val existingIds = getProductID2s().first()
            if (existingIds.contains(productId)) {
                val updatedIds = (existingIds - productId).joinToString(",")
                context.dataStore.edit { it[LIKE_ID_KEY] = updatedIds }
                Log.d("UserPreferenceManager", "Likes updated: $updatedIds")
            }
            else{
                Log.d("TAG", "removeProductID: No ID")
            }
        } else {
            Log.d("TAG", "removeProductID: Invalid like ID")
        }
    }

    fun getProductID1s(): Flow<List<String>> {
        return context.dataStore.data.map { preferences ->
            preferences[CART_ID_KEY]?.split(",")?.filter { it.isNotEmpty() } ?: emptyList()
        }.also {
            Log.d("UserPreferenceManager", "Retrieved cart product IDs")
        }
    }

    fun getProductIDs(): Flow<List<String>> {
        return context.dataStore.data.map { preferences ->
            preferences[PRODUCT_ID_KEY]?.split(",")?.filter { it.isNotEmpty() } ?: emptyList()
        }
    }
}