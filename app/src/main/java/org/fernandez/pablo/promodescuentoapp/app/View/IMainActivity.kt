package org.fernandez.pablo.promodescuentoapp.app.View

interface IMainActivity  {

    fun getMontoSinDescuento(): Double
    fun setMontoConDescuento(monto: Double): Unit
    fun getDescuento(): Int
    fun setDescuento(montoDescuento: Int): Unit
    fun showMessage(message: String): Unit

}