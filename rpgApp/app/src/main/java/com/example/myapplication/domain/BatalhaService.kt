package com.example.myapplication.domain

import com.example.myapplication.model.batalha.Batalha
import com.example.myapplication.model.batalha.ResultadoBatalha
import com.example.myapplication.model.inimigo.Inimigo
import com.example.myapplication.model.personagens.Personagem

class BatalhaService {

    fun iniciar(personagem: Personagem, inimigo: Inimigo): ResultadoBatalha {
        val batalha = Batalha(personagem, inimigo)
        return batalha.iniciar()
    }
}
