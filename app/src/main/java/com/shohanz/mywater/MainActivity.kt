package com.shohanz.mywater

import android.app.NotificationManager
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.util.TypedValue
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.shohanz.mywater.classes.AlarmHelp
import com.shohanz.mywater.classes.SqliteHelper
import com.shohanz.mywater.classes.AppUtils
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    private var totalIntake: Int = 0
    private var inTook: Int = 0
    private lateinit var sharedPref: SharedPreferences
    private lateinit var sqliteHelper: SqliteHelper
    private lateinit var dateNow: String
    private var notificStatus: Boolean = false
    private var selectedOption: Int? = null
    private var snackbar: Snackbar? = null
    private var doubleBackToExitPressedOnce = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        sharedPref = getSharedPreferences(AppUtils.USERS_SHARED_PREF, AppUtils.PRIVATE_MODE)
        sqliteHelper = SqliteHelper(this)
        totalIntake = sharedPref.getInt(AppUtils.TOTAL_INTAKE, 0)

        if (sharedPref.getBoolean(AppUtils.FIRST_RUN_KEY, true)) {
            startActivity(Intent(this, StartActivity::class.java))
            finish()
        } else if (totalIntake <= 0) {
            startActivity(Intent(this, StartUserActivity::class.java))
            finish()
        }

        dateNow = AppUtils.getCurrentDate()!!
    }

    private fun updateValues() {
        totalIntake = sharedPref.getInt(AppUtils.TOTAL_INTAKE, 0)
        inTook = sqliteHelper.getIntook(dateNow)
        setWaterLevel(inTook, totalIntake)
    }

    override fun onStart() {
        super.onStart()

        val outValue = TypedValue()
        applicationContext.theme.resolveAttribute(
            android.R.attr.selectableItemBackground,
            outValue,
            true
        )

        // Funciones Técnicas - Sistema Notificación
        notificStatus = sharedPref.getBoolean(AppUtils.NOTIFICATION_STATUS_KEY, true)
        val alarm = AlarmHelp()
        if (!alarm.checkAlarm(this) && notificStatus) {
            btnNotific.setImageDrawable(getDrawable(R.drawable.ic_bell))
            alarm.setAlarm(
                this,
                sharedPref.getInt(AppUtils.NOTIFICATION_FREQUENCY_KEY, 30).toLong()
            )
        }
        if (notificStatus) {
            btnNotific.setImageDrawable(getDrawable(R.drawable.ic_bell))
        } else {
            btnNotific.setImageDrawable(getDrawable(R.drawable.ic_bell_disabled))
        }

        // Funciones Técnicas - Conexión Base de Datos
        sqliteHelper.addAll(dateNow, 0, totalIntake)
        updateValues()

        // <INICIO> Funciones Técnicas - OnClickListener

        // Botón Menú
        //TODO OnClickListener Menú

        // Boton Añadir (+)
        fabAdd.setOnClickListener {
            if (selectedOption != null) {
                if ((inTook * 100 / totalIntake) <= 140) {
                    if (sqliteHelper.addIntook(dateNow, selectedOption!!) > 0) {
                        inTook += selectedOption!!
                        setWaterLevel(inTook, totalIntake)

                        Snackbar.make(it, "¡Se registro tu ingesta de agua!", Snackbar.LENGTH_SHORT)
                            .show()

                    }
                } else {
                    Snackbar.make(it, "Ya has llegado a tu objetivo", Snackbar.LENGTH_SHORT).show()
                }
                selectedOption = null
                op50ml.background = getDrawable(outValue.resourceId)
                op100ml.background = getDrawable(outValue.resourceId)
                op150ml.background = getDrawable(outValue.resourceId)
                op200ml.background = getDrawable(outValue.resourceId)
                op250ml.background = getDrawable(outValue.resourceId)
                op300ml.background = getDrawable(outValue.resourceId)

                // Desactivar notificaciones al completar el agua
                val mNotificationManager : NotificationManager =
                    getSystemService(NOTIFICATION_SERVICE) as NotificationManager
                mNotificationManager.cancelAll()
            } else {
                Snackbar.make(it, "Por favor, selecciona una opción", Snackbar.LENGTH_SHORT).show()
            }
        }

        // Botón Notificación
        //TODO OnClickListener Notificación

        // Botón 50ml
        op50ml.setOnClickListener {
            if (snackbar != null) {
                snackbar?.dismiss()
            }
            selectedOption = 50
            op50ml.background = getDrawable(R.drawable.option_select_bg)
            op100ml.background = getDrawable(outValue.resourceId)
            op150ml.background = getDrawable(outValue.resourceId)
            op200ml.background = getDrawable(outValue.resourceId)
            op250ml.background = getDrawable(outValue.resourceId)
            op300ml.background = getDrawable(outValue.resourceId)
        }

        // Botón 100ml
        op100ml.setOnClickListener {
            if (snackbar != null) {
                snackbar?.dismiss()
            }
            selectedOption = 100
            op50ml.background = getDrawable(outValue.resourceId)
            op100ml.background = getDrawable(R.drawable.option_select_bg)
            op150ml.background = getDrawable(outValue.resourceId)
            op200ml.background = getDrawable(outValue.resourceId)
            op250ml.background = getDrawable(outValue.resourceId)
            op300ml.background = getDrawable(outValue.resourceId)
        }

        // Botón 150ml
        op150ml.setOnClickListener {
            if (snackbar != null) {
                snackbar?.dismiss()
            }
            selectedOption = 150
            op50ml.background = getDrawable(outValue.resourceId)
            op100ml.background = getDrawable(outValue.resourceId)
            op150ml.background = getDrawable(R.drawable.option_select_bg)
            op200ml.background = getDrawable(outValue.resourceId)
            op250ml.background = getDrawable(outValue.resourceId)
            op300ml.background = getDrawable(outValue.resourceId)
        }

        // Botón 200ml
        op200ml.setOnClickListener {
            if (snackbar != null) {
                snackbar?.dismiss()
            }
            selectedOption = 200
            op50ml.background = getDrawable(outValue.resourceId)
            op100ml.background = getDrawable(outValue.resourceId)
            op150ml.background = getDrawable(outValue.resourceId)
            op200ml.background = getDrawable(R.drawable.option_select_bg)
            op250ml.background = getDrawable(outValue.resourceId)
            op300ml.background = getDrawable(outValue.resourceId)
        }

        // Botón 250ml
        op250ml.setOnClickListener {
            if (snackbar != null) {
                snackbar?.dismiss()
            }
            selectedOption = 250
            op50ml.background = getDrawable(outValue.resourceId)
            op100ml.background = getDrawable(outValue.resourceId)
            op150ml.background = getDrawable(outValue.resourceId)
            op200ml.background = getDrawable(outValue.resourceId)
            op250ml.background = getDrawable(R.drawable.option_select_bg)
            op300ml.background = getDrawable(outValue.resourceId)
        }

        // Botón 300ml
        op300ml.setOnClickListener {
            if (snackbar != null) {
                snackbar?.dismiss()
            }
            selectedOption = 300
            op50ml.background = getDrawable(outValue.resourceId)
            op100ml.background = getDrawable(outValue.resourceId)
            op150ml.background = getDrawable(outValue.resourceId)
            op200ml.background = getDrawable(outValue.resourceId)
            op250ml.background = getDrawable(outValue.resourceId)
            op300ml.background = getDrawable(R.drawable.option_select_bg)
        }
    }

    // <FINAL> Funciones Técnicas - OnClickListener


    // Función Técnica - Actualiza el Nivel de Agua
    private fun setWaterLevel(inTook: Int, totalIntake: Int) {
        tvIntook.text = "$inTook"
        tvTotalIntake.text = "/$totalIntake ml"
        if ((inTook * 100 / totalIntake) > 140) {
            Snackbar.make(main_activity_parent, "¡Llegaste a tu objetivo!", Snackbar.LENGTH_SHORT).show()
        }
    }

    // Función de Experiencia de Usuario - Retroceder
    override fun onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed()
            return
        }
        this.doubleBackToExitPressedOnce = true
        Snackbar.make(
            this.window.decorView.findViewById(android.R.id.content),
            "Por favor, pulsa Atrás otra vez para salir",
            Snackbar.LENGTH_SHORT
        ).show()
        Handler().postDelayed({ doubleBackToExitPressedOnce = false }, 1000)
    }
}