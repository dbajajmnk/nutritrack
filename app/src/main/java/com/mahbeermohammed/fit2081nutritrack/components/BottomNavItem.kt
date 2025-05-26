package com.mahbeermohammed.fit2081nutritrack.components


import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomNavItem(val route: String, val icon: ImageVector, val label: String) {
    object Home : BottomNavItem("home", Icons.Filled.Home, "Home")
    object Insights : BottomNavItem("insights", Icons.Filled.Face, "Insights")
    object NutriCoach : BottomNavItem("nutriCoach", Icons.Filled.Person, "NutriCoach")
    object Settings : BottomNavItem("settings", Icons.Filled.Settings, "Settings")
}
