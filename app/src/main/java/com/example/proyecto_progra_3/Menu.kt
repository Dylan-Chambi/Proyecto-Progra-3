package com.example.proyecto_progra_3
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import com.example.proyecto_progra_3.databinding.ActivityLoginScreenBinding
import com.example.proyecto_progra_3.databinding.ActivityMenuBinding
import com.google.firebase.auth.FirebaseAuth

class Menu : AppCompatActivity() {

    private lateinit var bindingMenu: ActivityMenuBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)

        bindingMenu = ActivityMenuBinding.inflate(layoutInflater)
        setContentView(bindingMenu.root)
        auth = FirebaseAuth.getInstance()

        val farmaciasButton = findViewById<ImageView>(R.id.farmacias)
        val centrosMEdicosButton = findViewById<ImageView>(R.id.cruzRoja)
        val ambulanciaButton = findViewById<ImageView>(R.id.ambulancia)
        val guiasButton = findViewById<ImageView>(R.id.libro)

        bindingMenu.logOutButton.setOnClickListener {
            logOut()
        }
        farmaciasButton.setOnClickListener{
            val intent = Intent(this, SearchProductActivity::class.java)
            startActivity(intent)
        }
        centrosMEdicosButton.setOnClickListener{
            val intent = Intent(this, CentrosMedicosMapActivity::class.java)
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
    private fun logOut(){
        auth.signOut()
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        showLongMessage(this, "Log Out successfully")
        finish()
    }
}