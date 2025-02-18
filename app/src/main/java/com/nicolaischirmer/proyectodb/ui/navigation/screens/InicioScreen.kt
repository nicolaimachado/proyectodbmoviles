package com.nicolaischirmer.proyectodb.ui.navigation.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ExitToApp
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.AbsoluteAlignment
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.android.volley.toolbox.ImageRequest
import com.munozcastrovirginia.proyectoapi.ui.screen.DeleteWeaponDialog
import com.nicolaischirmer.proyectodb.R
import com.nicolaischirmer.proyectodb.firebase.AuthManager
import com.nicolaischirmer.proyectodb.firebase.FirestoreManager
import com.nicolaischirmer.proyectodb.firebase.model.Weapons
import coil.request.ImageRequest.Builder

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScreenInicio(
    auth: AuthManager,
    firestore: FirestoreManager,
    navigateToLogin: () -> Unit
) {
    val user = auth.getCurrentUser()
    val factory = InicioViewModelFactory(firestore)
    val inicioViewModel = viewModel(InicioViewModel::class.java, factory = factory)
    val uiState by inicioViewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        horizontalArrangement = Arrangement.Start,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        if (user?.photoUrl != null) {
                            Image(
                                painter = painterResource(R.drawable.ic_usuario),
                                contentDescription = "Foto de perfil por defecto",
                                modifier = Modifier
                                    .padding(end = 8.dp)
                                    .size(40.dp)
                                    .clip(CircleShape)
                            )
                        } else {
                            Image(
                                painter = painterResource(R.drawable.ic_usuario),
                                contentDescription = "Foto de perfil por defecto",
                                modifier = Modifier
                                    .padding(end = 8.dp)
                                    .size(40.dp)
                                    .clip(CircleShape)
                            )

                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = user?.displayName ?: "Anónimo",
                                fontSize = 20.sp,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                            Text(
                                text = user?.email ?: "Sin correo",
                                fontSize = 12.sp,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(
                        ContextCompat.getColor(
                            LocalContext.current,
                            R.color.white
                        )
                    )
                ),
                actions = {
                    IconButton(onClick = {
                        inicioViewModel.onLogoutSelected()
                    }) {
                        Icon(
                            Icons.AutoMirrored.Outlined.ExitToApp,
                            contentDescription = "Cerrar sesión"
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { inicioViewModel.onAddWeaponSelected() },
                containerColor = Color.Gray
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "ADD WEAPON")
            }
        }
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
        ) {
            Column(modifier = Modifier.padding(8.dp)) {
                Text("Lista de armas",  style = TextStyle(fontSize = 24.sp))
            }
            Spacer(modifier = Modifier.height(10.dp))

            if (uiState.showLogoutDialog) {
                LogoutDialog(
                    onDismiss = { inicioViewModel.dismisShowLogoutDialog() },
                    onConfirm = {
                        auth.signOut()
                        navigateToLogin()
                        inicioViewModel.dismisShowLogoutDialog()
                    }
                )
            }

            if (uiState.showAddWeaponDialog) {
                AddWeaponDialog(
                    onWeaponAdded = { weapon ->
                        inicioViewModel.addWeapon(
                            Weapons(
                                id = "",
                                userId = auth.getCurrentUser()?.uid,
                                weapon.name ?: "",
                                weapon.description ?: "",
                                weapon.type ?: "",
                                weapon.damage ?: 0
                            )

                        )
                        inicioViewModel.dismisShowAddWeaponDialog()
                    },
                    onDialogDismissed = { inicioViewModel.dismisShowAddWeaponDialog() },
                    auth
                )
            }

            if (!uiState.weapons.isNullOrEmpty()) {

                LazyColumn(
                    modifier = Modifier.padding(top = 40.dp)
                ) {
                    items(uiState.weapons) { weapon ->
                        WeaponItem(
                            weapon = weapon,
                            deleteWeapon = {
                                inicioViewModel.deleteWeaponById(
                                    weapon.id ?: ""
                                )
                            },
                            updateWeapon = {
                                inicioViewModel.updateWeapon(it)
                            }
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
            } else {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("No hay datos")
                }
            }
        }
    }
}

@Composable
fun WeaponItem(
    weapon: Weapons,
    deleteWeapon: () -> Unit,
    updateWeapon: (Weapons) -> Unit
) {

    var showDeleteWeaponDialog by remember { mutableStateOf(false) }
    var showUpdateWeaponDialog by remember { mutableStateOf(false) }

    if (showDeleteWeaponDialog) {
        DeleteWeaponDialog(
            onConfirmDelete = {
                deleteWeapon()
                showDeleteWeaponDialog = false
            },
            onDismiss = { showDeleteWeaponDialog = false }
        )
    }

    if (showUpdateWeaponDialog) {
        UpdateWeaponDialog(
            weapon = weapon,
            onWeaponUpdated = { weapon ->
                updateWeapon(weapon)
                showUpdateWeaponDialog = false
            },
            onDialogDismissed = { showUpdateWeaponDialog = false }
        )
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { /* No action needed */ },
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row(modifier = Modifier.padding(16.dp)) {
            Column {
                Text(text = weapon.name ?: "", style = MaterialTheme.typography.titleLarge)
                Text(
                    text = "Description: ${weapon.description}",
                    style = MaterialTheme.typography.bodySmall
                )
                Text(
                    text = "Type: ${weapon.type}",
                    style = MaterialTheme.typography.bodySmall
                )
                Text(
                    text = "Damage: ${weapon.damage}",
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
        Row(
            modifier = Modifier
                .padding(16.dp)
                .align(AbsoluteAlignment.Right)
        ) {
            IconButton(
                onClick = { showUpdateWeaponDialog = true }
            ) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = "UPDATE WEAPON"
                )
            }
            IconButton(
                onClick = { showDeleteWeaponDialog = true }
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "DELETE WEAPON"
                )
            }
        }
    }
}

@Composable
fun LogoutDialog(onDismiss: () -> Unit, onConfirm: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Cerrar Sesión") },
        text = {
            Text("¿Estás seguro de que deseas cerrar sesión?")
        },
        confirmButton = {
            Button(onClick = onConfirm) {
                Text("Aceptar")
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    )
}