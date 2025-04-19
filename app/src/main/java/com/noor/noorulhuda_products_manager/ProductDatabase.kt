// ProductDatabase.kt
// Developed by Noorulhuda Khamees - April 18, 2025

package com.noor.noorulhuda_products_manager

import android.content.Context
import androidx.room.*
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

// Room database definition specifying Product as the entity
@Database(
    entities = [Product::class], // Define Product table
    version = 2,                 // Current version of the database
    exportSchema = false         // Prevent schema export for simplicity
)
abstract class ProductDatabase : RoomDatabase() {

    // Abstract DAO method to access database operations
    abstract fun productDao(): ProductDao

    companion object {
        // Singleton instance to prevent multiple DB accesses at once
        @Volatile
        private var INSTANCE: ProductDatabase? = null

        // Migration strategy from version 1 to 2
        private val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                // Adds a new column for tracking favorite products
                database.execSQL("ALTER TABLE products ADD COLUMN is_favorite INTEGER NOT NULL DEFAULT 0")
            }
        }

        // Get or create the database instance
        fun getDatabase(context: Context, scope: CoroutineScope): ProductDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,             // Use app context to prevent leaks
                    ProductDatabase::class.java,            // Reference to the DB class
                    "product_database"                      // DB file name
                )
                    .addCallback(ProductDatabaseCallback(scope)) // Populate sample data on create
                    .addMigrations(MIGRATION_1_2)               // Add migration logic
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }

    // RoomDatabase callback to populate the DB on first creation
    private class ProductDatabaseCallback(
        private val scope: CoroutineScope
    ) : RoomDatabase.Callback() {

        // Called only the first time the DB is created
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            INSTANCE?.let { database ->
                scope.launch {
                    populateDatabase(database.productDao()) // Fill DB with initial data
                }
            }
        }

        // Pre-populate the database with a few products
        private suspend fun populateDatabase(dao: ProductDao) {
            val products = listOf(
                Product(101, "iPhone 15", 1099.99, "2024-06-15", "Electronics", true),
                Product(102, "MacBook Pro", 1999.99, "2024-06-20", "Electronics", false),
                Product(103, "AirPods Pro", 249.99, "2024-06-10", "Accessories", true)
            )
            products.forEach { dao.insert(it) } // Insert each product
        }
    }
}