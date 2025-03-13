package cl.mi_empresa.apptregalo

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight



@Composable
fun SegundaPantalla(navController: NavController, viewModel: cl.mi_empresa.apptregalo.MainViewModel) {
    Scaffold(
        topBar = {
            CustomTopBar(title = "Inicio", navController = navController)
        },

        content = { innerPadding ->
            Column(modifier = Modifier.padding(innerPadding)) {
                // Contenido de la pantalla
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 20.dp)
                        .verticalScroll(rememberScrollState()), // Agregar desplazamiento vertical si es necesario
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp) // Espaciado uniforme entre los elementos
                ){
                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = "Bienvenid@ ",
                        style = MaterialTheme.typography.headlineLarge,
                        color = Color(0xFF00796B), // Verde oscuro
                        modifier = Modifier.padding(top = 8.dp)
                    )
                    Text(
                        text = "\"" + viewModel.mailLogin + "\"",
                        style = MaterialTheme.typography.headlineLarge,
                        color = Color.White, // Verde oscuro
                        modifier = Modifier.padding(top = 8.dp)
                    )

                    Box(
                        modifier = Modifier
                            .clickable {
                                // Navegará al perfil cuando la foto del usuario sea clickeada
                                navController.navigate("perfil-usuario")}
                            .padding(0.1.dp)
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.donar_inicio),
                            contentDescription = "Icono de perfil",
                            modifier = Modifier
                                .size(200.dp)
                                .fillMaxWidth()
                        )
                    }
                    // Aquí van los TextButton

                    TextButton(
                        onClick = {
                            navController.navigate("buscar-productos")
                        },
                        modifier = Modifier
                            .padding(top = 5.dp)
                            .fillMaxWidth(),
                        colors = ButtonDefaults.run { buttonColors(Color(0xFF8BC34A)) } //verde claro
                    ) {
                        Row {
                            val icon: Painter = painterResource(id = R.drawable.buscar)
                            Icon(
                                painter = icon,
                                contentDescription = "Buscar Icon",
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("¿Quieres buscar?", style = MaterialTheme.typography.bodyMedium)
                        }
                    }
                    TextButton(
                        onClick = {
                            navController.navigate("ingresar-producto")
                        },
                        modifier = Modifier
                            .padding(top = 5.dp)
                            .fillMaxWidth(),
                        colors = ButtonDefaults.run { buttonColors(Color(0xFFFFC107)) } //Amarillo oscuro
                    ) {
                        Row {
                            val icon: Painter = painterResource(id = R.drawable.regalar)
                            Icon(
                                painter = icon,
                                contentDescription = "Regalar Icon",
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("¿Quieres regalar?" , style = MaterialTheme.typography.bodyMedium)
                        }
                    }
                    TextButton(
                        onClick = {
                            navController.navigate("mostrar-productos")
                        },
                        modifier = Modifier
                            .padding(top = 5.dp)
                            .fillMaxWidth(),
                        colors = ButtonDefaults.run { buttonColors(Color(0xFF00796B)) } // Verde oscuro
                    ) {
                        Row {
                            val icon: Painter = painterResource(id = R.drawable.editar)
                            Icon(
                                painter = icon,
                                contentDescription = "Editar Icon",
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Editar tus productos" , style = MaterialTheme.typography.bodyMedium)
                        }
                    }
                }
            }
        }
    )
}

@Composable
fun CustomTopBar2(title: String, navController: NavController?) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp),
        color = Color(0xFF00796B)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 12.dp)

        ) {
            if (navController != null) {
                IconButton(onClick = { navController.navigateUp() }) {
                    Icon(Icons.Filled.ArrowBack, contentDescription = "Atrás", tint = Color.White)
                }
            }
            Spacer(modifier = Modifier.width(32.dp))
            Text(text = title, color = Color.White, style = MaterialTheme.typography.titleLarge)
        }
    }
}



@Composable
fun CustomTopBar(title: String, navController: NavController?) {
    var showDialog by remember { mutableStateOf(false) }

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp),
        color = Color(0xFF00796B)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 8.dp)
        ) {
            Spacer(modifier = Modifier.weight(5f)) // Espacio flexible para mover el título a la izquierda
            Text(text = title, color = Color.White, style = MaterialTheme.typography.headlineMedium)
            Spacer(modifier = Modifier.width(270.dp)) // Espacio entre el título y el botón de acción
            if (navController != null) {
                IconButton(onClick = { showDialog = true }) {
                    Icon(
                        painter = painterResource(id = R.drawable.logout),
                        contentDescription = "Cerrar sesión",
                        tint = Color.White,
                        modifier = Modifier.size(600.dp) // Cambia el tamaño aquí
                    )
                }
            }
        }
    }

    if (showDialog) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = Color.Black.copy(alpha = 0.8f) // Fondo oscuro semitransparente
        ) {
            AlertDialog(
                onDismissRequest = { showDialog = false },
                title = {
                    Text(
                        "Cerrar Sesión",
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                },
                text = {
                    Text(
                        "¿Estás seguro de cerrar sesión?",
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                },
                confirmButton = {
                    Button(
                        onClick = {
                            navController?.navigate("login")

                        },
                        colors = ButtonDefaults.buttonColors(Color(0xFF00796B))
                    ) {
                        Text("Sí", color = Color.White)
                    }
                },
                dismissButton = {
                    Button(
                        onClick = {
                            showDialog = false
                        },
                        colors = ButtonDefaults.buttonColors(Color.Transparent),
                        modifier = Modifier.border(1.dp, Color(0xFF00796B), shape = RoundedCornerShape(24.dp)) // Ajusta el tamaño y forma del borde según necesites

                    ) {
                        Text("Cancelar", color = Color.White)
                    }
                }
            )
        }
    }
}

@Composable
fun CustomBottomBar(navController: NavController) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = Color.Transparent
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            FloatingActionButton(
                onClick = { /* Acción del botón de correo */ },
                modifier = Modifier.size(56.dp),
            ) {
                Icon(
                    Icons.Filled.Email, contentDescription = "Email",
                    Modifier.size(36.dp))

            }
            FloatingActionButton(
                onClick = { navController.navigate("segunda-pantalla") },

                modifier = Modifier.size(56.dp),
            ) {
                Icon(
                    Icons.Filled.Home, contentDescription = "Inicio",
                    Modifier.size(36.dp))

            }
            FloatingActionButton(
                onClick = { navController.navigate("perfil-usuario") },
                modifier = Modifier.size(56.dp),
            ) {
                Icon(
                    Icons.Filled.Person, contentDescription = "perfil-usuario",
                    Modifier.size(36.dp))
            }
        }
    }
}