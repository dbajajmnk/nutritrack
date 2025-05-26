package com.mahbeermohammed.fit2081nutritrack

import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.mahbeermohammed.fit2081nutritrack.AppDatabase
import com.mahbeermohammed.fit2081nutritrack.FoodIntake
import kotlinx.coroutines.launch

@Composable
fun InsightsScreen(navController: NavController) {
    val context = LocalContext.current
    val db = AppDatabase.getDatabase(context)
    val dao = db.foodIntakeDao()
    val prefs = context.getSharedPreferences("NutriPrefs", Context.MODE_PRIVATE)
    val userId = prefs.getString("userId", null)

    var foodIntake by remember { mutableStateOf<FoodIntake?>(null) }
    val scope = rememberCoroutineScope()

    LaunchedEffect(userId) {
        if (userId != null) {
            foodIntake = dao.getLatestFoodIntakeForUser(userId)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            "Insights: Food Score",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )

        if (foodIntake != null) {
            val fruitScore = foodIntake!!.fruit.toDouble().coerceAtMost(10.0) * 10
            val vegScore = foodIntake!!.vegetables.toDouble().coerceAtMost(10.0) * 10
            val grainScore = foodIntake!!.grains.toDouble().coerceAtMost(10.0) * 10

            InsightBar(label = "Fruits", score = fruitScore)
            InsightBar(label = "Vegetables", score = vegScore)
            InsightBar(label = "Grains & Cereals", score = grainScore)

            Spacer(modifier = Modifier.height(16.dp))

            val totalQualityScore = ((fruitScore + vegScore + grainScore) / 3).coerceAtMost(100.0)

            Text(
                "Total Food Quality Score: ${totalQualityScore.toInt()} / 100",
                fontWeight = FontWeight.SemiBold,
                fontSize = 20.sp,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
        } else {
            Text(
                "No user data available.",
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp, Alignment.CenterHorizontally)
        ) {
            Button(
                onClick = { /* TODO: implement share logic */ },
                modifier = Modifier.weight(1f)
            ) {
                Icon(Icons.Default.Share, contentDescription = "Share")
                Spacer(modifier = Modifier.width(8.dp))
                Text("Share with someone")
            }
            Button(
                onClick = { navController.navigate("nutricoach") },
                modifier = Modifier.weight(1f)
            ) {
                Text("Improve my diet")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

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

@Preview(showBackground = true)
@Composable
fun InsightsScreenPreview() {
    val fakeNavController = rememberNavController()
    InsightsScreen(navController = fakeNavController)
}
