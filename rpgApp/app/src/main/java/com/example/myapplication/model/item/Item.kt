package com.example.myapplication.model.item

data class Item(
    val nome: String,
    val tipo: TipoItem,
    val descricao: String = "",
    val bonusAtaque: Int = 0,
    val bonusDefesa: Int = 0,
    val cura: Int = 0
)
