package com.example.myapplication.model.batalha

import com.example.myapplication.model.inimigo.Inimigo
import com.example.myapplication.model.personagens.Personagem

class Batalha(
    private val heroi: Personagem,
    private val inimigo: Inimigo
) {

    fun iniciar(): ResultadoBatalha {
        val log = StringBuilder()
        var heroiMorreu = false

        log.append("${heroi.nome} enfrenta ${inimigo.nome}!\n\n")

        while (heroi.estaVivo() && inimigo.estaVivo()) {
            // Turno do herói
            val danoHeroi = heroi.calcularDano()
            val danoCausado = inimigo.receberDano(danoHeroi)
            log.append("${heroi.nome} causa $danoCausado de dano em ${inimigo.nome}.\n")

            if (!inimigo.estaVivo()) {
                log.append("${inimigo.nome} foi derrotado!\n")

                // XP ganho
                log.append(heroi.ganharXp(inimigo.xpDrop))

                // Drop
                val drop = inimigo.droparItem()
                if (drop != null) {
                    log.append("${inimigo.nome} dropou: $drop!\n")
                    heroi.receberDrop(drop)
                }

                break
            }

            // Turno do inimigo
            val danoInimigo = inimigo.calcularDano()
            val danoRecebido = heroi.receberDano(danoInimigo)
            log.append("${inimigo.nome} ataca e causa $danoRecebido de dano em ${heroi.nome}.\n")

            if (!heroi.estaVivo()) {
                log.append("${heroi.nome} foi derrotado!\n")
                heroiMorreu = true
                break
            }

            log.append("\n")
        }

        return ResultadoBatalha(
            log = log.toString(),
            heroiMorreu = heroiMorreu
        )
    }
}
