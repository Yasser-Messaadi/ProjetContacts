package com.example.room.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity (tableName = "contacts")
data class Contact(
    @PrimaryKey(autoGenerate = true)
    val uid: Int = 0,
    val prenom: String,
    val nom: String,
    val telephone: String
)
