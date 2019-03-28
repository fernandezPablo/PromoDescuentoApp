package org.fernandez.pablo.promodescuentoapp.app.Presenter

import android.util.Log
import org.fernandez.pablo.promodescuentoapp.app.Model.Descuento
import org.fernandez.pablo.promodescuentoapp.app.Service.FileManager
import org.fernandez.pablo.promodescuentoapp.app.Service.JsonManager
import org.fernandez.pablo.promodescuentoapp.app.View.IMainActivity
import org.fernandez.pablo.promodescuentoapp.app.View.MainActivity
import java.text.DecimalFormat

class MainPresenter(val activity: IMainActivity) {

    fun guardarDescuento(montoDescuento: Int) {
        val descuento = Descuento(montoDescuento)
        Log.i("PromoDescuentoAppInfo","Descuento: $descuento")
        val jsonString = JsonManager.objectToJsonString(descuento)
        Log.i("PromoDescuentoAppInfo","jsonString: $jsonString")
        FileManager.writeFile(jsonString,this.activity as MainActivity)
    }

    fun leerDescuento(){
        val txtFile = FileManager.readFile(activity as MainActivity)
        var descuento = JsonManager.jsonToObject(txtFile,Descuento())
        Log.d("PromoDescuentoLa24Debug",descuento.toString())
        activity.setDescuento(descuento.monto)
    }

    fun calcularPrecioConDescuento() {
        val precioSinDescuento = activity.getMontoSinDescuento()
        val porcentajeDescuento = activity.getDescuento()
        var precioConDescuento = precioSinDescuento - (porcentajeDescuento * precioSinDescuento)/100
        activity.setMontoConDescuento(precioConDescuento)
    }


}