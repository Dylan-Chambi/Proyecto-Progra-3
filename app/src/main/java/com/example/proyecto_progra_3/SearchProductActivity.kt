package com.example.proyecto_progra_3

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import java.util.*

class SearchProductActivity : AppCompatActivity() {
    @ExperimentalStdlibApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_product)
        val textPatron = findViewById<EditText>(R.id.editTextPatron)
        val searchButton = findViewById<Button>(R.id.searchButton)
        val searchAbility = findViewById<ImageView>(R.id.searchAbility)


        val listaProductosTemp = listOf("Paracetamol", "Ibuprofeno", "Pastillas", "Vendas")

        searchButton.setOnClickListener {
            Toast.makeText(this, "Buscando...", Toast.LENGTH_LONG)
            val listaProductosEncontrados = mutableListOf<String>()
            listaProductosTemp.forEach { texto ->
                val patron = textPatron.text.toString()
                if (patron.isNotEmpty() && isOnTextKMP(patron, texto)) {
                    //cambiamos visibilidad de la farmacia a visible
                    listaProductosEncontrados.add(texto)
                } else {
                    //cambiamos la visibilidad del producto a gone
                }
            }
            //Prueba para ver si funciona el kmp
            if(listaProductosEncontrados.isNotEmpty()) {
                Toast.makeText(this, "Se encontraron los siguientes productos: $listaProductosEncontrados", Toast.LENGTH_LONG).show()
            }else{
                Toast.makeText(this, "No se encontro un producto con ese nombre", Toast.LENGTH_LONG).show()
            }

        }
        searchAbility.setOnClickListener{
            searchButton.visibility = View.VISIBLE
            textPatron.visibility = View.VISIBLE
        }
    }

    @ExperimentalStdlibApi
    fun isOnTextKMP(patron: String, texto: String): Boolean{
        val patronLength = patron.length
        val textoLength = texto.length
        val vectorTexto = IntArray(10000){0}

        //KMP Process
        var indice = 0
        var indicePatron = -1
        vectorTexto[0] = -1
        while(indice < patronLength){
            while(indicePatron >= 0 && patron[indice].lowercase() != patron[indicePatron].lowercase().stripAccents()){
                indicePatron = vectorTexto[indicePatron]
            }
            indice++
            indicePatron++
            vectorTexto[indice] = indicePatron
        }
        //KMP Search
        var freq = 0
        indice = 0
        indicePatron = 0
        while(indice < textoLength){
            while(indicePatron >= 0 && texto[indice].lowercase() != patron[indicePatron].lowercase().stripAccents()){
                indicePatron = vectorTexto[indicePatron]
            }
            indice++
            indicePatron++
            if(indicePatron == patronLength){
                ++freq
                indicePatron = vectorTexto[indicePatron]
                return true
            }
        }
        return false
    }

}