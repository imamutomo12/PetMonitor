package edu.imamutomo.petmonitor.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import edu.imamutomo.petmonitor.presentation.ui.screens.AddPetScreen
import edu.imamutomo.petmonitor.presentation.ui.screens.PetDetailScreen
import edu.imamutomo.petmonitor.presentation.ui.screens.PetListScreen
import edu.imamutomo.petmonitor.presentation.ui.theme.PetMonitorTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PetMonitorTheme {
                val navController = rememberNavController()
                PetCareNavHost(navController = navController)
            }
        }
    }
}


@Composable
fun PetCareNavHost(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = "pet_list"
    ) {
        composable("pet_list") {
            PetListScreen(
                onNavigateToAddPet = { navController.navigate("add_pet") },
                onNavigateToPetDetail = { petId ->
                    navController.navigate("pet_detail/$petId")
                }
            )
        }

        composable("add_pet") {
            AddPetScreen(
                onNavigateBack = { navController.popBackStack() },
                onPetAdded = {
                    navController.popBackStack()
                    navController.navigate("pet_list")
                }
            )
        }

        composable("pet_detail/{petId}") { backStackEntry ->
            val petId = backStackEntry.arguments?.getString("petId") ?: return@composable
            PetDetailScreen(
                petId = petId,
                onNavigateBack = { navController.popBackStack() }
            )
        }
    }}

