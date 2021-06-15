package ru.tashkent.gora.ui.users

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.tashkent.gora.data.local.UserView
import ru.tashkent.gora.data.remote.User
import ru.tashkent.gora.repositories.Repository
import ru.tashkent.gora.ui.core.BaseViewModel
import timber.log.Timber

class UsersViewModel(
    private val repository: Repository
) : BaseViewModel() {

    private val _users = MutableLiveData<List<UserView>>()
    val users: LiveData<List<UserView>> = _users

    fun loadUsers() = viewModelScope.launch {
        repository.users().fold(::handleFailure, ::handleUserList)
    }

    private fun handleUserList(users: List<User>) {
        _users.value = users.map { UserView(it.id, it.name) }
    }
}