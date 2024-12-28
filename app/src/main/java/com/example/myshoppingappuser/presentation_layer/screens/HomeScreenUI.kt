package com.example.myshoppingappuser.presentation_layer.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Shapes
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.myshoppingappuser.R
import com.example.myshoppingappuser.presentation_layer.navigation.Routes
import com.example.myshoppingappuser.presentation_layer.view_model.MyViewModel
import com.google.firebase.auth.FirebaseAuth

@Composable
//fun HomeScreenUI (viewModel: MyViewModel = hiltViewModel(),navController: NavController, firebaseAuth: FirebaseAuth) {
//
//    val list = listOf(
//        R.drawable.blouse to "Blouse",
//        R.drawable.bottoms to "Bottoms",
//        R.drawable.frock to "Frock",
//        R.drawable.jumpsuit to "Jumpsuit"
//    )
//
//    val _list = listOf(
//        R.drawable.frock1 to "One Shoulder Linen Dress",
//        R.drawable.frock2 to "Puff Slave Dress",
//        R.drawable.blouse2 to "Cross Stitch Top",
//        R.drawable.blouse3 to "Urbanise Saree"
//    )
//
//    val searchQuery = viewModel.getProductBySearchState.collectAsStateWithLifecycle()
//    val query = remember { mutableStateOf("") }
//
//    LaunchedEffect(Unit) {
//        viewModel.searchQuery()
//    }
//
//    LazyColumn(
//        modifier = Modifier.fillMaxSize()
//    ) {
//        item {
//
//            Row(
//                modifier = Modifier.fillMaxWidth(),
//                verticalAlignment = Alignment.CenterVertically,
//                horizontalArrangement = Arrangement.SpaceEvenly
//            ) {
//                OutlinedTextField(
//                    value = query.value,
//                    onValueChange = {
//                        query.value = it
//                        viewModel.onSearchQueryChange(it)
//                    },
//                    label = { Text(text = "Search For Product") },
//                    leadingIcon = {
//                        Icon(
//                            imageVector = Icons.Rounded.Search,
//                            contentDescription = null
//                        )
//                    },
//                    shape = Shapes().extraLarge,
//                    colors = OutlinedTextFieldDefaults.colors(
//                        unfocusedBorderColor = Color.Red,
//                        focusedBorderColor = Color.Gray
//                    )
//                )
//                Icon(
//                    painter = painterResource(id = R.drawable.notifications),
//                    contentDescription = null,
//                    modifier = Modifier.size(24.dp, 24.dp)
//                )
//            }
//        }
//            if (query.value.isNotEmpty() && !searchQuery.value.data.isNullOrEmpty()) {
//
//                item {
//                    LazyVerticalGrid(
//                        modifier = Modifier.fillMaxWidth(),
//                        columns = GridCells.Fixed(2),
//                        horizontalArrangement = Arrangement.spacedBy(16.dp),
//                        verticalArrangement = Arrangement.spacedBy(16.dp),
//                        contentPadding = PaddingValues(16.dp)
//                    ) {
//                        items(searchQuery.value.data ?: emptyList()) {
//                            Card(
//                                onClick = { navController.navigate(Routes.EachProductDetailScreen(it.productID.toString())) }
//                            ) {
//                                Text(text = it.name)
//                            }
//                        }
//                    }
//                }
//            } else {
//                item {
//                    Spacer(modifier = Modifier.size(10.dp))
//
//                    Row(
//                        horizontalArrangement = Arrangement.Absolute.SpaceBetween,
//                        verticalAlignment = Alignment.CenterVertically,
//                        modifier = Modifier.fillMaxWidth()
//                    ) {
//                        Text(
//                            text = "Categories",
//                            fontWeight = FontWeight.Bold,
//                            fontSize = 20.sp,
//                            color = Color.White,
//                            fontStyle = FontStyle.Italic,
//                            modifier = Modifier.padding(start = 10.dp)
//                        )
//                        Text(text = "See All",
//                            fontWeight = FontWeight.Bold,
//                            fontSize = 15.sp,
//                            color = Color.Red,
//                            fontStyle = FontStyle.Italic,
//                            modifier = Modifier
//                                .padding(end = 10.dp)
//                                .clickable(
//                                    enabled = true,
//                                    onClick = {
////                            viewModel.fetchProductUuid()
//                                    }
//                                )
//                        )
//                    }
//                }
//                item {
//                    Spacer(modifier = Modifier.size(10.dp))
//                    LazyRow(
//                        horizontalArrangement = Arrangement.SpaceEvenly,
//                        verticalAlignment = Alignment.CenterVertically,
//                        modifier = Modifier.fillMaxWidth()
//                    ) {
//
//                        items(list) {
//
//                            Column(
//                                horizontalAlignment = Alignment.CenterHorizontally,
//                                verticalArrangement = Arrangement.Center
//                            ) {
//                                Image(
//                                    painter = painterResource(id = it.first),
//                                    contentDescription = null,
//                                    modifier = Modifier.size(60.dp, 60.dp),
//                                    colorFilter = ColorFilter.tint(Color.White)
//                                )
//                                Text(text = it.second, color = Color.Gray)
//                            }
//                        }
//                    }
//                }
//                item{
//                Spacer(modifier = Modifier.size(10.dp))
//                Card {
//                    Image(
//                        painter = painterResource(id = R.drawable.banner3),
//                        contentDescription = null,
//                        modifier = Modifier.size(390.dp, 198.17.dp)
//                    )
//                }
//                Spacer(modifier = Modifier.size(10.dp))
//                Row(
//                    horizontalArrangement = Arrangement.Absolute.SpaceBetween,
//                    verticalAlignment = Alignment.CenterVertically,
//                    modifier = Modifier.fillMaxWidth()
//                ) {
//                    Text(
//                        text = "Flash Sale",
//                        fontWeight = FontWeight.Bold,
//                        fontSize = 20.sp,
//                        color = Color.White,
//                        fontStyle = FontStyle.Italic,
//                        modifier = Modifier.padding(start = 10.dp)
//                    )
//                    Text(text = "See All",
//                        fontWeight = FontWeight.Bold,
//                        fontSize = 15.sp,
//                        color = Color.Red,
//                        fontStyle = FontStyle.Italic,
//                        modifier = Modifier
//                            .padding(end = 10.dp)
//                            .clickable {
//                                navController.navigate(Routes.SeeAllProductScreen)
//                            }
//                    )
//                }
//            }
//                item {
//                    Spacer(modifier = Modifier.height(10.dp))
//
//                    LazyRow(
//                        horizontalArrangement = Arrangement.SpaceEvenly,
//                        verticalAlignment = Alignment.CenterVertically,
//                        modifier = Modifier.fillMaxWidth()
//                    ) {
//
//                        items(_list) {
//
//                            Column(
//                                horizontalAlignment = Alignment.CenterHorizontally,
//                                verticalArrangement = Arrangement.Center,
//                                modifier = Modifier.padding(6.dp)
//                            ) {
//                                Image(
//                                    painter = painterResource(id = it.first),
//                                    contentDescription = null,
//                                    modifier = Modifier.size(width = 100.dp, height = 140.dp)
//                                )
//                                Spacer(modifier = Modifier.height(6.dp))
//                                Card(
//                                    modifier = Modifier.size(100.dp, 116.dp)
//                                ) {
//                                    Text(text = it.second, fontSize = 30.sp)
//                                }
//                            }
//                        }
//                    }
//                }
//            }
//        }
//    }
//
//

