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
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.gms.maps.model.LatLng
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth


class Guias : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var drawer: DrawerLayout
    private lateinit var toogle: ActionBarDrawerToggle
    lateinit var recyclerViewCentroMedico: RecyclerView



    override fun onCreate(savedInstanceState: Bundle?) {
        theme.applyStyle(R.style.AppThemeBrown, true)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_guias)
        drawer = findViewById(R.id.drawerLayoutMenu)

        recyclerViewCentroMedico = findViewById(R.id.recyclerViewGuias)
        val toolbar = findViewById<Toolbar>(R.id.toolbarGuides)
        val adapter = guiasRecyclerViewAdapter(this, guiasList)
        val layoutManager = LinearLayoutManager(this)
        val navMenu = findViewById<NavigationView>(R.id.navigationViewMenu)

        recyclerViewCentroMedico.adapter = adapter
        recyclerViewCentroMedico.layoutManager = layoutManager
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

    override fun onBackPressed() {
        if(drawer.isDrawerOpen(GravityCompat.START)){
            drawer.closeDrawer(GravityCompat.START)
        }else {
            super.onBackPressed()
        }
    }

    inner class guiasRecyclerViewAdapter(val context: Context, private val list: List<Guia>): RecyclerView.Adapter<OptionsViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OptionsViewHolder {
            val layoutInflater = LayoutInflater.from(context)
            val itemListView = layoutInflater.inflate(R.layout.layout_guias_list, parent, false)
            return OptionsViewHolder(itemListView)
        }

        override fun onBindViewHolder(holder: OptionsViewHolder, position: Int) {
            holder.bind(context, list[position])
            holder.backgroundItem.setOnClickListener {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(list[position].url))
                startActivity(intent)
            }
//        }
        }
        override fun getItemCount() = guiasList.size
    }

    class OptionsViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        // variables en pantalla
        val miniaturaGuia: ImageView = itemView.findViewById(R.id.miniatura)
        val backgroundItem: ImageView = itemView.findViewById(R.id.backgroundItem)
        val tituloGuia: TextView = itemView.findViewById(R.id.tituloYoutube)
        val descripcionGuia: TextView = itemView.findViewById(R.id.descripcionYoutube)
        fun bind(context: Context, guia: Guia) {
            // bindear cada opcion en pantalla para cada elemento de la lista
            Glide.with(context)
                    .load(guia.imagen)
                    .into(miniaturaGuia)
            tituloGuia.text=guia.titulo
            descripcionGuia.text=guia.descripcion
        }
    }
}

