package com.example.myapplication.controller

import android.content.Intent
import android.os.Build
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import com.example.myapplication.R
import com.example.myapplication.data.PersonagemRepository
import com.example.myapplication.data.db.AppDatabase
import com.example.myapplication.domain.CriacaoPersonagemService
import com.example.myapplication.model.inimigo.GeradorDeInimigos
import com.example.myapplication.model.personagens.Personagem
import com.example.myapplication.ui.theme.MyApplicationTheme
import com.example.myapplication.viewmodel.BatalhaViewModel

class BatalhaActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val personagemId = intent.getLongExtra(MainActivity6.EXTRA_PERSONAGEM_ID, -1L)

        val db = AppDatabase.getInstance(this)
        val repo = PersonagemRepository(db)
        val criacaoService = CriacaoPersonagemService()
        val viewModel = ViewModelProvider(this)[BatalhaViewModel::class.java]

        setContent {
            MyApplicationTheme {
                Surface(color = Color.Black) {
                    BatalhaScreen(
                        viewModel = viewModel,
                        personagemId = personagemId,
                        repo = repo,
                        criacaoService = criacaoService
                    )
                }
            }
        }
    }
}

@Composable
fun BatalhaScreen(
    viewModel: BatalhaViewModel,
    personagemId: Long,
    repo: PersonagemRepository,
    criacaoService: CriacaoPersonagemService
) {
    val context = LocalContext.current

    if (personagemId <= 0L) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                "Personagem inválido (ID não recebido).",
                color = Color.White
            )
        }
        return
    }

    val personagemFlow = remember(personagemId) { repo.buscarPorId(personagemId) }
    val entity by personagemFlow.collectAsState(initial = null)
    val personagem: Personagem? = remember(entity) {
        entity?.let { criacaoService.criarAPartirDaEntity(it) }
    }

    val log = viewModel.logBatalha

    Box(modifier = Modifier.fillMaxSize()) {

        Image(
            painter = painterResource(id = R.drawable.batalha),
            contentDescription = "Herói enfrentando Goblin",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0x80000000))
        )

        if (personagem == null) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text("Carregando personagem...", color = Color(0xFFF9F5E6))
            }
            return
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp, vertical = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(40.dp))

            Text(
                text = "Batalha de ${personagem.nome}",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = Color(0xFFF9F5E6),
                textAlign = TextAlign.Center
            )

            Spacer(Modifier.height(16.dp))

            Button(
                onClick = {
                    val inimigo = GeradorDeInimigos.gerarInimigo(personagem.nivel)
                    viewModel.lutar(personagem, inimigo)

                    val serviceIntent = Intent(context, BatalhaForegroundService::class.java).apply {
                        putExtra(BatalhaForegroundService.EXTRA_PERSONAGEM_ID, personagemId)
                    }

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        context.startForegroundService(serviceIntent)
                    } else {
                        context.startService(serviceIntent)
                    }
                }
            ) {
                Text("Simular batalha")
            }

            Spacer(modifier = Modifier.weight(0.7f))

            if (log.isNotBlank()) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xCC1A120A)
                    ),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Text(
                        text = log,
                        modifier = Modifier.padding(16.dp),
                        color = Color(0xFFF9F5E6),
                        style = MaterialTheme.typography.bodyMedium,
                        textAlign = TextAlign.Left
                    )
                }
            }

            Spacer(modifier = Modifier.weight(0.3f))
        }
    }
}
