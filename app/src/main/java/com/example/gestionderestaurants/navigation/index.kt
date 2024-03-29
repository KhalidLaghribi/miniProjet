package com.example.gestionderestaurants.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.gestionderestaurants.Screens.CheckoutScreen
import com.example.gestionderestaurants.Screens.IndexScreen
import com.example.gestionderestaurants.data.loadPlates
import com.example.gestionderestaurants.model.Plate


@Composable
fun GraphNavigation(navController: NavHostController) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        val plates = loadPlates()
        val checkedPlates = remember { mutableStateListOf<Plate>() }

        NavHost(navController = navController, startDestination = "homeScreen") {
            composable("homeScreen") {
                IndexScreen(
                    plates = plates,
                    checkedPlates = checkedPlates,
                    navController = navController
                )
            }
            composable("checkout") {
                CheckoutScreen(checkedPlates)
            }
        }
    }
}