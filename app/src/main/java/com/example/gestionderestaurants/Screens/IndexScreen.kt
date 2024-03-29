package com.example.gestionderestaurants.Screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.gestionderestaurants.components.PlateCard
import com.example.gestionderestaurants.model.Plate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IndexScreen(
    plates: List<Plate>,
    checkedPlates: SnapshotStateList<Plate>,
    navController: NavController
) {
    val context = LocalContext.current
    Column(
        modifier = Modifier.fillMaxSize()
            .background(color = Color.White)
    ) {
        TopAppBar(
            title = { Text("Menu Selection") },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = Color.LightGray, // Light background color for the TopAppBar
                titleContentColor = Color.Black, // Text color for the title
                navigationIconContentColor = Color.Black, // Icon color for navigation icons
                actionIconContentColor = Color.Black // Icon color for action icons
            )
        )
        LazyColumn(modifier = Modifier.weight(1f)) {
            items(items = plates) { plate ->
                PlateCard(plate = plate) { isChecked ->
                    plate.isChecked.value = isChecked

                    if (isChecked) {
                        checkedPlates.add(plate)
                    } else {
                        checkedPlates.remove(plate)
                    }
                }
            }
        }
        Button(
            onClick = {
                if (checkedPlates.any { it.isChecked.value }) {
                    navController.navigate("checkout")
                } else {
                    Toast.makeText(context, "Please select at least one item", Toast.LENGTH_SHORT).show()
                }
            },
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(16.dp)
        ) {
            Text(text = "Checkout")
        }
    }
}
