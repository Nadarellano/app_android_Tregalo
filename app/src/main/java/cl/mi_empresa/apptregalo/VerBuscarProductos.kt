package cl.mi_empresa.apptregalo


import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import androidx.room.Room
import cl.mi_empresa.apptregalo.db.AppDatabase
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp

@Composable
fun BuscarProductos(navController: NavController) {
    val context = LocalContext.current
    val db = remember { Room.databaseBuilder(context, AppDatabase::class.java, "app_database").allowMainThreadQueries().build() }
    val productoDao = remember { db.productoDao() }
    val productos by productoDao.getAllProductos ().collectAsState(initial = emptyList())
    val scope = rememberCoroutineScope()

    Scaffold(
        bottomBar = { CustomBottomBuscar(navController = navController) } // Barra de navegación en el fondo
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
        ) {
            CustomTopBar2(title = "Buscar Productos", navController = navController)

            if (productos.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "No hay productos para regalar...")
                }
            } else {
                LazyColumn(
                    modifier = Modifier.weight(1f)
                ) {
                    items(productos) { producto ->
                        ProductosBuscar(producto = producto, productoDao = productoDao)
                    }
                }
            }
        }
    }
}