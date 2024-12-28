package com.example.myshoppingappuser.presentation_layer.screens

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Shapes
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.myshoppingappuser.R
import com.example.myshoppingappuser.domain_layer.models.UserData
import com.example.myshoppingappuser.presentation_layer.navigation.SubNavigation
import com.example.myshoppingappuser.presentation_layer.view_model.MyViewModel
import com.google.firebase.auth.FirebaseAuth

@Composable
fun ProfileScreenUI(modifier: Modifier = Modifier,navController: NavController , firebaseAuth: FirebaseAuth) {

    val viewModel = hiltViewModel<MyViewModel>()

    val isEditable = rememberSaveable { mutableStateOf(false) }

    val updateState = viewModel.updateUserResponse.collectAsState()

    val state = viewModel.getUserByIdState.collectAsState()
    val data = state.value.data
    val context = LocalContext.current

    val brush1 = Brush.linearGradient(
        listOf(
            Color(0xFFD20A1D),
            Color(0xFFB22BB0),
            Color(0xFF1555B7)
        )
    )

    LaunchedEffect(Unit) {
        viewModel.getUserById(firebaseAuth.currentUser?.uid ?: "")
    }

    when {
        state.value.isLoading -> {
            Box(modifier = modifier.fillMaxSize(),
                contentAlignment = Alignment.Center){
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center), Color.Red)
            }
        }
        state.value.error.isNotBlank() -> {
            Box(modifier = modifier.fillMaxSize(),
                contentAlignment = Alignment.Center){
                Text(text = state.value.error)
            }
        }
        state.value.data != null -> {

            val firstName = rememberSaveable { mutableStateOf(data?.firstName ?: "") }
            val lastName = rememberSaveable { mutableStateOf(data?.lastName ?: "") }
            val email = rememberSaveable { mutableStateOf(data?.email ?: "") }
            val phoneNumber = rememberSaveable { mutableStateOf(data?.phoneNumber ?: "") }
            val address = rememberSaveable { mutableStateOf(data?.address ?: "") }

                    if (!isEditable.value) {

                        Column {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Image(
                                    painter = painterResource(id = R.drawable.dummyprofile),
                                    contentDescription = null,
                                    modifier = Modifier
                                        .size(width = 120.dp, 120.dp)
                                        .padding(start = 10.dp)
                                )
                                Image(
                                    painter = painterResource(id = R.drawable.ellipse1),
                                    contentDescription = null,
                                    modifier = Modifier.size(width = 250.dp, height = 245.dp)
                                )
                            }
                            Spacer(modifier = Modifier.height(6.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                OutlinedTextField(
                                    value = firstName.value,
                                    onValueChange = {},
                                    readOnly = true,
                                    label = { Text(text = "first name") },
                                    modifier = Modifier
                                        .padding(10.dp)
                                        .width(180.dp)
                                        .clickable(
                                            enabled = true,
                                            onClick = {
                                                Toast
                                                    .makeText(
                                                        context,
                                                        "Plz Click Edit Profile",
                                                        Toast.LENGTH_SHORT
                                                    )
                                                    .show()
                                            }
                                        ),
                                    shape = Shapes().medium,
                                    colors = OutlinedTextFieldDefaults.colors(
                                        unfocusedBorderColor = Color.Red,
                                        focusedBorderColor = Color.Gray
                                    )
                                )
                                OutlinedTextField(
                                    value = lastName.value,
                                    onValueChange = {
                                        Toast.makeText(
                                            context,
                                            "Plz Click Edit Profile",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    },
                                    readOnly = true,
                                    label = { Text(text = "last name") },
                                    modifier = Modifier
                                        .padding(10.dp)
                                        .width(200.dp),
                                    shape = Shapes().medium,
                                    colors = OutlinedTextFieldDefaults.colors(
                                        unfocusedBorderColor = Color.Red,
                                        focusedBorderColor = Color.Gray
                                    )
                                )
                            }
                            OutlinedTextField(
                                value = email.value,
                                onValueChange = {
                                    Toast.makeText(
                                        context,
                                        "Plz Click Edit Profile",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                },
                                readOnly = true,
                                label = { Text(text = "email") },
                                shape = Shapes().medium,
                                colors = OutlinedTextFieldDefaults.colors(
                                    unfocusedBorderColor = Color.Red,
                                    focusedBorderColor = Color.Gray
                                ),
                                modifier = Modifier
                                    .padding(10.dp)
                                    .fillMaxWidth()
                            )
                            OutlinedTextField(
                                value = phoneNumber.value,
                                onValueChange = {
                                    Toast.makeText(
                                        context,
                                        "Plz Click Edit Profile",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                },
                                readOnly = true,
                                label = { Text(text = "Phone Number") },
                                shape = Shapes().medium,
                                colors = OutlinedTextFieldDefaults.colors(
                                    unfocusedBorderColor = Color.Red,
                                    focusedBorderColor = Color.Gray
                                ),
                                modifier = Modifier
                                    .padding(10.dp)
                                    .fillMaxWidth()
                            )
                            OutlinedTextField(
                                value = address.value,
                                onValueChange = {
                                    Toast.makeText(
                                        context,
                                        "Plz Click Edit Profile",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                },
                                readOnly = true,
                                label = { Text(text = "Address") },
                                shape = Shapes().medium,
                                colors = OutlinedTextFieldDefaults.colors(
                                    unfocusedBorderColor = Color.Red,
                                    focusedBorderColor = Color.Gray
                                ),
                                modifier = Modifier
                                    .padding(10.dp)
                                    .fillMaxWidth()
                            )
                            Button(
                                onClick = {
                                    firebaseAuth.signOut()
                                    navController.navigate(SubNavigation.LogInSignUpScreen)
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(start = 10.dp, end = 10.dp),
                                colors = ButtonDefaults.buttonColors(Color.Red),
                                shape = Shapes().large
                            ) {
                                Text(text = "Log Out")
                            }
                            Spacer(modifier = Modifier.height(6.dp))
                            Button(
                                onClick = {
                                    isEditable.value = !isEditable.value
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(start = 10.dp, end = 10.dp),
                                colors = ButtonDefaults.buttonColors(Color.White),
                                border = BorderStroke(0.75.dp, Color.Red),
                                shape = Shapes().large
                            ) {
                                Text(text = "Edit Profile", color = Color.Black)
                            }
                        }
                    } else {
                        Column {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Image(
                                    painter = painterResource(id = R.drawable.dummyprofile),
                                    contentDescription = null,
                                    modifier = Modifier
                                        .size(width = 120.dp, 120.dp)
                                        .padding(start = 10.dp)
                                )
                                Image(
                                    painter = painterResource(id = R.drawable.ellipse1),
                                    contentDescription = null,
                                    modifier = Modifier.size(width = 250.dp, height = 245.dp)
                                )
                            }
                            Spacer(modifier = Modifier.height(6.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                OutlinedTextField(
                                    value = firstName.value,
                                    onValueChange = {
                                        firstName.value = it
                                    },
                                    label = { Text(text = "first name") },
                                    modifier = Modifier
                                        .padding(10.dp)
                                        .width(180.dp),
                                    shape = Shapes().medium,
                                    colors = OutlinedTextFieldDefaults.colors(
                                        unfocusedBorderColor = Color.Red,
                                        focusedBorderColor = Color.Gray
                                    )
                                )
                                OutlinedTextField(
                                    value = lastName.value,
                                    onValueChange = {
                                        lastName.value = it
                                    },
                                    label = { Text(text = "last name") },
                                    modifier = Modifier
                                        .padding(10.dp)
                                        .width(200.dp),
                                    shape = Shapes().medium,
                                    colors = OutlinedTextFieldDefaults.colors(
                                        unfocusedBorderColor = Color.Red,
                                        focusedBorderColor = Color.Gray
                                    )
                                )
                            }
                            OutlinedTextField(
                                value = email.value,
                                onValueChange = {
                                    email.value = it
                                },
                                label = { Text(text = "email") },
                                shape = Shapes().medium,
                                colors = OutlinedTextFieldDefaults.colors(
                                    unfocusedBorderColor = Color.Red,
                                    focusedBorderColor = Color.Gray
                                ),
                                modifier = Modifier
                                    .padding(10.dp)
                                    .fillMaxWidth()
                            )
                            OutlinedTextField(
                                value = phoneNumber.value,
                                onValueChange = {
                                    phoneNumber.value = it
                                },
                                label = { Text(text = "Phone Number") },
                                shape = Shapes().medium,
                                colors = OutlinedTextFieldDefaults.colors(
                                    unfocusedBorderColor = Color.Red,
                                    focusedBorderColor = Color.Gray
                                ),
                                modifier = Modifier
                                    .padding(10.dp)
                                    .fillMaxWidth()
                            )
                            OutlinedTextField(
                                value = address.value,
                                onValueChange = {
                                    address.value = it
                                },
                                label = { Text(text = "Address") },
                                shape = Shapes().medium,
                                colors = OutlinedTextFieldDefaults.colors(
                                    unfocusedBorderColor = Color.Red,
                                    focusedBorderColor = Color.Gray
                                ),
                                modifier = Modifier
                                    .padding(10.dp)
                                    .fillMaxWidth()
                            )
                            Button(
                                onClick = {
                                    firebaseAuth.signOut()
                                    navController.navigate(SubNavigation.LogInSignUpScreen)
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(start = 10.dp, end = 10.dp),
                                colors = ButtonDefaults.buttonColors(Color.Red),
                                shape = Shapes().large
                            ) {
                                Text(text = "Log Out")
                            }
                            Spacer(modifier = Modifier.height(6.dp))
                            Button(
                                onClick = {
                                    viewModel.updateUserData(
                                        UserData(
                                            firstName = firstName.value,
                                            lastName = lastName.value,
                                            email = email.value,
                                            phoneNumber = phoneNumber.value,
                                            address = address.value,
                                            uid = firebaseAuth.currentUser?.uid ?: ""
                                        )
                                    )
                                    isEditable.value = !isEditable.value
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(start = 10.dp, end = 10.dp),
                                colors = ButtonDefaults.buttonColors(Color.White),
                                border = BorderStroke(0.75.dp, Color.Red),
                                shape = Shapes().large
                            ) {
                                Text(text = "Save Changes", color = Color.Black)
                            }

                        }
                    }
            }
        else -> {
            Text(text = "Something Went Wrong")
        }
    }
}