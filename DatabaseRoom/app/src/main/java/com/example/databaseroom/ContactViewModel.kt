package com.example.databaseroom

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.databaseroom.data.Contact
import com.example.databaseroom.data.ContactDao
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ContactViewModel(
    private val dao: ContactDao
): ViewModel() {

    private val _sortType = MutableStateFlow(SortType.FIRST_NAME)
    @OptIn(ExperimentalCoroutinesApi::class)
    private val _contacts = _sortType
        .flatMapLatest { sortType ->
            when(sortType) {
                SortType.FIRST_NAME -> dao.getContactsOrderByFistName()
                SortType.LAST_NAME -> dao.getContactsOrderByLastName()
                SortType.PHONE_NUMBER -> dao.getContactsOrderByPhoneNumber()
            }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())
    private val _state = MutableStateFlow(ContactState())
    val state = combine(_state, _sortType, _contacts) { state, sortType, contacts ->
        state.copy(
            contacts = contacts,
            sortType = sortType
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(500), ContactState())

    fun onEvent(event: ContactEvent) {
        when(event){
            is ContactEvent.DeleteContact -> {
                viewModelScope.launch {
                    dao.deleteContact(event.contacts)
                }
            }
            ContactEvent.HideDialog -> {
                _state.update { it.copy(
                    isAddingContact = false
                ) }
            }
            ContactEvent.SaveContact -> {
                val firstName = state.value.firstName
                val lastName = state.value.lastName
                val phoneNum = state.value.phoneNum

                if(firstName.isBlank() || lastName.isBlank() || phoneNum.isBlank()) {
                    return
                }

                val contact = Contact(
                    firstName = firstName,
                    lastName = lastName,
                    phoneNumber = phoneNum
                )

                viewModelScope.launch {
                    dao.upsertContact(contact)
                }

                _state.update { it.copy(
                    isAddingContact = false,
                    firstName = "",
                    lastName = "",
                    phoneNum = ""
                ) }
            }
            is ContactEvent.SetFirstName -> {
                _state.update { it.copy(
                    firstName = event.firstName
                ) }
            }
            is ContactEvent.SetLastName -> {
                _state.update { it.copy(
                    lastName = event.lastName
                ) }
            }
            is ContactEvent.SetPhoneNum -> {
                _state.update { it.copy(
                    phoneNum = event.phoneNum
                ) }
            }
            ContactEvent.ShowDialog -> {
                _state.update { it.copy(
                    isAddingContact = true
                ) }
            }
            is ContactEvent.SortContacts -> {
                _sortType.value = event.sortType
            }
        }
    }
}