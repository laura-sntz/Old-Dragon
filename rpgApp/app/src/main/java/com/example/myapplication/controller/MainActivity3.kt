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
import com.example.myapplication.model.classes.base.Classe as ModelClasse
import com.example.myapplication.model.classes.implementacoes.Clerigo
import com.example.myapplication.model.classes.implementacoes.Guerreiro
import com.example.myapplication.model.classes.implementacoes.Ladrao
import com.example.myapplication.model.classes.implementacoes.Mago
import com.example.myapplication.ui.theme.FieldLight
import com.example.myapplication.ui.theme.MedievalGold
import com.example.myapplication.ui.theme.MyApplicationTheme
import com.example.myapplication.ui.theme.classe.displayName
import com.example.myapplication.ui.theme.classe.imageRes

const val EXTRA_CLASSE = "CLASSE"

class MainActivity3 : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val nome = intent.getStringExtra(EXTRA_NOME) ?: "Herói sem nome"
        val raca = intent.getStringExtra(EXTRA_RACA)

        setContent {
            MyApplicationTheme {
                Surface(Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    EscolherClasse(
                        nomePersonagem = nome,
                        onSelecionar = { classe ->
                            val prox = Intent(this, MainActivity4::class.java).apply {
                                putExtra(EXTRA_NOME, nome)
                                putExtra(EXTRA_RACA, raca)
                                putExtra(EXTRA_CLASSE, classe::class.java.simpleName)
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
fun EscolherClasse(
    nomePersonagem: String,
    onSelecionar: (ModelClasse) -> Unit
) {
    val classes: List<ModelClasse> = remember { listOf(Guerreiro(), Ladrao(), Mago(), Clerigo()) }
    var index by remember { mutableStateOf(0) }
    val atual = classes[index]

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
                "Escolha a classe",
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
                    onClick = { index = if (index == 0) classes.lastIndex else index - 1 },
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
                        modifier = Modifier.size(width = 300.dp, height = 400.dp),
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
                    onClick = { index = if (index == classes.lastIndex) 0 else index + 1 },
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
                Text("Escolher ${atual.displayName()}", style = MaterialTheme.typography.titleMedium)
            }
        }
    }
}