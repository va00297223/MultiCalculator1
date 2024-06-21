package org.example.multicalculator1

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.layout
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.google.firebase.auth.FirebaseAuth

@Composable
fun MainScreen(navController: NavHostController, auth: FirebaseAuth) {
    val viewModel: AuthViewModel = viewModel()
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var displayText by remember { mutableStateOf("0") }
    var inputValue by remember { mutableStateOf("") }
    var loggedIn by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.LightGray),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
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
            TextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            TextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password") },
                modifier = Modifier
                    .fillMaxWidth(),
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(onClick = {
                viewModel.signIn(email, password, navController) { success, errorMessage ->
                    if (success) {
                        loggedIn = true
                    } else {
                        Toast.makeText(navController.context, "Sign in failed: $errorMessage", Toast.LENGTH_SHORT).show()
                    }
                }
            }) {
                Text("Sign In")
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(onClick = {
                navController.navigate("signUp")
            }) {
                Text("Sign Up")
            }
        }

        /*Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = {
            val result = viewModel.performLambdaInvocation(inputValue)
            displayText = "Lambda Result: ${result.output}"
        }) {
            Text("Invoke Lambda")
        }

        Spacer(modifier = Modifier.height(16.dp))

        TextField(
            value = inputValue,
            onValueChange = { inputValue = it },
            modifier = Modifier.fillMaxWidth()
        )*/
    }
}

@Composable
fun SignUpScreen(navController: NavHostController, auth: FirebaseAuth) {
    val viewModel: AuthViewModel = viewModel()
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var displayName by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(5.dp)
            .background(Color.LightGray),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Sign Up",
            fontSize = 32.sp,
            modifier = Modifier.padding(vertical = 32.dp)
        )

        TextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        TextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        TextField(
            value = displayName,
            onValueChange = { displayName = it },
            label = { Text("Display Name") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = {
            viewModel.signUp(email, password, displayName, navController)
        }) {
            Text("Sign Up")
        }
    }
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

        Button(onClick = {
            navController.navigate("conversionCalculator")
        }) {
            Text("Conversion Calculator")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = {
            navController.navigate("financialCalculator")
        }) {
            Text("Financial Calculator")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = {
            navController.navigate("scientificCalculator")
        }) {
            Text("Scientific Calculator")
        }
    }
}
