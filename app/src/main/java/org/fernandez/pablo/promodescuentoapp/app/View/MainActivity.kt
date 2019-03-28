package org.fernandez.pablo.promodescuentoapp.app.View

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AlertDialog
import android.util.Log
import android.widget.*
import org.fernandez.pablo.promodescuentoapp.R
import org.fernandez.pablo.promodescuentoapp.app.Presenter.MainPresenter
import java.math.RoundingMode
import java.text.DecimalFormat

class MainActivity() : AppCompatActivity(), IMainActivity {

    val presenter = MainPresenter(this)
    val PERMISO_ESCRIBIR_ALMACENAMIENTO = 1
    val PERMISO_LEER_ALMACENAMIENTO = 2

    @SuppressLint("ResourceType")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val mBuilder = AlertDialog.Builder(this)
        val mView = layoutInflater.inflate(R.layout.alert_dialog_settings,null)
        mBuilder.setView(mView)
        val alertDialog = mBuilder.create()

        val descuentoEditText = mView.findViewById<EditText>(R.id.porcentaje_descuento_edit_text)
        val cancelarButton = mView.findViewById<Button>(R.id.cancelar_button)
        cancelarButton.setOnClickListener {
            alertDialog.dismiss()
        }
        val guardarButton = mView.findViewById<Button>(R.id.guardar_button)
        guardarButton.setOnClickListener {
            presenter.guardarDescuento(descuentoEditText.text.toString().toInt())
            alertDialog.dismiss()
            this.presenter.leerDescuento()
        }

        val settingsButton = findViewById<ImageButton>(R.id.settings_image_button)
        settingsButton.setOnClickListener {
            descuentoEditText.setText("")
            if(this.comprobarPermisoEscritura()){
                alertDialog.show()
            }
            else{
                this.pedirPermisoEscritura()
            }
        }

        val calcularButton = findViewById<Button>(R.id.calcular_button)
        calcularButton.setOnClickListener {
            this.presenter.calcularPrecioConDescuento()
        }

        val refrescarButton = findViewById<Button>(R.id.refrescar_button)
        refrescarButton.setOnClickListener {
            this.nuevoCalculo()
        }
        if(this.comprobarPermisoLectura()){
            this.presenter.leerDescuento()
        }
        else{
            this.pedirPermisoLectura()
        }
    }

    private fun nuevoCalculo() {
        val precioSinDescuentoEditText = findViewById<EditText>(R.id.precio_sin_descuento_edit_text)
        precioSinDescuentoEditText.setText("")
        precioSinDescuentoEditText.requestFocus()
        val precioConDescuento = findViewById<TextView>(R.id.precio_con_descuento_text_view)
        precioConDescuento.setText("0.00")
    }

    override fun getMontoSinDescuento(): Double {
        val montoSinDescuentoEditText = findViewById<EditText>(R.id.precio_sin_descuento_edit_text)
        return montoSinDescuentoEditText.text.toString().toDouble()
    }

    override fun setMontoConDescuento(monto: Double) {
       val montoConDescuentoTextView = findViewById<TextView>(R.id.precio_con_descuento_text_view)
        val decimalFormat = DecimalFormat("#.##")
        decimalFormat.roundingMode = RoundingMode.CEILING
        montoConDescuentoTextView.setText(decimalFormat.format(monto))
    }

    override fun getDescuento(): Int {
        val descuentoTextView = findViewById<TextView>(R.id.porcentaje_descuento_text_view)
        return descuentoTextView.text.toString().toInt()
    }

    override fun setDescuento(montoDescuento: Int) {
        val porcentajeDescuentoTextView = findViewById<TextView>(R.id.porcentaje_descuento_text_view)
        porcentajeDescuentoTextView.setText(montoDescuento.toString())
    }

    override fun showMessage(message: String) {
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show()
    }

    fun pedirPermisoEscritura(){
        if(ContextCompat.checkSelfPermission(this,android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
            != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(
                this,
                arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE),
                this.PERMISO_ESCRIBIR_ALMACENAMIENTO
            )
        }
    }

    fun pedirPermisoLectura(){
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                this.PERMISO_LEER_ALMACENAMIENTO)
    }

    fun comprobarPermisoLectura(): Boolean = (ContextCompat.checkSelfPermission(this,Manifest.permission.READ_EXTERNAL_STORAGE)
    == PackageManager.PERMISSION_GRANTED)

    fun comprobarPermisoEscritura(): Boolean = (ContextCompat.checkSelfPermission(this,Manifest.permission.WRITE_EXTERNAL_STORAGE)
            == PackageManager.PERMISSION_GRANTED)

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when(requestCode){
            this.PERMISO_ESCRIBIR_ALMACENAMIENTO -> this.respuestaUsuario(requestCode,grantResults,"escritura")
            this.PERMISO_LEER_ALMACENAMIENTO -> this.respuestaUsuario(requestCode,grantResults,"lectura")
            else -> Log.d("PromoDescuentoAppDebug","No hay mensajes para este permiso")
        }
    }

    fun respuestaUsuario(requestCode: Int, grantResults: IntArray, permiso: String){
        if(grantResults.size > 0 &&
            grantResults[0].equals(PackageManager.PERMISSION_GRANTED)){
            Log.d("PromoDescuentoAppDebug","Permiso de $permiso Concedido!")
            this.presenter.leerDescuento()
        }
        else{
            Log.d("PromoDescuentoAppDebug","Permiso de $permiso Denegado!")
            Toast.makeText(
                this,
                "El permiso de acceso al almacenamiento es necesario para que la app funcione correctametne",
                Toast.LENGTH_LONG).show()
        }
    }
}