fun HomeScreenUI(viewModel: MyViewModel = hiltViewModel(), navController: NavController, firebaseAuth: FirebaseAuth) {
    val categories = listOf(
        R.drawable.blouse to "Blouse",
        R.drawable.bottoms to "Bottoms",
        R.drawable.frock to "Frock",
        R.drawable.jumpsuit to "Jumpsuit"
    )

    val flashSaleItems = listOf(
        R.drawable.frock1 to "One Shoulder Linen Dress",
        R.drawable.frock2 to "Puff Sleeve Dress",
        R.drawable.blouse2 to "Cross Stitch Top",
        R.drawable.blouse3 to "Urbanise Saree"
    )

    val searchQueryState = viewModel.getProductBySearchState.collectAsStateWithLifecycle()
    val query = remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        viewModel.searchQuery()
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(top = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(16.dp)
    ) {
        // Search Bar
        item {
            SearchBar(
                query = query.value,
                onQueryChanged = {
                    query.value = it
                    viewModel.onSearchQueryChange(it)
                }
            )
        }

        // Search Results Section
        if (query.value.isNotEmpty() && !searchQueryState.value.data.isNullOrEmpty()) {
            item {
                Text(
                    text = "Search Results",
                    style = androidx.compose.ui.text.TextStyle(fontWeight = FontWeight.Bold, fontSize = 20.sp)
                )
            }
            items(searchQueryState.value.data ?: emptyList()) { product ->
                ProductCard(
                    name = product.name,
                    onClick = {
                        navController.navigate(Routes.EachProductDetailScreen(product.productID.toString()))
                    }
                )
            }
        } else {
            // Categories Section
            item {
                SectionHeader(
                    title = "Categories",
                    onSeeAllClick = { /* Handle See All */ }
                )
            }
            item {
                CategoriesRow(categories)
            }

            // Banner Section
            item {
                Banner()
            }

            // Flash Sale Section
            item {
                SectionHeader(
                    title = "Flash Sale",
                    onSeeAllClick = { navController.navigate(Routes.SeeAllProductScreen) }
                )
            }
            item {
                FlashSaleRow(flashSaleItems)
            }
        }
    }
}

