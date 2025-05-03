package com.saeedev.movieadinterviewtest.presentation.selectUser

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.saeedev.movieadinterviewtest.common.onSuccess
import com.saeedev.movieadinterviewtest.domain.model.User
import com.saeedev.movieadinterviewtest.domain.usecase.GetUsersListUseCase
import com.saeedev.movieadinterviewtest.domain.usecase.SetNewUserLoginUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SelectUserBottomSheetViewModel @Inject constructor(
    private val getUsersListUseCase: GetUsersListUseCase,
    private val setNewUserLoginUseCase: SetNewUserLoginUseCase
) : ViewModel() {
    private val _userslList: MutableStateFlow<List<SelectUser>> = MutableStateFlow(emptyList())
    val usersList: StateFlow<List<SelectUser>> = _userslList.asStateFlow()

    init {
        fetchUsers()
    }

    private fun fetchUsers() {
        viewModelScope.launch {
            getUsersListUseCase().collectLatest { usersResult ->
                usersResult.onSuccess { list ->
                    _userslList.emit(
                        list.map { user -> user.toUi() }
                    )
                }
            }
        }
    }

    fun selectNewUser (id : Int){
        viewModelScope.launch {
            setNewUserLoginUseCase(id)
        }
    }
}

data class SelectUser(
    val name: String,
    val id: Int
)

private fun User.toUi() = SelectUser(
    name = name,
    id = id,
)