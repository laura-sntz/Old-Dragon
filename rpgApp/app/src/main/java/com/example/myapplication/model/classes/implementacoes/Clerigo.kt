package com.example.myapplication.model.classes.implementacoes

import com.example.myapplication.model.classes.base.Classe
import com.example.myapplication.model.classes.base.NivelInfo
//import org.example.classes.implementacoes.Clerigo
import com.example.myapplication.model.classes.progresso.Progressoes

open class Clerigo : Classe {

    override val nome = "Clérigo"
    override val descricao = "Guerreiro-sagrado, canaliza o poder divino para curar e combater o mal."
    override val habilidades = listOf("Cura", "Expulsar Mortos-vivos")
    override val subclasses = listOf("Druida", "Acadêmico")

    // Sistema de XP
    private val progressao = Progressoes.clerigo

    fun getNivelPorXp(xp: Int): NivelInfo {
        return progressao
            .filter { xp >= it.xpNecessario }
            .maxByOrNull { it.nivel }
            ?: progressao.first()
    }
}