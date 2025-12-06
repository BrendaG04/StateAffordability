package com.example.stateaffordability.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.stateaffordability.StateViewModel
import com.example.stateaffordability.data.StateAffordability
import java.text.NumberFormat
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun HomeScreen(
    stateViewModel: StateViewModel = viewModel(),
    onStateClicked: (String) -> Unit
) {
    val states by stateViewModel.allStates.collectAsState(initial = emptyList())

    var query by remember { mutableStateOf("") }
    var minWageFilter by remember { mutableStateOf("") }
    var maxHousingFilter by remember { mutableStateOf("") }
    var filtersVisible by remember { mutableStateOf(false) }

    val filteredStates = states.filter { state ->
        val wageFilterValue = minWageFilter.toDoubleOrNull()
        val housingFilterValue = maxHousingFilter.toDoubleOrNull()

        val matchesQuery = state.stateName.contains(query, ignoreCase = true)
        val matchesWage = wageFilterValue == null || state.minimumWage >= wageFilterValue
        val matchesHousing = housingFilterValue == null || state.medianHousingCost <= housingFilterValue

        matchesQuery && matchesWage && matchesHousing
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Affordability Dashboard") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        }
    ) { paddingValues ->
        Column(modifier = Modifier.padding(paddingValues)) {
            FilterSection(
                isVisible = filtersVisible,
                onToggleVisibility = { filtersVisible = !filtersVisible },
                query = query,
                onQueryChanged = { query = it },
                minWageFilter = minWageFilter,
                onMinWageChanged = { minWageFilter = it },
                maxHousingFilter = maxHousingFilter,
                onMaxHousingChanged = { maxHousingFilter = it }
            )

            LazyVerticalGrid(
                columns = GridCells.Adaptive(minSize = 160.dp),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(filteredStates, key = { it.stateName }) { state ->
                    StateCard(state = state, onClick = { onStateClicked(state.stateName) })
                }
            }
        }
    }
}

@Composable
fun FilterSection(
    isVisible: Boolean,
    onToggleVisibility: () -> Unit,
    query: String,
    onQueryChanged: (String) -> Unit,
    minWageFilter: String,
    onMinWageChanged: (String) -> Unit,
    maxHousingFilter: String,
    onMaxHousingChanged: (String) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(onClick = onToggleVisibility),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Filter Options", style = MaterialTheme.typography.titleMedium)
                Icon(
                    imageVector = Icons.Default.ExpandMore,
                    contentDescription = "Toggle Filters",
                    modifier = if (isVisible) Modifier.graphicsLayer(rotationZ = 180f) else Modifier
                )
            }

            AnimatedVisibility(visible = isVisible) {
                Column {
                    Spacer(modifier = Modifier.height(16.dp))
                    OutlinedTextField(
                        value = query,
                        onValueChange = onQueryChanged,
                        label = { Text("Search by state name") },
                        leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        OutlinedTextField(
                            value = minWageFilter,
                            onValueChange = onMinWageChanged,
                            label = { Text("Min Wage") },
                            leadingIcon = { Icon(Icons.Default.AttachMoney, contentDescription = null) },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            modifier = Modifier.weight(1f)
                        )
                        OutlinedTextField(
                            value = maxHousingFilter,
                            onValueChange = onMaxHousingChanged,
                            label = { Text("Max Housing") },
                            leadingIcon = { Icon(Icons.Default.Home, contentDescription = null) },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun StateCard(state: StateAffordability, onClick: () -> Unit) {

    val cardColor = Color(0xFFC8E6C9)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = cardColor)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = state.stateName,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(16.dp))

            InfoRow(icon = Icons.Default.AttachMoney, label = "Wage", value = "$${state.minimumWage}/hr")
            Spacer(modifier = Modifier.height(8.dp))
            InfoRow(icon = Icons.Default.Home, label = "Avrg rent", value = formatCurrency(state.medianHousingCost))
        }
    }
}

@Composable
private fun InfoRow(icon: androidx.compose.ui.graphics.vector.ImageVector, label: String, value: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = "$label:", fontSize = 14.sp, fontWeight = FontWeight.Normal)
        Spacer(modifier = Modifier.weight(1f))
        Text(text = value, fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
    }
}

private fun formatCurrency(value: Double): String {
    return NumberFormat.getCurrencyInstance(Locale.US).apply {
        maximumFractionDigits = 0
    }.format(value)
}
