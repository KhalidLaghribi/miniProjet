package com.example.gestionderestaurants.utils

import com.example.gestionderestaurants.model.Plate

fun calculateTotal(filteredPlates: List<Plate>): Double {
    return filteredPlates.sumOf { it.price }
}
fun generateEmailBody(filteredPlates: List<Plate>): String {
    val items = filteredPlates.joinToString("\n") {
        "- ${it.name} - ${it.price} DH"
    }
    return "Your order:\n$items\n\nTotal: ${"%.2f".format(calculateTotal(filteredPlates))} DH"
}