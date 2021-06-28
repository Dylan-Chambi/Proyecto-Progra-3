package com.example.proyecto_progra_3
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.example.proyecto_progra_3.databinding.ActivityMenuBinding
import com.google.firebase.auth.FirebaseAuth

class MenuActivity : AppCompatActivity() {

    private lateinit var bindingMenu: ActivityMenuBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var toogle: ActionBarDrawerToggle
    private lateinit var drawer: DrawerLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)

        bindingMenu = ActivityMenuBinding.inflate(layoutInflater)
        setContentView(bindingMenu.root)
        auth = FirebaseAuth.getInstance()

        val toolbar = bindingMenu.toolBarMenu
        val farmaciasButton = bindingMenu.fondoFarmacias
        val centrosMEdicosButton = bindingMenu.fondoCentrosMedicos
        val ambulanciaButton = bindingMenu.fondoAmbulancia
        val guiasButton = bindingMenu.fondoGuias
        val navMenu = bindingMenu.navigationViewMenu

        drawer = bindingMenu.drawerLayoutMenu

        toogle = ActionBarDrawerToggle(this, drawer, toolbar,R.string.open, R.string.close)
        drawer.addDrawerListener(toogle)
        toogle.syncState()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        navMenu.setNavigationItemSelectedListener {
            when(it.itemId){
                R.id.medicalCentersButton ->{
                    val intentMC = Intent(this, CentrosMedicosMapActivity::class.java)
                    startActivity(intentMC)
                    drawer.closeDrawer(GravityCompat.START)
                }
                R.id.pharmacyButton ->{
                    val intentP = Intent(this, FarmaciasActivity::class.java)
                    startActivity(intentP)
                    drawer.closeDrawer(GravityCompat.START)
                }
                R.id.ambulancesButton ->{
                    val intentA = Intent(this, PantallaAmbulancia::class.java)
                    startActivity(intentA)
                    drawer.closeDrawer(GravityCompat.START)
                }
                R.id.guidesButton ->{
                    val intentG = Intent(this, Guias::class.java)
                    startActivity(intentG)
                    drawer.closeDrawer(GravityCompat.START)
                }
                R.id.settingsButton ->{
                    showLongMessage(this,"Click on Settings")
                    /*
                    val intentMC = Intent(this, CentrosMedicosMapActivity::class.java)
                    startActivity(intentMC)
                     */
                }
                R.id.profileButton -> showLongMessage(this,"Click on Profile")
                R.id.logOutButton -> logOut()
            }
            true
        }
        farmaciasButton.setOnClickListener{
            val intent = Intent(this, FarmaciasActivity::class.java)
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
    //para que el boton funcione
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(toogle.onOptionsItemSelected(item)) {
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        if(drawer.isDrawerOpen(GravityCompat.START)){
            drawer.closeDrawer(GravityCompat.START)
        }else {
            super.onBackPressed()
        }
    }
}