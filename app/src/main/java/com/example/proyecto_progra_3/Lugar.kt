package com.example.proyecto_progra_3

open class Lugar(var nombre: String, var x: Int, var y: Int) {
    fun cambiarLocalizacion(nuevoX: Int, nuevoY: Int){
        x = nuevoX
        y = nuevoY
    }

    fun cambiarNombre(nuevoNombre: String){
        nombre = nuevoNombre
    }
}