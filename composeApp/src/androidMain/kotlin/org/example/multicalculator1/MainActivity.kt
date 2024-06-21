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
import androidx.navigation.NavHostController
import androidx.navigation.compose.*
import com.amazonaws.auth.CognitoCachingCredentialsProvider
import com.amazonaws.mobileconnectors.lambdainvoker.LambdaInvokerFactory
import com.amazonaws.regions.Regions
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth

class MainActivity : ComponentActivity() {

    private lateinit var lambdaInvokerFactory: LambdaInvokerFactory
    private lateinit var auth: FirebaseAuth

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

        // Set content using Jetpack Navigation
        setContent {
            MultiCalculatorApp()
        }
    }

    @Composable
    fun MultiCalculatorApp() {
        // NavHost setup
        val navController = rememberNavController()

        // Navigation graph
        NavHost(navController = navController, startDestination = "main") {
            composable("main") { MainScreen(navController = navController) }
            composable("basicCalculator") { BasicCalculatorScreen() }
            // Add more destinations as needed for other calculators or screens
        }
    }

    @Composable
    fun MainScreen(navController: NavHostController) {
        var email by remember { mutableStateOf("") }
        var password by remember { mutableStateOf("") }
        var displayText by remember { mutableStateOf("0") }
        var inputValue by remember { mutableStateOf("") }

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

            // Button for opening BasicCalculator screen
            Button(onClick = {
                navController.navigate("basicCalculator")
            }) {
                Text("Open Basic Calculator")
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Button for invoking Lambda function
            Button(onClick = {
                val result = performLambdaInvocation(inputValue)
                displayText = "Lambda Result: ${result.output}"
            }) {
                Text("Invoke Lambda")
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Email input field
            TextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Password input field
            TextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Button for signing in with Firebase
            Button(onClick = {
                signIn(email, password, navController)
            }) {
                Text("Sign In with Firebase")
            }
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

    private fun signIn(email: String, password: String, navController: NavHostController) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, navigate to the BasicCalculator screen
                    navController.navigate("basicCalculator")
                } else {
                    // Sign in failed, handle the error (e.g., show a toast)
                }
            }
    }

    @Composable
    fun BasicCalculatorScreen() {
        // Placeholder for BasicCalculator UI
        BasicCalculator()
    }
}

// Define Lambda request and response models
data class LambdaRequest(var input: String? = null)
data class LambdaResponse(var output: String? = null)

// Define Lambda interface for invoking functions
interface LambdaInterface {
    fun invokeLambdaFunction(request: LambdaRequest): LambdaResponse
}