@Composable
fun SearchBar(query: String, onQueryChanged: (String) -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        OutlinedTextField(
            value = query,
            onValueChange = onQueryChanged,
            label = { Text("Search For Product") },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Rounded.Search,
                    contentDescription = null
                )
            },
            modifier = Modifier.weight(1f),
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedBorderColor = Color.Red,
                focusedBorderColor = Color.Gray
            )
        )
        Spacer(modifier = Modifier.width(8.dp))
        Icon(
            painter = painterResource(id = R.drawable.notifications),
            contentDescription = null,
            modifier = Modifier.size(24.dp)
        )
    }
}

@Composable
fun SectionHeader(title: String, onSeeAllClick: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp,
            fontStyle = FontStyle.Italic
        )
        Text(
            text = "See All",
            color = Color.Red,
            fontStyle = FontStyle.Italic,
            modifier = Modifier.clickable(onClick = onSeeAllClick)
        )
    }
}

@Composable
fun CategoriesRow(categories: List<Pair<Int, String>>) {
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(horizontal = 16.dp)
    ) {
        items(categories) { category ->
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(id = category.first),
                    contentDescription = null,
                    modifier = Modifier.size(60.dp),
                    colorFilter = ColorFilter.tint(Color.White)
                )
                Text(text = category.second, color = Color.Gray)
            }
        }
    }
}

@Composable
fun Banner() {
    Card {
        Image(
            painter = painterResource(id = R.drawable.banner3),
            contentDescription = null,
            modifier = Modifier.size(390.dp, 198.17.dp)
        )
    }
}

@Composable
fun FlashSaleRow(items: List<Pair<Int, String>>) {
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(horizontal = 16.dp)
    ) {
        items(items) { saleItem ->
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(8.dp)
            ) {
                Image(
                    painter = painterResource(id = saleItem.first),
                    contentDescription = null,
                    modifier = Modifier.size(100.dp)
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(text = saleItem.second, fontSize = 12.sp)
            }
        }
    }
}

@Composable
fun ProductCard(name: String, onClick: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        onClick = onClick
    ) {
        Text(text = name, modifier = Modifier.padding(16.dp))
    }
}

