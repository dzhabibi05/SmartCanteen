package com.pemmob.smartcanteen.ui.menu

import com.pemmob.smartcanteen.data.model.MenuItem

sealed interface MenuUiState {
    data object Loading : MenuUiState
    data class Success(val menuList: List<MenuItem>) : MenuUiState
    data class Error(val message: String) : MenuUiState
}
