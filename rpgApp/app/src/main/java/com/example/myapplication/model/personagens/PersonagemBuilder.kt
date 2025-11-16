package com.example.myapplication.model.personagens

import com.example.myapplication.model.personagens.atributos.*
import com.example.myapplication.model.personagens.classes.base.Classe
import com.example.myapplication.model.personagens.classes.implementacoes.*
import com.example.myapplication.model.personagens.classes.subclasses.*
import com.example.myapplication.model.personagens.distribuicao.*
import com.example.myapplication.model.personagens.distribuicao.EstiloAventureiro
import com.example.myapplication.model.personagens.distribuicao.EstiloClassico
import com.example.myapplication.model.personagens.distribuicao.EstiloHeroico
import com.example.myapplication.model.personagens.distribuicao.MetodoDistribuicao
import com.example.myapplication.model.personagens.racas.*
import java.util.*

object PersonagemBuilder {

    fun criar(scanner: Scanner): Personagem {
        val nome = lerNome()
        val raca = escolherRaca(scanner)
        val classe = escolherClasse(scanner)
        val metodo = escolherMetodo(scanner)
        val valores = metodo.gerarAtributos()

        println("\nValores gerados: $valores")

        val atributos = distribuirAtributos(scanner, valores, metodo)

        return Personagem(nome, atributos, raca, classe)
    }

    private fun lerNome(): String {
        print("Digite o nome do personagem: ")
        return readlnOrNull()?.trim().takeUnless { it.isNullOrEmpty() } ?: "SemNome"
    }

    private fun escolherRaca(scanner: Scanner): Raca {
        val opcoes = listOf("Humano", "Elfo", "Anão", "Halfling")
        return when (lerOpcao(scanner, "raça", opcoes)) {
            1 -> Humano()
            2 -> Elfo()
            3 -> Anao()
            4 -> Halfling()
            else -> Humano()
        }
    }

    private fun escolherClasse(scanner: Scanner): Classe {
        val opcoes = listOf("Guerreiro", "Clérigo", "Ladrão", "Mago")
        val base = when (lerOpcao(scanner, "classe", opcoes)) {
            1 -> Guerreiro()
            2 -> Clerigo()
            3 -> Ladrao()
            4 -> Mago()
            else -> Guerreiro()
        }

        print("Deseja escolher uma subclasse agora? (s/n): ")
        if (scanner.next().lowercase() != "s") return base

        return escolherSubclasse(scanner, base)
    }

    private fun escolherSubclasse(scanner: Scanner, base: Classe): Classe = when (base) {
        is Guerreiro -> criarSubclasse(scanner, base, listOf("Bárbaro", "Paladino")) {
            when (it) {
                1 -> Barbaro()
                2 -> Paladino()
                else -> base
            }
        }

        is Clerigo -> criarSubclasse(scanner, base, listOf("Druida", "Acadêmico")) {
            when (it) {
                1 -> Druida()
                2 -> Academico()
                else -> base
            }
        }

        is Ladrao -> criarSubclasse(scanner, base, listOf("Ranger", "Bardo")) {
            when (it) {
                1 -> Ranger()
                2 -> Bardo()
                else -> base
            }
        }

        is Mago -> criarSubclasse(scanner, base, listOf("Ilusionista", "Necromante")) {
            when (it) {
                1 -> Ilusionista()
                2 -> Necromante()
                else -> base
            }
        }

        else -> base
    }

    private fun criarSubclasse(scanner: Scanner, base: Classe, nomes: List<String>, criador: (Int) -> Classe): Classe {
        println("\nSubclasses de ${base.nome}:")
        nomes.forEachIndexed { i, n -> println("${i + 1} - $n") }
        print("Opção: ")
        return criador(scanner.nextInt())
    }

    private fun escolherMetodo(scanner: Scanner): MetodoDistribuicao {
        val metodos = listOf("Clássico (3d6 na ordem)", "Aventureiro (3d6, distribui)", "Heróico (4d6 descarta o menor)")
        return when (lerOpcao(scanner, "método de geração", metodos)) {
            1 -> EstiloClassico()
            2 -> EstiloAventureiro()
            3 -> EstiloHeroico()
            else -> EstiloClassico()
        }
    }

    private fun distribuirAtributos(scanner: Scanner, valores: List<Int>, metodo: MetodoDistribuicao): List<Atributo> {
        val ordem = listOf(
            NomeAtributo.FORCA, NomeAtributo.DESTREZA, NomeAtributo.CONSTITUICAO,
            NomeAtributo.INTELIGENCIA, NomeAtributo.SABEDORIA, NomeAtributo.CARISMA
        )

        if (metodo is EstiloClassico)
            return ordem.zip(valores).map { (n, v) -> Atributo(n, v) }

        val atribuicoes = mutableListOf<Atributo>()
        val dispAtributos = ordem.toMutableList()
        val dispValores = valores.toMutableList()

        while (dispAtributos.isNotEmpty()) {
            println("\nAtributos disponíveis:")
            dispAtributos.forEachIndexed { i, a -> println("${i + 1} - ${a.name} (${a.sigla})") }
            println("Valores restantes: $dispValores")

            print("Escolha o atributo: ")
            val idxA = scanner.nextInt().coerceIn(1, dispAtributos.size) - 1
            print("Escolha o valor: ")
            val idxV = scanner.nextInt().coerceIn(1, dispValores.size) - 1

            atribuicoes += Atributo(dispAtributos.removeAt(idxA), dispValores.removeAt(idxV))
        }
        return atribuicoes
    }

    private fun lerOpcao(scanner: Scanner, tipo: String, opcoes: List<String>): Int {
        while (true) {
            println("\nEscolha a $tipo:")
            opcoes.forEachIndexed { i, o -> println("${i + 1} - $o") }
            print("Opção: ")
            val opcao = scanner.nextInt()
            if (opcao in 1..opcoes.size) return opcao
            println("Opção inválida. Tente novamente.")
        }
    }
}
