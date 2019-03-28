package org.fernandez.pablo.promodescuentoapp.app.Service

import android.app.Activity
import android.os.Environment
import android.support.v7.app.AppCompatActivity
import android.util.Log
import java.io.*
import java.lang.StringBuilder

object FileManager {

    val FILE_NAME = "porcentajeDescuento.json"
    val DIRECTORY = "/PromoDescuentoLa24/"

    fun writeFile(text: String, activity: AppCompatActivity){

            val path = File(Environment.getExternalStorageDirectory(),this.DIRECTORY)
            Log.i("PromoDescuentoAppInfo",path.absolutePath)
            var success = true
            if(!path.exists()){
                success = path.mkdir()
            }
            if(success){
                File(path,"porcentajeDescuento.json").writeText(text)
                }
            else{
                Log.d("PromoDescuentoAppDebug","No se pudo crear el directorio")
            }

    }

    fun readFile(activity: AppCompatActivity): String{
        val path = File(Environment.getExternalStorageDirectory(),this.DIRECTORY)
        var txt = "{monto:\"0\"}"
        var success = true
        if(!path.exists()){
            success = path.mkdir()
            return txt
        }
        if(success){
            val file = File(path,this.FILE_NAME)
            if (file.exists()){
                txt = file.readText()
            }
        }
        else{
            Log.d("PromoDescuentoAppDebug","No se pudo crear el directorio")
        }

        return txt
    }

}