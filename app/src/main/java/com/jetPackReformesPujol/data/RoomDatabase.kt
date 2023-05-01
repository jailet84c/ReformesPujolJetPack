package com.jetPackReformesPujol.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.jetPackReformesPujol.materiales.data.MaterialesDao
import com.jetPackReformesPujol.presupuestos.data.PresupuestosDao
import com.jetPackReformesPujol.trabajos.data.ClienteDao

@Database(
    entities = [MaterialesEntity::class, ClienteEntity::class, PresupuestoEntity::class],
    version = 7
)
abstract class RoomDatabase : RoomDatabase() {

    abstract fun MaterialesDao(): MaterialesDao
    abstract fun ClienteDao(): ClienteDao
    abstract fun PresupuestosDao(): PresupuestosDao

    companion object {
        private var INSTANCE: RoomDatabase? = null

        fun getInstance(context: Context): RoomDatabase {
            if (INSTANCE == null) {
                INSTANCE = Room.databaseBuilder(
                    context.applicationContext,
                    RoomDatabase::class.java,
                    "my-db"
                )
                    .addCallback(MigrationFrom4To5())
                    .build()
            }
            return INSTANCE as RoomDatabase
        }
    }

    private class MigrationFrom4To5 : RoomDatabase.Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            // Definir los cambios necesarios para actualizar la estructura de la base de datos
            // por ejemplo, agregar una nueva tabla, columna o índice

        }
    }
}

