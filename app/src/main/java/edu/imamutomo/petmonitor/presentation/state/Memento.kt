package edu.imamutomo.petmonitor.presentation.state


    interface Memento {
        fun getState(): AppState
        fun getTimestamp(): Long
        fun getDescription(): String
    }
