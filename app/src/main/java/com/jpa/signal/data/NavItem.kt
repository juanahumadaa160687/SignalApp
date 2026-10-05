package com.jpa.signal.data

data class NavItem(
    val title: String,
    val function: () -> Unit,
    val icon: Int,
    val route: String?,
)

