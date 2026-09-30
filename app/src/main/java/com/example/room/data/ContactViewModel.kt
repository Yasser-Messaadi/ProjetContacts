package com.example.room.data

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ContactViewModel(application: Application) : AndroidViewModel(application) {
    private val dao: ContactDao = AppDatabase.getDatabase(application.applicationContext).contactDao()

    val contacts: StateFlow<List<Contact>> = dao.getAll()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun add(contact: Contact) = viewModelScope.launch(Dispatchers.IO) {
        dao.insert(contact)
    }

    fun update(contact: Contact) = viewModelScope.launch(Dispatchers.IO) {
        dao.update(contact)
    }

    fun delete(contact: Contact) = viewModelScope.launch(Dispatchers.IO) {
        dao.delete(contact)
    }
}
