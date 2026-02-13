package edu.imamutomo.petmonitor.presentation.ui.screens


import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import edu.imamutomo.petmonitor.domain.model.pet.Pet
import edu.imamutomo.petmonitor.presentation.viewmodel.PetListViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PetListScreen(
    viewModel: PetListViewModel = hiltViewModel(),
    onNavigateToAddPet: () -> Unit,
    onNavigateToPetDetail: (String) -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("My Pets") },
                actions = {
                    IconButton(onClick = { viewModel.undo() }) {
                        Icon(Icons.Default.Undo, contentDescription = "Undo")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onNavigateToAddPet) {
                Icon(Icons.Default.Add, contentDescription = "Add Pet")
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
                uiState.pets.isEmpty() -> {
                    EmptyPetList(onAddClick = onNavigateToAddPet)
                }
                else -> {
                    PetList(
                        pets = uiState.pets,
                        onPetClick = { onNavigateToPetDetail(it.id) },
                        onDeleteClick = { viewModel.deletePet(it.id) }
                    )
                }
            }

            uiState.error?.let { error ->
                Snackbar(
                    modifier = Modifier.align(Alignment.BottomCenter),
                    action = {
                        TextButton(onClick = { /* Dismiss */ }) {
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
private fun PetList(
    pets: List<Pet>,
    onPetClick: (Pet) -> Unit,
    onDeleteClick: (Pet) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(pets, key = { it.id }) { pet ->
            PetCard(
                pet = pet,
                onClick = { onPetClick(pet) },
                onDelete = { onDeleteClick(pet) }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PetCard(
    pet: Pet,
    onClick: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = pet.name,
                    style = MaterialTheme.typography.headlineSmall
                )
                Text(
                    text = "${pet.species} ${pet.breed ?: ""}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = "Owner: ${pet.ownerName}",
                    style = MaterialTheme.typography.bodySmall
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

@Composable
private fun EmptyPetList(onAddClick: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Icons.Default.Pets,
            contentDescription = null,
            modifier = Modifier.size(64.dp),
            tint = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "No pets yet",
            style = MaterialTheme.typography.headlineSmall
        )
        Spacer(modifier = Modifier.height(8.dp))
        Button(onClick = onAddClick) {
            Text("Add Your First Pet")
        }
    }
}