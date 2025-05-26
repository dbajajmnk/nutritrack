package com.mahbeermohammed.fit2081nutritrack

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.FileProvider
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream

@Composable
fun InsightsScreen(navController: NavController) {
    val context = LocalContext.current
    val view = LocalView.current
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
                onClick = {
                    val bitmap = captureView(view)
                    bitmap?.let {
                        shareImage(context, it)
                    }
                },
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

private fun captureView(view: android.view.View): Bitmap? {
    return try {
        val bitmap = Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)
        val canvas = android.graphics.Canvas(bitmap)
        view.draw(canvas)
        bitmap
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}

private fun shareImage(context: Context, bitmap: Bitmap) {
    try {
        val cachePath = File(context.cacheDir, "images")
        cachePath.mkdirs()
        val file = File(cachePath, "shared_image.png")
        FileOutputStream(file).use { out ->
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out)
        }

        val contentUri = FileProvider.getUriForFile(
            context,
            "${context.packageName}.provider",
            file
        )

        val shareIntent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_STREAM, contentUri)
            type = "image/png"
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }

        context.startActivity(Intent.createChooser(shareIntent, "Share your insights"))
    } catch (e: Exception) {
        e.printStackTrace()
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