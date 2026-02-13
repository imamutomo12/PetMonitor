package edu.imamutomo.petmonitor.presentation.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Redo
import androidx.compose.material.icons.automirrored.filled.*
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.filled.AddAlert
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import edu.imamutomo.petmonitor.domain.export.ExportFormat
import edu.imamutomo.petmonitor.domain.model.pet.Pet
import edu.imamutomo.petmonitor.domain.model.reminder.FoodReminder
import edu.imamutomo.petmonitor.domain.model.reminder.MedicationFrequency
import edu.imamutomo.petmonitor.domain.model.reminder.MedicineReminder
import edu.imamutomo.petmonitor.domain.model.reminder.ReminderCategory
import edu.imamutomo.petmonitor.domain.model.reminder.ReminderType
import edu.imamutomo.petmonitor.domain.model.reminder.VaccineReminder
import edu.imamutomo.petmonitor.domain.model.schedule.ScheduleTemplate
import edu.imamutomo.petmonitor.presentation.state.PetDetailUiState
import edu.imamutomo.petmonitor.presentation.viewmodel.PetDetailViewModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PetDetailScreen(
    petId: String,
    viewModel: PetDetailViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    var showAddReminderDialog by remember { mutableStateOf(false) }
    var showTemplateDialog by remember { mutableStateOf(false) }
    var showExportMenu by remember { mutableStateOf(false) }

    LaunchedEffect(petId) {
        viewModel.loadPetDetails(petId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(uiState.pet?.name ?: "Pet Details") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = { viewModel.undo() }) {
                        Icon(Icons.AutoMirrored.Filled.Undo, contentDescription = "Undo")
                    }
                    IconButton(onClick = { viewModel.redo() }) {
                        Icon(Icons.AutoMirrored.Filled.Redo, contentDescription = "Redo")
                    }
                    IconButton(onClick = { showExportMenu = true }) {
                        Icon(Icons.Default.Share, contentDescription = "Export")
                    }
                    DropdownMenu(
                        expanded = showExportMenu,
                        onDismissRequest = { showExportMenu = false }
                    ) {
                        DropdownMenuItem(
                            text = { Text("Export as JSON") },
                            onClick = {
                                viewModel.exportPetData(ExportFormat.JSON)
                                showExportMenu = false
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("Export as XML") },
                            onClick = {
                                viewModel.exportPetData(ExportFormat.XML)
                                showExportMenu = false
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("Export as CSV") },
                            onClick = {
                                viewModel.exportPetData(ExportFormat.CSV)
                                showExportMenu = false
                            }
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                SmallFloatingActionButton(
                    onClick = { showTemplateDialog = true }
                ) {
                    Icon(Icons.Default.ContentCopy, contentDescription = "Apply Template")
                }
                FloatingActionButton(
                    onClick = { showAddReminderDialog = true }
                ) {
                    Icon(Icons.Default.AddAlert, contentDescription = "Add Reminder")
                }
            }
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            when {
                uiState.isLoading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                uiState.pet != null -> {
                    PetDetailContent(
                        uiState = uiState,
                        onCompleteReminder = { viewModel.completeReminder(it) },
                        onDeleteReminder = { viewModel.deleteReminder(it) }
                    )
                }
            }

            if (showAddReminderDialog) {
                AddReminderDialog(
                    onDismiss = { showAddReminderDialog = false },
                    onConfirm = { category, title, desc, time, params ->
                        viewModel.scheduleReminder(category, title, desc, time, params)
                        showAddReminderDialog = false
                    }
                )
            }

            if (showTemplateDialog) {
                TemplateSelectionDialog(
                    templates = uiState.availableTemplates,
                    onDismiss = { showTemplateDialog = false },
                    onSelect = { templateId ->
                        viewModel.applyTemplate(templateId)
                        showTemplateDialog = false
                    }
                )
            }

            uiState.error?.let { error ->
                Snackbar(
                    modifier = Modifier.align(Alignment.BottomCenter),
                    action = {
                        TextButton(onClick = { viewModel.clearError() }) {
                            Text("Dismiss")
                        }
                    }
                ) {
                    Text(error)
                }
            }
        }
    }
}



@Composable
private fun PetDetailContent(
    uiState: PetDetailUiState,
    onCompleteReminder: (String) -> Unit,
    onDeleteReminder: (String) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            PetInfoCard(pet = uiState.pet!!)
        }

        item {
            Text(
                text = "Upcoming Reminders",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(vertical = 8.dp)
            )
        }

        items(uiState.reminders, key = { it.id }) { reminder ->
            ReminderCard(
                reminder = reminder,
                onComplete = { onCompleteReminder(reminder.id) },
                onDelete = { onDeleteReminder(reminder.id) }
            )
        }
    }
}

@Composable
private fun PetInfoCard(pet: Pet) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = pet.name,
                style = MaterialTheme.typography.headlineMedium
            )
            Text(
                text = "${pet.species} • ${pet.breed ?: "Unknown breed"}",
                style = MaterialTheme.typography.bodyLarge
            )
            Divider(modifier = Modifier.padding(vertical = 8.dp))
            InfoRow("Owner", pet.ownerName)
            InfoRow("Contact", pet.ownerContact)
            pet.weight?.let { InfoRow("Weight", "$it kg") }
            pet.microchipId?.let { InfoRow("Microchip", it) }
            if (pet.allergies.isNotEmpty()) {
                InfoRow("Allergies", pet.allergies.joinToString(", "))
            }
        }
    }
}

