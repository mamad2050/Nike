package com.example.nikestore.data

import androidx.annotation.StringRes

data class EmptyState(
    val mustShow: Boolean,
    @StringRes val messageResourceId: Int = 0,
    val mustShowCallActionBtn: Boolean = false
)
