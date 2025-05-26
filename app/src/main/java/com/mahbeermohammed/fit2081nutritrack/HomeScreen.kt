package com.mahbeermohammed.fit2081nutritrack

import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.mahbeermohammed.fit2081nutritrack.components.BottomNavigationBar

@Composable
fun HomeScreen(navController: NavController) {
    val context = LocalContext.current
    val sharedPrefs = context.getSharedPreferences("NutriPrefs", Context.MODE_PRIVATE)

    val userId = remember { sharedPrefs.getString("userId", "Unknown") ?: "Unknown" }
    val gender = remember { sharedPrefs.getString("gender", "Male") ?: "Male" }

    val users = remember { loadUsersFromCSV(context) }
    val user = users.find { it.userId == userId }

    val score = if (user != null) {
        if (gender.lowercase() == "female") user.totalScoreFemale else user.totalScoreMale
    } else {
        0.0
    }

    Scaffold(
        bottomBar = {
            BottomNavigationBar(navController = navController)
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
                .padding(paddingValues),
            verticalArrangement = Arrangement.Top
        ) {
            Text("Hello, User $userId", fontSize = 24.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(16.dp))

            Text("Your Food Quality Score:")
            Text(
                text = "${score.toInt()} / 100",
                fontSize = 36.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text("This score reflects how well your diet aligns with the Australian Dietary Guidelines.")

            Spacer(modifier = Modifier.height(24.dp))

            Button(onClick = {
                navController.navigate("insights")
            }, modifier = Modifier.fillMaxWidth()) {
                Text("View Insights")
            }

            Spacer(modifier = Modifier.height(12.dp))

            Button(onClick = {
                navController.navigate("questionnaire")
            }, modifier = Modifier.fillMaxWidth()) {
                Text("Edit Questionnaire")
            }
        }
    }
}
