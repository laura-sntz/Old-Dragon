package com.example.myapplication.controller

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.model.atributos.NomeAtributo
import com.example.myapplication.model.distribuicao.EstiloRolagem
import com.example.myapplication.model.distribuicao.estiloParaMetodo
import com.example.myapplication.ui.theme.MyApplicationTheme

const val EXTRA_ATTRS  = "ATRIBUTOS_ARRAY"

class MainActivity5 : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val nome   = intent.getStringExtra(EXTRA_NOME) ?: "Herói sem nome"
        val raca   = intent.getStringExtra(EXTRA_RACA)
        val classe = intent.getStringExtra(EXTRA_CLASSE) ?: ""
        val sub    = intent.getStringExtra(EXTRA_SUB)

        setContent {
            MyApplicationTheme {
                Surface(Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    DistribuicaoScreen(
                        nomePersonagem = nome,
                        classeSimpleName = classe,
                        onConcluir = { mapa ->
                            // empacota os atributos na ordem do enum
                            val ordered = NomeAtributo.values().map { mapa[it] ?: 10 }.toIntArray()
                            val prox = Intent(this, MainActivity6::class.java).apply {
                                putExtra(EXTRA_NOME, nome)
                                putExtra(EXTRA_RACA, raca)
                                putExtra(EXTRA_CLASSE, classe)
                                if (sub != null) putExtra(EXTRA_SUB, sub)
                                putExtra(EXTRA_ATTRS, ordered)
                            }
                            startActivity(prox)
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun DistribuicaoScreen(
    nomePersonagem: String,
    classeSimpleName: String,
    onConcluir: (Map<NomeAtributo, Int>) -> Unit
) {
    var estilo by remember { mutableStateOf(EstiloRolagem.CLASSICO) }
    var rolagens by remember { mutableStateOf(emptyList<Int>()) }

    val atributos: List<NomeAtributo> = remember { NomeAtributo.values().toList() }

    var atribuicoes by remember {
        mutableStateOf<Map<NomeAtributo, Int?>>(atributos.associateWith { null })
    }

    fun limparDistribuicao() {
        atribuicoes = atributos.associateWith { null }
    }

    // embaralha as rolagens somente quando elas mudam
    val rolagensEmbaralhadas = remember(rolagens) { rolagens.shuffled() }

    // se for CLASSICO e já tivermos 6 rolagens, atribui automaticamente (aleatório)
    LaunchedEffect(estilo, rolagensEmbaralhadas) {
        if (estilo == EstiloRolagem.CLASSICO && rolagensEmbaralhadas.size == atributos.size) {
            val out = mutableMapOf<NomeAtributo, Int?>()
            atributos.forEachIndexed { idx, atr ->
                out[atr] = rolagensEmbaralhadas.getOrNull(idx)
            }
            atribuicoes = out
        }
    }

    val restantes: List<Int> = remember(rolagens, atribuicoes) {
        val usados = atribuicoes.values.filterNotNull()
        val copia = rolagens.toMutableList()
        usados.forEach { v -> copia.remove(v) }
        copia
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(60.dp))
        Text("Distribuição de Atributos", style = MaterialTheme.typography.titleLarge, textAlign = TextAlign.Center)

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            MetodoChip("Clássico", estilo == EstiloRolagem.CLASSICO) { estilo = EstiloRolagem.CLASSICO }
            MetodoChip("Aventureiro", estilo == EstiloRolagem.AVENTUREIRO) { estilo = EstiloRolagem.AVENTUREIRO }
            MetodoChip("Heróico", estilo == EstiloRolagem.HEROICO) { estilo = EstiloRolagem.HEROICO }
        }

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(onClick = {
                rolagens = estiloParaMetodo(estilo).gerarAtributos()
                limparDistribuicao()
            }) { Text("Gerar rolagens") }

            OutlinedButton(
                onClick = { autoDistribuir(classeSimpleName, rolagens) { atribuicoes = it } },
                enabled = rolagens.size == 6
            ) {  Text(
                text = "Preencher automaticamente",
                style = TextStyle(fontSize = 12.sp)
            ) }

            TextButton(onClick = { limparDistribuicao() }) { Text("Limpar") }
        }

        if (rolagens.isNotEmpty()) {
            Text("Rolagens: ${rolagens.joinToString(", ")}")
            Text("Restantes: ${restantes.joinToString(", ").ifEmpty { "—" }}")
        }

        Column(verticalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
            atributos.forEach { atributo ->
                val atual = atribuicoes[atributo] // pegamos sempre do estado
                if (estilo == EstiloRolagem.CLASSICO) {
                    // no clássico: não permitir escolha manual — apenas exibir o valor
                    AtributoPickerRow(
                        atributo = atributo,
                        valorAtual = atual,
                        opcoes = emptyList(),
                        permitirEscolha = false
                    )
                } else {
                    val opcoes = restantes + listOfNotNull(atribuicoes[atributo])
                    AtributoPickerRow(
                        atributo = atributo,
                        valorAtual = atual,
                        opcoes = opcoes,
                        permitirEscolha = true
                    ) { novoValor ->
                        atribuicoes = atribuicoes.toMutableMap().apply { this[atributo] = novoValor }
                    }
                }
            }
        }

        val tudoPreenchido = atribuicoes.values.all { it != null }
        Button(
            onClick = { onConcluir(atribuicoes.mapValues { it.value!! }) },
            enabled = tudoPreenchido
        ) { Text("Concluir distribuição") }
    }
}

@Composable
private fun MetodoChip(text: String, selected: Boolean, onClick: () -> Unit) {
    FilterChip(selected = selected, onClick = onClick, label = { Text(text) })
}

@Composable
private fun AtributoPickerRow(
    atributo: NomeAtributo,
    valorAtual: Int?,
    opcoes: List<Int>,
    permitirEscolha: Boolean = true,
    onEscolher: ((Int?) -> Unit)? = null
) {
    var menuAberto by remember { mutableStateOf(false) }
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text("${atributo.name} (${atributo.sigla})", style = MaterialTheme.typography.titleMedium)
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
            if (permitirEscolha) {
                Text(valorAtual?.toString() ?: "—")
                OutlinedButton(onClick = { menuAberto = true }, enabled = opcoes.isNotEmpty()) { Text("Escolher") }
                DropdownMenu(expanded = menuAberto, onDismissRequest = { menuAberto = false }) {
                    DropdownMenuItem(text = { Text("— (limpar)") }, onClick = { menuAberto = false; onEscolher?.invoke(null) })
                    opcoes.forEach { v ->
                        DropdownMenuItem(text = { Text(v.toString()) }, onClick = { menuAberto = false; onEscolher?.invoke(v) })
                    }
                }
            } else {
                // estilo clássico: apenas exibe o valor (não mostra o menu)
                Text(valorAtual?.toString() ?: "—")
            }
        }
    }
}

