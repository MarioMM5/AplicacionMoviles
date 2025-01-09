package com.example.mygymnastic2

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.mygymnastic.R
import org.json.JSONObject

class RecomendacionesEntrenamiento : AppCompatActivity() {

    private lateinit var listView: ListView
    private lateinit var btnStartTraining: Button

    private var selectedTrainingJson: String? = null
    private var selectedTrainingPosition: Int = -1
    private lateinit var trainingSet: MutableSet<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recomendaciones_entrenamiento)

        listView = findViewById(R.id.lvTrainings)
        btnStartTraining = findViewById(R.id.btnStartTraining)

        btnStartTraining.isEnabled = false

        // Entrenamientos predefinidos
        trainingSet = mutableSetOf(
            JSONObject(mapOf("name" to "Cardio Básico", "exercises" to listOf("Saltos", "Burpees", "Correr en el lugar"))).toString(),
            JSONObject(mapOf("name" to "Fuerza Superior", "exercises" to listOf("Flexiones", "Plancha", "Dominadas"))).toString(),
            JSONObject(mapOf("name" to "Yoga Relajante", "exercises" to listOf("Postura del niño", "Perro hacia abajo", "Torsión espinal"))).toString(),
            JSONObject(mapOf("name" to "Piernas Poderosas", "exercises" to listOf("Sentadillas", "Zancadas", "Puente de glúteos"))).toString(),
            JSONObject(mapOf("name" to "HIIT Avanzado", "exercises" to listOf("Burpees", "Sprints", "Saltos con tijera"))).toString()
        )

        val trainingList = trainingSet.map { jsonString ->
            val jsonObject = JSONObject(jsonString)
            jsonObject.getString("name")
        }

        val adapter = CustomAdapter2(this, trainingList)
        listView.adapter = adapter
        listView.setOnItemClickListener { _, _, position, _ ->
            selectedTrainingJson = trainingSet.elementAt(position)
            selectedTrainingPosition = position

            btnStartTraining.isEnabled = true
        }

        btnStartTraining.setOnClickListener {
            startTraining()
        }

    }

    private fun startTraining() {
        if (selectedTrainingJson != null) {
            val selectedTraining = JSONObject(selectedTrainingJson!!)
            val exercisesArray = selectedTraining.getJSONArray("exercises")
            val exercises = mutableListOf<String>()

            for (i in 0 until exercisesArray.length()) {
                exercises.add(exercisesArray.getString(i))
            }

            val resultIntent = Intent().apply {
                putStringArrayListExtra("exercises", ArrayList(exercises))
                putExtra("trainingName", selectedTraining.getString("name"))
            }

            setResult(Activity.RESULT_OK, resultIntent)
            finish()
        }
    }
}

class CustomAdapter2(context: Context, private val items: List<String>) : ArrayAdapter<String>(context, 0, items) {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: LayoutInflater.from(context).inflate(android.R.layout.simple_list_item_1, parent, false)

        // Cambia el color del texto según la posición
        val textView = view.findViewById<TextView>(android.R.id.text1)
        textView.text = items[position]

        textView.setTextColor(Color.WHITE)
        return view
    }
}
