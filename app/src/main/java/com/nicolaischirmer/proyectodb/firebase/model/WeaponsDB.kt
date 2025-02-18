package com.nicolaischirmer.proyectodb.firebase.model

data class WeaponsDB(
    val userId: String = "",
    val name:  String = "",
    val type: String = "",
    val description: String = "",
    val damage: Int = 0
)
