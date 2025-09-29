package com.example.myapplication.controller

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
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
import com.example.myapplication.model.personagens.racas.Raca as ModelRaca
import com.example.myapplication.model.personagens.racas.Humano
import com.example.myapplication.model.personagens.racas.Elfo
import com.example.myapplication.model.personagens.racas.Anao
import com.example.myapplication.model.personagens.racas.Halfling
import com.example.myapplication.ui.theme.raca.displayName
import com.example.myapplication.ui.theme.raca.imageRes
import com.example.myapplication.ui.theme.MyApplicationTheme
import com.example.myapplication.ui.theme.MedievalGold
import com.example.myapplication.ui.theme.FieldLight

const val EXTRA_RACA = "RACA"
class MainActivity2 : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // vem da primeira tela
        val nome = intent.getStringExtra(EXTRA_NOME) ?: "Herói sem nome"

        setContent {
            MyApplicationTheme {
                Surface(Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    EscolherRaca(
                        nomePersonagem = nome,
                        onSelecionar = { racaEscolhida ->
                            val prox = Intent(this, MainActivity3::class.java).apply {
                                putExtra(EXTRA_NOME, nome)
                                putExtra(EXTRA_RACA, racaEscolhida::class.java.simpleName)
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
fun EscolherRaca(
    nomePersonagem: String,
    onSelecionar: (ModelRaca) -> Unit
) {
    val racas: List<ModelRaca> = remember {
        listOf(Humano(), Elfo(), Anao(), Halfling())
    }

    var index by remember { mutableStateOf(0) }
    val atual = racas[index]

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(modifier = Modifier.height(40.dp))
            Text(
                "Escolha sua Linhagem",
                style = MaterialTheme.typography.headlineMedium,
                textAlign = TextAlign.Center,
                color = MedievalGold
            )

            // seletor de raça
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .height(600.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                IconButton(
                    onClick = {
                        index = if (index == 0) racas.lastIndex else index - 1
                    },
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
                        painter = painterResource(atual.imageRes()),
                        contentDescription = atual.displayName(),
                        modifier = Modifier
                            .size(width = 300.dp, height = 400.dp),
                        contentScale = ContentScale.Fit
                    )
                    Spacer(Modifier.height(16.dp))
                    Text(
                        atual.displayName(),
                        style = MaterialTheme.typography.titleLarge,
                        color = MedievalGold
                    )
                }

                IconButton(
                    onClick = {
                        index = if (index == racas.lastIndex) 0 else index + 1
                    },
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

            // botão de seleção
            Button(
                onClick = { onSelecionar(atual) },
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .height(56.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(
                    "Escolher ${atual.displayName()}",
                    style = MaterialTheme.typography.titleMedium
                )
            }
        }
    }
}