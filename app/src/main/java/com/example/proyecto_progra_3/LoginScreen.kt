package com.example.proyecto_progra_3

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.widget.Button
import android.os.Bundle
import android.view.View
import android.widget.*
import java.util.*
class LoginScreen : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_screen)
        val usuario1 = Usuario("Fer123", "Fernando Alvarado Lanza", 77716326, "Pass123")
        val usuario2 = Usuario("Salao", "Samuel Escobar Bejarano", 77585417, "pbbs159")
        val usuario3 = Usuario("Pary", "Luis Andres Paricollo Parra", 77585417, "samusalao")
        val listaUsuarios = mutableListOf(usuario1, usuario2, usuario3)
        val botonEntrar = findViewById<Button>(R.id.botonEntrar)
        val nombreDeUsuario = findViewById<EditText>(R.id.nombreDeUsuario)
        val contrasenia = findViewById<EditText>(R.id.contrasenia)
        botonEntrar.setOnClickListener{
            var usuarioEncontrado = false
            listaUsuarios.forEach {
                if (nombreDeUsuario.text.toString() == it.nombreUsuario && contrasenia.text.toString() == it.contrasenia) {
                    usuarioEncontrado = true
                    val intent = Intent(this, Menu::class.java)
                    startActivity(intent)
                }
            }
            if (!usuarioEncontrado){
            Toast.makeText(
                this,
                "Nombre de Usuario o Contraseña Incorrectos Porfavor Intentelo de Nuevo",
                Toast.LENGTH_LONG
            ).show()
        }

        }
    }

}