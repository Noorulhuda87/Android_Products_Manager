// ProductViewModel.kt
// Developed by Noorulhuda Khamees - April 18, 2025

package com.noor.noorulhuda_products_manager

import androidx.lifecycle.*              // Lifecycle-related components like ViewModel and LiveData
import androidx.lifecycle.asLiveData    // Extension to convert Flow to LiveData
import kotlinx.coroutines.launch        // For launching coroutines in ViewModelScope

// ViewModel class responsible for managing product data and business logic
class ProductViewModel(private val dao: ProductDao) : ViewModel() {

    // LiveData list of all products, automatically updated when the database changes
    val allProducts: LiveData<List<Product>> = dao.getAll().asLiveData()

    // LiveData list of favorite products only
    val favoriteProducts: LiveData<List<Product>> = dao.getFavorites().asLiveData()

    // Function to insert a new product using a coroutine
    fun addProduct(product: Product) = viewModelScope.launch {
        dao.insert(product)  // Insert product into Room database
    }

    // Function to update an existing product using a coroutine
    fun updateProduct(product: Product) = viewModelScope.launch {
        dao.update(product)  // Update product in Room database
    }

    // Function to delete a product using a coroutine
    fun deleteProduct(product: Product) = viewModelScope.launch {
        dao.delete(product)  // Delete product from Room database
    }

    // Companion object to provide a ViewModel factory for creating the ViewModel with parameters
    companion object {
        fun provideFactory(
            dao: ProductDao
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {

            // Override the default ViewModel creation to return a ProductViewModel with a DAO
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return ProductViewModel(dao) as T  // Safe casting
            }
        }
    }
}
