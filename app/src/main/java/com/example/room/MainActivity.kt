@file:Suppress("SpellCheckingInspection")

package com.example.room

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material3.Button
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
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
                ListeContactApp(
                    contactViewModel
                )
            }
        }
    }
}
enum class Ecran { LISTE, MODIF }
@Composable
fun ListeContactApp(contactViewModel: ContactViewModel,modifier: Modifier = Modifier) {
    val context = LocalContext.current

    val contacts by contactViewModel.contacts.collectAsState()
    var ecran by remember { mutableStateOf(Ecran.LISTE) }

    var selectionContact by remember { mutableStateOf(contacts[0]) }

    when (ecran) {
        Ecran.LISTE -> ContactListeView(contactViewModel)
        Ecran.MODIF -> ModifierContact(selectionContact, modifier)
    }
}

@Composable
fun ContactListeView (contactViewModel: ContactViewModel, modifier: Modifier = Modifier) {
    val contacts by contactViewModel.contacts.collectAsState()
    contactViewModel.add(Contact(   prenom = "John", nom = "Doe", telephone = "(514)-123-4567"))
    contactViewModel.add(Contact(   prenom = "prenomTest2", nom = "nomTest", telephone = "telephoneTest"))
    contactViewModel.add(Contact(   prenom = "prenomTest3", nom = "nomTest", telephone = "telephoneTest"))
    contactViewModel.add(Contact(   prenom = "prenomTest4", nom = "nomTest", telephone = "telephoneTest"))

    Scaffold(
        topBar = {
            TopAppBarPrincipale()
        },
        modifier = modifier.fillMaxSize()
    ) { padding ->
        LazyColumn(modifier = modifier.padding(padding))
        {
            items(contacts) { contact ->
                Row(
                    modifier = Modifier.fillMaxWidth().padding(8.dp),
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
                            contact.favori = !contact.favori;
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
fun ModifierContact(contact: Contact, modifier: Modifier = Modifier)
{
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
            modifier = Modifier.fillMaxWidth().padding(8.dp)
        )
        OutlinedTextField(
            value = nom,
            onValueChange = {nom = it},
            label = { Text(text = "Nom") },
            modifier = Modifier.fillMaxWidth().padding(8.dp)
        )
        OutlinedTextField(
            value = age.toString(),
            onValueChange = { age = it.toInt() },
            label = { Text(text = "Age") },
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
            Button(onClick = { }, modifier = Modifier.padding(8.dp)) { Text(text = "Sauvegarder") }
            Button(onClick = { }, modifier = Modifier.padding(8.dp)) { Text(text = "Retourner") }
        }

    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopAppBarPrincipale() {
    TopAppBar(
        title = { Text(text = "Contact App") },
        actions = {
            IconButton(
                onClick = { /* onClick */ }
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