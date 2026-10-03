package com.jpa.signal.data

import com.google.firebase.database.*
import kotlinx.coroutines.tasks.await

data class Usuario (
    val uid: String = "",
    val rut: String = "",
    val nombre: String = "",
    val apellido: String = "",
    val edad: String = "",
    val genero: String = "",
    val telefono: String = "",
    val correo: String = "",
    val direccion: String = ""
)

class UsuarioRepo {
     companion object {
         private var database: DatabaseReference = FirebaseDatabase.getInstance().getReference("usuarios")

         suspend fun getUsuarioByUid(uid: String): Usuario? {
             val snapshot = database.child(uid).get().await()
             return snapshot.getValue(Usuario::class.java)
         }

         suspend fun deleteUsuario(uid: String) {
             database.child(uid).removeValue().await()
         }

         suspend fun updateUsuario(usuario: Usuario) {
             database.child(usuario.uid).setValue(usuario).await()
         }

         suspend fun addUsuario(usuario: Usuario) {
             database.child(usuario.uid).setValue(usuario).await()
         }
     }
}