package com.example.myapplication.controller

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.model.classes.subclasses.Academico
import com.example.myapplication.model.classes.subclasses.Barbaro
import com.example.myapplication.model.classes.subclasses.Bardo
import com.example.myapplication.model.classes.subclasses.Druida
import com.example.myapplication.model.classes.subclasses.Ilusionista
import com.example.myapplication.model.classes.subclasses.Necromante
import com.example.myapplication.model.classes.subclasses.Paladino
import com.example.myapplication.model.classes.subclasses.Ranger
import com.example.myapplication.ui.theme.FieldLight
import com.example.myapplication.ui.theme.MedievalBrown
import com.example.myapplication.ui.theme.MedievalGold
import com.example.myapplication.ui.theme.classe.subclasse.subclasseDisplayName
import com.example.myapplication.ui.theme.classe.subclasse.subclasseImageRes
import com.example.myapplication.ui.theme.MyApplicationTheme

const val EXTRA_SUB    = "SUBCLASSE"

class MainActivity4 : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val nome   = intent.getStringExtra(EXTRA_NOME) ?: "Herói sem nome"
        val raca   = intent.getStringExtra(EXTRA_RACA)
        val classe = intent.getStringExtra(EXTRA_CLASSE)

        setContent {
            MyApplicationTheme {
                Surface(Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    TelaSubclasse(nomePersonagem = nome, selectedClasse = classe,
                        onPular = {
                            val prox = Intent(this, MainActivity5::class.java).apply {
                                putExtra(EXTRA_NOME, nome)
                                putExtra(EXTRA_RACA, raca)
                                putExtra(EXTRA_CLASSE, classe)
                            }
                            startActivity(prox)
                        },
                        onSelecionar = { subEscolhida ->
                            val prox = Intent(this, MainActivity5::class.java).apply {
                                putExtra(EXTRA_NOME, nome)
                                putExtra(EXTRA_RACA, raca)
                                putExtra(EXTRA_CLASSE, classe)
                                putExtra(EXTRA_SUB, subEscolhida::class.java.simpleName)
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
fun TelaSubclasse(
    nomePersonagem: String,
    selectedClasse: String?,
    onPular: () -> Unit,
    onSelecionar: (Any) -> Unit
) {

    val lista = remember(selectedClasse) {
        when (selectedClasse) {
            "Guerreiro" -> listOf(Barbaro(), Paladino())
            "Clérigo", "Clerigo" -> listOf(Druida(), Academico())
            "Ladrão", "Ladrao" -> listOf(Ranger(), Bardo())
            "Mago" -> listOf(Ilusionista(), Necromante())
            else -> listOf(Barbaro(), Paladino(), Druida(), Academico(), Ranger(), Bardo(), Ilusionista(), Necromante())
        }
    }


    var escolherAgora by remember { mutableStateOf<Boolean?>(null) }
    var index by remember { mutableStateOf(0) }
    val atual = lista[index]

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(40.dp))

            if (escolherAgora != true) {
                Text(
                    "Deseja escolher uma subclasse agora?",
                    style = MaterialTheme.typography.headlineMedium,
                    textAlign = TextAlign.Center,
                    color = MedievalBrown
                )
                Text(
                    "Você pode definir a subclasse depois.",
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Center
                )

                Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    Button(onClick = { escolherAgora = true }) { Text("Sim, escolher agora") }
                    OutlinedButton(onClick = onPular) { Text("Não, decidir depois") }
                }
            } else {
                // seletor de subclasses (aparece após clicar em Sim)
                Text(
                    "Escolha a subclasse",
                    style = MaterialTheme.typography.headlineMedium,
                    textAlign = TextAlign.Center,
                    color = MedievalGold
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .height(600.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    IconButton(
                        onClick = { index = if (index == 0) lista.lastIndex else index - 1 },
                        modifier = Modifier
                            .size(56.dp)
                            .clip(RoundedCornerShape(28.dp))
                    ) {
                        Icon(
                            Icons.Filled.ArrowBack,
                            contentDescription = "Anterior",
                            tint = MedievalGold,
                            modifier = Modifier.size(40.dp)
                        )
                    }

                    Column(
                        modifier = Modifier.weight(1f),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Image(
                            painter = painterResource(atual.subclasseImageRes()),
                            contentDescription = atual.subclasseDisplayName(),
                            modifier = Modifier.size(width = 300.dp, height = 400.dp),
                            contentScale = ContentScale.Fit
                        )
                        Spacer(Modifier.height(16.dp))
                        Text(
                            atual.subclasseDisplayName(),
                            style = MaterialTheme.typography.titleLarge,
                            color = MedievalGold
                        )
                    }

                    IconButton(
                        onClick = { index = if (index == lista.lastIndex) 0 else index + 1 },
                        modifier = Modifier
                            .size(56.dp)
                            .clip(RoundedCornerShape(28.dp))
                    ) {
                        Icon(
                            Icons.Filled.ArrowForward,
                            contentDescription = "Próxima",
                            tint = MedievalGold,
                            modifier = Modifier.size(40.dp)
                        )
                    }
                }

                Button(
                    onClick = { onSelecionar(atual) },
                    modifier = Modifier
                        .fillMaxWidth(0.8f)
                        .height(56.dp),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Escolher ${atual.subclasseDisplayName()}", style = MaterialTheme.typography.titleMedium)
                }
            }
        }
    }
}