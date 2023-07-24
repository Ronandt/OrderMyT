package com.example.ordermyt

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

fun calcOrder(drink: String, quantity: Int, size: String, flavour: String): Float {
    var sizePrice = if(size == "Medium") 0f else 3.5f

    var flavourPrice = when(flavour) {
        "Vanilla" -> 0.0f
        "Chocolate" -> 1.0f
        "Durian" -> 3.0f
        else -> {
            0.0f
        }
    }
    var total = orderTypes.find { it.title == drink}!!.price * quantity + sizePrice * quantity+ flavourPrice * quantity
    return total
}