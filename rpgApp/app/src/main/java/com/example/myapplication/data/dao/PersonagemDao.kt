package com.example.myapplication.data.dao

import androidx.room.*
import com.example.myapplication.data.entity.PersonagemEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PersonagemDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun inserir(personagem: PersonagemEntity): Long

    @Update
    suspend fun atualizar(personagem: PersonagemEntity)

    @Delete
    suspend fun deletar(personagem: PersonagemEntity)

    @Query("SELECT * FROM personagens ORDER BY id DESC")
    fun listarTodos(): Flow<List<PersonagemEntity>>

    @Query("SELECT * FROM personagens WHERE id = :id LIMIT 1")
    fun buscarPorId(id: Long): Flow<PersonagemEntity?>
}
