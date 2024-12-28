package com.example.myshoppingappuser.common

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle

const val CATEGORY = "CATEGORY"
const val PRODUCT = "PRODUCT"
const val ADD_TO_CART = "CART_PRODUCTS"
const val ADD_TO_CART_BY_USER = "CART_PRODUCTS_BY_USER"

@Composable
fun MultiColorText(firstText: String, secondText: String, modifier: Modifier = Modifier){
    val annotatedString = buildAnnotatedString {
        withStyle(style = SpanStyle(color = Color.Black)) {
            append(firstText)
        }
        withStyle(style = SpanStyle(color = Color.Blue)) {
            append(secondText)
        }
    }

    Text(text = annotatedString , modifier = modifier)
}