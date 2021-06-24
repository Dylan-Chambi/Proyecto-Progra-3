package com.example.proyecto_progra_3

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.*
import android.widget.*
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.location.*
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class CentrosMedicosMapActivity : AppCompatActivity(), OnMapReadyCallback {
    lateinit var recyclerViewCentroMedico: RecyclerView
    lateinit var map: GoogleMap
    lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    lateinit var locationRequest: LocationRequest
    lateinit var userLocation: LatLng
    companion object {
        const val REQUEST_CODE_LOCATION = 1010
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_centros_medicos_map)
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        recyclerViewCentroMedico = findViewById(R.id.recyclerViewCM)
        createMapFragment()
        val adapter = CentrosmedicosRecyclerViewAdapter(this, centrosMedicosListNear)
        val layoutManager = LinearLayoutManager(this)
        recyclerViewCentroMedico.adapter = adapter
        recyclerViewCentroMedico.layoutManager = layoutManager
    }
    private fun createMapFragment(){
        val mapFragment = supportFragmentManager.findFragmentById(R.id.mapCentrosMedicos) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        centrosMedicosListNear.forEach{
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
                map.animateCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 16f))
            }, 100
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
        with(locationRequest) {
            locationRequest = LocationRequest()
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
                }, 100
            )
        }
    }

    inner class CentrosmedicosRecyclerViewAdapter(val context: Context, private val list: List<CentroMedico>): RecyclerView.Adapter<OptionsViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OptionsViewHolder {
            val layoutInflater = LayoutInflater.from(context)
            val itemListView = layoutInflater.inflate(R.layout.layout_centros_medicos_list, parent, false)
            return OptionsViewHolder(itemListView)
        }

        override fun onBindViewHolder(holder: OptionsViewHolder, position: Int) {
            holder.bind(list[position])
            holder.itemView.setOnClickListener {
                map.animateCamera(CameraUpdateFactory.newLatLngZoom(list[position].latidud, 16f))
            }
            holder.buttonSeleccionarCM.setOnClickListener {
                Toast.makeText(this@CentrosMedicosMapActivity, "nombre: ${list[position].nombre} latitud: ${list[position].latidud.latitude} longitude: ${list[position].latidud.longitude}", Toast.LENGTH_LONG).show()
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
        override fun getItemCount() = centrosMedicosListNear.size
    }

    class OptionsViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        // variables en pantalla
        val nombreCentroMedico: TextView = itemView.findViewById(R.id.nombreCM)
        val buttonSeleccionarCM: Button = itemView.findViewById(R.id.seleccionarCM)
        fun bind(centroMedico: CentroMedico) {
            // bindear cada opcion en pantalla para cada elemento de la lista
            nombreCentroMedico.text = centroMedico.nombre

        }
    }
}

private val centrosMedicosListNear = listOf(
    CentroMedico("Centro Medico DarSalud",LatLng(-16.504024200101696, -68.13402742379976)),
    CentroMedico("Hospital Obrero",LatLng(-16.499304030713155, -68.11820522924992)),
    CentroMedico("Hospital San Gabriel",LatLng(-16.491417633237607, -68.1165448294589)),
    CentroMedico("Hospital Juan XXII",LatLng(-16.48754617592828, -68.15741547056331)),
    CentroMedico("Hospital Arco Iris",LatLng(-16.484140961667883, -68.1203588000111)),
    CentroMedico("Hospital Boliviano Holandes",LatLng(-16.52239640701212, -68.1536687705633)),
    CentroMedico("Hospital Municipal de Cotahuma", LatLng(-16.515550063703703, -68.1395231411155)),
    CentroMedico("Hospital Metodista",LatLng(-16.527081507360545, -68.10444582945891)),
    CentroMedico("Hospital Del Norte",LatLng(-16.490278176123415, -68.20442080001112)),
    CentroMedico("Hospital Universitario Nuestra Señora de La Paz",LatLng(-16.526615250171098, -68.1281684411155)),
    CentroMedico("Hospital De Clinicas",LatLng(-16.507528699100344, -68.11873685052839)),
    CentroMedico("Clinica Alemana",LatLng(-16.513638592006387, -68.12143527056331)),
    CentroMedico("Clinica Del Sur",LatLng(-16.525696807215358, -68.10872318221992)),
    CentroMedico("Clinica Medica Lausanne",LatLng(-16.52942647892273, -68.11036190001111)),
    CentroMedico("Clinica Rengel",LatLng(-16.514459202414344, -68.12882499002932)),
    CentroMedico("Clinica AMID",LatLng(-16.505894134320183, -68.11913560001112))
)