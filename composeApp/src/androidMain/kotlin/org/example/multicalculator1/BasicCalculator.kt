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
    var leftNumber by remember { mutableStateOf(0) }
    var rightNumber by remember { mutableStateOf(0) }
    var operation by remember { mutableStateOf("") }
    var complete by remember { mutableStateOf(false) }

    if (complete && operation.isNotEmpty()) {
        var answer = 0

        when (operation) {
            "+" -> answer = leftNumber + rightNumber
            "-" -> answer = leftNumber - rightNumber
            "*" -> answer = leftNumber * rightNumber
            "/" -> {
                if (rightNumber != 0) {
                    answer = leftNumber / rightNumber
                }
            }
        }
        displayText = answer.toString()
    } else if (operation.isNotEmpty() && !complete) {
        displayText = rightNumber.toString()
    } else {
        displayText = leftNumber.toString()
    }

    Column(modifier = Modifier
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
        for (i in listOf(7, 4, 1, 0)) {
            Row {
                for (j in 0..2) {
                    val num = i + j
                    if (num in 0..9) {
                        Button(onClick = {
                            if (complete) {
                                leftNumber = 0
                                rightNumber = 0
                                operation = ""
                                complete = false
                            }
                            if (operation.isNotBlank() && !complete) {
                                if (rightNumber == 0) {
                                    rightNumber = num
                                } else {
                                    rightNumber = rightNumber * 10 + num
                                }
                            } else if (operation.isBlank() && !complete) {
                                if (leftNumber == 0) {
                                    leftNumber = num
                                } else {
                                    leftNumber = leftNumber * 10 + num
                                }
                            }
                        }, modifier = Modifier
                            .padding(8.dp)
                            .weight(1f)
                            .height(80.dp)) {
                            Text(text = num.toString(), fontSize = 32.sp)
                        }
                    }
                }
            }
        }
        Row {
            Button(onClick = {
                operation = "+"
            }, modifier = Modifier
                .padding(8.dp)
                .weight(1f)
                .height(80.dp)) {
                Text(text = "+", fontSize = 32.sp)
            }
            Button(onClick = {
                complete = true
            }, modifier = Modifier
                .padding(8.dp)
                .weight(1f)
                .height(80.dp)) {
                Text(text = "=", fontSize = 32.sp)
            }
        }
    }
}