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
                navController.navigate("checkout")
            },
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(16.dp)
        ) {
            Text(text = "Checkout")
        }
    }
}



@Composable
fun CheckoutScreen(checkedPlates: List<Plate>) {
    val subjectState = remember { mutableStateOf("") }
    val emailState = remember { mutableStateOf("") }
    val contentState = remember { mutableStateOf("") }
    val buttonText = remember { mutableStateOf("") }
    val ctx = LocalContext.current
    val senderEmail = "admin@restaurant.com"
    val emailSubject = "Order Confirmation"
    val emailBody = "Test\n1\n2\n3"
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
            Text(text = "Total: $${"%.2f".format(total)}", fontWeight = FontWeight.Bold)
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
        Text(text = "$${plate.price}")
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
                text = "Price: ${plate.price}",
                style = MaterialTheme.typography.bodySmall
            )
            Text(
                text = "Rating: ${plate.rating}",
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
        Plate("Platee 1", R.drawable.plate1, "Category 1", "Description for Platee 1", 9.99, 4.5),
        Plate("Platee 2", R.drawable.plate1, "Category 2", "Description for Platee 2",  12.99, 4.0),
        Plate("Platee 3", R.drawable.plate1, "Category 3", "Description for Platee 3",  8.50, 3.8),
        Plate("Platee 4", R.drawable.plate1, "Category 4", "Description for Platee 4",  15.75, 4.2),
        Plate("Platee 5", R.drawable.plate1, "Category 5", "Description for Platee 5",  11.25, 4.7),
        Plate("Platee 6", R.drawable.plate1, "Category 6", "Description for Platee 6", 10.00, 4.3),
        Plate("Platee 7", R.drawable.plate1, "Category 7", "Description for Platee 7",  14.50, 4.8),
        Plate("Platee 8", R.drawable.plate1, "Category 8", "Description for Platee 8",  13.20, 4.6),
        Plate("Platee 9", R.drawable.plate1, "Category 9", "Description for Platee 9",  16.99, 4.9),
        Plate("Platee 10", R.drawable.plate1, "Category 10", "Description for Platee 10",  18.50, 4.4)
    )
}

