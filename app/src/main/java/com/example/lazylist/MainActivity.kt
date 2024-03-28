package com.example.lazylist

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Space
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.lazylist.ui.theme.LazyListTheme
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LazyListTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    Navigation(navController)
                }
            }
        }
    }
}

@Composable
fun Navigation(navController: NavHostController) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        val plates = loadPlates()
        val checkedPlates = remember { mutableStateListOf<Plate>() }

        NavHost(navController = navController, startDestination = "list") {
            composable("list") {
                ListOfPlates(
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

@Composable
fun ListOfPlates(
    plates: List<Plate>,
    checkedPlates: SnapshotStateList<Plate>,
    navController: NavController
) {
    val context = LocalContext.current
    Column(modifier = Modifier.fillMaxSize()) {
        LazyColumn(modifier = Modifier.weight(1f)) {
            items(items = plates) { plate ->
                PlateCard(plate = plate) { isChecked ->
                    // Update isChecked state directly
                    plate.isChecked.value = isChecked

                    // Update checkedPlates list (optional)
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

fun calculateTotal(filteredPlates: List<Plate>): Double {
    return filteredPlates.sumOf { it.price }
}
fun generateEmailBody(filteredPlates: List<Plate>): String {
    val items = filteredPlates.joinToString("\n") {
        "- ${it.name} - ${it.price} DH"
    }
    return "Your order:\n$items\n\nTotal: ${"%.2f".format(calculateTotal(filteredPlates))} DH"
}



@Composable
fun CheckoutScreen(checkedPlates: List<Plate>) {
    val ctx = LocalContext.current
    val senderEmail = "admin@restaurant.com"
    val emailSubject = "Order Confirmation"
    val emailBody = generateEmailBody(checkedPlates)
    var total: Double = 0.00
    for (plate in checkedPlates) {
        total += plate.price
    }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        // List of checked plates
        val filteredPlates = checkedPlates.filter { it.isChecked.value }
        LazyColumn(
            modifier = Modifier.weight(1f).padding(8.dp, 75.dp, 8.dp )
        ) {
            items(filteredPlates) { plate ->
                PlateRow(plate)
            }
        }

        // Row with total and button
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "Total: ${"%.2f".format(total)} DH", fontWeight = FontWeight.Bold)
            val context = LocalContext.current
            Button(
                onClick = {
                    // Create an intent with ACTION_SENDTO
                    val intent = Intent(Intent.ACTION_SENDTO).apply {
                        data = Uri.parse("mailto:") // This ensures only email apps can handle the intent
                        putExtra(Intent.EXTRA_EMAIL, arrayOf(senderEmail))
                        putExtra(Intent.EXTRA_SUBJECT, emailSubject)
                        putExtra(Intent.EXTRA_TEXT, emailBody)
                    }

                    // Create a chooser to let the user select an email client
                    val chooser = Intent.createChooser(intent, "Choose an Email client : ")

                    // Check if there's an app that can handle the intent
                    if (chooser.resolveActivity(ctx.packageManager) != null) {
                        ctx.startActivity(chooser)
                    } else {
                        // Handle the case where no email client is available
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


@Composable
fun PlateRow(plate: Plate) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 3.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = plate.name)
        Spacer(modifier = Modifier.width(10.dp))
        Text(text = "${plate.price} DH")
    }
}

@Composable
fun PlateCard(plate: Plate, onPlateCheckedChange: (Boolean) -> Unit) {
    Card(
        shape = MaterialTheme.shapes.medium,
        modifier = Modifier
            .padding(10.dp)
            .fillMaxWidth()
            .clickable { onPlateCheckedChange(plate.isChecked.value) }
    ) {
        Column(
            modifier = Modifier
                .padding(10.dp)
        ) {
            Text(
                text = plate.name,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(10.dp))
            Image(
                painter = painterResource(id = plate.image),
                contentDescription = plate.name,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .clip(RoundedCornerShape(10.dp))
            )
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = "Category: ${plate.category}",
                style = MaterialTheme.typography.bodySmall
            )
            Text(
                text = "Description: ${plate.description}",
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = "Price: ${plate.price} DH",
                style = MaterialTheme.typography.bodySmall
            )
            Text(
                text = "Rating: ${plate.rating} out of 5",
                style = MaterialTheme.typography.bodySmall
            )
            Spacer(modifier = Modifier.height(10.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                Checkbox(
                    checked = plate.isChecked.value,
                    onCheckedChange = onPlateCheckedChange,
                    colors = CheckboxDefaults.colors(
                        checkedColor = Color.Green,
                        uncheckedColor = Color.Gray
                    )
                )
            }
        }
    }
}


data class Plate(
    val name:String,
    val image:Int,
    val category: String,
    val description: String,
    val price: Double,
    val rating: Double,
    val isChecked: MutableState<Boolean> = mutableStateOf(false)
)



fun loadPlates(): List<Plate> {
    return listOf(
        Plate("Tagine", R.drawable.tagine, "Main course", "Best tagine that you'll ever have!",  84.99, 4.9),
        Plate("Couscous", R.drawable.couscous, "Main course", "Only available on Fridays.",  49.99, 1.3),
        Plate("Salade Niçoise", R.drawable.salad, "Salad", "Traditionally made of tomatoes, hard-boiled eggs, Niçoise olives and anchovies or tuna, dressed with olive oil.",  32.99, 4.2),
        Plate("Atay", R.drawable.atay, "Drink", "Atay bn3na3.",  12.99, 4.8),
        Plate("Pizza", R.drawable.pizza, "Main course", "Italian made pizza.", 79.99, 4.5),
        Plate("Burger", R.drawable.burger, "Main course", "Salty, buttery, and slightly sharp, Comté cheese crisps similarly to Parmesan, adding an irresistibly crunchy frico layer to these cheeseburgers.",  52.49, 4.1),
        Plate("Sprite 33cl", R.drawable.soda, "Drink", "Sprite!", 9.99, 4.8)
    )
}

