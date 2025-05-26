package com.mahbeermohammed.fit2081nutritrack.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun NutriCoachScreen(viewModel: NutriCoachViewModel = viewModel()) {
    var fruitName by remember { mutableStateOf("") }

    val fruitInfo by viewModel.fruitInfo.collectAsState()
    val loading by viewModel.loading.collectAsState()
    val error by viewModel.error.collectAsState()

    Box(Modifier.fillMaxSize()) {
        Column(
            Modifier
                .fillMaxWidth()
                .padding(24.dp)
                .align(Alignment.TopCenter)
        ) {
            Text(
                "NutriCoach",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
            Spacer(Modifier.height(32.dp))

            Text(
                "Fruitname",
                fontWeight = FontWeight.SemiBold,
                fontSize = 20.sp,
            )
            Spacer(Modifier.height(8.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                OutlinedTextField(
                    value = fruitName,
                    onValueChange = { fruitName = it },
                    modifier = Modifier.weight(1f),
                    placeholder = { Text("Enter fruit name") },
                    singleLine = true,
                )
                Spacer(Modifier.width(8.dp))
                Button(
                    onClick = {
                        if (fruitName.isNotBlank()) viewModel.fetchFruit(fruitName.trim())
                    },
                    modifier = Modifier.height(56.dp)
                ) {
                    Icon(Icons.Default.Info, contentDescription = "Details")
                    Spacer(Modifier.width(4.dp))
                    Text("Details")
                }
            }

            Spacer(Modifier.height(24.dp))

            when {
                loading -> CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
                error != null -> Text(
                    text = error ?: "",
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
                fruitInfo != null -> {
                    FruitInfoTable(fruitInfo!!)
                    Spacer(Modifier.height(16.dp))
                    Button(
                        onClick = { /* TODO: Handle Motivational Message action */ },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Motivational Message (AI)")
                    }
                }
            }
        }

        ExtendedFloatingActionButton(
            text = { Text("Show all tips") },
            icon = { Icon(Icons.Default.Done, contentDescription = "Done") },
            onClick = { /* TODO */ },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)
        )
    }
}

@Composable
fun FruitInfoTable(fruit: com.mahbeermohammed.fit2081nutritrack.data.FruitInfo) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, Color.Gray, RoundedCornerShape(4.dp))
    ) {
        TableRow("Family", fruit.family, true)
        TableRow("Calories", fruit.nutritions.calories.toString())
        TableRow("Fat", fruit.nutritions.fat.toString())
        TableRow("Sugar", fruit.nutritions.sugar.toString())
        TableRow("Carbohydrates", fruit.nutritions.carbohydrates.toString())
        TableRow("Protein", fruit.nutritions.protein.toString())
    }
}

@Composable
fun TableRow(label: String, value: String, isHeader: Boolean = false) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(if (isHeader) Color.LightGray else Color.Transparent)
            .padding(vertical = 12.dp, horizontal = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            fontWeight = if (isHeader) FontWeight.Bold else FontWeight.Normal,
            fontSize = if (isHeader) 16.sp else 14.sp
        )
        Text(
            text = value,
            fontWeight = if (isHeader) FontWeight.Bold else FontWeight.Normal,
            fontSize = if (isHeader) 16.sp else 14.sp,
        )
    }
}
