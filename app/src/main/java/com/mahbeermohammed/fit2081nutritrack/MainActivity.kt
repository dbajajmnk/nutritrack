package com.mahbeermohammed.fit2081nutritrack

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.*
import com.mahbeermohammed.fit2081nutritrack.components.BottomNavigationBar
import com.mahbeermohammed.fit2081nutritrack.screens.*
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NutriTrackApp()
        }
    }
}

@Composable
fun NutriTrackApp() {
    val navController = rememberNavController()
    val context = LocalContext.current

    // Load CSV into Room only on first app launch
    LaunchedEffect(Unit) {
        val db = AppDatabase.getDatabase(context)
        val dao = db.patientDao()
        val count = dao.getPatientCount()
        if (count == 0) {
            val patients = loadPatientsFromCSV(context)
            dao.insertAll(patients)
        }
    }

    val bottomNavRoutes = listOf("home", "insights", "nutriCoach", "settings")
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    Scaffold(
        bottomBar = {
            if (currentRoute in bottomNavRoutes) {
                BottomNavigationBar(navController = navController)
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = "welcome",
            modifier = Modifier.padding(innerPadding)
        ) {
            composable("welcome") { WelcomeScreen(navController) }
            composable("login") { LoginScreen(navController) }
            composable("questionnaire") { QuestionnaireScreen(navController) }
            composable("register") { RegisterScreen(navController) }

            // BottomNav Screens
            composable("home") { HomeScreen(navController) }
            composable("insights") { InsightsScreen(navController) }
            composable("nutriCoach") { NutriCoachScreen(navController) }
            composable("settings") { SettingsScreen(navController) }
        }
    }
}




