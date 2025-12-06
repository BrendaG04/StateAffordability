package com.example.stateaffordability.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface StateDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(states: List<StateAffordability>)

    @Query("SELECT * FROM state_affordability ORDER BY stateName ASC")
    fun getAllStates(): Flow<List<StateAffordability>>

    @Query("SELECT * FROM state_affordability WHERE stateName = :stateName")
    fun getStateByName(stateName: String): Flow<StateAffordability>


}