private fun autoDistribuir(
    classeSimpleName: String,
    rolagens: List<Int>,
    onAtribuir: (Map<NomeAtributo, Int?>) -> Unit
) {
    if (rolagens.size != 6) return
    val prioridade: List<NomeAtributo> = when (classeSimpleName) {
        "Guerreiro" -> listOf(NomeAtributo.FORCA, NomeAtributo.CONSTITUICAO, NomeAtributo.DESTREZA, NomeAtributo.SABEDORIA, NomeAtributo.CARISMA, NomeAtributo.INTELIGENCIA)
        "Mago"      -> listOf(NomeAtributo.INTELIGENCIA, NomeAtributo.SABEDORIA, NomeAtributo.DESTREZA, NomeAtributo.CONSTITUICAO, NomeAtributo.CARISMA, NomeAtributo.FORCA)
        "Clerigo"   -> listOf(NomeAtributo.SABEDORIA, NomeAtributo.CONSTITUICAO, NomeAtributo.FORCA, NomeAtributo.CARISMA, NomeAtributo.DESTREZA, NomeAtributo.INTELIGENCIA)
        "Ladrao"    -> listOf(NomeAtributo.DESTREZA, NomeAtributo.INTELIGENCIA, NomeAtributo.CARISMA, NomeAtributo.CONSTITUICAO, NomeAtributo.SABEDORIA, NomeAtributo.FORCA)
        else        -> listOf(NomeAtributo.FORCA, NomeAtributo.CONSTITUICAO, NomeAtributo.DESTREZA, NomeAtributo.INTELIGENCIA, NomeAtributo.SABEDORIA, NomeAtributo.CARISMA)
    }
    val ordenados = rolagens.sortedDescending()
    val out = mutableMapOf<NomeAtributo, Int?>()
    prioridade.forEachIndexed { i, atr -> out[atr] = ordenados.getOrNull(i) }
    onAtribuir(out)
}