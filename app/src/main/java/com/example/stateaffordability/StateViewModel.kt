package com.example.stateaffordability

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.stateaffordability.data.AppDatabase
import com.example.stateaffordability.data.StateAffordability
import com.example.stateaffordability.data.StateRepository
import kotlinx.coroutines.flow.Flow

class StateViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: StateRepository
    val allStates: Flow<List<StateAffordability>>

    init {
        val stateDao = AppDatabase.getDatabase(application).stateDao()
        repository = StateRepository(stateDao)
        allStates = repository.allStates
    }

    fun getState(name: String): Flow<StateAffordability> {
        return repository.getState(name)
    }
}
