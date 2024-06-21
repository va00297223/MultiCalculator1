package org.example.multicalculator1

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
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
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class MainActivity : ComponentActivity() {

    private lateinit var lambdaInvokerFactory: LambdaInvokerFactory
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize Firebase
        FirebaseApp.initializeApp(this)

        // Get an instance of FirebaseAuth
        auth = FirebaseAuth.getInstance()

        // Initialize AWS Lambda invoker (if needed)
        // Replace this with your AWS Lambda initialization code if still using it

        // Set content using Jetpack Navigation and Firebase Authentication
        setContent {
            MultiCalculatorApp()
        }
    }

    @Composable
    fun MultiCalculatorApp() {
        val navController = rememberNavController()

        NavHost(navController = navController, startDestination = "main") {
            composable("main") { MainScreen(navController = navController) }
            composable("calculatorSelection") { CalculatorSelectionScreen(navController = navController) }
            // Add more destinations as needed for other calculators or screens
        }
    }

    @Composable
    fun MainScreen(navController: NavHostController) {
        var email by remember { mutableStateOf("") }
        var password by remember { mutableStateOf("") }
        var displayText by remember { mutableStateOf("0") }
        var inputValue by remember { mutableStateOf("") }
        var loggedIn by remember { mutableStateOf(false) }

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

            if (loggedIn) {
                // User is logged in, display logout button
                Button(onClick = {
                    auth.signOut()
                    loggedIn = false
                    navController.navigate("main")
                }) {
                    Text("Logout")
                }
            } else {
                // User is not logged in, display login fields and button
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
                    signIn(email, password, navController) { success, errorMessage ->
                        if (success) {
                            loggedIn = true
                        } else {
                            Toast.makeText(this@MainActivity, "Sign in failed: $errorMessage", Toast.LENGTH_SHORT).show()
                        }
                    }
                }) {
                    Text("Sign In")
                }
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

            // Input field
            TextField(
                value = inputValue,
                onValueChange = { inputValue = it },
                modifier = Modifier.fillMaxWidth()
            )
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

    private fun signIn(email: String, password: String, navController: NavHostController, onResult: (Boolean, String?) -> Unit) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Log.d("MainActivity", "Sign in success")
                    // Sign in success, navigate to the Calculator Selection screen
                    val user = auth.currentUser
                    user?.let {
                        saveUserData(it)
                    }
                    navController.navigate("calculatorSelection")
                    onResult(true, null)
                } else {
                    Log.e("MainActivity", "Sign in failed", task.exception)
                    // Handle sign-in failure
                    onResult(false, task.exception?.localizedMessage)
                }
            }
    }

    private fun saveUserData(user: FirebaseUser) {
        val database = Firebase.database
        val usersRef = database.getReference("users")
        val userId = user.uid
        val userData = User(user.email ?: "", "John Doe") // Example data, replace with actual user data
        usersRef.child(userId).setValue(userData)
    }

    @Composable
    fun CalculatorSelectionScreen(navController: NavHostController) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .background(Color.LightGray),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Select Calculator",
                modifier = Modifier.padding(vertical = 32.dp),
                fontSize = 32.sp
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(onClick = {
                navController.navigate("basicCalculator")
            }) {
                Text("Basic Calculator")
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Add buttons for other calculators if needed
            // Button(onClick = { /* Navigate to another calculator */ }) {
            //     Text("Advanced Calculator")
            // }
        }
    }
}

// Define User data class
data class User(
    val email: String,
    val displayName: String
)

// Define Lambda request and response models
data class LambdaRequest(var input: String? = null)
data class LambdaResponse(var output: String? = null)

// Define Lambda interface for invoking functions
interface LambdaInterface {
    fun invokeLambdaFunction(request: LambdaRequest): LambdaResponse
}