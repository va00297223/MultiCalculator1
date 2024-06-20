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
fun FinancialCalculator() {
    var displayText by remember { mutableStateOf("0") }
    var principal by remember { mutableStateOf("") }
    var rate by remember { mutableStateOf("") }
    var time by remember { mutableStateOf("") }
    var result by remember { mutableStateOf("") }

    fun calculateSimpleInterest() {
        val p = principal.toDoubleOrNull() ?: return
        val r = rate.toDoubleOrNull() ?: return
        val t = time.toDoubleOrNull() ?: return
        val interest = p * r * t / 100
        result = "Simple Interest: $interest"
        displayText = result
    }

    fun calculateCompoundInterest() {
        val p = principal.toDoubleOrNull() ?: return
        val r = rate.toDoubleOrNull() ?: return
        val t = time.toDoubleOrNull() ?: return
        val amount = p * Math.pow((1 + r / 100), t)
        val interest = amount - p
        result = "Compound Interest: $interest"
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
        FinancialInputField("Principal", principal) { principal = it }
        Spacer(modifier = Modifier.height(16.dp))
        FinancialInputField("Rate (%)", rate) { rate = it }
        Spacer(modifier = Modifier.height(16.dp))
        FinancialInputField("Time (years)", time) { time = it }
        Spacer(modifier = Modifier.height(16.dp))
        Column(modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            FinancialButton("Calculate Simple Interest") { calculateSimpleInterest() }
            FinancialButton("Calculate Compound Interest") { calculateCompoundInterest() }
        }
    }
}

@Composable
fun FinancialInputField(label: String, value: String, onValueChange: (String) -> Unit) {
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
fun FinancialButton(text: String, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp)
    ) {
        Text(text = text, fontSize = 18.sp)
    }
}
