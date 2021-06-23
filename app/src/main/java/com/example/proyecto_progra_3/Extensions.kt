package com.example.proyecto_progra_3

import java.text.SimpleDateFormat
import java.util.*

fun String.stripAccents(): String{
    if (this == null) {
        return "";
    }

    val chars: CharArray = this.toCharArray()

    var sb = StringBuilder(this)
    var cont: Int = 0

    while (chars.size > cont) {
        var c: Char = chars[cont]
        var c2:String = c.toString()
        //these are my needs, in case you need to convert other accents just Add new entries aqui
        c2 = c2.replace("á", "a")
        c2 = c2.replace("é", "e")
        c2 = c2.replace("í", "i")
        c2 = c2.replace("ó", "o")
        c2 = c2.replace("ú", "u")
        c = c2.single()
        sb.setCharAt(cont, c)
        cont++

    }
    return sb.toString()
}

fun Date.toString(format: String, locale: Locale = Locale.getDefault()): String {
    val formatter = SimpleDateFormat(format, locale)
    return formatter.format(this)
}