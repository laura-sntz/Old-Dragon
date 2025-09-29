package com.example.myapplication.model.classes.subclasses
import com.example.myapplication.model.classes.implementacoes.Clerigo

class Academico : Clerigo() {

    override val nome = "Acadêmico"
    override val descricao = "Clérigos voltados ao conhecimento. Reverenciam as ciências e aplicam teoria em aventuras."
    override val habilidades = super.habilidades +  listOf("Erudição Sagrada", "Pesquisa e Arquivos", "Rituais Litúrgicos")
    override val subclasses = emptyList<String>()

}