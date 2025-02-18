package com.nicolaischirmer.proyectodb.ui.navigation.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.nicolaischirmer.proyectodb.firebase.FirestoreManager
import com.nicolaischirmer.proyectodb.firebase.model.Weapons
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class InicioViewModel(val firestoreManager: FirestoreManager) : ViewModel() {

    val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState

    private val _weapon = MutableStateFlow<Weapons?>(null)
    val weapon: StateFlow<Weapons?> = _weapon

    init {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            firestoreManager.getWeapons().collect { weapons ->
                _uiState.update { uiState ->
                    uiState.copy(
                        weapons = weapons,
                        isLoading = false
                    )
                }
            }
        }
    }

    fun addWeapon(weapon: Weapons) {
        viewModelScope.launch {
            firestoreManager.addWeapons(weapon)
        }
    }

    fun deleteWeaponById(weaponId: String) {
        if (weaponId.isEmpty()) return
        viewModelScope.launch {
            firestoreManager.deleteWeaponById(weaponId)
        }
    }

    fun updateWeapon(weaponNew: Weapons) {
        viewModelScope.launch {
            firestoreManager.updateWeapons(weaponNew)
        }
    }

    fun getWeaponById(weaponId: String) {
        viewModelScope.launch {
            _weapon.value = firestoreManager.getWeaponById(weaponId)
        }
    }

    fun onAddWeaponSelected() {
        _uiState.update { it.copy(showAddWeaponDialog = true) }
    }

    fun dismisShowAddWeaponDialog() {
        _uiState.update { it.copy(showAddWeaponDialog = false) }
    }

    fun onLogoutSelected() {
        _uiState.update { it.copy(showLogoutDialog = true) }
    }

    fun dismisShowLogoutDialog() {
        _uiState.update { it.copy(showLogoutDialog = false) }
    }
}

data class UiState(
    val weapons: List<Weapons> = emptyList(),
    val isLoading: Boolean = false,
    val showAddWeaponDialog: Boolean = false,
    val showLogoutDialog: Boolean = false
)

class InicioViewModelFactory(private val firestoreManager: FirestoreManager) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return InicioViewModel(firestoreManager) as T
    }
}