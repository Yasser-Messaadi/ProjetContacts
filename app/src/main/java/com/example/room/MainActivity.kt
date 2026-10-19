@file:Suppress("SpellCheckingInspection")

package com.example.room

import android.annotation.SuppressLint
import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
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

enum class Ecran { LISTE, MODIF, AJOUT, LISTEFILTREE }

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
                },
                clicFiltre = {
                    ecran = Ecran.LISTEFILTREE
                },
                filtre = false
            )

        Ecran.LISTEFILTREE ->
            ContactListeView(
                contactViewModel,
                contacts,
                clicContact = {
                    selectionContact = it
                    ecran = Ecran.MODIF
                },
                clicAjout = {
                    ecran = Ecran.AJOUT
                },
                clicFiltre = {
                    ecran = Ecran.LISTE
                },
                filtre = true
            )

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
    clicAjout: () -> Unit,
    clicFiltre: () -> Unit,
    filtre: Boolean
) {
    Scaffold(
        topBar = {
            TopAppBarPrincipale(clicAjout,clicFiltre, filtre)
        },
        modifier = modifier.fillMaxSize()
    ) { padding ->

        if (contacts != null) {
            LazyColumn(
                modifier = modifier.padding(padding)
            )
            {
                items(items = contacts.filter { contact ->
                    contact.favori || !filtre
                })
                { contact ->
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
                            iconeEtoile = when (contact.favori) {
                                true -> Icons.Default.Star
                                false -> Icons.Default.StarBorder
                            }
                            if(!filtre){
                                IconButton(onClick = {
                                    contact.favori = !contact.favori
                                    iconeEtoile = when (contact.favori) {
                                        true -> Icons.Default.Star
                                        false -> Icons.Default.StarBorder
                                    }
                                    contactViewModel.update(contact)
                            }
                            ) { Icon(imageVector = iconeEtoile, contentDescription = "Favori") }}
                            else{Icon(imageVector = Icons.Default.Star, contentDescription = "Favori") }

                        }
                    }
                    HorizontalDivider()
                }
            }
        }

    }
}

@SuppressLint("LocalContextGetResourceValueCall")
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

    val context = LocalContext.current;

    var uriAttente by remember {
        mutableStateOf<Uri?>(null)
    }

    var photoUri by remember {
        mutableStateOf<Uri?>(null)
    }

    var imageChoisie by remember {
        mutableStateOf<Uri?>(contact.photo.toUri())
    }

    val choisirImage = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        if (uri != null) {
            context.contentResolver.takePersistableUriPermission(
                uri,
                Intent.FLAG_GRANT_READ_URI_PERMISSION
            )
            photoUri = uri
            imageChoisie = uri
            Toast.makeText(context, context.getString(R.string.succes), Toast.LENGTH_SHORT).show()
        }
    }

    val prendrePhoto = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { succes ->
        val uri = uriAttente
        if (succes && uri != null) {
            photoUri = uri
            imageChoisie = uri
            Toast.makeText(context, context.getString(R.string.succes), Toast.LENGTH_SHORT).show()
        } else if (uri != null) {
            context.contentResolver.delete(uri, null, null)
            Toast.makeText(context, context.getString(R.string.erreur_camera), Toast.LENGTH_SHORT)
                .show()

        }
        uriAttente = null
    }

    Scaffold(
        topBar = {
            TopAppBarModif(contact, contactViewModel, ajouter, clicRetour)
        },
        modifier = modifier.fillMaxSize()
    ) { padding ->
        val tailleBouton = 150.dp

        Column(modifier = modifier.padding(padding)) {
            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier.fillMaxWidth()
            ) {
                AsyncImage(
                    model = imageChoisie,
                    contentScale = ContentScale.Inside,
                    contentDescription = "",
                    modifier = Modifier
                        .size(150.dp)
                        .padding(top = 8.dp)
                )
            }
            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Button(
                    onClick = {
                        val uri = creationUriImage(context)
                            ?: return@Button
                        uriAttente = uri
                        try {
                            prendrePhoto.launch(uri)
                        } catch (e: ActivityNotFoundException) {
                            context.contentResolver.delete(uri, null, null)
                            uriAttente = null
                        }

                    },
                    modifier = Modifier
                        .padding(8.dp)
                        .width(tailleBouton)
                ) { Text(text = stringResource(R.string.prendre_une_photo)) }
                Button(
                    onClick = {
                        val request = PickVisualMediaRequest(
                            ActivityResultContracts.PickVisualMedia.ImageOnly
                        )
                        choisirImage.launch(request)
                    },
                    modifier = Modifier
                        .padding(8.dp)
                        .width(tailleBouton)
                ) { Text(text = stringResource(R.string.gallerie)) }
            }

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
            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Button(
                    onClick = {
                        clicRetour()
                        contact.prenom = prenom
                        contact.nom = nom
                        contact.age = age
                        contact.telephone = telephone
                        contact.courriel = courriel
                        contact.adresse = adresse
                        if (photoUri != null)
                            contact.photo = photoUri.toString()
                        if (ajouter) {
                            contactViewModel.add(contact)
                            Toast.makeText(
                                context,
                                context.getString(R.string.contact_ajoute),
                                Toast.LENGTH_SHORT
                            ).show()
                        } else {
                            contactViewModel.update(contact)
                            Toast.makeText(
                                context,
                                context.getString(R.string.contact_modifie),
                                Toast.LENGTH_SHORT
                            ).show()

                        }
                    },
                    modifier = Modifier
                        .padding(bottom = 4.dp, top = 8.dp, start = 8.dp, end = 8.dp)
                        .width(tailleBouton)
                ) { Text(text = stringResource(R.string.sauvegarder)) }
                Button(
                    onClick = {
                        clicRetour()
                    },
                    modifier = Modifier
                        .padding(top = 4.dp, bottom = 8.dp, start = 8.dp, end = 8.dp)
                        .width(tailleBouton)

                ) { Text(text = stringResource(R.string.retourner)) }


            }

        }
    }
}