package com.mahbeermohammed.fit2081nutritrack.screens

import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.mahbeermohammed.fit2081nutritrack.AppDatabase
import kotlinx.coroutines.launch

@Composable
fun LoginScreen(navController: NavHostController) {
    val context = LocalContext.current
    val db = AppDatabase.getDatabase(context)
    val dao = db.patientDao()

    var userId by remember { mutableStateOf("") }
    var phoneNumber by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var hasPassword by remember { mutableStateOf<Boolean?>(null) }
    var error by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    // Fetch user info when userId changes
    LaunchedEffect(userId) {
        if (userId.isNotBlank()) {
            val user = dao.getPatientById(userId)
            hasPassword = user?.password?.isNotBlank() == true
        } else {
            hasPassword = null
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Login", fontSize = 24.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = userId,
            onValueChange = { userId = it },
            label = { Text("User ID") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        if (hasPassword == true) {
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password") },
                modifier = Modifier.fillMaxWidth()
            )
        } else if (hasPassword == false) {
            OutlinedTextField(
                value = phoneNumber,
                onValueChange = { phoneNumber = it },
                label = { Text("Phone Number") },
                modifier = Modifier.fillMaxWidth()
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (error) {
            Text("Invalid credentials", color = MaterialTheme.colorScheme.error)
        }

        Button(
            onClick = {
                scope.launch {
                    val user = dao.getPatientById(userId)
                    if (user != null) {
                        val loginSuccess =
                            if (hasPassword == true) user.password == password
                            else user.phoneNumber == phoneNumber

                        if (loginSuccess) {
                            val prefs = context.getSharedPreferences("NutriPrefs", Context.MODE_PRIVATE)
                            prefs.edit().apply {
                                putString("userId", user.userId)
                                putString("gender", user.sex)
                                apply()
                            }

                            error = false
                            if (hasPassword == true) {
                                navController.navigate("questionnaire")
                            } else {
                                navController.navigate("claim_account")
                            }
                        } else {
                            error = true
                        }
                    } else {
                        error = true
                    }
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Continue")
        }
    }
}