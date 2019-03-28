package org.fernandez.pablo.promodescuentoapp.app.Service

import com.google.gson.Gson
import org.fernandez.pablo.promodescuentoapp.app.Model.Descuento

object JsonManager {

    val gson = Gson()

    fun jsonToObject(jsonString: String, descuento: Descuento) : Descuento = gson.fromJson(jsonString,descuento::class.java)

    fun objectToJsonString(descuento: Descuento): String = gson.toJson(descuento)
}