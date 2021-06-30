package com.example.proyecto_progra_3

import android.content.Context
import android.content.Intent
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
import android.widget.ImageButton
import androidx.appcompat.app.AlertDialog
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class AmbulanciasActivity : AppCompatActivity() {

    private lateinit var recyclerViewAmbulancias: RecyclerView
    private lateinit var auth: FirebaseAuth
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var drawer: DrawerLayout
    private lateinit var toogle: ActionBarDrawerToggle
    private lateinit var alertDialogMenu: AlertDialog
    private lateinit var databaseReference: DatabaseReference
    private  lateinit var database: FirebaseDatabase

    override fun onCreate(savedInstanceState: Bundle?) {

        theme.applyStyle(R.style.AppThemeSkyBlue, true)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ambulancias)
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        recyclerViewAmbulancias = findViewById(R.id.recyclerViewP)


        val toolbar = findViewById<Toolbar>(R.id.toolBarMC)
        val adapter = AmbulanciasRecyclerViewAdapter(this, ambulanciasListNear)
        val layoutManager = LinearLayoutManager(this)
        val navMenu = findViewById<NavigationView>(R.id.navigationViewMenu)
        recyclerViewAmbulancias.adapter = adapter
        recyclerViewAmbulancias.layoutManager = layoutManager
        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()
        databaseReference = database.reference.child("Users")
        getDatabaseData()

        drawer = findViewById(R.id.drawerLayoutMenu)
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
                    finish()
                }
                R.id.pharmacyButton ->{
                    val intentP = Intent(this, FarmaciasActivity::class.java)
                    startActivity(intentP)
                    drawer.closeDrawer(GravityCompat.START)
                    finish()
                }
                R.id.ambulancesButton ->{
                    drawer.closeDrawer(GravityCompat.START)
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
            val itemListView = layoutInflater.inflate(R.layout.layout_ambulance_phone_recycler_view, parent, false)
            return OptionsViewHolder(itemListView)
        }

        override fun onBindViewHolder(holder: OptionsViewHolder, position: Int) {
            holder.bind(list[position])

            holder.itemView.setOnClickListener {
                Toast.makeText(this@AmbulanciasActivity, "nombre: ${list[position].nombre} número: ${list[position].telefono} ", Toast.LENGTH_LONG).show()
            }
            holder.makeCallButton.setOnClickListener {

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
        val ambulanceName: TextView = itemView.findViewById(R.id.ambulanceName)
        val ambulancePhone: TextView = itemView.findViewById(R.id.ambulancePhone)
        val makeCallButton: ImageButton = itemView.findViewById(R.id.callButton)
        fun bind(ambulancia: Ambulancia) {
            // bindear cada opcion en pantalla para cada elemento de la lista
            ambulanceName.text = ambulancia.nombre
            ambulancePhone.text = ambulancia.telefono.toString()

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
