package cl.mi_empresa.apptregalo

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.room.Room
import cl.mi_empresa.apptregalo.db.AppDatabase
import kotlinx.coroutines.launch
import androidx.compose.runtime.rememberCoroutineScope
import cl.mi_empresa.apptregalo.modelo.UsuarioLogin

@Composable
fun MostrarProductos(navController: NavController, usuarioLogin: UsuarioLogin) {
    val context = LocalContext.current
    val db = remember { Room.databaseBuilder(context, AppDatabase::class.java, "app_database").allowMainThreadQueries().build() }
    val productoDao = remember { db.productoDao() }
    val productos by productoDao.getProductos(usuarioLogin.mail).collectAsState(initial = emptyList())
    val scope = rememberCoroutineScope()

    Scaffold(
        bottomBar = { CustomBottomBuscar(navController = navController) } // Barra de navegación en el fondo
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
        ) {
            CustomTopBar2(title = "Mis Productos para Regalar", navController = navController)

            LazyColumn(
                modifier = Modifier.weight(1f)
            ) {
                items(productos) { producto ->
                    ProductoCard(producto = producto, productoDao = productoDao)
                }
            }

            Button(
                onClick = {
                    scope.launch {
                        productoDao.deleteAll(usuarioLogin.mail)
                        Toast.makeText(context, "Todos los productos han sido eliminados", Toast.LENGTH_SHORT).show()
                    }
                },
                colors = ButtonDefaults.buttonColors(Color(0xFF8B0000)),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text("Eliminar Todos Los Productos", color = Color.White)
            }
        }
    }
}
