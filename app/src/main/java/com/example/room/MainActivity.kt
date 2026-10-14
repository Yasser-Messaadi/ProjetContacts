@file:Suppress("SpellCheckingInspection")

package com.example.room

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.room.data.Contact
import com.example.room.data.ContactViewModel
import com.example.room.ui.theme.RoomTheme

class MainActivity : ComponentActivity() {
    private val contactViewModel: ContactViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            RoomTheme {
                contactViewModel.add(Contact(prenom = "John", nom = "Doe", telephone = "(514)-123-4567"))
                ListeContactApp(
                    contactViewModel
                )
            }
        }
    }
}

enum class Ecran { LISTE, MODIF, AJOUT }
@Composable
fun ListeContactApp(contactViewModel: ContactViewModel) {
    val contacts by contactViewModel.contacts.collectAsState()
    var ecran by remember { mutableStateOf(Ecran.LISTE) }
    var selectionContact by remember { mutableStateOf<Contact?>(null) }

    if (contacts.isEmpty()) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    } else {
        when (ecran) {
            Ecran.LISTE -> ContactListeView(
                contactViewModel,
                clicContact = {
                    selectionContact = it
                    ecran = Ecran.MODIF},
                clicAjout = {
                    ecran = Ecran.AJOUT
                })

            Ecran.MODIF -> selectionContact?.let {
                ModifierAjouterContact(
                    contactViewModel,
                    contact = it,
                    clicRetour = {
                        ecran = Ecran.LISTE},
                    ajouter = false
                )
            }
            Ecran.AJOUT -> ModifierAjouterContact(
                contactViewModel,
                contact = Contact(prenom = "Prenom", nom = "Nom", telephone = "Telephone"),
                clicRetour = {
                    ecran = Ecran.LISTE},
                ajouter = true
            )
        }
    }
}

@Composable
fun ContactListeView (contactViewModel: ContactViewModel, modifier: Modifier = Modifier, clicContact: (Contact) -> Unit,clicAjout: () -> Unit) {
    val contacts by contactViewModel.contacts.collectAsState()

    Scaffold(
        topBar = {
            TopAppBarPrincipale(clicAjout)
        },
        modifier = modifier.fillMaxSize()
    ) { padding ->
        LazyColumn(modifier = modifier.padding(padding)
        )
        {
            items(contacts) { contact ->
                Row(
                    modifier = Modifier.fillMaxWidth().padding(8.dp).clickable{clicContact(contact)},
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    // Ajouter Image Contact
                    Text(text = "${contact.prenom} ${contact.nom} \n ${contact.telephone}")
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(8.dp),
                        horizontalArrangement = Arrangement.End) {
                        var iconeEtoile by remember { mutableStateOf(Icons.Default.StarBorder) }
                        IconButton(onClick = {contactViewModel.delete(contact)}){ Icon(imageVector = Icons.Default.Delete, contentDescription = "Effacer") }
                        IconButton(onClick = {
                            iconeEtoile = when (contact.favori) {
                                true -> Icons.Default.StarBorder
                                false -> Icons.Default.Star
                            }
                            contact.favori = !contact.favori
                            contactViewModel.update(contact)}
                        ){ Icon(imageVector = iconeEtoile, contentDescription = "Favori")}
                    }
                }
                HorizontalDivider()
            }
        }
    }
}

@Composable
fun ModifierAjouterContact(contactViewModel: ContactViewModel, contact: Contact, modifier: Modifier = Modifier, clicRetour: () -> Unit, ajouter: Boolean)
{
    // Modifier et ajouter un contact utilisent la meme fonction;
    // Si ajouter est vrai, un nouveau contact est créé avec ces info, sinon un contact existant est mis a jour

    var prenom by remember { mutableStateOf(contact.prenom) }
    var nom by remember { mutableStateOf(contact.nom) }
    var age by remember { mutableStateOf(contact.age) }
    var telephone by remember { mutableStateOf(contact.telephone) }
    var courriel by remember { mutableStateOf(contact.courriel) }
    var adresse by remember { mutableStateOf(contact.adresse) }

    Column(modifier = modifier) {
        OutlinedTextField(
            value = prenom,
            onValueChange = { prenom = it },
            label = { Text(text = "Prenom") },
            modifier = Modifier.fillMaxWidth().padding(top = 36.dp, start = 8.dp, end = 8.dp, bottom = 8.dp)
        )
        OutlinedTextField(
            value = nom,
            onValueChange = {nom = it},
            label = { Text(text = "Nom") },
            modifier = Modifier.fillMaxWidth().padding(8.dp)
        )
        OutlinedTextField(
            value = age.toString(),
            onValueChange = { newValue: String ->
                if (newValue.isNotEmpty() && newValue.all { it.isDigit() })
                    age = newValue.toInt()
                else{
                    age = 0
                }
            },
            label = {Text(text = "Age")},
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number
            ),
            singleLine = true,
            modifier = Modifier.fillMaxWidth().padding(8.dp)
        )
        OutlinedTextField(
            value = telephone,
            onValueChange = {telephone = it},
            label = { Text(text = "Téléphone") },
            modifier = Modifier.fillMaxWidth().padding(8.dp)
        )
        courriel?.let { it1 ->
            OutlinedTextField(
                value = it1,
                onValueChange = {courriel = it},
                label = { Text(text = "Courriel") },
                modifier = Modifier.fillMaxWidth().padding(8.dp)
            )
        }
        adresse?.let { it1 ->
            OutlinedTextField(
                value = it1,
                onValueChange = {adresse = it},
                label = { Text(text = "Adresse") },
                modifier = Modifier.fillMaxWidth().padding(8.dp)
            )
        }
        Row {
            Button(onClick = {
                clicRetour()
                contact.prenom = prenom
                contact.nom = nom
                contact.age = age
                contact.telephone = telephone
                contact.courriel = courriel
                contact.adresse = adresse
                if(ajouter){
                    contactViewModel.add(contact)
                }
                else{
                    contactViewModel.update(contact)
                }
        }, modifier = Modifier.padding(8.dp)) { Text(text = "Sauvegarder") }

            Button(onClick = {
                clicRetour()
            }, modifier = Modifier.padding(8.dp)) { Text(text = "Retourner") }
        }

    }
}


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