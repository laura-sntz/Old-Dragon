package com.example.myapplication.model.personagens.classes.subclasses
import com.example.myapplication.model.personagens.classes.implementacoes.Guerreiro

class Paladino : Guerreiro() {

    override val nome = "Paladino"
    override val descricao = "Guerreiros sagrados que mantêm Aparar e Maestria em Arma."
    override val habilidades = super.habilidades + "Aura Sagrada"
    override val subclasses: List<String> = emptyList()
}