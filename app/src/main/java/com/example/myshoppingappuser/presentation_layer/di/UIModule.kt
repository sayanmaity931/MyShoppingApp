package com.example.myshoppingappuser.presentation_layer.di

import android.content.Context
import com.example.myshoppingappuser.data_layer.repo_impl.RepoImpl
import com.example.myshoppingappuser.domain_layer.repo.Repo
import com.example.myshoppingappuser.user_pref.UserPreferenceManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UIModule {

    @Provides
    fun provideRepo(firebase: FirebaseFirestore , firebaseAuth: FirebaseAuth) : Repo {
        return RepoImpl(
            firebase,
            firebaseAuth
        )
    }

    @Singleton
    @Provides
    fun provideUserPreferenceManager(@ApplicationContext context: Context) = UserPreferenceManager(context=context)

}