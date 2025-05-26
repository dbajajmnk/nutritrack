package com.mahbeermohammed.fit2081nutritrack.screens

import android.app.TimePickerDialog
import android.content.Context
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.mahbeermohammed.fit2081nutritrack.AppDatabase
import com.mahbeermohammed.fit2081nutritrack.FoodIntake
import kotlinx.coroutines.launch
import java.util.*

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

    var fruitServings by remember { mutableStateOf("") }
    var vegetablesServings by remember { mutableStateOf("") }
    var grainsServings by remember { mutableStateOf("") }
    var meatServings by remember { mutableStateOf("") }
    var dairyServings by remember { mutableStateOf("") }

    var showValidationError by remember { mutableStateOf(false) }

    val isFormValid = breakfastTime.isNotEmpty() &&
            lunchTime.isNotEmpty() &&
            dinnerTime.isNotEmpty() &&
            fruitServings.isNotEmpty() &&
            vegetablesServings.isNotEmpty() &&
            grainsServings.isNotEmpty() &&
            meatServings.isNotEmpty() &&
            dairyServings.isNotEmpty()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalAlignment = Alignment.Start
    ) {
        Text("Questionnaire", fontSize = 24.sp, fontWeight = FontWeight.Bold)

        DropdownMenuField("Select Persona", listOf("Active Teen", "Adult Male", "Adult Female")) { persona = it }

        TimeInputField("Breakfast Time", breakfastTime) { breakfastTime = it }
        TimeInputField("Lunch Time", lunchTime) { lunchTime = it }
        TimeInputField("Dinner Time", dinnerTime) { dinnerTime = it }

        NumberInputField(label = "Fruit Servings (e.g. 2)", value = fruitServings) { fruitServings = it }
        NumberInputField(label = "Vegetable Servings", value = vegetablesServings) { vegetablesServings = it }
        NumberInputField(label = "Grain Servings", value = grainsServings) { grainsServings = it }
        NumberInputField(label = "Meat/Alternatives Servings", value = meatServings) { meatServings = it }
        NumberInputField(label = "Dairy Servings", value = dairyServings) { dairyServings = it }

        if (showValidationError && !isFormValid) {
            Text("Please fill out all fields.", color = MaterialTheme.colorScheme.error)
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                if (!isFormValid) {
                    showValidationError = true
                } else {
                    scope.launch {
                        dao.insert(
                            FoodIntake(
                                userId = userId,
                                persona = persona,
                                breakfastTime = breakfastTime,
                                lunchTime = lunchTime,
                                dinnerTime = dinnerTime,
                                fruit = fruitServings,
                                vegetables = vegetablesServings,
                                grains = grainsServings,
                                meat = meatServings,
                                dairy = dairyServings
                            )
                        )
                        navController.navigate("insights")
                    }
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
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

@Composable
fun TimeInputField(label: String, time: String, onTimeSelected: (String) -> Unit) {
    val context = LocalContext.current
    val calendar = Calendar.getInstance()
    val hour = calendar.get(Calendar.HOUR_OF_DAY)
    val minute = calendar.get(Calendar.MINUTE)

    OutlinedTextField(
        value = time,
        onValueChange = {},
        label = { Text(label) },
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                TimePickerDialog(
                    context,
                    { _, selectedHour, selectedMinute ->
                        val formatted = "%02d:%02d".format(selectedHour, selectedMinute)
                        onTimeSelected(formatted)
                    },
                    hour, minute, true
                ).show()
            },
        readOnly = true,
        singleLine = true
    )
}

@Composable
fun NumberInputField(label: String, value: String, onValueChange: (String) -> Unit) {
    OutlinedTextField(
        value = value,
        onValueChange = { input ->
            if (input.all { it.isDigit() }) {
                onValueChange(input)
            }
        },
        label = { Text(label) },
        modifier = Modifier.fillMaxWidth()
    )
}
