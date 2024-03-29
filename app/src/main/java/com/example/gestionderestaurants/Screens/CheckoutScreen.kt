package com.example.gestionderestaurants.Screens

import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.gestionderestaurants.components.PlateRow
import com.example.gestionderestaurants.model.Plate
import com.example.gestionderestaurants.utils.generateEmailBody

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CheckoutScreen(checkedPlates: List<Plate>) {
    val ctx = LocalContext.current
    val senderEmail = "admin@restaurant.com"
    val emailSubject = "Order Confirmation"
    val emailBody = generateEmailBody(checkedPlates)
    var total = 0.00
    val navController = rememberNavController()
    for (plate in checkedPlates) {
        total += plate.price
    }

    BackHandler(onBack = {
        navController.popBackStack()
    })

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.White)
    ) {
        val filteredPlates = checkedPlates.filter { it.isChecked.value }
        TopAppBar(
            title = { Text("Checkout") },
            navigationIcon = {
                IconButton(onClick = {
                    navController.navigate("homeScreen")
                }) {
                    Icon(Icons.Filled.ArrowBack, contentDescription = "Back to home")
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = Color.LightGray, 
                titleContentColor = Color.Black, 
                navigationIconContentColor = Color.Black,
                actionIconContentColor = Color.Black 
            )
        )
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .padding(8.dp, 75.dp, 8.dp)
        ) {
            items(filteredPlates) { plate ->
                PlateRow(plate)
            }
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "Total: ${"%.2f".format(total)} DH", fontWeight = FontWeight.Bold, color = Color.Black)
            Button(
                onClick = {
                    val intent = Intent(Intent.ACTION_SENDTO).apply {
                        data = Uri.parse("mailto:") 
                        putExtra(Intent.EXTRA_EMAIL, arrayOf(senderEmail))
                        putExtra(Intent.EXTRA_SUBJECT, emailSubject)
                        putExtra(Intent.EXTRA_TEXT, emailBody)
                    }

                    val chooser = Intent.createChooser(intent, "Choose an Email client : ")

                    if (chooser.resolveActivity(ctx.packageManager) != null) {
                        ctx.startActivity(chooser)
                    } else {
                        Toast.makeText(ctx, "No email client found", Toast.LENGTH_SHORT).show()
                    }
                },
                modifier = Modifier.wrapContentWidth(),
            ) {
                Text(text = "Confirm order")
            }
        }
    }
}
