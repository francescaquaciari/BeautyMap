package com.example.beautymap.ui.common

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalPermissionsApi::class)                                      //serve per gestire i permessi
@Composable                                                                                  //la fun successiva crea un componente per l'interfaccia utente
fun PermissionGate (
    permissions: List<String> = emptyList(),
    onPermissionsAllowed: @Composable () -> Unit = {},                                       //parametro che accetta la schermata da mostrare quando i permessi sono concessi
){
    if (permissions.isEmpty()){
        onPermissionsAllowed()                                                                //se non ci sono permessi mostra la schermata
        return
    }
    // Inizializza lo stato dei permessi forniti dalla libreria Accompanist
    val permissionState = rememberMultiplePermissionsState(permissions)                         //memorizza lo stato attuale dei permessi
    // Se TUTTI i permessi nella lista sono stati concessi...
    if (permissionState.allPermissionsGranted){
        onPermissionsAllowed()      // ...mostra il contenuto protetto
    } else {

        if (permissionState.shouldShowRationale) {       // L'utente ha rifiutato una volta, ma il sistema ci permette di riprovare spiegando perché il permesso è necessario.


            val isDialogVisible = remember { mutableStateOf(true) }              //mostra il popup di spiegazione
            if (isDialogVisible.value) {
                PermissionDialog(
                    title = "Permission required",
                    message = "Permission required to show my location on map",
                    onDismiss = {                                                                 //se l'utente annulla
                        isDialogVisible.value = false
                    },
                    onConfirm = {                                                                 //se l'utente conferma
                        permissionState.launchMultiplePermissionRequest()       // Rilancia la richiesta di sistema
                    }
                )
            }
        } else {
            SideEffect {
                permissionState.launchMultiplePermissionRequest()                             //popup nativo di sistema per richiedere i permessi
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PermissionDialog (
    title: String = "Title",
    message: String = "Message",
    onDismiss: () -> Unit = {},
    onConfirm: () -> Unit = {},
) {
    BasicAlertDialog(
        onDismissRequest = onDismiss,
    ) {
        ElevatedCard {
            Column(
                modifier = Modifier.padding(16.dp)
            ){
                Text(
                    text = title,
                    style = typography.titleMedium
                )

                //componente Text per mostrare il messaggio
                Text(
                    text = message,
                    style = typography.bodyMedium,
                    modifier = Modifier.padding(top = 8.dp)
                )

                Row(
                    modifier = Modifier.fillMaxWidth().padding(top = 16.dp),
                    horizontalArrangement = Arrangement.End
                ){
                    OutlinedButton (
                        onClick = onDismiss,
                    ){
                        Text("Cancel")
                    }

                    Button(
                        onClick = onConfirm,
                        modifier = Modifier.padding(start = 8.dp)
                    ){
                        Text("Request")
                    }
                }
            }
        }
    }
}
