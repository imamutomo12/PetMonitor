package edu.imamutomo.petmonitor.presentation.ui.screens

import java.util.UUID
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import edu.imamutomo.petmonitor.presentation.viewmodel.AddPetViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddPetScreen(
    viewModel: AddPetViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit,
    onPetAdded: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    // Form fields
    var name by remember { mutableStateOf("") }
    var species by remember { mutableStateOf("") }
    var ownerName by remember { mutableStateOf("") }
    var ownerContact by remember { mutableStateOf("") }
    var breed by remember { mutableStateOf("") }
    var weight by remember { mutableStateOf("") }
    var microchipId by remember { mutableStateOf("") }

    val speciesOptions = listOf("dog", "cat", "bird", "fish", "rabbit",
        "hamster", "reptile")
    var expanded by remember { mutableStateOf(false) }



    LaunchedEffect(uiState.isSuccess) {
        if (uiState.isSuccess) {
            onPetAdded()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Add New Pet") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Progress indicator
            LinearProgressIndicator(
                progress = uiState.currentStep / uiState.totalSteps.toFloat(),
                modifier = Modifier.fillMaxWidth()
            )

            Text(
                text = "Step ${uiState.currentStep} of ${uiState.totalSteps}",
                style = MaterialTheme.typography.bodySmall
            )

            // Step 1: Required Info
            if (uiState.currentStep == 1) {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Pet Name *") },
                    modifier = Modifier.fillMaxWidth()
                )

                ExposedDropdownMenuBox(
                    expanded = expanded,
                    onExpandedChange = { expanded = !expanded }
                ) {
                    OutlinedTextField(
                        value = species,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Species *") },
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                        },
                        modifier = Modifier
                            .menuAnchor()
                            .fillMaxWidth()
                    )

                    ExposedDropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        speciesOptions.forEach { option ->
                            DropdownMenuItem(
                                text = { Text(option) },
                                onClick = {
                                    species = option
                                    expanded = false
                                }
                            )
                        }
                    }
                }


                OutlinedTextField(
                    value = ownerName,
                    onValueChange = { ownerName = it },
                    label = { Text("Your Name *") },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = ownerContact,
                    onValueChange = { ownerContact = it },
                    label = { Text("Email or Phone *") },
                    modifier = Modifier.fillMaxWidth()
                )

                Button(
                    onClick = {
                        viewModel.startBuilding(
                            UUID.randomUUID().toString(),
                            name,
                            species,
                            ownerName,
                            ownerContact
                        )
                        viewModel.setBasicInfo(
                            breed = breed.takeIf { it.isNotBlank() },
                            birthDate = null,
                            color = null
                        )
                    },
                    enabled = name.isNotBlank() && species.isNotBlank()
                            && ownerName.isNotBlank() && ownerContact.isNotBlank(),
                    modifier = Modifier.align(Alignment.End)
                ) {
                    Text("Next")
                }
            }

            // Step 2: Optional Info
            if (uiState.currentStep == 2) {
                OutlinedTextField(
                    value = breed,
                    onValueChange = { breed = it },
                    label = { Text("Breed (optional)") },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = weight,
                    onValueChange = { weight = it },
                    label = { Text("Weight in kg (optional)") },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = microchipId,
                    onValueChange = { microchipId = it },
                    label = { Text("Microchip ID (optional)") },
                    modifier = Modifier.fillMaxWidth()
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    TextButton(onClick = { viewModel.undo() }) {
                        Text("Back")
                    }

                    Button(
                        onClick = {
                            viewModel.setMedicalInfo(
                                weight = weight.toDoubleOrNull(),
                                microchipId = microchipId.takeIf { it.isNotBlank() },
                                allergies = emptyList()
                            )
                        }
                    ) {
                        Text("Next")
                    }
                }
            }

            // Step 3: Review & Save
            if (uiState.currentStep >= 3) {
                Card(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text("Review Information", style = MaterialTheme.typography.titleMedium)
                        Text("Name: $name")
                        Text("Species: $species")
                        Text("Owner: $ownerName")
                        if (breed.isNotBlank()) Text("Breed: $breed")
                        if (weight.isNotBlank()) Text("Weight: $weight kg")
                    }
                }

                if (uiState.isLoading) {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
                } else {
                    Button(
                        onClick = { viewModel.savePet() },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Save Pet")
                    }
                }
            }

            // Error display
            uiState.error?.let { error ->
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.errorContainer
                    ),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = error,
                        color = MaterialTheme.colorScheme.onErrorContainer,
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }
        }
    }
}