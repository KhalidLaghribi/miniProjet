package com.example.gestionderestaurants.model

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf

data class Plate(
    val name:String,
    val image:Int,
    val category: String,
    val description: String,
    val price: Double,
    val rating: Double,
    val isChecked: MutableState<Boolean> = mutableStateOf(false)
)