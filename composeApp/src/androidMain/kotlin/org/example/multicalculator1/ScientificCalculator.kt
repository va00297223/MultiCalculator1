package org.example.multicalculator1

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.*

@Composable
fun ScientificCalculator() {
    var displayText by remember { mutableStateOf("0") }
    var inputValue by remember { mutableStateOf("") }
    var result by remember { mutableStateOf("") }

    fun performOperation(operation: String) {
        val value = inputValue.toDoubleOrNull() ?: return
        val calculationResult = when (operation) {
            "sin" -> sin(value)
            "cos" -> cos(value)
            "tan" -> tan(value)
            "log" -> log10(value)
            "sqrt" -> sqrt(value)
            else -> 0.0
        }
        result = "$operation($value) = $calculationResult"
        displayText = result
    }

    Column(
        modifier = Modifier
            .background(Color.LightGray)
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = displayText,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 32.dp),
            fontSize = 32.sp
        )
        Spacer(modifier = Modifier.height(16.dp))
        ScientificInputField("Value", inputValue) { inputValue = it }
        Spacer(modifier = Modifier.height(16.dp))
        Column(modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            ScientificButton("sin") { performOperation("sin") }
            ScientificButton("cos") { performOperation("cos") }
            ScientificButton("tan") { performOperation("tan") }
            ScientificButton("log") { performOperation("log") }
            ScientificButton("sqrt") { performOperation("sqrt") }
        }
    }
}

@Composable
fun ScientificInputField(label: String, value: String, onValueChange: (String) -> Unit) {
    Column {
        Text(text = label, fontSize = 20.sp)
        TextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
fun ScientificButton(text: String, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp)
    ) {
        Text(text = text, fontSize = 18.sp)
    }
}