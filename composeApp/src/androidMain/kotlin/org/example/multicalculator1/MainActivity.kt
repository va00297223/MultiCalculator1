package org.example.multicalculator1

import App
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            CalcView()
        }
    }
}

@Composable
fun CalcView() {
    val displayText = remember { mutableStateOf("0") }
    var leftNumber by rememberSaveable { mutableStateOf(0) }
    var rightNumber by rememberSaveable { mutableStateOf(0) }
    var operation by rememberSaveable { mutableStateOf("") }
    var complete by rememberSaveable { mutableStateOf(false) }

    if (complete && operation.isNotEmpty()) {
        var answer = 0

        when (operation) {
            "+" -> answer = leftNumber + rightNumber
            "-" -> answer = leftNumber - rightNumber
            "*" -> answer = leftNumber * rightNumber
            "/" -> {
                if (rightNumber != 0) {
                    answer = leftNumber / rightNumber
                } else {
                }
            }
        }
        displayText.value = answer.toString()

    }else if (operation.isNotEmpty() && !complete) {
        displayText.value = rightNumber.toString()
    }else {
        displayText.value = leftNumber.toString()
    }

    fun numberPress(btnNum: Int) {
        if (complete) {
            leftNumber = 0
            rightNumber = 0
            operation = ""
            complete = false
        }

        if (operation.isNotBlank() && !complete) {
            if (rightNumber == 0) {
                rightNumber = btnNum
            } else {
                rightNumber = rightNumber * 10 + btnNum
            }
        } else if (operation.isBlank() && !complete) {
            if (leftNumber == 0) {
                leftNumber = btnNum
            } else {
                leftNumber = leftNumber * 10 + btnNum
            }
        }
    }

    fun operationPress(op: String) {
        if (!complete) {
            operation = op
        }
    }

    fun equalsPress() {
        complete = true
    }

    Column(modifier = Modifier
        .background(Color.LightGray)
        .fillMaxSize()
        .height(350.dp)){
        Row {
            CalcDisplay(display = displayText)
        }
        Row(modifier = Modifier.fillMaxWidth()
            .padding(horizontal = 5.dp)) {
            Column {
                for (i in 7 downTo 1 step 3) {
                    CalcRow(onPress = { numberPress(it) }, startNum = i, numButtons = 3)
                }
                Row {
                    CalcNumericButton(onPress = { numberPress(it) }, number = 0)
                    CalcEqualsButton(onPress = { equalsPress() })
                    CalcClearButton(onPress = { numberPress(0) })
                }
            }
            Column {
                val operations = listOf("+", "-", "*", "/")
                for (operation in operations) {
                    CalcOperationButton(onPress = { operationPress(it) }, operation = operation)
                }
            }
        }
    }
}

@Composable
fun CalcRow(onPress: (Int) -> Unit, startNum: Int, numButtons: Int) {
    val endNum = startNum + numButtons
    Row(modifier = Modifier
        .padding(0.dp)) {
        for (i in startNum until endNum) {
            CalcNumericButton(onPress = onPress,number = i)
        }
    }
}

@Composable
fun CalcDisplay(display: MutableState<String>) {
    Text(
        text = display.value,
        modifier = Modifier
            .height(100.dp)
            .padding(10.dp)
            .fillMaxWidth(),
        fontSize = 45.sp
    )
}

@Composable
fun CalcNumericButton(onPress: (Int) -> Unit, number: Int) {
    Button(
        onClick = { onPress(number) },
        modifier = Modifier
            .padding(5.dp)
            .size(85.dp)
    ) {
        Text(text = number.toString(), fontSize = 35.sp)
    }
}

@Composable
fun CalcOperationButton(onPress: (String) -> Unit, operation: String) {
    Button(
        /* logic will be added here when button is pressed*/
        onClick = { onPress(operation) },
        modifier = Modifier
            .padding(5.dp)
            .size(85.dp)
    ) {
        Text(text = operation, fontSize = 35.sp)
    }
}

@Composable
fun CalcEqualsButton(onPress: ()-> Unit) {
    Button(
        onClick = { onPress() },
        modifier = Modifier.padding(5.dp)
            .size(85.dp)
    ) {
        Text(text = "=", fontSize = 35.sp)
    }
}

@Composable
fun CalcClearButton(onPress: ()-> Unit) {
    Button(
        onClick = { onPress() },
        modifier = Modifier.padding(5.dp)
            .size(85.dp)
    ) {
        Text(text = "C", fontSize = 35.sp)
    }
}