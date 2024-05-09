package com.example.assignment_1.service

import android.content.Intent
import android.util.Log
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import com.example.assignment_1.DB_USER
import com.example.assignment_1.entity.UserData
import com.example.assignment_1.tables
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.common.api.ApiException
import com.google.firebase.Firebase
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.auth
import com.google.firebase.database.Query
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class UserManagement {
    companion object{
        private var authenticateUser: UserManagement? = null
        val instance: UserManagement
            get() {
                if (authenticateUser == null) {
                    authenticateUser = UserManagement()
                }
                return authenticateUser!!
            }
    }



    fun createUser(data: UserData){
        tables
            .child(DB_USER)
            .child(System.currentTimeMillis().toString())
            .setValue(data)
    }

    fun findUserByEmail(email: String?): Query {
        return tables.child(DB_USER)
            .orderByChild("email")
            .equalTo(email)
//            .get().result.getValue(UserData::class.java)
    }

}

@Composable
fun rememberFirebaseAuthLauncher(
    onAuthComplete: (AuthResult) -> Unit,
    onAuthError: (ApiException) -> Unit
): ManagedActivityResultLauncher<Intent, ActivityResult> {
    val scope = rememberCoroutineScope()
    return rememberLauncherForActivityResult(contract = ActivityResultContracts.StartActivityForResult()) { result ->
        val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
        try {
            val account = task.getResult(ApiException::class.java)
            if (account != null) {
                val credential = GoogleAuthProvider.getCredential(account.idToken!!, null)
                scope.launch {
                    try {
                        val authResult = Firebase.auth.signInWithCredential(credential).await()
                        onAuthComplete(authResult)
                    } catch (e: Exception) {
                        // Handle authentication error
                        Log.e("Firebase Auth", "Error signing in with credential: ${e.message}")
                    }
                }
            } else {
                // Handle null account
                Log.e("Google Sign-In", "Google sign-in account is null")
            }
        } catch (e: ApiException) {
            // Handle API exception
            Log.e("Google Sign-In", "API exception: ${e.message}")
            onAuthError(e)
        }

    }
}


