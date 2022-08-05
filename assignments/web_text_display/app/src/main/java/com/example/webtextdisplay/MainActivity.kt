package com.example.webtextdisplay

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.util.*
import io.ktor.util.reflect.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        //creating a coroutine to run the  getMessage function since it is a suspend function
        //We use the dispatcher main to return the result of the coroutine to the main thread
        GlobalScope.launch(Dispatchers.Main){
            //calling the getMessage function
            val labelText = async { getMessage("http://206.189.207.206/test/hello.php") }

            //selecting the text view by id
            val myTextView:TextView = findViewById(R.id.label1)
            //setting the text property to the response from server
            myTextView.text = labelText.await()
        }

    }

    suspend fun getMessage(linkUrl: String): String {
        // create an instance of a client
        val client = HttpClient(CIO)

        //get data from an endpoint and store it
        val result: HttpResponse = client.get(linkUrl)
        val responseBody :String = result.body()
        // closing to release held up resources
        client.close()

        //return the http responseBody as a string
        return responseBody
    }
}