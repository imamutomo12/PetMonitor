package edu.imamutomo.petmonitor.presentation.state

data class AppStateMemento(private val state: AppState,
                           private val timestamp: Long = System.currentTimeMillis(),
                           private val description: String = "State snapshot"
) : Memento {
    override fun getState(): AppState = state
    override fun getTimestamp(): Long = timestamp
    override fun getDescription(): String = description
}
