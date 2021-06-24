package com.example.proyecto_progra_3

import android.Manifest
import android.annotation.SuppressLint
import android.app.DownloadManager
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.maps.CameraUpdate
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import kotlinx.android.synthetic.main.activity_centros_medicos_map.*
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject
import org.w3c.dom.Text

class CentrosMedicosMapActivity : AppCompatActivity(), OnMapReadyCallback {
    lateinit var recyclerViewCentroMedico: RecyclerView
    lateinit var map: GoogleMap
    private lateinit var ubicacion: Location
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
        enableLocation()
    }

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

        override fun getItemCount(): Int {
            return centrosMedicosListNear.size
        }
    }

    class OptionsViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        // variables en pantalla
        val nombreCentroMedico: TextView = itemView.findViewById(R.id.nombreCM)

        fun bind(centroMedico: CentroMedico) {
            // bindear cada opcion en pantalla para cada elemento de la lista
            nombreCentroMedico.text = centroMedico.nombre
        }
    }
}

private val centrosMedicosListNear = listOf(
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