private val guiasList = listOf(
    Guia("https://i.ytimg.com/vi/qR9s7oGMuNE/default.jpg", "Cuidados del paciente " +
            "con CoVID-19 en el hogar", "Me diagnosticaron COVID-19 y me dijeron que " +
            "me debía quedar en casa: Si a usted o a un miembro de su familia le diagnosticaron " +
            "COVID-19 y no ha ...", "https://www.youtube.com/watch?v=yIVspK_seNE"),
    Guia("https://i.ytimg.com/vi/qR9s7oGMuNE/default.jpg", "Cuidados del paciente " +
            "con CoVID-19 en el hogar", "Me diagnosticaron COVID-19 y me dijeron que " +
            "me debía quedar en casa: Si a usted o a un miembro de su familia le diagnosticaron " +
            "COVID-19 y no ha ...", "https://www.youtube.com/watch?v=yIVspK_seNE"),
    Guia("https://i.ytimg.com/vi/qR9s7oGMuNE/default.jpg", "Cuidados del paciente " +
            "con CoVID-19 en el hogar", "Me diagnosticaron COVID-19 y me dijeron que " +
            "me debía quedar en casa: Si a usted o a un miembro de su familia le diagnosticaron " +
            "COVID-19 y no ha ...", "https://www.youtube.com/watch?v=yIVspK_seNE"),
    Guia("https://i.ytimg.com/vi/qR9s7oGMuNE/default.jpg", "Cuidados del paciente " +
            "con CoVID-19 en el hogar", "Me diagnosticaron COVID-19 y me dijeron que " +
            "me debía quedar en casa: Si a usted o a un miembro de su familia le diagnosticaron " +
            "COVID-19 y no ha ...", "https://www.youtube.com/watch?v=yIVspK_seNE"),
    Guia("https://i.ytimg.com/vi/qR9s7oGMuNE/default.jpg", "Cuidados del paciente " +
            "con CoVID-19 en el hogar", "Me diagnosticaron COVID-19 y me dijeron que " +
            "me debía quedar en casa: Si a usted o a un miembro de su familia le diagnosticaron " +
            "COVID-19 y no ha ...", "https://www.youtube.com/watch?v=yIVspK_seNE"),
    Guia("https://i.ytimg.com/vi/qR9s7oGMuNE/default.jpg", "Cuidados del paciente " +
            "con CoVID-19 en el hogar", "Me diagnosticaron COVID-19 y me dijeron que " +
            "me debía quedar en casa: Si a usted o a un miembro de su familia le diagnosticaron " +
            "COVID-19 y no ha ...", "https://www.youtube.com/watch?v=yIVspK_seNE"),
    Guia("https://i.ytimg.com/vi/qR9s7oGMuNE/default.jpg", "Cuidados del paciente " +
            "con CoVID-19 en el hogar", "Me diagnosticaron COVID-19 y me dijeron que " +
            "me debía quedar en casa: Si a usted o a un miembro de su familia le diagnosticaron " +
            "COVID-19 y no ha ...", "https://www.youtube.com/watch?v=yIVspK_seNE"),
    Guia("https://i.ytimg.com/vi/qR9s7oGMuNE/default.jpg", "Cuidados del paciente " +
            "con CoVID-19 en el hogar", "Me diagnosticaron COVID-19 y me dijeron que " +
            "me debía quedar en casa: Si a usted o a un miembro de su familia le diagnosticaron " +
            "COVID-19 y no ha ...", "https://www.youtube.com/watch?v=yIVspK_seNE"),
    Guia("https://i.ytimg.com/vi/qR9s7oGMuNE/default.jpg", "Cuidados del paciente " +
            "con CoVID-19 en el hogar", "Me diagnosticaron COVID-19 y me dijeron que " +
            "me debía quedar en casa: Si a usted o a un miembro de su familia le diagnosticaron " +
            "COVID-19 y no ha ...", "https://www.youtube.com/watch?v=yIVspK_seNE"),
    Guia("https://i.ytimg.com/vi/qR9s7oGMuNE/default.jpg", "Cuidados del paciente " +
            "con CoVID-19 en el hogar", "Me diagnosticaron COVID-19 y me dijeron que " +
            "me debía quedar en casa: Si a usted o a un miembro de su familia le diagnosticaron " +
            "COVID-19 y no ha ...", "https://www.youtube.com/watch?v=yIVspK_seNE"),
    Guia("https://i.ytimg.com/vi/qR9s7oGMuNE/default.jpg", "Cuidados del paciente " +
            "con CoVID-19 en el hogar", "Me diagnosticaron COVID-19 y me dijeron que " +
            "me debía quedar en casa: Si a usted o a un miembro de su familia le diagnosticaron " +
            "COVID-19 y no ha ...", "https://www.youtube.com/watch?v=yIVspK_seNE"),
    Guia("https://i.ytimg.com/vi/qR9s7oGMuNE/default.jpg", "Cuidados del paciente " +
            "con CoVID-19 en el hogar", "Me diagnosticaron COVID-19 y me dijeron que " +
            "me debía quedar en casa: Si a usted o a un miembro de su familia le diagnosticaron " +
            "COVID-19 y no ha ...", "https://www.youtube.com/watch?v=yIVspK_seNE")

)