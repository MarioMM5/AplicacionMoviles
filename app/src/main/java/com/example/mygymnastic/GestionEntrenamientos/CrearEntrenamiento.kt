package com.example.mygymnastic2

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.mygymnastic.R
import org.json.JSONArray
import org.json.JSONObject

class CrearEntrenamiento : AppCompatActivity() {

    private lateinit var etTrainingName: EditText
    private lateinit var etExercise: EditText
    private lateinit var llExerciseList: LinearLayout
    private val exercises = mutableListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_crear_entrenamiento)

        etTrainingName = findViewById(R.id.etTrainingName)
        etExercise = findViewById(R.id.etExercise)
        llExerciseList = findViewById(R.id.llExerciseList)

        val btnAddExercise: Button = findViewById(R.id.btnAddExercise)
        val btnSaveTraining: Button = findViewById(R.id.btnSaveTraining)

        btnAddExercise.setOnClickListener {
            val exerciseName = etExercise.text.toString()
            if (exerciseName.isNotEmpty()) {
                exercises.add(exerciseName)
                addExerciseToList(exerciseName)
                etExercise.text.clear()
            }
        }

        btnSaveTraining.setOnClickListener {
            val trainingName = etTrainingName.text.toString()
            if (trainingName.isNotEmpty() && exercises.isNotEmpty()) {
                saveTraining(trainingName, exercises)
                finish()
            }
        }
    }


    private fun addExerciseToList(exerciseName: String) {
        if (exerciseName.isBlank()) return

        val textView = TextView(this).apply {
            text = exerciseName
            textSize = 16f
            setPadding(0, 8, 0, 8)

            // Cambiar el color del texto antes de agregarlo a la lista
            setTextColor(ContextCompat.getColor(context, R.color.white))

        }
        llExerciseList.addView(textView)

    }
    private fun saveTraining(trainingName: String, exercises: List<String>) {
        val sharedPreferences = getSharedPreferences("Trainings", MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        val trainingSet =
            sharedPreferences.getStringSet("TrainingsSet", mutableSetOf())?.toMutableSet()
                ?: mutableSetOf()

        val trainingJson = JSONObject().apply {
            put("name", trainingName)
            put("exercises", JSONArray(exercises))
        }

        trainingSet.add(trainingJson.toString())
        editor.putStringSet("TrainingsSet", trainingSet)
        editor.apply()
    }
}
