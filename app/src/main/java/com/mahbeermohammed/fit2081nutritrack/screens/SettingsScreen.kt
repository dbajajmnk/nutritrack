package com.mahbeermohammed.fit2081nutritrack.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController

@Composable
fun SettingsScreen(navController: NavHostController) {
    val context = LocalContext.current
    val sharedPrefs = context.getSharedPreferences("NutriPrefs", 0)
    val userName = sharedPrefs.getString("name", "Unknown User") ?: "Unknown User"
    val phoneNumber = sharedPrefs.getString("phoneNumber", "Unknown Phone") ?: "Unknown Phone"
    val userId = sharedPrefs.getString("userId", "Unknown ID") ?: "Unknown ID"

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {
        Text(
            text = "Settings",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )

        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = "Account",
            fontWeight = FontWeight.SemiBold,
            fontSize = 20.sp,
            modifier = Modifier.padding(bottom = 12.dp)
        )

        UserDetailRow(icon = Icons.Default.Person, label = "Name", value = userName)
        UserDetailRow(icon = Icons.Default.Phone, label = "Phone", value = phoneNumber)
        UserDetailRow(icon = Icons.Default.AccountBox, label = "ID", value = userId)

        Spacer(modifier = Modifier.height(24.dp))

        Divider()

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Other settings",
            fontWeight = FontWeight.SemiBold,
            fontSize = 20.sp,
            modifier = Modifier.padding(bottom = 12.dp)
        )

        SettingsOption(
            label = "Logout",
            icon = Icons.Default.ExitToApp,
            onClick = {
                // Clear saved userId to logout
                sharedPrefs.edit().remove("userId").apply()
                // Navigate to welcome/login, clear backstack
                navController.navigate("welcome") {
                    popUpTo(0) { inclusive = true }
                }
            }
        )

        SettingsOption(
            label = "Clinician Login",
            icon = Icons.Default.Person,
            onClick = {
                navController.navigate("clinicianLogin")
            }
        )
    }
}

@Composable
fun UserDetailRow(icon: ImageVector, label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            modifier = Modifier.size(28.dp)
        )
        Spacer(modifier = Modifier.width(12.dp))
        Column {
            Text(text = label, fontWeight = FontWeight.Medium, fontSize = 14.sp)
            Text(text = value, fontWeight = FontWeight.Normal, fontSize = 16.sp)
        }
    }
}

@Composable
fun SettingsOption(label: String, icon: ImageVector, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp)
            .clickable { onClick() },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            modifier = Modifier.size(28.dp)
        )
        Spacer(modifier = Modifier.width(12.dp))
        Text(
            text = label,
            fontSize = 16.sp,
            modifier = Modifier.weight(1f),
            fontWeight = FontWeight.Medium
        )
        Icon(
            imageVector = Icons.Default.KeyboardArrowRight,
            contentDescription = "Navigate",
            modifier = Modifier.size(24.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewSettingsScreen() {
    val navController = rememberNavController()
    SettingsScreen(navController)
}
