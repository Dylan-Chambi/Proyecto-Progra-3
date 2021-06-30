package com.example.proyecto_progra_3

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.proyecto_progra_3.YouTubeResponse.Example
import com.example.proyecto_progra_3.YouTubeResponse.Items
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class Guias : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var drawer: DrawerLayout
    private lateinit var toogle: ActionBarDrawerToggle
    lateinit var recyclerViewGuias: RecyclerView
    private lateinit var alertDialogMenu: AlertDialog
    private lateinit var alertDialog: AlertDialog
    private lateinit var databaseReference: DatabaseReference
    private lateinit var database: FirebaseDatabase




    override fun onCreate(savedInstanceState: Bundle?) {
        theme.applyStyle(R.style.AppThemeBrown, true)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_guias)
        drawer = findViewById(R.id.drawerLayoutMenu)
        alertDialog = AlertDialog.Builder(this).apply {
            setTitle("Cargando")
            setMessage("Obteniendo datos de la nube...")
            setCancelable(false)
        }.create()
        alertDialog.show()
        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()
        databaseReference = database.reference.child("Users")
        getDatabaseData()
        cargarVideos()

        val toolbar = findViewById<Toolbar>(R.id.toolbarGuides)
        val navMenu = findViewById<NavigationView>(R.id.navigationViewMenu)

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
                    val intentA = Intent(this, AmbulanciasActivity::class.java)
                    startActivity(intentA)
                    drawer.closeDrawer(GravityCompat.START)
                    finish()
                }
                R.id.guidesButton ->{
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

    private fun cargarVideos(){
        val youtube = APIClient.getClientYoutube()?.obtenerVideos("snippet", "guias para el covid", 30, "AIzaSyAprtyzLqPPeIJprReQb4vkin1oAoS6vl8")?.enqueue(object:Callback<Example>{
            override fun onResponse(call: Call<Example>, response: Response<Example>) {
//                Toast.makeText(this@Guias, response.body().toString(), Toast.LENGTH_SHORT).show()
                val answer = response.body() as Example
                if(answer.items.isNotEmpty()) {
                    recyclerViewGuias = findViewById(R.id.recyclerViewGuias)
                    val adapter = guiasRecyclerViewAdapter(this@Guias, answer.items)
                    val layoutManager = LinearLayoutManager(this@Guias)
                    recyclerViewGuias.adapter = adapter
                    recyclerViewGuias.layoutManager = layoutManager
                }else{
                    showLongMessage(this@Guias, "No se encontraron videos")
                }
                alertDialog.dismiss()
            }

            override fun onFailure(call: Call<Example>, t: Throwable) {
                Toast.makeText(this@Guias, "ERROR!", Toast.LENGTH_SHORT).show()
                alertDialog.dismiss()
            }

        })
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

    inner class guiasRecyclerViewAdapter(val context: Context, private val list: List<Items>): RecyclerView.Adapter<OptionsViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OptionsViewHolder {
            val layoutInflater = LayoutInflater.from(context)
            val itemListView = layoutInflater.inflate(R.layout.layout_guias_list, parent, false)
            return OptionsViewHolder(itemListView)
        }

        override fun onBindViewHolder(holder: OptionsViewHolder, position: Int) {
            holder.bind(context, list[position])
            holder.backgroundItem.setOnClickListener {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/watch?v=" + list[position].id.videoId))
                startActivity(intent)
            }
//        }
        }
        override fun getItemCount() = list.size
    }

    class OptionsViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        // variables en pantalla
        val miniaturaGuia: ImageView = itemView.findViewById(R.id.miniatura)
        val backgroundItem: ImageView = itemView.findViewById(R.id.backgroundItem)
        val tituloGuia: TextView = itemView.findViewById(R.id.tituloYoutube)
        val descripcionGuia: TextView = itemView.findViewById(R.id.descripcionYoutube)
        fun bind(context: Context, item: Items) {
            // bindear cada opcion en pantalla para cada elemento de la lista
            Glide.with(context)
                    .load(item.snippet.thumbnails.medium.url)
                    .into(miniaturaGuia)
            tituloGuia.text=item.snippet.title
            descripcionGuia.text=item.snippet.description
        }
    }
}