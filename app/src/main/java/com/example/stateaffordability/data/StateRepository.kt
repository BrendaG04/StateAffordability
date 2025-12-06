package com.example.stateaffordability.data

import kotlinx.coroutines.flow.Flow

class StateRepository(private val stateDao: StateDao) {

    val allStates: Flow<List<StateAffordability>> = stateDao.getAllStates()

    fun getState(stateName: String): Flow<StateAffordability> {
        return stateDao.getStateByName(stateName)
    }

}
