package org.example.multicalculator1

import android.app.Application
import android.content.Context
import com.amazonaws.auth.CognitoCachingCredentialsProvider
import com.amazonaws.regions.Regions
import com.amazonaws.services.lambda.AWSLambda
import com.amazonaws.services.lambda.AWSLambdaClient
import com.amazonaws.services.lambda.model.InvokeRequest
import com.amazonaws.services.lambda.model.InvokeResult
import java.nio.ByteBuffer

object LambdaService {

    private var context: Context? = null

    fun initialize(application: Application) {
        context = application.applicationContext
    }

    private val credentialsProvider: CognitoCachingCredentialsProvider by lazy {
        if (context == null) {
            throw IllegalStateException("Context not initialized")
        }
        CognitoCachingCredentialsProvider(
            context,
            "us-east-2_GhcdFsxLo",
            Regions.US_EAST_2
        )
    }

    private val lambdaClient: AWSLambda by lazy {
        AWSLambdaClient.builder()
            .credentialsProvider(credentialsProvider)
            .region(Regions.US_EAST_2)
            .build()
    }

    fun invokeLambdaFunction(functionName: String, payload: String): InvokeResult {
        val request = InvokeRequest()
            .withFunctionName(functionName)
            .withPayload(ByteBuffer.wrap(payload.toByteArray()))
        return lambdaClient.invoke(request)
    }
}
