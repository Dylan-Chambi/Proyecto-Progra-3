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
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.proyecto_progra_3.googleMapsPlacesResponse.PlacesResults
import com.example.proyecto_progra_3.googleMapsPlacesResponse.Result
import com.google.android.gms.location.*
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FarmaciasActivity : AppCompatActivity(), OnMapReadyCallback {
    lateinit var recyclerViewFarmacia: RecyclerView
    lateinit var map: GoogleMap
    private lateinit var auth: FirebaseAuth
    lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    lateinit var locationRequest: LocationRequest
    private lateinit var drawer: DrawerLayout
    private lateinit var toogle: ActionBarDrawerToggle
    private lateinit var searchActivity: ImageButton
    private lateinit var alertDialog: AlertDialog
    private lateinit var alertDialogMenu: AlertDialog
    private lateinit var databaseReference: DatabaseReference
    private lateinit var database: FirebaseDatabase


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

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()
        databaseReference = database.reference.child("Users")
        getDatabaseData()
        createMapFragment()

        val toolbar = findViewById<Toolbar>(R.id.toolbarPharmacy)
        val navMenu = findViewById<NavigationView>(R.id.navigationViewMenu)

        searchActivity = findViewById(R.id.searchActiviy)

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
        searchActivity.setOnClickListener {
            val intent = Intent(this, PharmacySearch::class.java)
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

    private fun onLocationChanged(location: LatLng) {
        val key = getText(R.string.google_maps_key).toString()
        val currentLocation = location.latitude.toString() + "," + location.longitude
        val type = "pharmacy"
        val googleMapAPI =
            APIClient.getClientMedicalCenters()?.getNearBy(currentLocation, 150000, type, "", key)
                ?.enqueue(object : Callback<PlacesResults> {
                    override fun onResponse(call: Call<PlacesResults>, response: Response<PlacesResults>) {

                        val answer: List<Result> = response.body()?.getResults()!!
                        if(answer.isNotEmpty()) {
                            answer.forEach{
                                map.addMarker(MarkerOptions().position(it.geometry!!.location!!.getLatLng()))
                            }
                            val adapter = FarmaciasRecyclerViewAdapter(
                                this@FarmaciasActivity,
                                answer
                            )
                            val layoutManager = LinearLayoutManager(this@FarmaciasActivity)
                            recyclerViewFarmacia.adapter = adapter
                            recyclerViewFarmacia.layoutManager = layoutManager
                            answer.forEach {
                                map.addMarker(MarkerOptions().position(it.geometry!!.location!!.getLatLng()))
                            }
                        }else{
                            showLongMessage(this@FarmaciasActivity, "No medical centers were found nearby")
                        }
                        alertDialog.dismiss()
                    }
                    override fun onFailure(call: Call<PlacesResults?>?, t: Throwable) {
                        Toast.makeText(applicationContext, t.message, Toast.LENGTH_LONG).show()
                        alertDialog.dismiss()
                    }
                })
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
        if(!checkPermission()){
            requestPermissionLocation()
            return
        }
        enableLocation()
        getLastLocation()
        if(!checkPermission()) {
            requestPermissionLocation()
            return
        }else {
            try {
                alertDialog = AlertDialog.Builder(this).apply {
                    setTitle("Cargando")
                    setMessage("Obteniendo datos de la nube...")
                    setCancelable(false)
                }.create()
                alertDialog.show()
                Handler(Looper.getMainLooper()).postDelayed(
                    {
                        if (userLocation != null && checkPermission()) {
                            onLocationChanged(userLocation!!)
                            map.animateCamera(
                                CameraUpdateFactory.newLatLngZoom(
                                    userLocation!!,
                                    16f
                                )
                            )
                        } else {
                            showLongMessage(this, "Error getting location.")
                        }
                    }, 1000
                )
            }catch (error: NullPointerException){
                showLongMessage(this, "Error getting location.")
            }
        }

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
                if(userLocation != null && checkPermission()) {
                    map.animateCamera(CameraUpdateFactory.newLatLngZoom(userLocation!!, 16f))
                }
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

    inner class FarmaciasRecyclerViewAdapter(val context: Context, private val list: List<Result>): RecyclerView.Adapter<OptionsViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OptionsViewHolder {
            val layoutInflater = LayoutInflater.from(context)
            val itemListView = layoutInflater.inflate(R.layout.layout_farmacias_list, parent, false)
            return OptionsViewHolder(itemListView)
        }

        override fun onBindViewHolder(holder: OptionsViewHolder, position: Int) {
            holder.bind(list[position])
            holder.itemView.setOnClickListener {
                map.animateCamera(CameraUpdateFactory.newLatLngZoom(list[position].geometry!!.location!!.getLatLng(), 16f))
            }
            holder.buttonSeleccionarCM.setOnClickListener {

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
        override fun getItemCount() = list.size
    }

    class OptionsViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        // variables en pantalla
        val nombreFarmacia: TextView = itemView.findViewById(R.id.nombre)
        val buttonSeleccionarCM: Button = itemView.findViewById(R.id.selectPharmacy)
        fun bind(result: Result) {
            // bindear cada opcion en pantalla para cada elemento de la lista
            nombreFarmacia.text = result.name

        }
    }
}