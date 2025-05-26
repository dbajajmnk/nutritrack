package com.mahbeermohammed.fit2081nutritrack

import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@Composable
fun InsightsScreen(navController: NavController) {
    val context = LocalContext.current
    val prefs = context.getSharedPreferences("NutriPrefs", Context.MODE_PRIVATE)

    val userId = prefs.getString("userId", null)
    val gender = prefs.getString("gender", "Male") ?: "Male"

    val users = remember { loadUsersFromCSV(context) }
    val user = users.find { it.userId == userId }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Dietary Insights", fontSize = 24.sp)

        if (user != null) {
            val fruitScore = if (gender == "Female")  user.totalScoreFemale else user.totalScoreMale
            val vegScore = (fruitScore * 0.7).coerceAtMost(10.0)
            val grainScore = (fruitScore * 0.6).coerceAtMost(10.0)

            InsightBar(label = "Fruits", score = fruitScore)
            InsightBar(label = "Vegetables", score = vegScore)
            InsightBar(label = "Grains & Cereals", score = grainScore)
        } else {
            Text("No user data available.")
        }

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = { navController.navigate("home") },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Back to Home")
        }
    }
}

@Composable
fun InsightBar(label: String, score: Double) {
    Column {
        Text("$label: ${score.toInt()} / 100")
        LinearProgressIndicator(
            progress = (score / 100).toFloat(),
            modifier = Modifier
                .fillMaxWidth()
                .height(16.dp)
        )
    }
}
