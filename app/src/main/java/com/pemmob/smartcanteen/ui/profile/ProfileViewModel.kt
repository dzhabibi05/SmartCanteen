package com.pemmob.smartcanteen.ui.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pemmob.smartcanteen.data.model.User
import com.pemmob.smartcanteen.data.repository.CanteenRepository
import com.pemmob.smartcanteen.data.repository.FakeCanteenRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

class ProfileViewModel(
    private val repository: CanteenRepository = FakeCanteenRepository
) : ViewModel() {

    val currentUser: StateFlow<User> = repository.currentUser
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = repository.currentUser.value
        )
}
