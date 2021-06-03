package com.example.proyecto_progra_3
import java.text.SimpleDateFormat
import java.util.*

fun Date.toString(format: String, locale: Locale = Locale.getDefault()): String {
    val formatter = SimpleDateFormat(format, locale)
    return formatter.format(this)
}

class Producto (val nombre:String, var precio: Float, var cantidad: Int){
    fun cambiarPrecio(nuevoPrecio:Float){
        precio = nuevoPrecio
    }

    fun vender(cantidadVendida: Int){
        if(cantidadVendida < 0){
            println("No es posible vender un numero negativo, intentelo de nuevo")
        }
        if(cantidadVendida > cantidad){
            println("Esto no es posible, no se puede vender lo que no se tiene, por favor introduce el numero correcto")
        }else {
            cantidad -= cantidadVendida
        }
    }

    fun agregar(cantidadAgregada: Int){
        if(cantidadAgregada < 0){
            println("No es posible agregar un numero negativo, intentelo de nuevo")
        }
        cantidad += cantidadAgregada
    }
}