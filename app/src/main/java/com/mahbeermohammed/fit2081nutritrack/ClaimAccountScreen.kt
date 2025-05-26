package com.mahbeermohammed.fit2081nutritrack

import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import kotlinx.coroutines.launch


@Composable
fun ClaimAccountScreen(navController: NavController) {
    val context = LocalContext.current
    val db = AppDatabase.getDatabase(context)
    val dao = db.patientDao()

    val prefs = context.getSharedPreferences("NutriPrefs", Context.MODE_PRIVATE)
    val userId = prefs.getString("userId", null)

    if (userId == null) {
        Text("No user ID found. Please login again.")
        return
    }

    var name by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val scope = rememberCoroutineScope()
    var error by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Claim Your Account", fontSize = 24.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Your Name") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Set Password") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        if (error) {
            Text("Failed to update user info", color = MaterialTheme.colorScheme.error)
        }

        Button(
            onClick = {
                scope.launch {
                    val user = dao.getPatientById(userId)
                    if (user != null) {
                        val updatedUser = user.copy(name = name, password = password)
                        dao.insertAll(listOf(updatedUser))
                        error = false
                        navController.navigate("questionnaire")
                    } else {
                        error = true
                    }
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Continue to Questionnaire")
        }
    }
}
