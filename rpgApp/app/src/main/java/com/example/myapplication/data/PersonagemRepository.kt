package com.example.myapplication.data

import com.example.myapplication.data.db.AppDatabase
import com.example.myapplication.data.entity.PersonagemEntity
import kotlinx.coroutines.flow.Flow

class PersonagemRepository(private val db: AppDatabase) {

    fun listarTodos(): Flow<List<PersonagemEntity>> =
        db.personagemDao().listarTodos()

    fun buscarPorId(id: Long): Flow<PersonagemEntity?> =
        db.personagemDao().buscarPorId(id)

    suspend fun salvar(personagem: PersonagemEntity): Long =
        db.personagemDao().inserir(personagem)

    suspend fun atualizar(personagem: PersonagemEntity) =
        db.personagemDao().atualizar(personagem)

    suspend fun deletar(personagem: PersonagemEntity) =
        db.personagemDao().deletar(personagem)
}
