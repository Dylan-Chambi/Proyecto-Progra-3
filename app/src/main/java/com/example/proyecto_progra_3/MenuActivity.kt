package com.example.proyecto_progra_3
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.example.proyecto_progra_3.databinding.ActivityMenuBinding
import com.example.proyecto_progra_3.databinding.LayoutNavigationMenuBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase

class MenuActivity : AppCompatActivity() {

    private lateinit var bindingMenu: ActivityMenuBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var toogle: ActionBarDrawerToggle
    private lateinit var drawer: DrawerLayout
    private lateinit var alertDialogMenu: AlertDialog
    private lateinit var database: FirebaseDatabase
    private lateinit var databaseReference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        theme.applyStyle(R.style.AppTheme, true)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)
        bindingMenu = ActivityMenuBinding.inflate(layoutInflater)
        setContentView(bindingMenu.root)
        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()
        databaseReference = database.reference.child("Users")
        getDatabaseData()
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
                    val intentA = Intent(this, AmbulanciasActivity::class.java)
                    startActivity(intentA)
                    drawer.closeDrawer(GravityCompat.START)
                }
                R.id.guidesButton ->{
                    val intentG = Intent(this, Guias::class.java)
                    startActivity(intentG)
                    drawer.closeDrawer(GravityCompat.START)
                }
                R.id.settingsButton ->{
                    showLongMessage(this,"Click en Ajustes")
                    /*
                    val intentMC = Intent(this, CentrosMedicosMapActivity::class.java)
                    startActivity(intentMC)
                     */
                }
                R.id.profileButton -> showLongMessage(this,"Click en Perfil")
                R.id.logOutButton -> {
                    alertDialogMenu = AlertDialog.Builder(this).apply {
                        setTitle("Cerrando Sesion...")
                        setMessage("¿Seguro que quieres cerrar sesion?")
                        setPositiveButton("SI") { _, _ ->
                            logOut()
                        }
                        setCancelable(false)
                        setNegativeButton("NO") { _, _ -> }
                    }.create()
                    alertDialogMenu.show()
                }
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
            val intent = Intent(this, AmbulanciasActivity::class.java)
            startActivity(intent)
        }
        guiasButton.setOnClickListener {
//            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/watch?v=6hLnEUN5UXw"))
            val intent = Intent(this, Guias::class.java)
            startActivity(intent)
        }

    }
    private fun getDatabaseData(){
        val user = auth.currentUser!!.uid
        databaseReference = FirebaseDatabase.getInstance().getReference("Users")
        databaseReference.child(user).get().addOnSuccessListener {
            findViewById<TextView>(R.id.userNameFirebase).text = it.child("userName").value.toString()
            findViewById<TextView>(R.id.userEmailFirebase).text = it.child("userEmail").value.toString()
        }
    }

    private fun logOut() {
        auth.signOut()
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        showLongMessage(this, "Salio de su cuenta satisfactoriamente")
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