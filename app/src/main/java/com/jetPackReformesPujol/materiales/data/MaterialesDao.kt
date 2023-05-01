package com.jetPackReformesPujol.materiales.data

import androidx.room.*
import com.jetPackReformesPujol.data.MaterialesEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MaterialesDao {

    @Query("SELECT * from MaterialesEntity")
    fun getMateriales(): Flow<List<MaterialesEntity>>

    @Insert
    suspend fun addMaterial(item: MaterialesEntity)

    @Update
    suspend fun updateMaterial(item: MaterialesEntity)

    @Delete
    suspend fun deleteMaterial(item: MaterialesEntity)
}