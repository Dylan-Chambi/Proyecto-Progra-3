package com.example.proyecto_progra_3

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.proyecto_progra_3.databinding.ActivityPharmacySearchBinding
import com.google.android.gms.maps.model.LatLng

class PharmacySearch : AppCompatActivity() {

    lateinit var recyclerViewPharmacySearch: RecyclerView
    lateinit var searchProduct: EditText
    lateinit var searchButtonP: ImageButton
    private lateinit var bindingSearch: ActivityPharmacySearchBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        theme.applyStyle(R.style.AppThemeGreen, true)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pharmacy_search)
        bindingSearch = ActivityPharmacySearchBinding.inflate(layoutInflater)
        setContentView(bindingSearch.root)

        recyclerViewPharmacySearch = bindingSearch.recyclerViewSP

        val adapter = PharmacySearchViewAdapter(this, products)
        val layoutManager = LinearLayoutManager(this)

        recyclerViewPharmacySearch.adapter = adapter
        recyclerViewPharmacySearch.layoutManager = layoutManager
        searchProduct = bindingSearch.searchProduct
        searchButtonP = bindingSearch.searchButtonP

        // toolbar
        val toolbar = findViewById<View>(R.id.toolbarPharmacySearch) as Toolbar
        setSupportActionBar(toolbar)

        // add back arrow to toolbar
        if (supportActionBar != null) {
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
            supportActionBar!!.title = ""
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // handle arrow click here
        if (item.itemId === android.R.id.home) {
            finish() // close this activity and return to preview activity (if there is any)
        }
        return super.onOptionsItemSelected(item)
    }





    inner class PharmacySearchViewAdapter(val context: Context, private val list: List<String>): RecyclerView.Adapter<OptionsViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OptionsViewHolder {
            val layoutInflater = LayoutInflater.from(context)
            val itemListView = layoutInflater.inflate(R.layout.layout_pharmacy_list_recycler_view, parent, false)
            return OptionsViewHolder(itemListView)
        }

        override fun onBindViewHolder(holder: OptionsViewHolder, position: Int) {
            holder.bind(list[position])

        }
        override fun getItemCount() = products.size
    }

    class OptionsViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

        // variables en pantalla
        private var productName: TextView = itemView.findViewById(R.id.productName)
        private var productIcon: View = itemView.findViewById(R.id.productIcon)
        private var pharmacyCount: TextView = itemView.findViewById(R.id.pharmacyCount)
        fun bind(product: String) {
            // bindear cada opcion en pantalla para cada elemento de la lista
            productName.text = product
            pharmacyCount.text = "Pharmacy with that item: ${countProductsPharmacy(product)}"
        }

        private fun countProductsPharmacy(product: String): Int{
            var cont = 0
            pharmacyList.forEach {
                if(hasOneProduct(product, it.productList)){
                    cont++
                }
            }
            return cont
        }

        private fun hasOneProduct(product: String, productList: List<Producto>): Boolean{
            for (x in productList) {
                if(product == x.nombre && x.cantidad > 0) {
                    return true
                    break
                }
            }
            return false
        }
    }
}
val products = listOf("Vitamin C pills", "Sunscreen")

val productList = listOf(
    Producto("Vitamin C pills", 6),
    Producto("Vitamin C pills", 6),
    Producto("Vitamin C pills", 6),
    Producto("Vitamin C pills", 6),
    Producto("Vitamin C pills", 6),
    Producto("Vitamin C pills", 6),
    Producto("Vitamin C pills", 6),
    Producto("Vitamin C pills", 6),
    Producto("Vitamin C pills", 6),
    Producto("Vitamin C pills", 6),
    Producto("Sunscreen", 6)
)

val pharmacyList = listOf(
    Pharmacy("Farmacia Bolivia", LatLng(-16.504024200101696, -68.13402742379976), productList),
    Pharmacy("Farmacia Bolivia", LatLng(-16.504024200101696, -68.13402742379976), productList),
    Pharmacy("Farmacia Bolivia", LatLng(-16.504024200101696, -68.13402742379976), productList),
    Pharmacy("Farmacia Bolivia", LatLng(-16.504024200101696, -68.13402742379976), productList),
    Pharmacy("Farmacia Bolivia", LatLng(-16.504024200101696, -68.13402742379976), productList),
    Pharmacy("Farmacia Bolivia", LatLng(-16.504024200101696, -68.13402742379976), productList),
    Pharmacy("Farmacia Bolivia", LatLng(-16.504024200101696, -68.13402742379976), productList),
    Pharmacy("Farmacia Bolivia", LatLng(-16.504024200101696, -68.13402742379976), productList),
    Pharmacy("Farmacia Bolivia", LatLng(-16.504024200101696, -68.13402742379976), productList),
    Pharmacy("Farmacia Bolivia", LatLng(-16.504024200101696, -68.13402742379976), productList),
    Pharmacy("Farmacia Bolivia", LatLng(-16.504024200101696, -68.13402742379976), productList),
)

data class Pharmacy(val nombre: String, val latLng: LatLng, val productList: List<Producto>)
