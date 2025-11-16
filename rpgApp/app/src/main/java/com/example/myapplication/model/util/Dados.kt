package com.example.myapplication.model.util

import kotlin.random.Random

object Dados {

    /** Rola um número de dados (ex: 3d6 → rolar(3, 6)) */
    fun rolar(qtd: Int, faces: Int): Int {
        return (1..qtd).sumOf { Random.nextInt(1, faces + 1) }
    }

    /** Rola 4d6 e descarta o menor resultado (método heróico clássico) */
    fun rolar4d6DescartaMenor(): Int {
        val rolagens = List(4) { Random.nextInt(1, 7) }
        return rolagens.sortedDescending().take(3).sum()
    }

    /** Converte string tipo "1d8" ou "+1d10" e retorna o valor rolado */
    fun rolarDadoVida(descricao: String): Int {
        val padrao = Regex("""\+?(\d*)d(\d+)""")
        val match = padrao.find(descricao) ?: return 0
        val qtd = match.groupValues[1].ifEmpty { "1" }.toInt()
        val faces = match.groupValues[2].toInt()
        return rolar(qtd, faces)
    }

    // Métodos rápidos para uso direto
    fun d4() = rolar(1, 4)
    fun d6() = rolar(1, 6)
    fun d8() = rolar(1, 8)
    fun d10() = rolar(1, 10)
    fun d12() = rolar(1, 12)
    fun d20() = rolar(1, 20)
}
