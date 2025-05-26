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
import com.mahbeermohammed.fit2081nutritrack.Patient
import com.mahbeermohammed.fit2081nutritrack.utils.PasswordUtils
import kotlinx.coroutines.launch

@Composable
fun RegisterScreen(navController: NavHostController) {
    var name by remember { mutableStateOf("") }
    var userId by remember { mutableStateOf("") }
    var phoneNumber by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var errorText by remember { mutableStateOf("") }

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
        Text("Register", fontSize = 24.sp, fontWeight = FontWeight.Bold)

        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Name") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = userId,
            onValueChange = { userId = it },
            label = { Text("User ID") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = phoneNumber,
            onValueChange = { phoneNumber = it },
            label = { Text("Phone Number") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = confirmPassword,
            onValueChange = { confirmPassword = it },
            label = { Text("Confirm Password") },
            modifier = Modifier.fillMaxWidth()
        )

        if (errorText.isNotEmpty()) {
            Text(errorText, color = MaterialTheme.colorScheme.error)
        }

        Button(
            onClick = {
                scope.launch {
                    if (name.isBlank() || userId.isBlank() || phoneNumber.isBlank() || password.isBlank() || confirmPassword.isBlank()) {
                        errorText = "All fields are required"
                        return@launch
                    }

                    if (password != confirmPassword) {
                        errorText = "Passwords do not match"
                        return@launch
                    }

                    val existingPatient = patientDao.getPatientById(userId)
                    if (existingPatient == null) {
                        val newPatient = Patient(
                            userId = userId,
                            phoneNumber = phoneNumber,
                            password = PasswordUtils.hashPassword(password),
                            name = name,
                            sex = "Unspecified"
                        )
                        patientDao.insert(newPatient)

                        val prefs = context.getSharedPreferences("NutriPrefs", Context.MODE_PRIVATE)
                        prefs.edit()
                            .putString("userId", userId)
                            .putString("name", name)
                            .putString("phoneNumber", phoneNumber)
                            .apply()

                        navController.navigate("home")
                    } else {
                        errorText = "User already exists"
                    }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
        ) {
            Text("Register")
        }

        Button(
            onClick = { navController.popBackStack() },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
        ) {
            Text("Log in")
        }

        Text(
            text = "This app is only for pre-registered users.\nPlease enter your ID and password or Register to claim your account.",
            fontSize = 14.sp
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewRegisterScreen() {
    RegisterScreen(navController = rememberNavController())
}