@Composable
private fun InfoRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Text(
            text = "$label: ",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@Composable
private fun ReminderCard(
    reminder: ReminderType,
    onComplete: () -> Unit,
    onDelete: () -> Unit
) {
    val dateFormat = SimpleDateFormat("MMM dd, yyyy HH:mm", Locale.getDefault())

    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = reminder.title,
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = reminder.description,
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = dateFormat.format(Date(reminder.scheduledTime)),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.primary
                )
                // Type-specific details
                when (reminder) {
                    is FoodReminder -> {
                        Text("Food: ${reminder.foodType} (${reminder.portionSize})")
                    }
                    is MedicineReminder -> {
                        Text("Medicine: ${reminder.medicationName} - ${reminder.dosage}")
                    }
                    is VaccineReminder -> {
                        Text("Vaccine: ${reminder.vaccineName} with ${reminder.veterinarian}")
                    }
                }
            }

            Row {
                IconButton(onClick = onComplete) {
                    Icon(
                        Icons.Default.CheckCircle,
                        contentDescription = "Complete",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
                IconButton(onClick = onDelete) {
                    Icon(
                        Icons.Default.Delete,
                        contentDescription = "Delete",
                        tint = MaterialTheme.colorScheme.error
                    )
                }
            }
        }
    }
}

@Composable
private fun AddReminderDialog(
    onDismiss: () -> Unit,
    onConfirm: (ReminderCategory, String, String, Long, Map<String, Any>) -> Unit
) {
    var selectedCategory by remember { mutableStateOf(ReminderCategory.FOOD) }
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var daysFromNow by remember { mutableStateOf("1") }
    var timeOfDay by remember { mutableStateOf("12:00") }

    // Category-specific fields
    var foodType by remember { mutableStateOf("") }
    var portionSize by remember { mutableStateOf("") }
    var medicationName by remember { mutableStateOf("") }
    var dosage by remember { mutableStateOf("") }
    var vaccineName by remember { mutableStateOf("") }
    var veterinarian by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Add Reminder") },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Category selection
                SingleChoiceSegmentedButtonRow {
                    ReminderCategory.values().forEachIndexed { index, category ->
                        SegmentedButton(
                            selected = selectedCategory == category,
                            onClick = { selectedCategory = category },
                            shape = SegmentedButtonDefaults.itemShape(
                                index = index,
                                count = ReminderCategory.values().size
                            )
                        ) {
                            Text(category.name.take(4))
                        }
                    }
                }

                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Title") }
                )

                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Description") }
                )

                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedTextField(
                        value = daysFromNow,
                        onValueChange = { daysFromNow = it },
                        label = { Text("Days") },
                        modifier = Modifier.weight(1f)
                    )
                    OutlinedTextField(
                        value = timeOfDay,
                        onValueChange = { timeOfDay = it },
                        label = { Text("Time (HH:mm)") },
                        modifier = Modifier.weight(1f)
                    )
                }

                // Dynamic fields based on category
                when (selectedCategory) {
                    ReminderCategory.FOOD -> {
                        OutlinedTextField(
                            value = foodType,
                            onValueChange = { foodType = it },
                            label = { Text("Food Type") }
                        )
                        OutlinedTextField(
                            value = portionSize,
                            onValueChange = { portionSize = it },
                            label = { Text("Portion Size") }
                        )
                    }
                    ReminderCategory.MEDICINE -> {
                        OutlinedTextField(
                            value = medicationName,
                            onValueChange = { medicationName = it },
                            label = { Text("Medication Name") }
                        )
                        OutlinedTextField(
                            value = dosage,
                            onValueChange = { dosage = it },
                            label = { Text("Dosage") }
                        )
                    }
                    ReminderCategory.VACCINE -> {
                        OutlinedTextField(
                            value = vaccineName,
                            onValueChange = { vaccineName = it },
                            label = { Text("Vaccine Name") }
                        )
                        OutlinedTextField(
                            value = veterinarian,
                            onValueChange = { veterinarian = it },
                            label = { Text("Veterinarian") }
                        )
                    }
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    val calendar = Calendar.getInstance()
                    calendar.add(Calendar.DAY_OF_YEAR, daysFromNow.toIntOrNull() ?: 1)
                    val (hour, minute) = timeOfDay.split(":").map { it.toIntOrNull() ?: 0 }
                    calendar.set(Calendar.HOUR_OF_DAY, hour)
                    calendar.set(Calendar.MINUTE, minute)

                    val params = when (selectedCategory) {
                        ReminderCategory.FOOD -> mapOf(
                            "foodType" to foodType,
                            "portionSize" to portionSize
                        )
                        ReminderCategory.MEDICINE -> mapOf(
                            "medicationName" to medicationName,
                            "dosage" to dosage,
                            "frequency" to MedicationFrequency.ONCE_DAILY,
                            "withFood" to false
                        )
                        ReminderCategory.VACCINE -> mapOf(
                            "vaccineName" to vaccineName,
                            "veterinarian" to veterinarian
                        )
                    }

                    onConfirm(selectedCategory, title, description, calendar.timeInMillis, params)
                }
            ) {
                Text("Add")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

@Composable
private fun TemplateSelectionDialog(
    templates: List<ScheduleTemplate>,
    onDismiss: () -> Unit,
    onSelect: (String) -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Apply Schedule Template") },
        text = {
            LazyColumn {
                items(templates) { template ->
                    ListItem(
                        headlineContent = { Text(template.name) },
                        supportingContent = { Text(template.description) },
                        trailingContent = {
                            Text("${template.items.size} items")
                        },
                        modifier = Modifier.clickable { onSelect(template.id) }
                    )
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}