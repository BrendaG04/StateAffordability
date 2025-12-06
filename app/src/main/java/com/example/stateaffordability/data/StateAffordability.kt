package com.example.stateaffordability.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "state_affordability")
data class StateAffordability(
    @PrimaryKey
    val stateName: String,
    val minimumWage: Double,
    val medianHousingCost: Double
)
