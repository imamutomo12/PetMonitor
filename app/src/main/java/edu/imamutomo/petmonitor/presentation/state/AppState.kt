package edu.imamutomo.petmonitor.presentation.state


data class AppState(
val currentScreen: Screen,
val selectedPetId: String?,
val activeReminders: List<String>,
val filterSettings: FilterSettings,
val sortOrder: SortOrder,
val isEditing: Boolean,
val draftPet: DraftPetState?
)

enum class Screen { DASHBOARD, PET_LIST, PET_DETAIL, ADD_PET, SETTINGS, REMINDERS }
data class FilterSettings(val species: String?, val showCompleted: Boolean)
enum class SortOrder { NAME_ASC, NAME_DESC, DATE_ADDED, LAST_UPDATED }