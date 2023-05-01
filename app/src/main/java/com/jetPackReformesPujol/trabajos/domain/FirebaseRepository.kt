package com.jetPackReformesPujol.trabajos.domain

import android.net.Uri
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import com.jetPackReformesPujol.trabajos.model.ClienteModelFirebase
import kotlinx.coroutines.tasks.await
import timber.log.Timber
import javax.inject.Inject


class FirebaseRepository @Inject constructor(
    private val FireBaseDB: FirebaseDatabase,
    private val FireBaseStorage: FirebaseStorage
) {

    fun addClienteFirebase(cliente: ClienteModelFirebase) {
        try {
            val database = FirebaseDatabase.getInstance()
            val myRef = database.getReference("clients").child(cliente.clienteid!!)
            myRef.setValue(cliente)
        } catch (e: Exception) {
            Timber.tag("ERROR FIREBASE").e("Error adding data to Firebase: %s", e.message)
        }
    }

    fun deleteClienteFirebase(clienteId: String) {
        try {
            FireBaseDB.getReference("clients").child(clienteId).removeValue()
            Timber.tag("FIREBASE OK").d("Data saved successfully to Firebase.")
        } catch (e: Exception) {
            Timber.tag("ERROR FIREBASE").e("Error deleting data from Firebase: %s", e.message)
        }
    }

    suspend fun getPicturesFromFirebaseStorage(clienteId: String): List<String> {
        val listUrls = mutableListOf<String>()
        try {
            val mStoragePictures =
                FirebaseStorage.getInstance().reference.child("Fotos Clients").child(clienteId)
            val listResult = mStoragePictures.listAll().await()
            for (item in listResult.items) {
                val url = item.downloadUrl.await().toString()
                listUrls.add(url)
            }
        } catch (e: Exception) {
            Timber.tag("GetPicturesFromDB").e(e, "Error: %s", e.message)
        }
        return listUrls
    }

    fun addPicturesFromStorageToFireBaseStorage(
        clienteId: String,
        selectedImageUri: Uri?
    ) {
        val firebaseStorage = Firebase.storage
        val imageRef = selectedImageUri?.let { uri ->
            firebaseStorage.reference.child("Fotos Clients").child(clienteId)
                .child(uri.lastPathSegment!!)
        }
        val uploadTask = imageRef?.putFile(selectedImageUri)
        uploadTask?.addOnSuccessListener {
            imageRef.downloadUrl.addOnSuccessListener { uri ->

            }
        }
    }

    fun deletePicturesFirebaseStorage(photoId: String) {
        try {
            FireBaseStorage.getReferenceFromUrl(photoId).delete()
            Timber.tag("FIREBASE OK").d("Data saved successfully to Firebase.")
        } catch (e: Exception) {
            Timber.tag("ERROR FIREBASE").e("Error saving data to Firebase: %s", e.message)
        }
    }
}