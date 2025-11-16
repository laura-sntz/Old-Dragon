package com.example.myapplication.model.personagens

import com.example.myapplication.model.personagens.distribuicao.EstiloAventureiro
import com.example.myapplication.model.personagens.distribuicao.EstiloClassico
import com.example.myapplication.model.personagens.distribuicao.EstiloHeroico
import com.example.myapplication.model.personagens.distribuicao.MetodoDistribuicao
import com.example.myapplication.model.personagens.atributos.Atributo
import com.example.myapplication.model.personagens.atributos.NomeAtributo
import com.example.myapplication.model.personagens.classes.base.Classe
import com.example.myapplication.model.personagens.classes.implementacoes.Clerigo
import com.example.myapplication.model.personagens.classes.implementacoes.Guerreiro
import com.example.myapplication.model.personagens.classes.implementacoes.Ladrao
import com.example.myapplication.model.personagens.classes.implementacoes.Mago
import com.example.myapplication.model.personagens.classes.subclasses.Academico
import com.example.myapplication.model.personagens.classes.subclasses.Barbaro
import com.example.myapplication.model.personagens.classes.subclasses.Bardo
import com.example.myapplication.model.personagens.classes.subclasses.Druida
import com.example.myapplication.model.personagens.classes.subclasses.Ilusionista
import com.example.myapplication.model.personagens.classes.subclasses.Necromante
import com.example.myapplication.model.personagens.classes.subclasses.Paladino
import com.example.myapplication.model.personagens.classes.subclasses.Ranger
import com.example.myapplication.model.personagens.racas.Anao
import com.example.myapplication.model.personagens.racas.Elfo
import com.example.myapplication.model.personagens.racas.Halfling
import com.example.myapplication.model.personagens.racas.Humano
import com.example.myapplication.model.personagens.racas.Raca
import com.example.myapplication.model.util.Dados
import java.util.*

