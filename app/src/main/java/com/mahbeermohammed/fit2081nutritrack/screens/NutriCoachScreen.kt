package com.mahbeermohammed.fit2081nutritrack.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import com.mahbeermohammed.fit2081nutritrack.MotivationalMessage
import com.mahbeermohammed.fit2081nutritrack.data.FruitInfo
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun NutriCoachScreen(viewModel: NutriCoachViewModel = viewModel()) {
    var fruitName by remember { mutableStateOf("") }
    var showTipsDialog by remember { mutableStateOf(false) }

    val fruitInfo by viewModel.fruitInfo.collectAsState()
    val loading by viewModel.loading.collectAsState()
    val error by viewModel.error.collectAsState()
    val motivation by viewModel.motivation.collectAsState()
    val allTips by viewModel.allTips.collectAsState()

    LaunchedEffect(showTipsDialog) {
        if (showTipsDialog) {
            viewModel.loadAllTips()
        }
    }

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
                "Fruit name",
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
                        onClick = { viewModel.generateMotivation() },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Motivational Message (AI)")
                    }
                    motivation?.let {
                        Spacer(Modifier.height(12.dp))
                        Text(
                            text = it,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color(0xFF388E3C),
                            modifier = Modifier.align(Alignment.CenterHorizontally)
                        )
                    }
                }
            }
        }

        ExtendedFloatingActionButton(
            text = { Text("Show all tips") },
            icon = { Icon(Icons.Default.Done, contentDescription = "Done") },
            onClick = { showTipsDialog = true },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)
        )

        if (showTipsDialog) {
            TipsDialog(
                tips = allTips,
                onDismiss = { showTipsDialog = false }
            )
        }
    }
}

@Composable
fun TipsDialog(tips: List<MotivationalMessage>, onDismiss: () -> Unit) {
    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Column(
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.surface)
                    .padding(16.dp)
            ) {
                Text(
                    text = "All Motivational Tips",
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                if (tips.isEmpty()) {
                    Text(
                        text = "No tips generated yet",
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(vertical = 16.dp)
                    )
                } else {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(max = 400.dp)
                    ) {
                        items(tips) { tip ->
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp)
                            ) {
                                Column(modifier = Modifier.padding(12.dp)) {
                                    Text(
                                        text = tip.message,
                                        style = MaterialTheme.typography.bodyLarge
                                    )
                                    Text(
                                        text = SimpleDateFormat("MMM dd, HH:mm", Locale.getDefault())
                                            .format(Date(tip.timestamp)),
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                                        modifier = Modifier.padding(top = 4.dp)
                                    )
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = onDismiss,
                    modifier = Modifier.align(Alignment.End)
                ) {
                    Text("Close")
                }
            }
        }
    }
}

@Composable
fun FruitInfoTable(fruit: FruitInfo) {
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