package com.mahbeermohammed.fit2081nutritrack.screens

import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.mahbeermohammed.fit2081nutritrack.AppDatabase
import com.mahbeermohammed.fit2081nutritrack.utils.PasswordUtils
import kotlinx.coroutines.launch

@Composable
fun LoginScreen(navController: NavHostController) {
    var userId by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var error by remember { mutableStateOf(false) }

    val context = LocalContext.current
    val db = AppDatabase.getDatabase(context)
    val patientDao = db.patientDao()
    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp, Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Log in", fontSize = 24.sp, fontWeight = FontWeight.Bold)

        OutlinedTextField(
            value = userId,
            onValueChange = { userId = it },
            label = { Text("User ID") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            modifier = Modifier.fillMaxWidth()
        )

        if (error) {
            Text("Invalid credentials", color = MaterialTheme.colorScheme.error)
        }

        Button(
            onClick = {
                scope.launch {
                    val patient = patientDao.getPatientById(userId)
                    val hashedInput = PasswordUtils.hashPassword(password)

                    if (patient != null && patient.password == hashedInput) {
                        val prefs = context.getSharedPreferences("NutriPrefs", Context.MODE_PRIVATE)
                        prefs.edit()
                            .putString("userId", patient.userId)
                            .putString("name", patient.name)
                            .putString("phoneNumber", patient.phoneNumber)
                            .apply()
                        navController.navigate("home")
                    } else {
                        error = true
                    }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
        ) {
            Text("Continue")
        }

        Button(
            onClick = { navController.navigate("register") },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
        ) {
            Text("Register")
        }

        Text(
            text = "This app is only for pre-registered users.\nPlease enter your ID and password or Register to claim your account.",
            fontSize = 14.sp
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewLoginScreen() {
    LoginScreen(navController = rememberNavController())
}
