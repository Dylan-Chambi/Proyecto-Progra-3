package com.example.proyecto_progra_3

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class CentrosMedicosMapActivity : AppCompatActivity(), OnMapReadyCallback {
    lateinit var recyclerViewCentroMedico: RecyclerView
    lateinit var map: GoogleMap
    lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    companion object {
        const val REQUEST_CODE_LOCATION = 0
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_centros_medicos_map)
        recyclerViewCentroMedico = findViewById(R.id.recyclerViewCM)
        createMapFragment()
        val adapter = CentrosmedicosRecyclerViewAdapter(this, centrosMedicosListNear)
        val layoutManager = LinearLayoutManager(this)
        recyclerViewCentroMedico.adapter = adapter
        recyclerViewCentroMedico.layoutManager = layoutManager

    }
    fun createMapFragment(){
        val mapFragment = supportFragmentManager.findFragmentById(R.id.mapCentrosMedicos) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        centrosMedicosListNear.forEach{
            map.addMarker(MarkerOptions().position(it.latidud))
        }
        enableLocation()
        /* Intento de obtenmer la ubicacion y hacer zoom
        val ubicacionUsuario = getLocation()
        if(ubicacionUsuario != null) {
            map.animateCamera(CameraUpdateFactory.newLatLngZoom(ubicacionUsuario, 10f))
        }
        */
    }
    /** Funcion que deberia dar la ubicacion
    private fun getLocation(): LatLng?{
        var ubicacion: LatLng? = null
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) return ubicacion
        fusedLocationProviderClient.lastLocation.addOnSuccessListener {
            if(it == null){
                Toast.makeText(this, "Sorry can't get location", Toast.LENGTH_SHORT).show()
            }else it.apply {
                val latitude =  it.latitude
                val longitude = it.longitude
                ubicacion = LatLng(latitude, longitude)
                Toast.makeText(this@CentrosMedicosMapActivity, "latitude: $latitude, longitude: $longitude", Toast.LENGTH_SHORT).show()
            }
        }
        return ubicacion
    }
    */

    fun isLocationPermissionGranted() = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED


    @SuppressLint("MissingPermission")
    private fun enableLocation(){
        if(!::map.isInitialized) return
        if(isLocationPermissionGranted()){
            map.isMyLocationEnabled = true
        }else{
            requestPermissionLocation()
        }
    }


    fun requestPermissionLocation(){
        if(ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)){
            Toast.makeText(this, "active permissions on settings", Toast.LENGTH_SHORT).show()
        }else{
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                REQUEST_CODE_LOCATION)
        }
    }

    @SuppressLint("MissingPermission", "MissingSuperCall")
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray)   {
        when(REQUEST_CODE_LOCATION){ REQUEST_CODE_LOCATION -> if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            map.isMyLocationEnabled = true
        }else{
                    Toast.makeText(this, "active permissions on settings to do something", Toast.LENGTH_SHORT).show()
        }
            else -> {}
        }
    }

    @SuppressLint("MissingPermission")
    override fun onResumeFragments() {
        super.onResumeFragments()
        if(!::map.isInitialized)return
        if(!isLocationPermissionGranted()){
            map.isMyLocationEnabled = false
            Toast.makeText(this, "active permissions on settings to do something", Toast.LENGTH_SHORT).show()
        }
    }

    inner class CentrosmedicosRecyclerViewAdapter(val context: Context, val list: List<CentroMedico>): RecyclerView.Adapter<OptionsViewHolder>() {

        var funcionMenuOptionClick: ((centroMedico: CentroMedico) -> Unit)? = null

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