package cl.mi_empresa.apptregalo


import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.room.Room
import cl.mi_empresa.apptregalo.db.AppDatabase
import cl.mi_empresa.apptregalo.model.Producto
import cl.mi_empresa.apptregalo.modelo.UsuarioLogin

import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IngresarProducto(navController: NavController, usuarioLogin: UsuarioLogin) {
    val context = LocalContext.current
    val db = remember { Room.databaseBuilder(context, AppDatabase::class.java, "app_database").allowMainThreadQueries().build() }
    val productoDao = remember { db.productoDao() }
    val scope = rememberCoroutineScope()

    var nombre by remember { mutableStateOf(TextFieldValue("")) }
    var descripcion by remember { mutableStateOf(TextFieldValue("")) }

    Scaffold(
        bottomBar = { CustomBottomBuscar(navController = navController) }  // Barra de navegación en el fondo
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(20.dp),
        ) {
            CustomTopBar2(title = "Ingresar Producto", navController = navController)

            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(
                value = nombre,
                onValueChange = { nombre = it },
                label = { Text("Escribe el nombre del producto") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
            OutlinedTextField(
                value = descripcion,
                onValueChange = { descripcion = it },
                label = { Text("Escribe la descripción") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
            Spacer(modifier = Modifier.height(10.dp))
            Button(
                onClick = {
                    val nombreStr = nombre.text
                    val descripcionStr = descripcion.text
                    if (nombreStr.isNotBlank() && descripcionStr.isNotBlank()) {
                        scope.launch {
                            productoDao.insert(
                                Producto(
                                    nombre = nombreStr,
                                    descripcion = descripcionStr,
                                    mail = usuarioLogin.mail
                                )
                            )
                        }
                        nombre = TextFieldValue("")
                        descripcion = TextFieldValue("")
                        Toast.makeText(context, "Producto registrado correctamente", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(context, "Falta completar la información del producto", Toast.LENGTH_SHORT).show()
                    }
                },
                colors = ButtonDefaults.buttonColors(Color(0xFF6F9C3B)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Guardar Producto", color = Color.White)
            }
        }
    }
}
