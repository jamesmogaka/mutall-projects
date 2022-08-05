package com.example.calculator

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        // selecting the ui elements by code
        val number1 = findViewById<EditText>(R.id.number1)
        val number2 = findViewById<EditText>(R.id.number2)
        val answer = findViewById<TextView>(R.id.answer)
        val calculate = findViewById<Button>(R.id.calculate)
        // listener to implement sum functionality on the button
        calculate.setOnClickListener {
            // get values from the EditText and converting them to int befor adding
            var num1 = number1.text.toString().toInt()
            var num2 = number2.text.toString().toInt()
            var sum = num1 + num2

            //seting the answer TextView text property to the result gotten
            answer.text = sum.toString()

            //clearing the EditText after operations
            number1.setText("")
            number2.setText("")
        }
    }
}


