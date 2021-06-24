package com.example.proyecto_progra_3

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.*
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class CentrosMedicosActivity: AppCompatActivity(){
    lateinit var recyclerView: RecyclerView
    lateinit var mapAdapter: RecyclerView.Adapter<MapAdapter.ViewHolder>
    lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    private val recycleListener = RecyclerView.RecyclerListener { holder ->
        val mapHolder = holder as MapAdapter.ViewHolder
        mapHolder.clearView()
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        setContentView(R.layout.activity_centros_medicos)
        mapAdapter = MapAdapter()
        recyclerView = findViewById<RecyclerView>(R.id.recyclerViewCentrosMedicos).apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(this@CentrosMedicosActivity)
            adapter = mapAdapter
            setRecyclerListener(recycleListener)
        }
    }

    private fun getLocation(): LatLng?{
        var ubicacion: LatLng? = null
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) return ubicacion
        fusedLocationProviderClient.lastLocation?.addOnSuccessListener {
            if(it == null){
                Toast.makeText(this, "Sorry can't get location", Toast.LENGTH_SHORT).show()
            }else it.apply {
                val latitude =  it.latitude
                val longitude = it.longitude
                ubicacion = LatLng(latitude, longitude)
                Toast.makeText(this@CentrosMedicosActivity, "latitude: $latitude, longitude: $longitude", Toast.LENGTH_SHORT).show()
            }
        }
        return ubicacion
    }

    @SuppressLint("MissingSuperCall")
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if(requestCode == 1){
            if(grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show()
                    getLocation()
                } else {
                    Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    inner class MapAdapter : RecyclerView.Adapter<CentrosMedicosActivity.MapAdapter.ViewHolder>() {

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.bindView(position)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val inflated = LayoutInflater.from(parent.context).inflate(R.layout.layout_recycler_view_centros_medicos, parent, false)
            return ViewHolder(inflated)
        }

        override fun getItemCount() = centrosMedicosList.size

        /** ViewHolder para el mapa y su nombre */
        inner class ViewHolder(view: View): RecyclerView.ViewHolder(view), OnMapReadyCallback {
            private val layout: View = view
            private val mapView: MapView = layout.findViewById(R.id.mapaCentroMedico)
            private val title: TextView = layout.findViewById(R.id.nombreCentroMedico)
            private lateinit var map: GoogleMap
            private lateinit var latLng: LatLng

            /** Inicializa MapView llamando a los metodos de lifecycle */
            init {
                    /** Inicializar el MapView */
                    mapView.onCreate(null)
                    mapView.getMapAsync(this@ViewHolder)
            }

            private fun setMapLocation() {
                if (!::map.isInitialized) return
                map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 13f))
                map.addMarker(MarkerOptions().position(latLng))
                map.mapType = GoogleMap.MAP_TYPE_NORMAL
                map.setOnMapClickListener {
                    /**
                    Toast.makeText(this@CentrosMedicosActivity, "Clicked on ${title.text}", Toast.LENGTH_SHORT).show()
                     */
                    getLocation()
                }
            }

            override fun onMapReady(googleMap: GoogleMap?) {
                MapsInitializer.initialize(applicationContext)
                /** solo se ejecuta si ya esta inicializado */
                map = googleMap?: return
                setMapLocation()
            }

            /** Para que el RecyclerView enlace el ViewHolder */
            fun bindView(position: Int) {
                centrosMedicosList[position].let {
                    latLng = it.latidud
                    mapView.tag = this
                    title.text = it.nombre
                    setMapLocation()
                }
            }

            /** cuando el recyclerListener necesita borrar el mapa, para ahorrar recursos */
            fun clearView() {
                    map.clear()
                    map.mapType = GoogleMap.MAP_TYPE_NONE
            }
        }
    }
}
val centrosMedicosList = listOf(
    CentroMedico("Clinica Del Sur", LatLng(-16.52570128266036, -68.10872254226858)),
    CentroMedico("Hospital Arcoiris", LatLng(-16.52570128266036, -68.10872254226858)),
    CentroMedico("Hospital Cuarenta", LatLng(-16.52570128266036, -68.10872254226858)),
    CentroMedico("Clinica Del Norte", LatLng(-16.52570128266036, -68.10872254226858)),
    CentroMedico("Clinica Del Sur", LatLng(-16.52570128266036, -68.10872254226858)),
    CentroMedico("Clinica Del Sur", LatLng(-16.52570128266036, -68.10872254226858)),
    CentroMedico("Clinica Del Sur", LatLng(-16.52570128266036, -68.10872254226858)),
    CentroMedico("Clinica Del Sur", LatLng(-16.52570128266036, -68.10872254226858)),
    CentroMedico("Clinica Del Sur", LatLng(-16.52570128266036, -68.10872254226858)),
    CentroMedico("Clinica Del Sur", LatLng(-16.52570128266036, -68.10872254226858)),
    CentroMedico("Clinica Del Sur", LatLng(-16.52570128266036, -68.10872254226858))
)
