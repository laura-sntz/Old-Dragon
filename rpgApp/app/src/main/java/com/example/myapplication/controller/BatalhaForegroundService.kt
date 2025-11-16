package com.example.myapplication.controller

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.ServiceInfo
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.example.myapplication.R
import com.example.myapplication.data.PersonagemRepository
import com.example.myapplication.data.db.AppDatabase
import com.example.myapplication.domain.BatalhaService
import com.example.myapplication.domain.CriacaoPersonagemService
import com.example.myapplication.model.inimigo.GeradorDeInimigos
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch

class BatalhaForegroundService : Service() {

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val personagemId = intent?.getLongExtra(EXTRA_PERSONAGEM_ID, -1L) ?: -1L
        if (personagemId == -1L) {
            stopSelf()
            return START_NOT_STICKY
        }

        iniciarComoForeground()

        scope.launch {
            val db = AppDatabase.getInstance(applicationContext)
            val repo = PersonagemRepository(db)
            val entity = repo.buscarPorId(personagemId).firstOrNull()

            if (entity == null) {
                stopSelf()
                return@launch
            }

            val criacaoService = CriacaoPersonagemService()
            val personagem = criacaoService.criarAPartirDaEntity(entity)

            val inimigo = GeradorDeInimigos.gerarInimigo(personagem.nivel)

            val batalhaService = BatalhaService()
            val resultado = batalhaService.iniciar(personagem, inimigo)

            if (resultado.heroiMorreu) {
                mostrarNotificacaoMorte(personagem.nome)
            }

            stopSelf()
        }

        return START_NOT_STICKY
    }

    private fun iniciarComoForeground() {
        criarCanalNotificacao(CHANNEL_ID, "Batalhas", NotificationManager.IMPORTANCE_LOW)

        val notification: Notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle("Batalha em andamento")
            .setContentText("Seu personagem está lutando em segundo plano.")
            .setOngoing(true)
            .build()

        if (Build.VERSION.SDK_INT >= 34) {
            startForeground(
                NOTIFICATION_ID_FOREGROUND,
                notification,
                ServiceInfo.FOREGROUND_SERVICE_TYPE_SPECIAL_USE
            )
        } else {
            startForeground(NOTIFICATION_ID_FOREGROUND, notification)
        }
    }

    private fun mostrarNotificacaoMorte(nomePersonagem: String) {
        criarCanalNotificacao(
            CHANNEL_MORTE_ID,
            "Resultados de batalha",
            NotificationManager.IMPORTANCE_HIGH
        )

        val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val notification = NotificationCompat.Builder(this, CHANNEL_MORTE_ID)
            .setSmallIcon(R.mipmap.ic_launcher) // ou um ícone de “morte”
            .setContentTitle("Seu personagem morreu")
            .setContentText("$nomePersonagem foi derrotado em batalha.")
            .setAutoCancel(true)
            .build()

        manager.notify(NOTIFICATION_ID_MORTE, notification)
    }

    private fun criarCanalNotificacao(id: String, nome: String, importance: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            val canalExistente = manager.getNotificationChannel(id)
            if (canalExistente == null) {
                val channel = NotificationChannel(id, nome, importance)
                manager.createNotificationChannel(channel)
            }
        }
    }

    companion object {
        const val EXTRA_PERSONAGEM_ID = "extra_personagem_id"

        private const val CHANNEL_ID = "batalha_channel"
        private const val CHANNEL_MORTE_ID = "batalha_result_channel"

        private const val NOTIFICATION_ID_FOREGROUND = 1
        private const val NOTIFICATION_ID_MORTE = 2
    }
}
