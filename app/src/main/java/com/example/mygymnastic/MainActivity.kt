package com.example.mygymnastic2

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.mygymnastic.R
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class MainActivity : AppCompatActivity() {

    private lateinit var tvDate: TextView
    private lateinit var tvExercise: TextView
    private lateinit var tvPhrase: TextView
    private lateinit var btnPrev: Button
    private lateinit var btnNext: Button
    private lateinit var btnCreateTraining: Button
    private lateinit var btnSelectTraining: Button

    private var currentExerciseIndex = 0
    private var exercises: List<String> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setTheme(R.style.Theme_Mygymnastic)
        setContentView(R.layout.activity_main)

        tvDate = findViewById(R.id.tvDate)
        tvExercise = findViewById(R.id.tvExercise)
        tvPhrase = findViewById(R.id.tvPhrase)  // TextView para mostrar el nombre del entrenamiento
        btnPrev = findViewById(R.id.btnPrev)
        btnNext = findViewById(R.id.btnNext)
        btnCreateTraining = findViewById(R.id.btnCreateTraining)
        btnSelectTraining = findViewById(R.id.btnSelectTraining)

        val currentDate = getCurrentDateInSpanish()
        tvDate.text = currentDate

        updateExerciseDisplay()
        updateTrainingName(null)  // Muestra un mensaje predeterminado al inicio

        btnPrev.setOnClickListener {
            if (currentExerciseIndex > 0) {
                currentExerciseIndex--
                updateExerciseDisplay()
            }
        }

        btnNext.setOnClickListener {
            if (currentExerciseIndex < exercises.size - 1) {
                currentExerciseIndex++
                updateExerciseDisplay()
            }
        }

        btnCreateTraining.setOnClickListener {
            val intent = Intent(this, CrearEntrenamiento::class.java)
            startActivity(intent)
        }

        btnSelectTraining.setOnClickListener {
            val intent = Intent(this, SeleccionarEntrenamiento::class.java)
            startActivityForResult(intent, SELECT_TRAINING_REQUEST)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == SELECT_TRAINING_REQUEST && resultCode == RESULT_OK) {
            val exercises = data?.getStringArrayListExtra("exercises") ?: emptyList()
            val trainingName = data?.getStringExtra("trainingName")

            this.exercises = exercises
            currentExerciseIndex = 0

            updateExerciseDisplay()
            updateTrainingName(trainingName)  // Actualiza el nombre del entrenamiento
        }
    }

    private fun updateExerciseDisplay() {
        if (exercises.isNotEmpty() && currentExerciseIndex in exercises.indices) {
            tvExercise.text = exercises[currentExerciseIndex]
        } else {
            tvExercise.text = "No hay ejercicios"
        }

        btnPrev.isEnabled = currentExerciseIndex > 0
        btnNext.isEnabled = currentExerciseIndex < exercises.size - 1
    }

    private fun updateTrainingName(trainingName: String?) {
        tvPhrase.text = if (trainingName != null) {
            "Entrenamiento seleccionado: $trainingName"
        } else {
            "No hay entrenamiento seleccionado"
        }
    }

    private fun getCurrentDateInSpanish(): String {
        val locale = Locale("es", "ES")
        val calendar = Calendar.getInstance()
        val dateFormat = SimpleDateFormat("EEEE, dd 'de' MMMM 'de' yyyy", locale)

        // Convertir la primera letra del día a mayúscula
        return dateFormat.format(calendar.time).replaceFirstChar { it.uppercase(locale) }
    }

    companion object {
        private const val SELECT_TRAINING_REQUEST = 1
    }
}