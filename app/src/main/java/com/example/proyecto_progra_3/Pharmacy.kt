package com.example.proyecto_progra_3

import com.google.android.gms.maps.model.LatLng

data class Pharmacy(val namePharmacy: String, val location: LatLng, val productList: List<Producto>)