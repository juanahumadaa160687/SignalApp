package com.jpa.signal.data

import com.google.firebase.database.*
import kotlinx.coroutines.tasks.await

data class Usuario (
    val rut: String = "",
    val nombre: String = "",
    //val apellido: String = "",
    //val edad: Int = 0,
    //val genero: String = "",
    //val telefono: String = "",
    //val correo: String = "",
    //val direccion: String = ""
)

class UsuarioRepo {
     companion object {
         private var database: DatabaseReference = FirebaseDatabase.getInstance().getReference("usuarios")

         suspend fun getUsuario(rut: String): Usuario? {
             val snapshot = database.child(rut).get().await()
             return snapshot.getValue(Usuario::class.java)
         }
         suspend fun getUsuarios(): List<Usuario> {
             val snapshot = database.get().await()
             return snapshot.children.mapNotNull { it.getValue(Usuario::class.java) }
         }

         suspend fun addUsuario(usuario: Usuario) {
             database.child(usuario.rut).setValue(usuario).await()
         }
     }
}