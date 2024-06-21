package org.example.multicalculator1

import androidx.lifecycle.ViewModel
import com.amazonaws.services.lambda.model.InvokeResult

class LambdaViewModel : ViewModel() {

    fun invokeLambdaFunction(functionName: String, payload: String, onResult: (String) -> Unit) {
        // Invoke Lambda function asynchronously
        val result = LambdaService.invokeLambdaFunction(functionName, payload)

        // Process the result (assuming Lambda returns a string)
        val responsePayload = String(result.payload.array(), Charsets.UTF_8)

        // Pass the result back to UI or perform further processing
        onResult(responsePayload)
    }
}
