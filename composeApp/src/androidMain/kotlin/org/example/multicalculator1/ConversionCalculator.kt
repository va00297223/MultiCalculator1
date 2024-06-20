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

@Composable
fun ConversionCalculator() {
    var displayText by remember { mutableStateOf("0") }
    var inputValue by remember { mutableStateOf("") }
    var result by remember { mutableStateOf("") }

    fun convertLength() {
        val value = inputValue.toDoubleOrNull() ?: return
        val resultInMeters = value * 0.3048 // Example: converting feet to meters
        result = "Length: $resultInMeters meters"
        displayText = result
    }

    fun convertWeight() {
        val value = inputValue.toDoubleOrNull() ?: return
        val resultInKilograms = value * 0.453592 // Example: converting pounds to kilograms
        result = "Weight: $resultInKilograms kg"
        displayText = result
    }

    fun convertTemperature() {
        val value = inputValue.toDoubleOrNull() ?: return
        val resultInCelsius = (value - 32) * 5 / 9 // Example: converting Fahrenheit to Celsius
        result = "Temperature: $resultInCelsius °C"
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
        ConversionInputField("Value", inputValue) { inputValue = it }
        Spacer(modifier = Modifier.height(16.dp))
        Column(modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            ConversionButton("Convert Length") { convertLength() }
            ConversionButton("Convert Weight") { convertWeight() }
            ConversionButton("Convert Temperature") { convertTemperature() }
        }
    }
}

@Composable
fun ConversionInputField(label: String, value: String, onValueChange: (String) -> Unit) {
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
fun ConversionButton(text: String, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp)
    ) {
        Text(text = text, fontSize = 18.sp)
    }
}