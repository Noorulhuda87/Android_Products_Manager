// Developed by Noorulhuda Khamees - April 18, 2025
package com.noor.noorulhuda_products_manager

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "products")
data class Product(
    @PrimaryKey val id: Int,
    val name: String,
    val price: Double,
    val date: String,
    val category: String,
    @ColumnInfo(name = "is_favorite") // Make sure this matches your query
    val isFavorite: Boolean = false
)