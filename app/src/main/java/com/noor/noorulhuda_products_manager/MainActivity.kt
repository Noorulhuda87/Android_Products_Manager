// MainActivity.kt
// Developed by Noorulhuda Khamees - April 18, 2025

package com.noor.noorulhuda_products_manager

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.lifecycleScope

// Main entry point of the app, extends ComponentActivity to use Compose
class MainActivity : ComponentActivity() {

    // Lazily initialize the Room database using the application context and lifecycle scope
    private val database by lazy {
        ProductDatabase.getDatabase(this, lifecycleScope)
    }

    // Called when the activity is created
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Set the content of the activity using Jetpack Compose
        setContent {
            MaterialTheme { // Apply the app's Material3 theme
                Surface(
                    modifier = Modifier.fillMaxSize(), // Fill the entire screen
                    color = MaterialTheme.colorScheme.background // Set background color from theme
                ) {
                    TaskManagerApp(database) // Launch the main app UI with the database
                }
            }
        }
    }
}