package com.jetPackReformesPujol.data

import android.app.Application
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.*
import androidx.datastore.core.handlers.ReplaceFileCorruptionHandler
import androidx.datastore.migrations.SharedPreferencesMigration
import androidx.datastore.preferences.SharedPreferencesMigration
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.preferencesDataStoreFile
import androidx.room.Room
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.jetPackReformesPujol.login.data.UserPreferences
import com.jetPackReformesPujol.login.data.repository.AuthRepositoryImpl
import com.jetPackReformesPujol.login.domain.repository.AuthRepository
import com.jetPackReformesPujol.materiales.data.MaterialesDao
import com.jetPackReformesPujol.presupuestos.data.PresupuestosDao
import com.jetPackReformesPujol.trabajos.domain.FirebaseRepository
import com.jetPackReformesPujol.trabajos.data.ClienteDao
import com.jetPackReformesPujol.trabajos.ui.TrabajosViewModel
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import javax.inject.Singleton

private const val USER_PREFERENCES = "user_preferences"

//Módulo Dagger (Módulo de inyección)
@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    fun provideMaterialesDao(roomDatabase: RoomDatabase): MaterialesDao {
        return roomDatabase.MaterialesDao()
    }

    @Provides
    fun provideTrabajosDao(roomDatabase: RoomDatabase): ClienteDao {
        return roomDatabase.ClienteDao()
    }

    @Provides
    fun providePresupuestosDao(roomDatabase: RoomDatabase): PresupuestosDao {
        return roomDatabase.PresupuestosDao()
    }

    @Provides
    @Singleton
    fun provideMaterialesDatabase(@ApplicationContext appContext: Context): RoomDatabase {
        return Room.databaseBuilder(appContext, RoomDatabase::class.java, "MaterialesDatabase").build()
    }

    @Provides
    fun provideAuthRepository(): AuthRepository = AuthRepositoryImpl(auth = Firebase.auth)

    @Provides
    @Singleton
    fun provideFirebaseDatabase(): FirebaseDatabase {
        return FirebaseDatabase.getInstance()
    }

    @Provides
    @Singleton
    fun provideFirebaseStorage(): FirebaseStorage {
        return FirebaseStorage.getInstance()
    }

    @Provides
    @Singleton
    fun provideFirebaseRepository(firebaseDatabase: FirebaseDatabase, firebaseStorage: FirebaseStorage): FirebaseRepository {
        return FirebaseRepository(firebaseDatabase, firebaseStorage)
    }

    @Provides
    fun provideUserPreferences(context: Context): UserPreferences {
        return UserPreferences(context)
    }

    @Provides
    fun provideContext(application: Application): Context {
        return application.applicationContext
    }
}
