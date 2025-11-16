package com.example.myapplication.model.inimigo

import com.example.myapplication.model.util.Dados

class Inimigo(
    val nome: String,
    val nivel: Int,
    var vida: Int,
    val forca: Int,
    val defesa: Int,
    val xpDrop: Int,
    val possivelDrop: List<String> = listOf("Poção de Cura", "Espada Enferrujada", "Escudo Velho")
) {

    fun calcularDano(): Int {
        return forca + Dados.rolar(1, 6)
    }

    fun receberDano(dano: Int): Int {
        val danoFinal = (dano - defesa).coerceAtLeast(0)
        vida -= danoFinal
        if (vida < 0) vida = 0
        return danoFinal
    }

    fun estaVivo(): Boolean = vida > 0

    fun droparItem(): String? {
        // 30% de chance de dropar um item
        val chance = Dados.rolar(1, 100)
        return if (chance <= 30) possivelDrop.random() else null
    }
}
