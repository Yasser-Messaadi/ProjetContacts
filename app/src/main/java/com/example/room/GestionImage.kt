package com.example.room

import android.R.attr.name
import android.content.ActivityNotFoundException
import android.content.ContentValues
import android.content.Context
import android.provider.MediaStore
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil3.Uri
import coil3.compose.AsyncImage


fun creationUriImage(context: Context): android.net.Uri? {
    val values = ContentValues().apply {
        put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
        put(
            MediaStore.Images.Media.DISPLAY_NAME,
            "photo_${System.currentTimeMillis()}.jpg"
        )
    }
    return context.contentResolver.insert(
        MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
        values
    )
}

@Composable
fun PrendrePhoto(){
    var context = LocalContext.current;
    val modifier = null

    var pendingUri by remember{
        mutableStateOf<android.net.Uri?>(null)
    }

    var photoUri by remember{
        mutableStateOf<android.net.Uri?>(null)
    }

    val takePicture = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success ->
        val uri = pendingUri
        if (success && uri != null) {
            photoUri = uri
        } else if (uri != null) {
            context.contentResolver.delete(uri, null, null)
        }
        pendingUri = null
    }

    Button(onClick = {
        val uri = com.example.room.creationUriImage(context)
            ?: return@Button
        pendingUri = uri
        try {
            takePicture.launch(uri)
        } catch (e: ActivityNotFoundException) {
            context.contentResolver.delete(uri, null, null)
            pendingUri = null
        }
    }) {
        Text("Prendre une photo")
    }
    photoUri?.let { uri ->
        AsyncImage(
            model = uri,
            contentDescription = "Photo prise",
            modifier = Modifier.size(200.dp)
        )
    }

}



