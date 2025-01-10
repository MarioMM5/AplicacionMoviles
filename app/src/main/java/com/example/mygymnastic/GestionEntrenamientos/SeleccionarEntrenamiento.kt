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

class SeleccionarEntrenamiento : AppCompatActivity() {

    private lateinit var listView: ListView
    private lateinit var btnStartTraining: Button
    private lateinit var btnDeleteTraining: Button

    private var selectedTrainingJson: String? = null
    private var selectedTrainingPosition: Int = -1
    private lateinit var trainingSet: MutableSet<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_seleccionar_entrenamiento)

        listView = findViewById(R.id.lvTrainings)
        btnStartTraining = findViewById(R.id.btnStartTraining)
        btnDeleteTraining = findViewById(R.id.btnDeleteTraining)

        btnStartTraining.isEnabled = false
        btnDeleteTraining.isEnabled = false

        val sharedPreferences = getSharedPreferences("Trainings", MODE_PRIVATE)
        trainingSet = sharedPreferences.getStringSet("TrainingsSet", mutableSetOf())?.toMutableSet()
            ?: mutableSetOf()

        val trainingList = trainingSet.map { jsonString ->
            val jsonObject = JSONObject(jsonString)
            jsonObject.getString("name")
        }

        val adapter = CustomAdapter(this, trainingList)
        listView.adapter = adapter
        listView.setOnItemClickListener { _, _, position, _ ->
            selectedTrainingJson = trainingSet.elementAt(position)
            selectedTrainingPosition = position

            btnStartTraining.isEnabled = true
            btnDeleteTraining.isEnabled = true
        }

        btnStartTraining.setOnClickListener {
            startTraining()
        }

        btnDeleteTraining.setOnClickListener {
            deleteTraining()
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

    private fun deleteTraining() {
        if (selectedTrainingJson != null && selectedTrainingPosition != -1) {
            trainingSet.remove(selectedTrainingJson)

            val sharedPreferences = getSharedPreferences("Trainings", MODE_PRIVATE)
            val editor = sharedPreferences.edit()
            editor.putStringSet("TrainingsSet", trainingSet)
            editor.apply()

            Toast.makeText(this, "Entrenamiento eliminado", Toast.LENGTH_SHORT).show()

            updateTrainingList()
            btnStartTraining.isEnabled = false
            btnDeleteTraining.isEnabled = false
        }
    }

    private fun updateTrainingList() {
        val trainingList = trainingSet.map { jsonString ->
            val jsonObject = JSONObject(jsonString)
            jsonObject.getString("name")
        }

        val adapter = CustomAdapter(this, trainingList)
        listView.adapter = adapter
    }
}
class CustomAdapter(context: Context, private val items: List<String>) : ArrayAdapter<String>(context, 0, items) {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: LayoutInflater.from(context).inflate(android.R.layout.simple_list_item_1, parent, false)

        val textView = view.findViewById<TextView>(android.R.id.text1)
        textView.text = items[position]

        textView.setTextColor(Color.WHITE)
        return view
    }
}
