package com.example.proyecto_progra_3

class Tienda (var nombre:String, var localizacion:String){
    val listaDeProductos: MutableList<Producto> = mutableListOf()

    fun cambiarNombre(nuevoNombre: String){
        nombre = nuevoNombre
    }

    fun cambiarLocalizacion(nuevaLocalizacion: String){
        localizacion = nuevaLocalizacion
    }

    fun agregarProducto(nuevoProducto: Producto){
        listaDeProductos.add(nuevoProducto)
    }
}
