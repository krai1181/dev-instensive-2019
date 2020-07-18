package ru.skillbranch.devintensive.viewmodels

import androidx.lifecycle.ViewModel
import ru.skillbranch.devintensive.repositories.ChatRepository

class MainViewModel:ViewModel() {

    val chatRepository = ChatRepository
    init {

    }
}