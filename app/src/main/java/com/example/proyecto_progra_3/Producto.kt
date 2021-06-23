package com.example.proyecto_progra_3

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