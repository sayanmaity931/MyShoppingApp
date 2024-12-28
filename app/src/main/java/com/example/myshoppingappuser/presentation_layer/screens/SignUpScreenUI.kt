package com.example.myshoppingappuser.presentation_layer.screens

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.RemoveRedEye
import androidx.compose.material.icons.rounded.VpnKeyOff
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.credentials.PasswordCredential
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.myshoppingappuser.common.MultiColorText
import com.example.myshoppingappuser.domain_layer.models.UserData
import com.example.myshoppingappuser.presentation_layer.navigation.Routes
import com.example.myshoppingappuser.presentation_layer.view_model.MyViewModel

@Composable
fun SignUpScreenUI(modifier: Modifier = Modifier,viewModel: MyViewModel = hiltViewModel(),navController: NavController) {

    val state = viewModel.registerUserState.collectAsState().value

    when {

        state.isLoading -> {
            CircularProgressIndicator()
        }

        state.error.isNotEmpty() -> {
            Toast.makeText(LocalContext.current, state.error, Toast.LENGTH_SHORT).show()
        }

        state.data.isNotEmpty() -> {
            Toast.makeText(LocalContext.current, state.data, Toast.LENGTH_SHORT).show()
        }

        else -> {

        Column(
            modifier = modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            val firstName = remember { mutableStateOf("") }

            val lastName = remember { mutableStateOf("") }

            val email = remember { mutableStateOf("") }

            val password = remember { mutableStateOf("") }

            val phoneNumber = remember { mutableStateOf("") }

            val address = remember { mutableStateOf("") }

            val userImage = remember { mutableStateOf("") }

            OutlinedTextField(
                value = firstName.value,
                onValueChange = {
                    firstName.value = it
                },
                label = { Text(text = "First Name") }
            )

            OutlinedTextField(
                value = lastName.value,
                onValueChange = {
                    lastName.value = it
                },
                label = { Text(text = "Last Name") }
            )

            OutlinedTextField(
                value = email.value,
                onValueChange = {
                    email.value = it
                },
                label = { Text(text = "email") }
            )

            val visibility = remember { mutableStateOf(false) }

            val icon = if (visibility.value) {
                Icons.Rounded.RemoveRedEye
            } else {
                Icons.Rounded.VpnKeyOff
            }
            OutlinedTextField(
                value = password.value,
                onValueChange = {
                    password.value = it
                },
                label = { Text(text = "password") },
                trailingIcon = {
                    IconButton(
                        onClick = {
                            visibility.value = !visibility.value
                        }
                    ) {
                        Icon(imageVector = icon, contentDescription = null)
                    }
                },
                visualTransformation =
                if (visibility.value) {
                    VisualTransformation.None
                } else {
                    PasswordVisualTransformation()
                }
            )

            OutlinedTextField(
                value = phoneNumber.value,
                onValueChange = {
                    phoneNumber.value = it
                },
                label = { Text(text = "Phone Number") }
            )

            OutlinedTextField(
                value = address.value,
                onValueChange = {
                    address.value = it
                },
                label = { Text(text = "Address") }
            )

            Button(
                onClick = {
                    val data = UserData(
                        firstName = firstName.value,
                        lastName = lastName.value,
                        email = email.value,
                        password = password.value,
                        phoneNumber = phoneNumber.value,
                        address = address.value
                    )
                    viewModel.registerUser(userData = data)
                }
            ) {
                Text(text = "Sign Up")
                }

            Spacer(modifier = Modifier.size(20.dp))

            MultiColorText("Already have an account? ", "LogIn" , modifier = Modifier.clickable{
                navController.navigate(Routes.LogInScreen){
                    navController.navigateUp()
                         }
                    }
                )
            }
        }
    }
}