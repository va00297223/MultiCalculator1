package org.example.multicalculator1

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.amazonaws.auth.CognitoCachingCredentialsProvider
import com.amazonaws.mobileconnectors.lambdainvoker.LambdaInvokerFactory
import com.amazonaws.regions.Regions
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth

class MainActivity : ComponentActivity() {

    private lateinit var lambdaInvokerFactory: LambdaInvokerFactory
    private lateinit var auth: FirebaseAuth
    private var displayText by mutableStateOf("0")
    private var inputValue by mutableStateOf("")
    private var selectedCalculator by mutableStateOf("")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize Firebase
        FirebaseApp.initializeApp(this)

        // Get an instance of FirebaseAuth
        auth = FirebaseAuth.getInstance()

        // Initialize AWS Lambda invoker
        val credentialsProvider = CognitoCachingCredentialsProvider(
            applicationContext,
            "us-east-2_GhcdFsxLo",  // Replace with your Cognito Identity Pool ID
            Regions.US_EAST_1  // Replace with your AWS region
        )
        lambdaInvokerFactory = LambdaInvokerFactory.builder()
            .context(applicationContext)
            .region(Regions.US_EAST_1)  // Replace with your AWS region
            .credentialsProvider(credentialsProvider)
            .build()

        setContent {
            MaterialTheme {
                Surface(color = Color.White) {
                    MainScreen()
                }
            }
        }
    }

    @Composable
    fun MainScreen() {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .background(Color.LightGray)
        ) {
            // Display text or result area
            Text(
                text = displayText,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 32.dp),
                fontSize = 32.sp
            )
            Spacer(modifier = Modifier.height(16.dp))

            // Input field
            TextField(
                value = inputValue,
                onValueChange = { inputValue = it },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Calculator selection buttons
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                CalculatorButton("Basic Calculator") {
                    selectedCalculator = "BasicCalculator"
                }
                // Add buttons for other calculators if needed
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Button for invoking Lambda function
            Button(onClick = {
                val result = performLambdaInvocation(inputValue)
                displayText = "Lambda Result: ${result.output}"
            }) {
                Text("Invoke Lambda")
            }

            // Button for signing in with Firebase
            Button(onClick = {
                signIn("user@example.com", "password")  // Replace with actual email and password
            }) {
                Text("Sign In with Firebase")
            }

            // Navigate to selected calculator screen
            if (selectedCalculator.isNotBlank()) {
                when (selectedCalculator) {
                    "BasicCalculator" -> BasicCalculator()
                    // Add cases for other calculators if needed
                }
            }
        }
    }

    @Composable
    fun CalculatorButton(text: String, onClick: () -> Unit) {
        Button(onClick = onClick) {
            Text(text)
        }
    }

    private fun performLambdaInvocation(inputValue: String): LambdaResponse {
        try {
            // Example: AWS Lambda invocation
            val lambdaInterface = lambdaInvokerFactory.build(LambdaInterface::class.java)

            // Example request
            val request = LambdaRequest()
            request.input = inputValue

            // Example invocation
            return lambdaInterface.invokeLambdaFunction(request)
        } catch (e: Exception) {
            e.printStackTrace()
            // Handle the error gracefully, perhaps return an error response or show a message
            return LambdaResponse(output = "Error: ${e.message}")
        }
    }

    private fun signIn(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success
                    val user = auth.currentUser
                    // ... do something with the user ...
                } else {
                    // Sign in failed
                    // ... handle the error ...
                }
            }
    }
}

// Define Lambda request and response models
data class LambdaRequest(var input: String? = null)
data class LambdaResponse(var output: String? = null)

// Define Lambda interface for invoking functions
interface LambdaInterface {
    fun invokeLambdaFunction(request: LambdaRequest): LambdaResponse
}