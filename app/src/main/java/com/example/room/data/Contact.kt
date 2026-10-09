package com.example.room.data

import android.media.Image
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity (tableName = "contacts")
data class Contact(
    @PrimaryKey(autoGenerate = true)
    val uid: Int = 0,
    val prenom: String, // Obligatoire sans défaut
    val nom: String, // Obligatoire sans défaut
    val telephone: String, // Obligatoire sans défaut
    val courriel: String? = null,
    val adresse: String? = null,
    val age: Int? = null,
    var favori: Boolean = false
    //val photo: Image? = null
)
