package com.example.myapplication.model.classes.implementacoes

import com.example.myapplication.model.classes.base.Classe
import com.example.myapplication.model.classes.base.NivelInfo
import com.example.myapplication.model.classes.progresso.Progressoes
//import org.example.classes.implementacoes.Mago

open class Mago : Classe {

    override val nome = "Mago"
    override val descricao = "Estudioso das artes arcanas, conjura magias a partir de grimórios."
    override val habilidades = listOf("Magia Arcana", "Leitura de Grimórios")
    override val subclasses = listOf("Ilusionista", "Necromante")

    // Sistema de XP
    private val progressao = Progressoes.mago

    fun getNivelPorXp(xp: Int): NivelInfo {
        return progressao
            .filter { xp >= it.xpNecessario }
            .maxByOrNull { it.nivel }
            ?: progressao.first()
    }
}