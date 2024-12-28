package com.example.myshoppingappuser.data_layer.di

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object DataModel {

    @Provides
    fun provideFirebase() : FirebaseFirestore{
        return FirebaseFirestore.getInstance()
    }

    @Provides
    fun provideFirebaseAuthentication() : FirebaseAuth{
        return FirebaseAuth.getInstance()
    }

}