package com.example.myapplication.model.personagens.racas

interface Raca {

    val nome: String
    val movimentoBase: Int // em metros/rodada
    val infravisao: Int? // em metros (null se não possuir)
    val alinhamentoTendencia: String
    val habilidadesRaciais: List<String>

}