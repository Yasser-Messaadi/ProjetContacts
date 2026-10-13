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
    var courriel: String? = null,
    var adresse: String? = null,
    var age: Int? = null,
    var favori: Boolean = false
    //val photo: Image? = null
)
