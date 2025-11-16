package com.example.myapplication.model.inimigo

import com.example.myapplication.model.util.Dados

object GeradorDeInimigos {

    fun gerarInimigo(nivelHeroi: Int): Inimigo {
        val tipo = escolherTipoPorNivel(nivelHeroi)
        val baseVida = when (tipo) {
            TipoInimigo.GOBLIN -> 10..18
            TipoInimigo.ORC -> 18..30
            TipoInimigo.ESQUELETO -> 15..25
            TipoInimigo.BANDIDO -> 20..28
            TipoInimigo.DRAGAO -> 60..120
            TipoInimigo.CHEFAO -> 150..250
        }

        val vida = Dados.rolar(1, baseVida.random())
        val forca = (nivelHeroi / 2) + Dados.rolar(1, 6)
        val defesa = (nivelHeroi / 3) + Dados.rolar(1, 4)
        val xp = calcularXp(tipo, nivelHeroi)

        val drop = when (tipo) {
            TipoInimigo.GOBLIN -> listOf("Bolsa de Moedas", "Poção de Cura")
            TipoInimigo.ORC -> listOf("Machado Orc", "Escudo Rústico")
            TipoInimigo.ESQUELETO -> listOf("Espada Velha", "Anel Místico")
            TipoInimigo.BANDIDO -> listOf("Adaga Enferrujada", "Corda", "Bolsa de Ouro")
            TipoInimigo.DRAGAO -> listOf("Gema Vermelha", "Armadura Dracônica", "Espada Lendária")
            TipoInimigo.CHEFAO -> listOf("Relíquia Sombria", "Coração das Trevas")
        }

        return Inimigo(
            nome = tipo.nomeExibicao,
            nivel = nivelHeroi,
            vida = vida,
            forca = forca,
            defesa = defesa,
            xpDrop = xp,
            possivelDrop = drop
        )
    }

    private fun escolherTipoPorNivel(nivel: Int): TipoInimigo {
        return when {
            nivel <= 2 -> TipoInimigo.GOBLIN
            nivel in 3..4 -> listOf(TipoInimigo.ORC, TipoInimigo.BANDIDO).random()
            nivel in 5..7 -> listOf(TipoInimigo.ESQUELETO, TipoInimigo.ORC).random()
            nivel in 8..9 -> TipoInimigo.DRAGAO
            else -> TipoInimigo.CHEFAO
        }
    }

    private fun calcularXp(tipo: TipoInimigo, nivelHeroi: Int): Int {
        val baseXp = when (tipo) {
            TipoInimigo.GOBLIN -> 50
            TipoInimigo.ORC -> 100
            TipoInimigo.ESQUELETO -> 120
            TipoInimigo.BANDIDO -> 90
            TipoInimigo.DRAGAO -> 500
            TipoInimigo.CHEFAO -> 2000
        }
        return baseXp + (nivelHeroi * 15)
    }
}
