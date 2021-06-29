package com.example.proyecto_progra_3

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.*
import android.widget.*
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.location.*
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth

class FarmaciasActivity : AppCompatActivity(), OnMapReadyCallback {
    lateinit var recyclerViewFarmacia: RecyclerView
    lateinit var map: GoogleMap
    private lateinit var auth: FirebaseAuth
    lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    lateinit var locationRequest: LocationRequest
    private lateinit var drawer: DrawerLayout
    private lateinit var toogle: ActionBarDrawerToggle
    private lateinit var searchActivity: ImageButton
    var userLocation: LatLng? = null

    companion object {
        const val REQUEST_CODE_LOCATION = 1010
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        theme.applyStyle(R.style.AppThemeGreen, true)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_farmacias)
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        recyclerViewFarmacia = findViewById(R.id.recyclerViewP)
        createMapFragment()
        val toolbar = findViewById<Toolbar>(R.id.toolbarPharmacy)
        val adapter = FarmaciasRecyclerViewAdapter(this, farmaciasListNear)
        val layoutManager = LinearLayoutManager(this)
        val navMenu = findViewById<NavigationView>(R.id.navigationViewMenu)
        searchActivity = findViewById(R.id.searchActiviy)
        recyclerViewFarmacia.adapter = adapter
        recyclerViewFarmacia.layoutManager = layoutManager

        drawer = findViewById(R.id.drawerLayoutMenu)

        toogle = ActionBarDrawerToggle(this, drawer, toolbar, R.string.open, R.string.close)
        drawer.addDrawerListener(toogle)
        toogle.syncState()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        navMenu.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.medicalCentersButton -> {
                    val intentMC = Intent(this, CentrosMedicosMapActivity::class.java)
                    startActivity(intentMC)
                    drawer.closeDrawer(GravityCompat.START)
                    finish()
                }
                R.id.pharmacyButton -> {
                    drawer.closeDrawer(GravityCompat.START)
                }
                R.id.ambulancesButton -> {
                    val intentA = Intent(this, AmbulanciasActivity::class.java)
                    startActivity(intentA)
                    drawer.closeDrawer(GravityCompat.START)
                    finish()
                }
                R.id.guidesButton -> {
                    val intentG = Intent(this, Guias::class.java)
                    startActivity(intentG)
                    drawer.closeDrawer(GravityCompat.START)
                    finish()
                }
                R.id.settingsButton -> {
                    showLongMessage(this, "Click on Settings")
                    /*
                    val intentMC = Intent(this, CentrosMedicosMapActivity::class.java)
                    startActivity(intentMC)
                     */
                }
                R.id.profileButton -> showLongMessage(this, "Click on Profile")
                R.id.logOutButton -> logOut()
            }
            true
        }
        searchActivity.setOnClickListener {
            val intent = Intent(this, PharmacySearch::class.java)
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

    private fun createMapFragment(){
        val mapFragment = supportFragmentManager.findFragmentById(R.id.mapFarmacia) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        farmaciasListNear.forEach{
            map.addMarker(MarkerOptions().position(it.latidud))
        }
        if(!checkPermission()){
            requestPermissionLocation()
            return
        }
        enableLocation()
        getLastLocation()
        Handler(Looper.getMainLooper()).postDelayed(
            {
                if(userLocation != null && checkPermission()) {
                    map.animateCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 16f))
                }else{
                    showLongMessage(this, "Error getting location.")
                }
            }, 1000
        )

    }

    @SuppressLint("MissingPermission")
    private fun enableLocation(){
        if(!::map.isInitialized) return
        if(isLocationEnabled()){
            map.isMyLocationEnabled = true
        }else{
            requestPermissionLocation()
        }
    }

    @SuppressLint("MissingPermission")
    fun getLastLocation(){
        if(checkPermission()){
            if(isLocationEnabled()){
                fusedLocationProviderClient.lastLocation.addOnCompleteListener {task->
                    val location:Location? = task.result
                    if(location == null){
                        getNewLocation()
                    }else {
                        userLocation = LatLng(location.latitude, location.longitude)
                    }
                }
            }else{
                Toast.makeText(this,"Please Turn on Your device Location",Toast.LENGTH_SHORT).show()
            }
        }else{
            requestPermissionLocation()
        }
    }


    @SuppressLint("MissingPermission")
    fun getNewLocation(){
        locationRequest = LocationRequest()
        with(locationRequest) {
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
            interval = 0
            fastestInterval = 0
            numUpdates = 1
        }
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        fusedLocationProviderClient.requestLocationUpdates(
            locationRequest,locationCallback,Looper.myLooper()
        )
    }


    private val locationCallback = object : LocationCallback(){
        override fun onLocationResult(locationResult: LocationResult) {
            val lastLocation: Location = locationResult.lastLocation
            userLocation = LatLng(lastLocation.latitude, lastLocation.longitude)
        }
    }

    private fun checkPermission() = ActivityCompat.checkSelfPermission(this,android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(this,android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED

    private fun requestPermissionLocation(){
        if(ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION) ||
            ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_COARSE_LOCATION)){
            Toast.makeText(this, "active permissions on settings", Toast.LENGTH_SHORT).show()
        }else{
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION),
                REQUEST_CODE_LOCATION)
        }
    }

    private fun isLocationEnabled():Boolean{
        val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    }


    @SuppressLint("MissingSuperCall", "MissingPermission")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if(requestCode == REQUEST_CODE_LOCATION){
            if(grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                map.isMyLocationEnabled = true
                map.animateCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 16f))
            }
        }
    }

    @SuppressLint("MissingPermission")
    override fun onResumeFragments() {
        super.onResumeFragments()
        if(!::map.isInitialized)return
        if(!checkPermission()){
            map.isMyLocationEnabled = false
            Toast.makeText(this, "active permissions on settings to do something", Toast.LENGTH_SHORT).show()
        }else{
            enableLocation()
            getLastLocation()
            Handler(Looper.getMainLooper()).postDelayed(
                {
                    map.animateCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 16f))
                }, 1000
            )
        }
    }

    inner class FarmaciasRecyclerViewAdapter(val context: Context, private val list: List<Localizacion>): RecyclerView.Adapter<OptionsViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OptionsViewHolder {
            val layoutInflater = LayoutInflater.from(context)
            val itemListView = layoutInflater.inflate(R.layout.layout_farmacias_list, parent, false)
            return OptionsViewHolder(itemListView)
        }

        override fun onBindViewHolder(holder: OptionsViewHolder, position: Int) {
            holder.bind(list[position])
            holder.itemView.setOnClickListener {
                map.animateCamera(CameraUpdateFactory.newLatLngZoom(list[position].latidud, 16f))
            }
            holder.buttonSeleccionarCM.setOnClickListener {
                Toast.makeText(this@FarmaciasActivity, "nombre: ${list[position].nombre} latitud: ${list[position].latidud.latitude} longitude: ${list[position].latidud.longitude}", Toast.LENGTH_LONG).show()
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
        override fun getItemCount() = farmaciasListNear.size
    }

    class OptionsViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        // variables en pantalla
        val nombreFarmacia: TextView = itemView.findViewById(R.id.pharmacyName)
        val buttonSeleccionarCM: Button = itemView.findViewById(R.id.selectPharmacy)
        fun bind(Localizacion: Localizacion) {
            // bindear cada opcion en pantalla para cada elemento de la lista
            nombreFarmacia.text = Localizacion.nombre

        }
    }
}

