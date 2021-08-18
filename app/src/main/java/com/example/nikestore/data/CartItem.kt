package com.example.nikestore.data

import com.example.nikestore.data.Product

data class CartItem(
    val cart_item_id: Int,
    val count: Int,
    val product: Product
)