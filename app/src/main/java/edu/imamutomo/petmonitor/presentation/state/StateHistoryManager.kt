package edu.imamutomo.petmonitor.presentation.state

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.Stack
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class StateHistoryManager @Inject constructor() {
    private val history = Stack<Memento>()
    private val redoStack = Stack<Memento>()
    private val maxHistorySize = 50

    private val _canUndo = MutableStateFlow(false)
    val canUndo: StateFlow<Boolean> = _canUndo.asStateFlow()

    private val _canRedo = MutableStateFlow(false)
    val canRedo: StateFlow<Boolean> = _canRedo.asStateFlow()

    fun saveState(state: AppState, description: String = "Auto-save") {
        // Clear redo stack on new action
        redoStack.clear()

        val memento = AppStateMemento(
            state = state,
            description = description
        )

        history.push(memento)

        // Limit history size
        if (history.size > maxHistorySize) {
            val tempStack = Stack<Memento>()
            repeat(maxHistorySize) {
                tempStack.push(history.pop())
            }
            history.clear()
            history.addAll(tempStack.reversed())
        }

        updateStates()
    }

    fun undo(): Memento? {
        if (history.isEmpty()) return null

        val current = history.pop()
        redoStack.push(current)

        val previous = history.peekOrNull()
        updateStates()
        return previous
    }

    fun redo(): Memento? {
        if (redoStack.isEmpty()) return null

        val memento = redoStack.pop()
        history.push(memento)

        updateStates()
        return memento
    }

    fun getHistory(): List<Memento> = history.toList()

    fun clear() {
        history.clear()
        redoStack.clear()
        updateStates()
    }

    private fun updateStates() {
        _canUndo.value = history.isNotEmpty()
        _canRedo.value = redoStack.isNotEmpty()
    }

    private fun <T> Stack<T>.peekOrNull(): T? = if (isEmpty()) null else peek()
}
