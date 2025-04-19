// Developed by Noorulhuda Khamees - April 18, 2025

package com.noor.noorulhuda_products_manager

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface ProductDao {
    @Insert
    suspend fun insert(product: Product)

    @Update
    suspend fun update(product: Product)

    @Delete
    suspend fun delete(product: Product)

    @Query("SELECT * FROM products")
    fun getAll(): Flow<List<Product>>
    @Query("SELECT * FROM products WHERE is_favorite = 1") // Must match @ColumnInfo
    fun getFavorites(): Flow<List<Product>>


    // For sample data initialization
    @Query("SELECT COUNT(*) FROM products")
    suspend fun count(): Int
}