private val farmaciasListNear = listOf(
    Localizacion("Farmacias Bolivia Centro",LatLng(-16.500762838991538, -68.13311031737985)),
    Localizacion("Farmacorp Prado",LatLng(-16.50344164456266, -68.1317027049167)),
    Localizacion("Farmacias Chavez",LatLng(-16.499188355286304, -68.1333999130788)),
    Localizacion("Farmacia Gloria",LatLng(-16.538828245112256, -68.08354062485503)),
    Localizacion("Farmacia JuÃ¡rez",LatLng(-16.505190691346147, -68.12075132945577)),
    Localizacion("Farmacias Bolivia Obrajes",LatLng(-16.53003930317316, -68.1004140390046)),
    Localizacion("Todo Oxigeno",LatLng(-16.516427863082022, -68.142772549087)),
    Localizacion("Oximed",LatLng(-16.503544591705797, -68.13773462332128)),
    Localizacion("HP Medical SRL",LatLng(-16.51191758051096, -68.12878481503125)),
    Localizacion("Farmacia Camacho",LatLng(-16.498817578255, -68.13422862991723)),
    Localizacion("Farmacorp Avaroa",LatLng(-16.51128969182278, -68.12667967056024)),
    Localizacion("Farmacia MediLucy",LatLng(-16.501249260317607, -68.10531755553932)),
    Localizacion("Marca Medical",LatLng(-16.504552594230894, -68.12062149248311)),
    Localizacion("Latin Med Bolivia",LatLng(-16.498876952871754, -68.12096686178442)),
    Localizacion("Medicat SRL",LatLng(-16.541632100797813, -68.08997172135292)),
    Localizacion("Disamed SRL",LatLng(-16.510872063400225, -68.12360351166465))
)