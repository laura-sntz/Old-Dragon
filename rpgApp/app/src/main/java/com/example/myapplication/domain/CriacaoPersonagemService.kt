package com.example.myapplication.domain

import com.example.myapplication.data.entity.PersonagemEntity
import com.example.myapplication.model.personagens.Personagem
import com.example.myapplication.model.personagens.atributos.Atributo
import com.example.myapplication.model.personagens.atributos.NomeAtributo
import com.example.myapplication.model.personagens.classes.base.Classe
import com.example.myapplication.model.personagens.classes.implementacoes.Clerigo
import com.example.myapplication.model.personagens.classes.implementacoes.Guerreiro
import com.example.myapplication.model.personagens.classes.implementacoes.Ladrao
import com.example.myapplication.model.personagens.classes.implementacoes.Mago
import com.example.myapplication.model.personagens.racas.Anao
import com.example.myapplication.model.personagens.racas.Elfo
import com.example.myapplication.model.personagens.racas.Halfling
import com.example.myapplication.model.personagens.racas.Humano
import com.example.myapplication.model.personagens.racas.Raca

class CriacaoPersonagemService {

    /**
     * Ainda atende o PersonagemViewModel: cria um personagem "do zero"
     * com atributos base 10.
     */
    fun criarPersonagem(nome: String, raca: Raca, classe: Classe): Personagem {
        val atributosBase = NomeAtributo.values().map { atributo ->
            Atributo(nome = atributo, valor = 10)
        }
        return Personagem(
            nome = nome,
            atributos = atributosBase,
            raca = raca,
            classe = classe
        )
    }

    /**
     * Constrói um Personagem de domínio a partir do PersonagemEntity salvo no Room.
     */
    fun criarAPartirDaEntity(entity: PersonagemEntity): Personagem {
        val raca: Raca = when (entity.raca) {
            "Humano" -> Humano()
            "Anão", "Anao" -> Anao()
            "Elfo" -> Elfo()
            "Halfling" -> Halfling()
            else -> Humano()
        }

        val classe: Classe = when (entity.classe) {
            "Guerreiro" -> Guerreiro()
            "Clérigo", "Clerigo" -> Clerigo()
            "Ladrão", "Ladrao" -> Ladrao()
            "Mago" -> Mago()
            else -> Guerreiro()
        }

        val atributos = listOf(
            Atributo(NomeAtributo.FORCA,        entity.forca),
            Atributo(NomeAtributo.DESTREZA,     entity.destreza),
            Atributo(NomeAtributo.CONSTITUICAO, entity.constituicao),
            Atributo(NomeAtributo.INTELIGENCIA, entity.inteligencia),
            Atributo(NomeAtributo.SABEDORIA,    entity.sabedoria),
            Atributo(NomeAtributo.CARISMA,      entity.carisma),
        )

        return Personagem(
            nome = entity.nome,
            atributos = atributos,
            raca = raca,
            classe = classe
        )
    }
}
