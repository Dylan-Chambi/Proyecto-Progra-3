package com.example.proyecto_progra_3
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView

class Menu : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)

        val farmaciasButton = findViewById<ImageView>(R.id.farmacias)
        val centrosMEdicosButton = findViewById<ImageView>(R.id.cruzRoja)
        val ambulanciaButton = findViewById<ImageView>(R.id.ambulancia)
        val guiasButton = findViewById<ImageView>(R.id.libro)

        farmaciasButton.setOnClickListener{
            val intent = Intent(this, SearchProductActivity::class.java)
            startActivity(intent)
        }
        centrosMEdicosButton.setOnClickListener{
            val intent = Intent(this, CentrosMedicosActivity::class.java)
            startActivity(intent)
        }
        ambulanciaButton.setOnClickListener {
            val intent = Intent(this, PantallaAmbulancia::class.java)
            startActivity(intent)
        }
        guiasButton.setOnClickListener {
            val intent = Intent(this, Guias::class.java)
            startActivity(intent)
        }

    }
}