package com.example.ordermyt

data class OrderItem(
    val price: Float,
    val title: String
)

val orderTypes = listOf(OrderItem(7.3f, "Red bull bubble tea"), OrderItem(8.0f, "Brown Sugar tea"), OrderItem(9.0f, "Rare bubble tea")
,  OrderItem(10.0f, "Juicy Bubble Tea")
)
