package com.example.room

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.FilterListOff
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.room.data.Contact
import com.example.room.data.ContactViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopAppBarPrincipale(clicAjout: () -> Unit, clicFiltre : () -> Unit, filtre:Boolean) {
    TopAppBar(
        title = { Text(text = "Contact App") },
        actions = {
            var iconeFiltre by remember { mutableStateOf(Icons.Default.FilterList) }
            iconeFiltre = when (filtre) {
                true -> Icons.Default.FilterListOff
                false -> Icons.Default.FilterList
            }

            IconButton(
                onClick = {
                    clicFiltre()
                }
            ) {
                Icon(
                    imageVector = iconeFiltre,
                    contentDescription = "Filter"
                )
            }
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

@SuppressLint("LocalContextGetResourceValueCall")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopAppBarModif(contact:Contact, contactViewModel: ContactViewModel, ajouter: Boolean, clicRetour: () -> Unit){
    val titre = when(ajouter){
        true -> stringResource(R.string.ajouter_un_contact)
        false -> stringResource(R.string.modifier_un_contact)
    }
    val context = LocalContext.current;

    TopAppBar(
        title = { Text(text = titre) },
        actions = {
            if(!ajouter){
                IconButton(
                    onClick = {
                        contactViewModel.delete(contact)
                        clicRetour()
                        Toast.makeText(context, context.getString(R.string.contact_efface), Toast.LENGTH_SHORT).show()

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