class Personagem(
    val nome: String,
    val atributos: List<Atributo>,
    val raca: Raca,
    val classe: Classe
) {
    // --- CAMPOS E LÓGICA DE COMBATE (USADOS PELA BATALHA) -----------------

    var xpAtual: Int = 0
        private set

    var nivel: Int = 1
        private set

    var vidaMaxima: Int = 0
        private set

    var vidaAtual: Int = 0
        private set

    private val inventario: MutableList<String> = mutableListOf()

    init {
        // quando o personagem nasce, já calcula nível e vida
        nivel = calcularNivelAtual()
        val vidaInicial = calcularVidaInicial()
        vidaMaxima = vidaInicial
        vidaAtual = vidaInicial
    }

    /** Busca um atributo pelo nome (FORÇA, CONSTITUICAO, etc.) */
    private fun obterAtributo(nome: NomeAtributo): Atributo? =
        atributos.firstOrNull { it.nome == nome }

    /** Calcula o nível atual a partir do XP, usando a progressão da classe. */
    private fun calcularNivelAtual(): Int {
        val xp = xpAtual
        val nivelInfo = when (val c = classe) {
            is Guerreiro -> c.getNivelPorXp(xp)
            is Clerigo   -> c.getNivelPorXp(xp)
            is Ladrao    -> c.getNivelPorXp(xp)
            is Mago      -> c.getNivelPorXp(xp)
            else         -> null
        }
        return nivelInfo?.nivel ?: 1
    }

    /** Calcula a vida inicial do personagem (nível 1). */
    private fun calcularVidaInicial(): Int {
        val conMod = obterAtributo(NomeAtributo.CONSTITUICAO)?.modificador ?: 0

        val infoNivel1 = when (val c = classe) {
            is Guerreiro -> c.getNivelPorXp(0)
            is Clerigo   -> c.getNivelPorXp(0)
            is Ladrao    -> c.getNivelPorXp(0)
            is Mago      -> c.getNivelPorXp(0)
            else         -> null
        }

        val baseDescricao = infoNivel1?.pontosDeVida ?: "6"

        val vidaBase = if (baseDescricao.contains("d")) {
            // Usa o método que EXISTE no teu Dados.kt
            Dados.rolarDadoVida(baseDescricao)
        } else {
            baseDescricao.toIntOrNull() ?: 6
        }

        return (vidaBase + conMod).coerceAtLeast(1)
    }

    /** Diz se o personagem ainda está vivo (vida > 0). */
    fun estaVivo(): Boolean = vidaAtual > 0

    /** Calcula o dano de ataque do personagem. */
    fun calcularDano(): Int {
        val forcaMod = obterAtributo(NomeAtributo.FORCA)?.modificador ?: 0

        val nivelInfo = when (val c = classe) {
            is Guerreiro -> c.getNivelPorXp(xpAtual)
            is Clerigo   -> c.getNivelPorXp(xpAtual)
            is Ladrao    -> c.getNivelPorXp(xpAtual)
            is Mago      -> c.getNivelPorXp(xpAtual)
            else         -> null
        }

        val baseAtaque = nivelInfo?.baseDeAtaque ?: 1
        val dado = Dados.d6() // arma básica 1d6
        val dano = baseAtaque + forcaMod + dado
        return dano.coerceAtLeast(0)
    }

    /**
     * Aplica dano no personagem.
     * Retorna o dano efetivamente sofrido (não negativo).
     */
    fun receberDano(dano: Int): Int {
        val danoFinal = dano.coerceAtLeast(0)
        vidaAtual -= danoFinal
        if (vidaAtual < 0) vidaAtual = 0
        return danoFinal
    }

    /**
     * Ganha XP, atualiza nível e vida se subir de nível.
     * Retorna um texto de log para a batalha.
     */
    fun ganharXp(xp: Int): String {
        if (xp <= 0) return ""

        val nivelAntes = nivel
        xpAtual += xp
        nivel = calcularNivelAtual()

        val sb = StringBuilder()
        sb.append("$nome ganha $xp XP.\n")

        if (nivel > nivelAntes) {
            val niveisGanhos = nivel - nivelAntes
            sb.append("$nome sobe para o nível $nivel!\n")

            val vidaGanha = rolarVidaPorNivel(niveisGanhos)
            vidaMaxima += vidaGanha
            vidaAtual += vidaGanha
            sb.append("Ganha $vidaGanha pontos de vida. Vida: $vidaAtual/$vidaMaxima.\n")
        }

        return sb.toString()
    }

    /** Rola os pontos de vida ganhos ao subir um ou mais níveis. */
    private fun rolarVidaPorNivel(qtdNiveis: Int): Int {
        val conMod = obterAtributo(NomeAtributo.CONSTITUICAO)?.modificador ?: 0
        var total = 0

        repeat(qtdNiveis) {
            val info = when (val c = classe) {
                is Guerreiro -> c.getNivelPorXp(xpAtual)
                is Clerigo   -> c.getNivelPorXp(xpAtual)
                is Ladrao    -> c.getNivelPorXp(xpAtual)
                is Mago      -> c.getNivelPorXp(xpAtual)
                else         -> null
            }

            val desc = info?.pontosDeVida ?: "+1d6"
            val vidaRolada = if (desc.contains("d")) {
                Dados.rolarDadoVida(desc)
            } else {
                desc.toIntOrNull() ?: 1
            }

            total += (vidaRolada + conMod).coerceAtLeast(1)
        }

        return total
    }

    /** Adiciona um item ganho de drop. */
    fun receberDrop(item: String) {
        inventario.add(item)
    }

    // ==========================
    //   FICHA (CÓDIGO ORIGINAL)
    // ==========================

    fun exibirFicha() {
        println("\n===== FICHA DO PERSONAGEM =====")
        println("Nome: $nome")
        println("Raça: ${raca.nome}")

        val classeLinha = when (classe) {
            is Barbaro -> "Guerreiro (Subclasse: ${classe.nome})"
            is Paladino -> "Guerreiro (Subclasse: ${classe.nome})"
            is Ranger -> "Ladrão (Subclasse: ${classe.nome})"
            is Bardo -> "Ladrão (Subclasse: ${classe.nome})"
            is Druida -> "Clérigo (Subclasse: ${classe.nome})"
            is Academico -> "Mago (Subclasse: ${classe.nome})"
            is Ilusionista -> "Mago (Subclasse: ${classe.nome})"
            is Necromante -> "Mago (Subclasse: ${classe.nome})"
            else -> classe.nome
        }
        println("Classe: $classeLinha")
        println("Conceito da Classe: ${classe.descricao}")

        println("Movimento: ${raca.movimentoBase} m")
        println("Infravisão: ${raca.infravisao ?: "Nenhuma"}")
        println("Alinhamento: ${raca.alinhamentoTendencia}")

        println("\nAtributos:")
        atributos.forEach { a ->
            val modStr = if (a.modificador >= 0) "+${a.modificador}" else a.modificador.toString()
            println(" - ${a.nome.sigla} (${a.nome.name}): ${a.valor}  [mod: $modStr]")
        }

        if (classe.habilidades.isNotEmpty()) {
            println("\nHabilidades de Classe:")
            classe.habilidades.forEach { println(" - $it") }
        }

        if (raca.habilidadesRaciais.isNotEmpty()) {
            println("\nHabilidades Raciais:")
            raca.habilidadesRaciais.forEach { println(" - $it") }
        }

        println("================================\n")
    }

    companion object {

        fun criarPersonagem(scanner: Scanner): Personagem {
            println("=== Criação de Personagem ===")

            print("Nome do personagem: ")
            val nome = scanner.nextLine().ifBlank { "Herói sem nome" }

            val raca = escolherRaca(scanner)
            val classe = escolherClasse(scanner)

            val metodo = escolherMetodoDistribuicao(scanner)
            val valores = metodo.gerarAtributos()

            val atributos = distribuirAtributos(scanner, valores, metodo)

            return Personagem(nome, atributos, raca, classe)
        }

        private fun escolherRaca(scanner: Scanner): Raca {
            while (true) {
                println("\nEscolha a raça do personagem:")
                val racas = listOf("Humano", "Elfo", "Anão", "Halfling")
                racas.forEachIndexed { index, raca ->
                    println("${index + 1} - $raca")
                }
                print("Opção: ")
                when (scanner.nextInt()) {
                    1 -> return Humano()
                    2 -> return Elfo()
                    3 -> return Anao()
                    4 -> return Halfling()
                    else -> println("Opção inválida. Tente novamente.")
                }
            }
        }

        private fun escolherClasse(scanner: Scanner): Classe {
            while (true) {
                println("\nEscolha a classe do personagem:")
                val classes = listOf("Guerreiro", "Clérigo", "Ladrão", "Mago")
                classes.forEachIndexed { index, classe ->
                    println("${index + 1} - $classe")
                }
                print("Opção: ")
                val classeBase: Classe = when (scanner.nextInt()) {
                    1 -> Guerreiro()
                    2 -> Clerigo()
                    3 -> Ladrao()
                    4 -> Mago()
                    else -> {
                        println("Opção inválida. Tente novamente.")
                        continue
                    }
                }

                print("Deseja escolher uma subclasse agora? (s/n): ")
                val resposta = scanner.next().trim().lowercase()
                if (resposta != "s")
                    return classeBase

                return when (classeBase) {
                    is Guerreiro -> {
                        println("\nSubclasses de Guerreiro:")
                        println("1 - Bárbaro")
                        println("2 - Paladino")
                        print("Opção: ")
                        when (scanner.nextInt()) {
                            1 -> Barbaro()
                            2 -> Paladino()
                            else -> {
                                println("Opção inválida. Mantendo classe base.")
                                classeBase
                            }
                        }
                    }

                    is Clerigo -> {
                        println("\nSubclasses de Clérigo:")
                        println("1 - Druida")
                        println("2 - Acadêmico")
                        print("Opção: ")
                        when (scanner.nextInt()) {
                            1 -> Druida()
                            2 -> Academico()
                            else -> {
                                println("Opção inválida. Mantendo classe base.")
                                classeBase
                            }
                        }
                    }

                    is Ladrao -> {
                        println("\nSubclasses de Ladrão:")
                        println("1 - Ranger")
                        println("2 - Bardo")
                        print("Opção: ")
                        when (scanner.nextInt()) {
                            1 -> Ranger()
                            2 -> Bardo()
                            else -> {
                                println("Opção inválida. Mantendo classe base.")
                                classeBase
                            }
                        }
                    }

                    is Mago -> {
                        println("\nSubclasses de Mago:")
                        println("1 - Ilusionista")
                        println("2 - Necromante")
                        print("Opção: ")
                        when (scanner.nextInt()) {
                            1 -> Ilusionista()
                            2 -> Necromante()
                            else -> {
                                println("Opção inválida. Mantendo classe base.")
                                classeBase
                            }
                        }
                    }

                    else -> classeBase
                }
            }
        }

        private fun escolherMetodoDistribuicao(scanner: Scanner): MetodoDistribuicao {
            while (true) {
                println("\nEscolha o método de distribuição de atributos:")
                println("1 - Clássico (3d6 em ordem)")
                println("2 - Aventureiro (4d6 descarta o menor, distribuição livre)")
                println("3 - Heróico (valores altos, distribuição livre)")
                print("Opção: ")
                when (scanner.nextInt()) {
                    1 -> return EstiloClassico()
                    2 -> return EstiloAventureiro()
                    3 -> return EstiloHeroico()
                    else -> println("Opção inválida. Tente novamente.")
                }
            }
        }

        private fun distribuirAtributos(
            scanner: Scanner,
            valores: List<Int>,
            metodo: MetodoDistribuicao
        ): List<Atributo> {
            val ordemPadrao = listOf(
                NomeAtributo.FORCA,
                NomeAtributo.DESTREZA,
                NomeAtributo.CONSTITUICAO,
                NomeAtributo.INTELIGENCIA,
                NomeAtributo.SABEDORIA,
                NomeAtributo.CARISMA
            )

            // Se for clássico → ordem fixa
            if (metodo is EstiloClassico) {
                val atribuicoes = mutableListOf<Atributo>()
                for (i in ordemPadrao.indices) {
                    atribuicoes += Atributo(ordemPadrao[i], valores[i])
                }
                return atribuicoes
            }

            // Se for aventureiro ou heróico → distribuição manual
            val atribuicoes = mutableListOf<Atributo>()
            val atributosDisponiveis = ordemPadrao.toMutableList()
            val valoresRestantes = valores.toMutableList()

            while (atributosDisponiveis.isNotEmpty()) {
                println("\nAtributos disponíveis:")
                atributosDisponiveis.forEachIndexed { index, atributo ->
                    println("${index + 1} - ${atributo.name}")
                }

                println("\nValores disponíveis: $valoresRestantes")
                print("Escolha o atributo (1-${atributosDisponiveis.size}): ")
                val escolhaAttr = scanner.nextInt()
                if (escolhaAttr !in 1..atributosDisponiveis.size) {
                    println("Opção inválida.")
                    continue
                }

                print("Escolha o índice do valor (1-${valoresRestantes.size}) para atribuir a ${atributosDisponiveis[escolhaAttr - 1].name}: ")
                val escolhaVal = scanner.nextInt()
                if (escolhaVal !in 1..valoresRestantes.size) {
                    println("Opção inválida.")
                    continue
                }

                val valor = valoresRestantes.removeAt(escolhaVal - 1)
                val atributo = atributosDisponiveis.removeAt(escolhaAttr - 1)
                atribuicoes += Atributo(atributo, valor)
            }

            return atribuicoes
        }
    }
}
