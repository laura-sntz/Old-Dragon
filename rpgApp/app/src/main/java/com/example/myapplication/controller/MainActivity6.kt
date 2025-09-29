package com.example.myapplication.controller

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.myapplication.R
import com.example.myapplication.model.atributos.Atributo
import com.example.myapplication.model.atributos.NomeAtributo
import com.example.myapplication.ui.theme.MyApplicationTheme

class MainActivity6 : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val nome   = intent.getStringExtra(EXTRA_NOME) ?: "Herói sem nome"
        val raca   = intent.getStringExtra(EXTRA_RACA) ?: "—"
        val classe = intent.getStringExtra(EXTRA_CLASSE) ?: "—"
        val sub    = intent.getStringExtra(EXTRA_SUB) // pode ser nulo
        val attrs  = intent.getIntArrayExtra(EXTRA_ATTRS) ?: IntArray(6) { 10 }

        setContent {
            MyApplicationTheme {
                Surface(Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    FichaBasicaScreen(
                        nome = nome,
                        raca = raca,
                        classe = classe,
                        subclasse = sub,
                        atributosArray = attrs
                    )
                }
            }
        }
    }
}

@Composable
fun FichaBasicaScreen(
    nome: String,
    raca: String,
    classe: String,
    subclasse: String?,
    atributosArray: IntArray
) {
    // reconstrói a lista de Atributo usando teu Model
    val atributos: List<Atributo> = remember(atributosArray.toList()) {
        NomeAtributo.values().mapIndexed { idx, na ->
            Atributo(nome = na, valor = atributosArray.getOrNull(idx) ?: 10)
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = R.drawable.medieval_field_background),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Spacer(modifier = Modifier.height(60.dp))
            Text(
                "Ficha do Personagem",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
            )

            Card {
                Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Linha("Nome", nome)
                    Linha("Raça", raca)
                    Linha("Classe", classe)
                    if (subclasse != null) Linha("Subclasse", subclasse)
                }
            }

            Card {
                Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        "Atributos",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                    atributos.forEach { atr ->
                        Linha(
                            titulo = "${atr.nome.name} (${atr.nome.sigla})",
                            valor = "${atr.valor}  (mod ${fmtMod(atr.modificador)})"
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun Linha(titulo: String, valor: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(titulo)
        Text(valor, fontWeight = FontWeight.SemiBold)
    }
}

private fun fmtMod(m: Int) = if (m >= 0) "+$m" else "$m"