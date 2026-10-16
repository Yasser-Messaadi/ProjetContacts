package com.example.room

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.room.data.Contact
import com.example.room.data.ContactViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopAppBarPrincipale(clicAjout: () -> Unit) {
    TopAppBar(
        title = { Text(text = "Contact App") },
        actions = {
            IconButton(
                onClick = {
                    clicAjout()
                }
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Home"
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary,
            titleContentColor = MaterialTheme.colorScheme.onPrimary,
            actionIconContentColor = MaterialTheme.colorScheme.onPrimary,
        ),
        expandedHeight = 45.dp
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopAppBarModif(contact:Contact, contactViewModel: ContactViewModel, ajouter: Boolean, clicRetour: () -> Unit){
    val titre = when(ajouter){
        true -> stringResource(R.string.ajouter_un_contact)
        false -> stringResource(R.string.modifier_un_contact)
    }

    TopAppBar(
        title = { Text(text = titre) },
        actions = {
            if(!ajouter){
                IconButton(
                    onClick = {
                        contactViewModel.delete(contact)
                        clicRetour()
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Effacer"
                    )
                }
            }

        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary,
            titleContentColor = MaterialTheme.colorScheme.onPrimary,
            actionIconContentColor = MaterialTheme.colorScheme.onPrimary,
        ),
        expandedHeight = 45.dp
    )
}