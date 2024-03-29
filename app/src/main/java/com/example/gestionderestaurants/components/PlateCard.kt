package com.example.gestionderestaurants.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.gestionderestaurants.model.Plate

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
            Row(
                horizontalArrangement = Arrangement.End
            ){
                Text(
                    text = plate.name,
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(start = 135.dp)
                )
            }
            Spacer(modifier = Modifier.height(10.dp))
            Box( // Use a Box to control aspect ratio
                modifier = Modifier
                    .fillMaxSize() // Make the Box fill the available space
                    .aspectRatio(1f) // Set aspect ratio to 1:1 (square)
            ) {
                Image(
                    painter = painterResource(id = plate.image),
                    contentDescription = plate.name,
                    modifier = Modifier.fillMaxSize()// Make the image fill the Box
                        .clip(RoundedCornerShape(10.dp)), // Apply rounded corners
                    contentScale = ContentScale.Crop

                )
            }
            Spacer(modifier = Modifier.height(5.dp))
            Spacer(modifier = Modifier.height(5.dp))
            Text(
                text = "Rating: ${plate.rating} out of 5",
                style = MaterialTheme.typography.bodySmall
            )
            Text(
                text = "Category: ${plate.category}",
                style = MaterialTheme.typography.bodySmall
            )
            Spacer(modifier = Modifier.height(15.dp))
            Text(
                text = "${plate.description}",
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(modifier = Modifier.height(5.dp))
            Spacer(modifier = Modifier.height(10.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Bottom
            ) {
                Text(
                    text = "Price: ${plate.price} DH",
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(bottom = 13.dp)
                )
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