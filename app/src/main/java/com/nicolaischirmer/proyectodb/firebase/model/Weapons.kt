package com.nicolaischirmer.proyectodb.firebase.model

data class Weapons(
    var id: String ?= null,
    val userId: String?,
    val name:  String?,
    val type: String?,
    val description: String?,
    val damage: Int?
)
