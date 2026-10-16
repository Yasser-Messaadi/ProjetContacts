@file:Suppress("SpellCheckingInspection")

package com.example.room

import android.content.ActivityNotFoundException
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import coil3.compose.AsyncImage
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.room.data.Contact
import com.example.room.data.ContactViewModel
import com.example.room.ui.theme.RoomTheme
import androidx.core.net.toUri


class MainActivity : ComponentActivity() {
    private val contactViewModel: ContactViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            RoomTheme {
                ListeContactApp(
                    contactViewModel
                )
            }
        }
    }
}

enum class Ecran { LISTE, MODIF, AJOUT }

@Composable
fun ListeContactApp(
    contactViewModel: ContactViewModel
) {
    val contacts by contactViewModel.contacts.collectAsState(initial = null)
    var ecran by remember { mutableStateOf(Ecran.LISTE) }
    var selectionContact by remember { mutableStateOf<Contact?>(null) }

    when (ecran) {
        Ecran.LISTE ->
            ContactListeView(
                contactViewModel,
                contacts,
                clicContact = {
                    selectionContact = it
                    ecran = Ecran.MODIF
                },
                clicAjout = {
                    ecran = Ecran.AJOUT
                })

        Ecran.MODIF -> selectionContact?.let {
            ModifierAjouterContact(
                contactViewModel,
                contact = it,
                clicRetour = {
                    ecran = Ecran.LISTE
                },
                ajouter = false
            )
        }

        Ecran.AJOUT -> ModifierAjouterContact(
            contactViewModel,
            contact = Contact(
                prenom = stringResource(R.string.prenom),
                nom = stringResource(R.string.nom),
                telephone = stringResource(
                    R.string.telephone
                )
            ),
            clicRetour = {
                ecran = Ecran.LISTE
            },
            ajouter = true
        )

    }
}


@Composable
fun ContactListeView(
    contactViewModel: ContactViewModel,
    contacts: List<Contact>?,
    modifier: Modifier = Modifier,
    clicContact: (Contact) -> Unit,
    clicAjout: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBarPrincipale(clicAjout)
        },
        modifier = modifier.fillMaxSize()
    ) { padding ->

        if (contacts != null) {
            LazyColumn(
                modifier = modifier.padding(padding)
            )
            {
                items(contacts) { contact ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                            .clickable { clicContact(contact) },
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        val image = contact.photo.toUri()
                        AsyncImage(
                            model = image,
                            contentScale = ContentScale.Inside,
                            contentDescription = "",
                            modifier = Modifier.size(65.dp)
                        )
                        val prenomNomCoupe =
                            when ("${contact.prenom} ${contact.nom}".length >= 30) {
                                true -> "${contact.prenom} ${contact.nom}".substring(0, 29) + "..."
                                false -> "${contact.prenom} ${contact.nom}"
                            }
                        Text(text = "$prenomNomCoupe \n ${contact.telephone}")
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = 16.dp, 8.dp, 8.dp, 8.dp),
                            horizontalArrangement = Arrangement.End
                        ) {
                            var iconeEtoile by remember { mutableStateOf(Icons.Default.StarBorder) }
                            IconButton(onClick = {
                                iconeEtoile = when (contact.favori) {
                                    true -> Icons.Default.StarBorder
                                    false -> Icons.Default.Star
                                }
                                contact.favori = !contact.favori
                                contactViewModel.update(contact)
                            }
                            ) { Icon(imageVector = iconeEtoile, contentDescription = "Favori") }
                        }
                    }
                    HorizontalDivider()
                }
            }
        }

    }
}

@Composable
fun ModifierAjouterContact(
    contactViewModel: ContactViewModel,
    contact: Contact,
    modifier: Modifier = Modifier,
    clicRetour: () -> Unit,
    ajouter: Boolean
) {
    // Modifier et ajouter un contact utilisent la meme fonction;
    // Si ajouter est vrai, un nouveau contact est créé avec ces info, sinon un contact existant est mis a jour
    var prenom by remember { mutableStateOf(contact.prenom) }
    var nom by remember { mutableStateOf(contact.nom) }
    var age by remember { mutableStateOf(contact.age) }
    var telephone by remember { mutableStateOf(contact.telephone) }
    var courriel by remember { mutableStateOf(contact.courriel) }
    var adresse by remember { mutableStateOf(contact.adresse) }

    Scaffold(
        topBar = {
            TopAppBarModif(contact, contactViewModel, ajouter, clicRetour)
        },
        modifier = modifier.fillMaxSize()
    ) { padding ->
        Column(modifier = modifier.padding(padding)) {
            Button(
                onClick = {},
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) { Text(text = "Prendre Une Photo") }

            OutlinedTextField(
                value = prenom,
                onValueChange = { prenom = it },
                label = { Text(text = stringResource(R.string.prenom)) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            )
            OutlinedTextField(
                value = nom,
                onValueChange = { nom = it },
                label = { Text(text = stringResource(R.string.nom)) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            )
            OutlinedTextField(
                value = age.toString(),
                onValueChange = { newValue: String ->
                    age = if (newValue.isNotEmpty() && newValue.all { it.isDigit() })
                        newValue.toInt()
                    else {
                        0
                    }
                },
                label = { Text(text = "Age") },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number
                ),
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            )
            OutlinedTextField(
                value = telephone,
                onValueChange = { telephone = it },
                label = { Text(text = stringResource(R.string.telephone)) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            )
            courriel?.let { it1 ->
                OutlinedTextField(
                    value = it1,
                    onValueChange = { courriel = it },
                    label = { Text(text = stringResource(R.string.courriel)) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                )
            }
            adresse?.let { it1 ->
                OutlinedTextField(
                    value = it1,
                    onValueChange = { adresse = it },
                    label = { Text(text = stringResource(R.string.adresse)) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                )
            }
            Column {
                Button(
                    onClick = {
                        clicRetour()
                        contact.prenom = prenom
                        contact.nom = nom
                        contact.age = age
                        contact.telephone = telephone
                        contact.courriel = courriel
                        contact.adresse = adresse
                        if (ajouter) {
                            contactViewModel.add(contact)
                        } else {
                            contactViewModel.update(contact)
                        }
                    },
                    modifier = Modifier.padding(bottom = 4.dp,top = 8.dp, start = 8.dp, end = 8.dp).fillMaxWidth()
                ) { Text(text = stringResource(R.string.sauvegarder)) }
                Button(
                    onClick = {
                        clicRetour()
                    },
                    modifier = Modifier.padding(top = 4.dp,bottom = 8.dp, start = 8.dp, end = 8.dp).fillMaxWidth()

                ) { Text(text = stringResource(R.string.retourner)) }


            }

        }
    }
}

