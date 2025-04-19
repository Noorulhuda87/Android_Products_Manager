// Author: Noorulhuda Khamees
// Date: 2025-04-18
package com.noor.noorulhuda_products_manager

// Import necessary Compose and Android components
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.graphics.Color

// Main composable that sets up navigation between Home and Add/Edit screens
@Composable
fun TaskManagerApp(database: ProductDatabase) {
    val navController = rememberNavController() // Navigation controller
    val viewModel: ProductViewModel = viewModel(
        factory = ProductViewModel.provideFactory(database.productDao()) // Injecting DAO
    )

    // Navigation host that defines the screen routes
    NavHost(navController, startDestination = "home") {
        composable("home") {
            HomeScreen(viewModel) {
                navController.navigate("add") // Navigate to Add/Edit screen
            }
        }
        composable("add") {
            AddEditScreen(
                viewModel = viewModel,
                onBack = { navController.popBackStack() } // Go back to previous screen
            )
        }
    }
}

// Composable to display list of products and add/filter actions
@Composable
fun HomeScreen(
    viewModel: ProductViewModel,
    onAddClick: () -> Unit // Callback for Add Product button
) {
    val products by viewModel.allProducts.observeAsState(emptyList()) // Observe all products
    val favoriteProducts by viewModel.favoriteProducts.observeAsState(emptyList()) // Observe favorites
    var showFavorites by remember { mutableStateOf(false) } // UI state toggle

    val displayedProducts = if (showFavorites) favoriteProducts else products // Choose list to show

    Scaffold(
        topBar = {
            AppHeader(title = "Product Manager") // Display app header
        },
        bottomBar = {
            AppFooter() // Display footer
        }
    ) { innerPadding ->
        Column(
            Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Button(onClick = onAddClick) {
                    Text("Add Product")
                }
                Button(onClick = { showFavorites = !showFavorites }) {
                    Text(if (showFavorites) "Show All" else "Show Favorites")
                }
            }

            if (displayedProducts.isEmpty()) {
                Text(
                    text = "No products found",
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
            } else {
                LazyColumn {
                    items(displayedProducts) { product ->
                        ProductItem(
                            product = product,
                            onToggleFavorite = {
                                viewModel.updateProduct(product.copy(isFavorite = !product.isFavorite))
                            },
                            onDelete = {
                                viewModel.deleteProduct(product)
                            }
                        )
                    }
                }
            }
        }
    }
}

// Composable to display a single product item with favorite and delete actions
@Composable
fun ProductItem(
    product: Product,
    onToggleFavorite: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = product.name,
                    style = MaterialTheme.typography.titleLarge
                )
                IconButton(onClick = onToggleFavorite) {
                    Icon(
                        imageVector = if (product.isFavorite) Icons.Filled.Favorite else Icons.Outlined.Favorite,
                        contentDescription = "Favorite"
                    )
                }
            }
            Text("Price: $${"%.2f".format(product.price)}")
            Text("Delivery: ${product.date}")
            Text("Category: ${product.category}")
            Button(
                onClick = onDelete,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF4B0082) // Custom dark purple button
                )
            ) {
                Text("Delete")
            }
        }
    }
}

// Composable to add or edit a product with form fields
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditScreen(
    viewModel: ProductViewModel,
    onBack: () -> Unit
) {
    Scaffold(
        topBar = {
            AppHeader(title = "Add Product", showBack = true, onBackClick = onBack)
        },
        bottomBar = {
            AppFooter()
        }
    ) { innerPadding ->
        var name by remember { mutableStateOf("") }
        var price by remember { mutableStateOf("") }
        var category by remember { mutableStateOf("Electronics") }
        var date by remember { mutableStateOf("2024-01-01") }
        var isFavorite by remember { mutableStateOf(false) }
        var expanded by remember { mutableStateOf(false) }

        val categories = listOf("Electronics", "Appliances", "Media", "Accessories")

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Product Name") },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = price,
                onValueChange = { price = it },
                label = { Text("Price") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )
            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = !expanded }
            ) {
                OutlinedTextField(
                    readOnly = true,
                    value = category,
                    onValueChange = {},
                    label = { Text("Category") },
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor()
                )
                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    categories.forEach { selectionOption ->
                        DropdownMenuItem(
                            text = { Text(selectionOption) },
                            onClick = {
                                category = selectionOption
                                expanded = false
                            }
                        )
                    }
                }
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Favorite:")
                Switch(
                    checked = isFavorite,
                    onCheckedChange = { isFavorite = it }
                )
            }
            Button(
                onClick = {
                    viewModel.addProduct(
                        Product(
                            id = (100..999).random(),
                            name = name,
                            price = price.toDoubleOrNull() ?: 0.0,
                            date = date,
                            category = category,
                            isFavorite = isFavorite
                        )
                    )
                    onBack()
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = name.isNotBlank() && price.isNotBlank()
            ) {
                Text("Add Product")
            }
        }
    }
}

// Reusable top bar composable with optional back button
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppHeader(title: String, showBack: Boolean = false, onBackClick: (() -> Unit)? = null) {
    TopAppBar(
        title = { Text(title) },
        navigationIcon = {
            if (showBack && onBackClick != null) {
                IconButton(onClick = onBackClick) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                }
            }
        }
    )
}

// Footer component for app copyright
@Composable
fun AppFooter() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        contentAlignment = Alignment.Center
    ) {
        Text("\u00A9 2025 Noorulhuda Khamees", style = MaterialTheme.typography.bodySmall)
    }
}
