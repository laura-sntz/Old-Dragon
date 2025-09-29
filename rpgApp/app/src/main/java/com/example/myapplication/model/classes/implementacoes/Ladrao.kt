package com.example.myapplication.model.classes.implementacoes

import com.example.myapplication.model.classes.base.Classe
import com.example.myapplication.model.classes.base.NivelInfo
import com.example.myapplication.model.classes.progresso.Progressoes
//import org.example.classes.implementacoes.Ladrao

open class Ladrao : Classe {

    override val nome = "Ladrão"
    override val descricao = "Especialista em furtividade, armadilhas e ataques de oportunidade."
    override val habilidades = listOf("Ataque Furtivo", "Furtividade")
    override val subclasses = listOf("Ranger", "Bardo")

    // Sistema de XP
    private val progressao = Progressoes.ladrao

    fun getNivelPorXp(xp: Int): NivelInfo {
        return progressao
            .filter { xp >= it.xpNecessario }
            .maxByOrNull { it.nivel }
            ?: progressao.first()
    }
}