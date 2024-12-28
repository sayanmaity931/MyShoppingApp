package com.example.myshoppingappuser.common

sealed class State<out T> {
    data class Success<out T>(val data: T) : State<T>()
    data class Error(val exception: String) : State<Nothing>()
    object Loading : State<Nothing>()
}