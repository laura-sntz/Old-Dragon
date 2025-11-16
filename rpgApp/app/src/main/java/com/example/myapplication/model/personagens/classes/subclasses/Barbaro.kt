package com.example.myapplication.model.personagens.classes.subclasses
import com.example.myapplication.model.personagens.classes.implementacoes.Guerreiro

class Barbaro : Guerreiro() {

    override val nome = "Bárbaro"
    override val descricao = "Guerreiros rústicos, ligados à natureza, resistentes e instintivos."
    override val habilidades = super.habilidades + listOf("Fúria", "Instinto Selvagem")
    override val subclasses: List<String> = emptyList()

}