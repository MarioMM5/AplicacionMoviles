package com.example.mygymnastic2

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.mygymnastic.R
import java.util.stream.Collectors.toList

class RecomendacionesEntrenamiento : AppCompatActivity() {

    private lateinit var listView: ListView
    private lateinit var btnStartTraining: Button

    private var selectedTrainingName: String? = null
    private var selectedExercises: List<String>? = null

    // Lista de entrenamientos predefinida
    private val trainingData: Map<String, List<String>> = mapOf(
        "Cardio Básico" to listOf("Saltos", "Burpees", "Correr en el lugar"),
        "Fuerza Superior" to listOf("Flexiones", "Plancha", "Dominadas"),
        "Yoga Relajante" to listOf("Postura del niño", "Perro hacia abajo", "Torsión espinal"),
        "Piernas Poderosas" to listOf("Sentadillas", "Zancadas", "Puente de glúteos"),
        "HIIT Avanzado" to listOf("Burpees", "Sprints", "Saltos con tijera")
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recomendaciones_entrenamiento)

        listView = findViewById(R.id.listaRecomendados)
        btnStartTraining = findViewById(R.id.btnEmpezarEntrenamiento)

        btnStartTraining.isEnabled = false
        val arrayNombres: Array<String>
        // Configurar el adaptador de la lista con los nombres de los entrenamientos
        val adapter = CustomAdapter(
            this,trainingData.keys.toList()
        )
        listView.adapter = adapter

        // Manejar la selección en el ListView
        listView.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
            selectedTrainingName = trainingData.keys.elementAt(position)
            selectedExercises = trainingData.values.elementAt(position)

            btnStartTraining.isEnabled = true
        }

        // Configurar el botón para iniciar entrenamiento
        btnStartTraining.setOnClickListener {
            startTraining()
        }
    }

    private fun startTraining() {
        selectedTrainingName?.let { trainingName ->
            selectedExercises?.let { exercises ->
                // Guardar en SharedPreferences
                val sharedPreferences = getSharedPreferences("TrainingPrefs", Context.MODE_PRIVATE)
                val editor = sharedPreferences.edit()

                // Guardar el nombre del entrenamiento y la lista de ejercicios
                editor.putString("trainingName", trainingName)
                editor.putStringSet("exercises", exercises.toSet())

                editor.apply()

                // Enviar al MainActivity
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
    }
    class CustomAdapter(context: Context, private val items: List<String>) : ArrayAdapter<String>(context, 0, items) {
        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
            val view = convertView ?: LayoutInflater.from(context).inflate(android.R.layout.simple_list_item_1, parent, false)

            // Cambia el color del texto según la posición
            val textView = view.findViewById<TextView>(android.R.id.text1)
            textView.text = items[position]

            textView.setTextColor(Color.WHITE)
            return view
        }
    }

}

