package com.jetPackReformesPujol.trabajos.data

import androidx.room.*
import com.jetPackReformesPujol.data.ClienteEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ClienteDao {

    @Query("SELECT * from ClienteEntity")
    fun getClientes(): Flow<List<ClienteEntity>>

    @Query("SELECT * from ClienteEntity WHERE id = :id ")
    fun getClienteById(id: Int): ClienteEntity

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addCliente(item: ClienteEntity)

    @Update
    suspend fun updateCliente(item: ClienteEntity)

    @Delete
    suspend fun deleteCliente(item: ClienteEntity)
}