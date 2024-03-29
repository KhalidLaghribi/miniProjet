package com.example.gestionderestaurants.data

import com.example.gestionderestaurants.R
import com.example.gestionderestaurants.model.Plate

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