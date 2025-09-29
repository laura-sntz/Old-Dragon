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
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.R
import com.example.myapplication.ui.theme.MyApplicationTheme

const val EXTRA_NOME = "NOME_PERSONAGEM"

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    TelaInicial(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun TelaInicial(modifier: Modifier = Modifier) {
    val contexto = LocalContext.current
    var nome by remember { mutableStateOf("") }
    var mostrarCampoNome by remember { mutableStateOf(false) } // controla se mostra input
    val nomeValido = nome.isNotBlank()

    Box(Modifier.fillMaxSize().then(modifier)) {
        // imagem de fundo
        Image(
            painter = painterResource(id = R.drawable.medieval_tavern_background),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        // gradiente por cima (suaviza a imagem para o texto ficar legível)
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color(0xCC000000),
                            Color(0x88000000)
                        )
                    )
                )
        )

        Column(
            modifier = Modifier
                .align(Alignment.Center)
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // título principal medieval
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    "OLD DRAGON",
                    style = TextStyle(
                        fontSize = 50.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                        fontFamily = FontFamily.Serif,
                        color = Color(0xFFD4AF37)
                    )
                )

                Spacer(modifier = Modifier.height(50.dp))
            }

            // botão nome do personagem
            Button(
                onClick = { mostrarCampoNome = true },
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .height(56.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(
                    "Nome do personagem", // texto fixo
                    style = MaterialTheme.typography.titleMedium
                )
            }

            // campo para digitar o nome (só aparece depois de clicar no botão acima)
            if (mostrarCampoNome) {
                OutlinedTextField(
                    value = nome,
                    onValueChange = { nome = it },
                    label = { Text("Digite o nome do seu herói") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(0.8f),
                    shape = RoundedCornerShape(8.dp),
                    textStyle = TextStyle(color = Color.White)
                )
            }

            // botão iniciar jornada
            Button(
                onClick = {
                    val intent = Intent(contexto, MainActivity2::class.java)
                    intent.putExtra(EXTRA_NOME, nome.trim())
                    contexto.startActivity(intent)
                },
                enabled = nomeValido,
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .height(56.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(
                    "Iniciar jornada",
                    style = MaterialTheme.typography.titleMedium
                )
            }
        }
    }
}