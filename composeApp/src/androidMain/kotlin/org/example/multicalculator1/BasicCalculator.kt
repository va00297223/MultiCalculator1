package org.example.multicalculator1

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun BasicCalculator() {
    var displayText by remember { mutableStateOf("0") }
    var leftNumber by remember { mutableStateOf(0.0) }
    var rightNumber by remember { mutableStateOf(0.0) }
    var operation by remember { mutableStateOf("") }
    var complete by remember { mutableStateOf(false) }
    var currentInput by remember { mutableStateOf("") }

    if (complete && operation.isNotEmpty()) {
        var answer = 0.0

        when (operation) {
            "+" -> answer = leftNumber + rightNumber
            "-" -> answer = leftNumber - rightNumber
            "*" -> answer = leftNumber * rightNumber
            "/" -> {
                if (rightNumber != 0.0) {
                    answer = leftNumber / rightNumber
                }
            }
            "%" -> {
                if (rightNumber != 0.0) {
                    answer = leftNumber % rightNumber
                }
            }
        }
        displayText = answer.toString()
    } else if (operation.isNotEmpty() && !complete) {
        displayText = "$leftNumber $operation $rightNumber"
    } else {
        displayText = currentInput.ifBlank { "0" }
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
            fontSize = 48.sp
        )

        // Updated loop to create buttons in rows with 4 buttons each
        val buttons = listOf(
            listOf("C", "⌫", "%", "/"),
            listOf("7", "8", "9", "*"),
            listOf("4", "5", "6", "-"),
            listOf("1", "2", "3", "+"),
            listOf("0", ".", "=" )
        )

        for (row in buttons) {
            Row {
                for (buttonText in row) {
                    Button(onClick = {
                        when (buttonText) {
                            "C" -> {
                                leftNumber = 0.0
                                rightNumber = 0.0
                                operation = ""
                                complete = false
                                currentInput = ""
                            }
                            in "0".."9" -> {
                                currentInput += buttonText
                                if (operation.isNotBlank() && !complete) {
                                    rightNumber = currentInput.toDoubleOrNull() ?: 0.0
                                } else if (operation.isBlank() && !complete) {
                                    leftNumber = currentInput.toDoubleOrNull() ?: 0.0
                                }
                            }
                            "." -> {
                                if (!currentInput.contains(".")) {
                                    currentInput += "."
                                }
                            }
                            "⌫" -> {
                                currentInput = if (currentInput.isNotEmpty()) {
                                    currentInput.dropLast(1)
                                } else {
                                    ""
                                }
                                if (operation.isNotBlank()) {
                                    rightNumber = currentInput.toDoubleOrNull() ?: 0.0
                                } else {
                                    leftNumber = currentInput.toDoubleOrNull() ?: 0.0
                                }
                            }
                            "+" -> {
                                operation = "+"
                                leftNumber = currentInput.toDoubleOrNull() ?: 0.0
                                currentInput = ""
                            }
                            "-" -> {
                                operation = "-"
                                leftNumber = currentInput.toDoubleOrNull() ?: 0.0
                                currentInput = ""
                            }
                            "*" -> {
                                operation = "*"
                                leftNumber = currentInput.toDoubleOrNull() ?: 0.0
                                currentInput = ""
                            }
                            "/" -> {
                                operation = "/"
                                leftNumber = currentInput.toDoubleOrNull() ?: 0.0
                                currentInput = ""
                            }
                            "%" -> {
                                operation = "%"
                                leftNumber = currentInput.toDoubleOrNull() ?: 0.0
                                currentInput = ""
                            }
                            "=" -> complete = true
                        }
                    }, modifier = Modifier
                        .padding(8.dp)
                        .weight(1f)
                        .height(65.dp)) {
                        Text(text = buttonText, fontSize = 32.sp)
                    }
                }
            }
        }
    }
}
