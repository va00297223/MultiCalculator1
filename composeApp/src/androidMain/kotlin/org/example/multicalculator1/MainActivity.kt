package org.example.multicalculator1

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.auth.FirebaseAuth
import okhttp3.*
import java.io.IOException
import org.example.multicalculator1.BasicCalculator
import org.example.multicalculator1.ConversionCalculator
import org.example.multicalculator1.FinancialCalculator
import org.example.multicalculator1.ScientificCalculator

class MainActivity : ComponentActivity() {
    private lateinit var auth: FirebaseAuth
    private val client = OkHttpClient()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance()

        setContent {
            MultiCalculatorApp()
        }
    }

    fun signInAnonymously() {
        auth.signInAnonymously()
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    val user = auth.currentUser
                    // TODO: Handle signed-in user
                } else {
                    // If sign in fails, display a message to the user.
                    // TODO: Handle sign-in failure
                }
            }
    }

    fun callLambdaFunction(input: String) {
        val url = "YOUR_AWS_LAMBDA_ENDPOINT_URL"
        val json = """{"input": "$input"}"""
        val body = RequestBody.create(MediaType.parse("application/json"), json)
        val request = Request.Builder()
            .url(url)
            .post(body)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onResponse(call: Call, response: Response) {
                val responseData = response.body()?.string()
                // Handle Lambda response data
            }

            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
                // Handle failure
            }
        })
    }
}

@Composable
fun MultiCalculatorApp() {
    var currentCalculator by remember { mutableStateOf("Basic") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Multi Calculator") }
            )
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            when (currentCalculator) {
                "Basic" -> {
                    BasicCalculator()
                }
                "Science" -> {
                    ScientificCalculator()
                }
                "Money" -> {
                    FinancialCalculator()
                }
                "Conversion" -> {
                    ConversionCalculator()
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                CalculatorTabButton("Basic") { currentCalculator = "Basic" }
                CalculatorTabButton("Science") { currentCalculator = "Science" }
                CalculatorTabButton("Money") { currentCalculator = "Money" }
                CalculatorTabButton("Conversion") { currentCalculator = "Conversion" }
            }
        }
    }
}

@Composable
fun CalculatorTabButton(text: String, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .weight(1f)
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Text(text = text, fontSize = 18.sp)
    }
}
