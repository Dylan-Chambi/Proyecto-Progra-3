package com.example.proyecto_progra_3

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import android.net.Uri
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class AmbulanciasActivity : AppCompatActivity() {

    lateinit var recyclerViewAmbulancias: RecyclerView
    lateinit var map: GoogleMap
    private lateinit var auth: FirebaseAuth
    lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    lateinit var locationRequest: LocationRequest
    private lateinit var drawer: DrawerLayout
    private lateinit var toogle: ActionBarDrawerToggle
    var userLocation: LatLng? = null
    companion object {
        const val REQUEST_CODE_LOCATION = 1010
    }

    override fun onCreate(savedInstanceState: Bundle?) {


//        theme.applyStyle(R.style.AppThemeRed, true)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ambulancias)
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        recyclerViewAmbulancias = findViewById(R.id.recyclerViewCM)


//        createMapFragment()


        val toolbar = findViewById<Toolbar>(R.id.toolBarMC)
        val adapter = AmbulanciasRecyclerViewAdapter(this, ambulanciasListNear)
        val layoutManager = LinearLayoutManager(this)
        val navMenu = findViewById<NavigationView>(R.id.navigationViewMenu)
        recyclerViewAmbulancias.adapter = adapter
        recyclerViewAmbulancias.layoutManager = layoutManager

        drawer = findViewById(R.id.drawerLayoutMenu)
        toogle = ActionBarDrawerToggle(this, drawer, toolbar,R.string.open, R.string.close)
        drawer.addDrawerListener(toogle)
        toogle.syncState()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        navMenu.setNavigationItemSelectedListener {
            when(it.itemId){
                R.id.medicalCentersButton ->{
                    drawer.closeDrawer(GravityCompat.START)
                }
                R.id.pharmacyButton ->{
                    val intentP = Intent(this, FarmaciasActivity::class.java)
                    startActivity(intentP)
                    drawer.closeDrawer(GravityCompat.START)
                    finish()
                }
                R.id.ambulancesButton ->{
                    val intentA = Intent(this, AmbulanciasActivity::class.java)
                    startActivity(intentA)
                    drawer.closeDrawer(GravityCompat.START)
                    finish()
                }
                R.id.guidesButton ->{
                    val intentG = Intent(this, Guias::class.java)
                    startActivity(intentG)
                    drawer.closeDrawer(GravityCompat.START)
                    finish()
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

//    override fun onBackPressed() {
//        if(drawer.isDrawerOpen(GravityCompat.START)){
//            drawer.closeDrawer(GravityCompat.START)
//        }else {
//            super.onBackPressed()
//        }
//    }

    inner class AmbulanciasRecyclerViewAdapter(val context: Context, private val list: List<Ambulancia>): RecyclerView.Adapter<OptionsViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OptionsViewHolder {
            val layoutInflater = LayoutInflater.from(context)
            val itemListView = layoutInflater.inflate(R.layout.layout_centros_medicos_list, parent, false)
            return OptionsViewHolder(itemListView)
        }

        override fun onBindViewHolder(holder: OptionsViewHolder, position: Int) {
            holder.bind(list[position])
            holder.itemView.setOnClickListener {

//  En este bloque hacemos una llamada directamente sin pasar por el marcador, si lo usamos no
//  olviemos usar el permiso en el android manifest:

//                if(ContextCompat.checkSelfPermission(this@AmbulanciasActivity, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED){
//                    val i = Intent(Intent.ACTION_CALL)
//                    i.data = Uri.parse("tel:${list[position].telefono}")
//                    startActivity(i)
//                    Toast.makeText(this@AmbulanciasActivity, "nombre: ${list[position].nombre} número: ${list[position].telefono} ", Toast.LENGTH_LONG).show()
//                }else{
//                    ActivityCompat.requestPermissions(this@AmbulanciasActivity, arrayOf(Manifest.permission.CALL_PHONE), 123)
//                }


//  Este bloque permite hacer la llamada pasando antes por el marcador del telefono
                val diale = Intent(Intent.ACTION_DIAL)
                diale.data = Uri.parse("tel:${list[position].telefono}")
                startActivity(diale)
            }
            holder.buttonSeleccionarCM.setOnClickListener {
                Toast.makeText(this@AmbulanciasActivity, "nombre: ${list[position].nombre} número: ${list[position].telefono} ", Toast.LENGTH_LONG).show()
            }
            /* Para llamar a algun elemento en especifico
            holder.imageButton.setOnClickListener {
                funcionMenuOptionClick?.invoke(list[position])
            }
            */

            // En todo el elemento
//        holder.itemView.setOnClickListener {
//            funcionMenuOptionClick?.invoke(list[position])
//        }
        }

        override fun getItemCount() = ambulanciasListNear.size
    }
    class OptionsViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        // variables en pantalla
        val nombreAmbulancia: TextView = itemView.findViewById(R.id.nombreCM)
        val buttonSeleccionarCM: Button = itemView.findViewById(R.id.seleccionarCM)
        fun bind(ambulancia: Ambulancia) {
            // bindear cada opcion en pantalla para cada elemento de la lista
            nombreAmbulancia.text = ambulancia.nombre

        }
    }

}
private val ambulanciasListNear = listOf(
    Ambulancia("Hospital Obrero", 22245518),
    Ambulancia("Hospital Del Niño", 22441749),
    Ambulancia("Clínica Del Sur", 22784003),
    Ambulancia("S.S.U.", 22434262),
    Ambulancia("Caja Petrolera", 22372160),
    Ambulancia("Hospital Arco Iris", 74901912),
    Ambulancia("PROSALUD", 22184100),
    Ambulancia("Clínica Rengel", 227748888)
)
