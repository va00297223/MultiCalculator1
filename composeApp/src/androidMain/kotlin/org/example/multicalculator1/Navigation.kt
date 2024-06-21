package org.example.multicalculator1

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.google.firebase.auth.FirebaseAuth
import androidx.navigation.compose.rememberNavController

@Composable
fun MultiCalculatorApp(auth: FirebaseAuth) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "main") {
        composable("main") { MainScreen(navController = navController, auth = auth) }
        composable("signUp") { SignUpScreen(navController = navController, auth = auth) }
        composable("calculatorSelection") { CalculatorSelectionScreen(navController = navController) }
        composable("basicCalculator") { BasicCalculator() }
        composable("conversionCalculator") { ConversionCalculator() }
        composable("financialCalculator") { FinancialCalculator() }
        composable("scientificCalculator") { ScientificCalculator() }
    }
}
