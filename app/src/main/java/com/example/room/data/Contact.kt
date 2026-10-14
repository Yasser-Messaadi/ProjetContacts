package com.example.room.data

import android.media.Image
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity (tableName = "contacts")
data class Contact(
    @PrimaryKey(autoGenerate = true)
    val uid: Int = 0,
    var prenom: String, // Obligatoire sans défaut
    var nom: String, // Obligatoire sans défaut
    var telephone: String, // Obligatoire sans défaut
    var courriel: String? = "",
    var adresse: String? = "",
    var age: Int? = 0,
    var favori: Boolean = false
    //val photo: Image? = null
)
