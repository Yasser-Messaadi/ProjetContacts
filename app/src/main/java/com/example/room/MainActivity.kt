package com.example.room

import android.os.Bundle
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
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    ListeContactApp(
                        contactViewModel,
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun ListeContactApp (contactViewModel: ContactViewModel, modifier: Modifier = Modifier) {
    val contacts by contactViewModel.contacts.collectAsState()
    contactViewModel.add(Contact(   prenom = "prenomTest1", nom = "nomTest", telephone = "telephoneTest"))
    contactViewModel.add(Contact(   prenom = "prenomTest2", nom = "nomTest", telephone = "telephoneTest"))
    contactViewModel.add(Contact(   prenom = "prenomTest3", nom = "nomTest", telephone = "telephoneTest"))
    contactViewModel.add(Contact(   prenom = "prenomTest4", nom = "nomTest", telephone = "telephoneTest"))

    Scaffold(
        topBar = {
            TopAppBarPrincipale()
        },
        modifier = modifier.fillMaxSize()
    ) { padding ->
        LazyColumn(modifier = modifier.padding(top = 50.dp, start = 8.dp, end = 8.dp))
        {
            items(contacts) { contact ->
                Row(
                    modifier = Modifier.fillMaxWidth().padding(8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text(text = "${contact.prenom} ${contact.nom} \n ${contact.telephone}")
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(8.dp),
                        horizontalArrangement = Arrangement.End) {
                        Button(onClick = {contactViewModel.delete(contacts[contacts.indexOf(contact)])}){ Text(text = "Effacer") }

                    }
                }
                HorizontalDivider()
            }
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
        expandedHeight = 35.dp
    )
}