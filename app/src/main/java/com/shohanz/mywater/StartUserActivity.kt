package com.shohanz.mywater

import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.text.TextUtils
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.shohanz.mywater.classes.AppUtils
import kotlinx.android.synthetic.main.activity_start_user.*
import java.math.RoundingMode
import java.text.DecimalFormat
import java.util.*

class StartUserActivity : AppCompatActivity() {

    private var weight: String = ""
    private var workTime: String = ""
    private var wakeupTime: Long = 0
    private var sleepingTime: Long = 0
    private lateinit var sharedPref: SharedPreferences
    private var doubleBackToExitPressedOnce = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val is24h = android.text.format.DateFormat.is24HourFormat(this.applicationContext)

        setContentView(R.layout.activity_start_user)

        sharedPref = getSharedPreferences(AppUtils.USERS_SHARED_PREF, AppUtils.PRIVATE_MODE)
        wakeupTime = sharedPref.getLong(AppUtils.WAKEUP_TIME, 1558323000000)
        sleepingTime = sharedPref.getLong(AppUtils.SLEEPING_TIME_KEY, 1558369800000)

        // Campo de Texto Hora Despertar
        etWakeUpTime.editText!!.setOnClickListener {
            val calendar = Calendar.getInstance()
            calendar.timeInMillis = wakeupTime

            val mTimePicker = TimePickerDialog(
                this,
                { _, selectedHour, selectedMinute ->
                    val time = Calendar.getInstance()
                    time.set(Calendar.HOUR_OF_DAY, selectedHour)
                    time.set(Calendar.MINUTE, selectedMinute)
                    wakeupTime = time.timeInMillis

                    etWakeUpTime.editText!!.setText(
                        String.format("%02d:%02d", selectedHour, selectedMinute)
                    )
                }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), is24h
            )
            mTimePicker.setTitle("Hora Despertar")
            mTimePicker.show()
        }

        // Campo de Texto Hora Dormir
        etSleepTime.editText!!.setOnClickListener {
            val calendar = Calendar.getInstance()
            calendar.timeInMillis = sleepingTime

            val mTimePicker = TimePickerDialog(
                this,
                { _, selectedHour, selectedMinute ->
                    val time = Calendar.getInstance()
                    time.set(Calendar.HOUR_OF_DAY, selectedHour)
                    time.set(Calendar.MINUTE, selectedMinute)
                    sleepingTime = time.timeInMillis

                    etSleepTime.editText!!.setText(
                        String.format("%02d:%02d", selectedHour, selectedMinute)
                    )
                }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), is24h
            )
            mTimePicker.setTitle("Hora Dormir")
            mTimePicker.show()
        }

        // Botón Continuar
        btnContinue.setOnClickListener {
            val imm: InputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(init_user_info_parent_layout.windowToken, 0)

            weight = etWeight.editText!!.text.toString()
            workTime = etWorkTime.editText!!.text.toString()

            when {
                TextUtils.isEmpty(weight) -> Snackbar.make(it, "Por favor, ingresa tu peso", Snackbar.LENGTH_SHORT).show()
                weight.toInt() > 200 || weight.toInt() < 20 -> Snackbar.make(
                    it,
                    "Por favor, ingresa un peso válido",
                    Snackbar.LENGTH_SHORT
                ).show()
                TextUtils.isEmpty(workTime) -> Snackbar.make(
                    it,
                    "Por favor, ingresa tu tiempo de actividad física",
                    Snackbar.LENGTH_SHORT
                ).show()
                workTime.toInt() > 500 || workTime.toInt() < 0 -> Snackbar.make(
                    it,
                    "Por favor, ingresa un tiempo de actividad física válido",
                    Snackbar.LENGTH_SHORT
                ).show()
                else -> {
                    val editor = sharedPref.edit()
                    editor.putInt(AppUtils.WEIGHT_KEY, weight.toInt())
                    editor.putInt(AppUtils.WORK_TIME_KEY, workTime.toInt())
                    editor.putLong(AppUtils.WAKEUP_TIME, wakeupTime)
                    editor.putLong(AppUtils.SLEEPING_TIME_KEY, sleepingTime)
                    editor.putBoolean(AppUtils.FIRST_RUN_KEY, false)

                    val totalIntake = AppUtils.calculateIntake(weight.toInt(), workTime.toInt())
                    val df = DecimalFormat("#")
                    df.roundingMode = RoundingMode.CEILING
                    editor.putInt(AppUtils.TOTAL_INTAKE, df.format(totalIntake).toInt())

                    editor.apply()
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                }
            }
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
            "Pulsa Atrás nuevamente para salir",
            Snackbar.LENGTH_SHORT
        ).show()

        Handler().postDelayed({ doubleBackToExitPressedOnce = false }, 1000)
    }
}