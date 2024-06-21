package org.example.multicalculator1

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.navigation.NavHostController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class AuthViewModel : ViewModel() {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    fun signIn(email: String, password: String, navController: NavHostController, onResult: (Boolean, String?) -> Unit) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d("AuthViewModel", "Sign in success")
                    navController.navigate("calculatorSelection")
                    onResult(true, null)
                } else {
                    Log.e("AuthViewModel", "Sign in failed", task.exception)
                    onResult(false, task.exception?.localizedMessage)
                }
            }
    }

    fun signUp(email: String, password: String, displayName: String, navController: NavHostController) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d("AuthViewModel", "Sign up success")
                    val user = auth.currentUser
                    user?.let {
                        val profileUpdates = userProfileChangeRequest {
                            this.displayName = displayName
                        }
                        it.updateProfile(profileUpdates).addOnCompleteListener { profileTask ->
                            if (profileTask.isSuccessful) {
                                saveUserData(it)
                                navController.navigate("calculatorSelection")
                            } else {
                                Log.e("AuthViewModel", "Profile update failed", profileTask.exception)
                            }
                        }
                    }
                } else {
                    Log.e("AuthViewModel", "Sign up failed", task.exception)
                }
            }
    }

    private fun saveUserData(user: FirebaseUser) {
        val db = Firebase.firestore // Use Firestore instead of Realtime Database
        val usersRef = db.collection("users") // Use collection for Firestore
        val userId = user.uid
        val userData = hashMapOf(
            "email" to (user.email ?: ""),
            "displayName" to (user.displayName ?: "Anonymous")
        ) // Use HashMap for Firestore
        usersRef.document(userId).set(userData) // Use set for Firestore
    }

    fun signOut() {
        auth.signOut()
    }
}
