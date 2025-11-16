package com.example.myapplication.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.myapplication.domain.BatalhaService
import com.example.myapplication.model.inimigo.Inimigo
import com.example.myapplication.model.personagens.Personagem

class BatalhaViewModel : ViewModel() {

    private val batalhaService = BatalhaService()

    var logBatalha by mutableStateOf("")
        private set

    var heroiMorreu by mutableStateOf(false)
        private set

    fun lutar(personagem: Personagem, inimigo: Inimigo) {
        val resultado = batalhaService.iniciar(personagem, inimigo)
        logBatalha = resultado.log
        heroiMorreu = resultado.heroiMorreu
    }
}
