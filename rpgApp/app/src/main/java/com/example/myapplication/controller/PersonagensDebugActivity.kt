package com.example.myapplication.controller

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AssistChip
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.myapplication.ui.theme.MyApplicationTheme
import com.example.myapplication.data.PersonagemRepository
import com.example.myapplication.data.db.AppDatabase
import com.example.myapplication.data.entity.PersonagemEntity
import kotlinx.coroutines.flow.collectLatest

class PersonagensDebugActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val repo = PersonagemRepository(AppDatabase.getInstance(this))

        setContent {
            MyApplicationTheme {
                Surface(Modifier.fillMaxSize()) {
                    DebugListaScreen(repo)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DebugListaScreen(repo: PersonagemRepository) {
    var lista by remember { mutableStateOf<List<PersonagemEntity>>(emptyList()) }

    LaunchedEffect(Unit) {
        repo.listarTodos().collectLatest { lista = it }
    }

    Surface(Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.surface) {
        androidx.compose.foundation.layout.Column(Modifier.fillMaxSize()) {
            CenterAlignedTopAppBar(
                title = { Text("Banco local — Personagens") }
            )
            HorizontalDivider()

            LazyColumn(
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(lista, key = { it.id }) { p ->
                    PersonagemCard(p)
                }
            }
        }
    }
}

@Composable
private fun PersonagemCard(p: PersonagemEntity) {
    ElevatedCard(
        modifier = Modifier.fillMaxSize(),
        shape = MaterialTheme.shapes.large
    ) {
        androidx.compose.foundation.layout.Column(
            Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            androidx.compose.foundation.layout.Column {
                Text(
                    text = p.nome,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(Modifier.height(2.dp))
                Text(
                    text = "${p.raca}  •  ${p.classe}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            AtributosChips(
                mapOf(
                    "FOR" to p.forca,
                    "DES" to p.destreza,
                    "CON" to p.constituicao,
                    "INT" to p.inteligencia,
                    "SAB" to p.sabedoria,
                    "CAR" to p.carisma
                )
            )
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun AtributosChips(attrs: Map<String, Int>) {
    FlowRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        attrs.forEach { (rotulo, valor) ->
            AssistChip(
                onClick = {},
                label = {
                    Text("$rotulo $valor", style = MaterialTheme.typography.labelLarge)
                }
            )
        }
    }
}
