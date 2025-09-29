package com.example.myapplication.model.classes.implementacoes

import com.example.myapplication.model.classes.base.Classe
import com.example.myapplication.model.classes.base.NivelInfo
import com.example.myapplication.model.classes.progresso.Progressoes
//import org.example.classes.implementacoes.Guerreiro

open class Guerreiro : Classe {

    override val nome = "Guerreiro"
    override val descricao = "Combatente da linha de frente, especialista em armas e defesa."
    override val habilidades = listOf("Aparar", "Maestria em Arma")
    override val subclasses = listOf("Bárbaro", "Paladino")

    // Sistema de XP
    private val progressao = Progressoes.guerreiro

    fun getNivelPorXp(xp: Int): NivelInfo {
        return progressao
            .filter { xp >= it.xpNecessario }
            .maxByOrNull { it.nivel }
            ?: progressao.first()
    }
}