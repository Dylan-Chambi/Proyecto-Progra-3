package com.example.proyecto_progra_3

class Tienda(nombre: String, x: Int, y: Int, var localizacion: String) : Lugar(nombre, x, y){
    val listaDeProductos: MutableList<Producto> = mutableListOf()

    fun cambiarLocalizacion(nuevaLocalizacion: String){
        localizacion = nuevaLocalizacion
    }

    fun agregarProducto(nuevoProducto: Producto){
        listaDeProductos.add(nuevoProducto)
    }
}