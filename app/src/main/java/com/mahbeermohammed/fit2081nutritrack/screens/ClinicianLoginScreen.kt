package com.mahbeermohammed.fit2081nutritrack.screens

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController

@Composable
fun ClinicianLoginScreen(navController: NavHostController) {
    val context = LocalContext.current
    var key by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Clinician Login",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 32.dp)
        )

        OutlinedTextField(
            value = key,
            onValueChange = { key = it },
            label = { Text("Enter Clinician Key") },
            singleLine = true,
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                if (key == "yourSecretKey") {
                    Toast.makeText(context, "Access Granted", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, "Invalid Key", Toast.LENGTH_SHORT).show()
                }
            },
            modifier = Modifier.fillMaxWidth(),
        ) {
            Icon(Icons.Default.ExitToApp, contentDescription = "Login")
            Spacer(modifier = Modifier.width(8.dp))
            Text("Clinician Login")
        }
    }
}
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ClinicianLoginScreenPreview() {
    val navController = rememberNavController()
    MaterialTheme {
        ClinicianLoginScreen(navController = navController)
    }
}
