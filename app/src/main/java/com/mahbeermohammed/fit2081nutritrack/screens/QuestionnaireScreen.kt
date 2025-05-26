package com.mahbeermohammed.fit2081nutritrack.screens

import android.annotation.SuppressLint
import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import kotlinx.coroutines.launch
import com.mahbeermohammed.fit2081nutritrack.AppDatabase
import com.mahbeermohammed.fit2081nutritrack.FoodIntake
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown


@SuppressLint("MutableCollectionMutableState")
@Composable
fun QuestionnaireScreen(navController: NavController) {
    val context = LocalContext.current
    val db = AppDatabase.getDatabase(context)
    val dao = db.foodIntakeDao()
    val prefs = context.getSharedPreferences("NutriPrefs", Context.MODE_PRIVATE)
    val userId = prefs.getString("userId", null) ?: return
    val scope = rememberCoroutineScope()

    var persona by remember { mutableStateOf("Active Teen") }
    var breakfastTime by remember { mutableStateOf("") }
    var lunchTime by remember { mutableStateOf("") }
    var dinnerTime by remember { mutableStateOf("") }
    var fruit by remember { mutableStateOf("") }
    var vegetables by remember { mutableStateOf("") }
    var grains by remember { mutableStateOf("") }
    var meat by remember { mutableStateOf("") }
    var dairy by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalAlignment = Alignment.Start
    ) {
        Text("Questionnaire", fontSize = 24.sp, fontWeight = FontWeight.Bold)

        DropdownMenuField("Persona", listOf("Active Teen", "Adult Male", "Adult Female")) { persona = it }

        OutlinedTextField(value = breakfastTime, onValueChange = { breakfastTime = it }, label = { Text("Breakfast Time") })
        OutlinedTextField(value = lunchTime, onValueChange = { lunchTime = it }, label = { Text("Lunch Time") })
        OutlinedTextField(value = dinnerTime, onValueChange = { dinnerTime = it }, label = { Text("Dinner Time") })

        OutlinedTextField(value = fruit, onValueChange = { fruit = it }, label = { Text("Fruits (e.g. 2 servings)") })
        OutlinedTextField(value = vegetables, onValueChange = { vegetables = it }, label = { Text("Vegetables") })
        OutlinedTextField(value = grains, onValueChange = { grains = it }, label = { Text("Grains") })
        OutlinedTextField(value = meat, onValueChange = { meat = it }, label = { Text("Meat/Alternatives") })
        OutlinedTextField(value = dairy, onValueChange = { dairy = it }, label = { Text("Dairy") })

        Button(onClick = {
            scope.launch {
                dao.insert(
                    FoodIntake(
                        userId = userId,
                        persona = persona,
                        breakfastTime = breakfastTime,
                        lunchTime = lunchTime,
                        dinnerTime = dinnerTime,
                        fruit = fruit,
                        vegetables = vegetables,
                        grains = grains,
                        meat = meat,
                        dairy = dairy
                    )
                )
                navController.navigate("insights")
            }
        }) {
            Text("Submit & View Insights")
        }
    }
}

@Composable
fun DropdownMenuField(label: String, options: List<String>, onSelect: (String) -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    var selectedOption by remember { mutableStateOf(options.first()) }

    Column {
        OutlinedTextField(
            value = selectedOption,
            onValueChange = {},
            label = { Text(label) },
            modifier = Modifier.fillMaxWidth(),
            readOnly = true,
            trailingIcon = {
                IconButton(onClick = { expanded = true }) {
                    Icon(Icons.Default.ArrowDropDown, contentDescription = null)
                }
            }
        )
        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            options.forEach { option ->
                DropdownMenuItem(text = { Text(option) }, onClick = {
                    selectedOption = option
                    onSelect(option)
                    expanded = false
                })
            }
        }
    }
}
