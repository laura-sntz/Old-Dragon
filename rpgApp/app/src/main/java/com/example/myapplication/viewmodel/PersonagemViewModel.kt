package com.example.myapplication.viewmodel

import androidx.lifecycle.ViewModel
import com.example.myapplication.domain.CriacaoPersonagemService
import com.example.myapplication.model.personagens.classes.base.Classe
import com.example.myapplication.model.personagens.racas.Raca
import com.example.myapplication.model.personagens.Personagem

class PersonagemViewModel : ViewModel() {
    private val criacaoService = CriacaoPersonagemService()
    var personagem: Personagem? = null
        private set

    fun criar(nome: String, raca: Raca, classe: Classe) {
        personagem = criacaoService.criarPersonagem(nome, raca, classe)
    }
}
