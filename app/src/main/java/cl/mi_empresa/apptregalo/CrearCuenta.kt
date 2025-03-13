package cl.mi_empresa.apptregalo

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController

@Composable
fun CrearCuenta(navController: NavController) {
    var mail_new by remember { mutableStateOf("") }
    var pass by remember { mutableStateOf("") }
    var nombre by remember { mutableStateOf("") }
    var apellido by remember { mutableStateOf("") }

    val context = LocalContext.current
    val viewModel: MainViewModel = viewModel()
    val userCreationResult by viewModel.userCreationResult.collectAsState()

    LaunchedEffect(userCreationResult) {
        when (val result = userCreationResult) {
            is MainViewModel.UserCreationResult.Success -> {
                val respuesta = result.data as String

                if (respuesta == "OK") {
                    Toast.makeText(context, "¡Registro Exitoso!", Toast.LENGTH_LONG).show()
                    println("HC: ¡Registro Exitoso!")
                    navController.navigate("login")
                } else {
                    Toast.makeText(context, "Complete el registro", Toast.LENGTH_LONG).show()
                    println("HC: Complete el registro")
                }
            }

            is MainViewModel.UserCreationResult.Error -> {
                Toast.makeText(context, result.message, Toast.LENGTH_LONG).show()
                println("HC: ${result.message}")
            }

            null -> {}
        }
    }

    Surface(
        modifier = Modifier.fillMaxSize().padding(10.dp),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.new_user),
                contentDescription = "Imagen crear cuenta",
                modifier = Modifier
                    .size(200.dp)
                    .clip(CircleShape)
            )
            Spacer(modifier = Modifier.height(3.dp))
            Text(
                text = "Crear cuenta",
                fontSize = 32.sp,
                color = Color(0xFF00796B),
                modifier = Modifier.padding(bottom = 20.dp)
            )
            TextField(
                value = mail_new,
                onValueChange = { mail_new = it },
                textStyle = TextStyle(fontSize = 14.sp, color = Color.White),
                placeholder = { Text("Correo Electrónico", style = typography.bodySmall) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 6.dp)
                    .height(47.dp)
            )
            TextField(
                value = pass,
                onValueChange = { pass = it },
                textStyle = TextStyle(fontSize = 14.sp, color = Color.White),
                placeholder = { Text("Contraseña", style = typography.bodySmall) },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 6.dp)
                    .height(47.dp)
            )
            TextField(
                value = nombre,
                onValueChange = { nombre = it },
                textStyle = TextStyle(fontSize = 14.sp, color = Color.White),
                placeholder = { Text("Nombre", style = typography.bodySmall) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 6.dp)
                    .height(47.dp)
            )
            TextField(
                value = apellido,
                onValueChange = { apellido = it },
                textStyle = TextStyle(fontSize = 14.sp, color = Color.White),
                placeholder = { Text("Apellido", style = typography.bodySmall) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 6.dp)
                    .height(47.dp)
            )
            Spacer(modifier = Modifier.height(10.dp))

            Button(
                onClick = {
                    viewModel.createUser(mail_new, pass, nombre, apellido)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 6.dp, horizontal = 20.dp),
                colors = ButtonDefaults.buttonColors(Color(0xFF00796B))
            ) {
                Text("Registrarse", style = typography.bodyMedium)
            }

            TextButton(
                onClick = { navController.navigate("login") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 6.dp, horizontal = 20.dp),
                colors = ButtonDefaults.buttonColors(Color(0xFF8BC34A))
            ) {
                Text("Ya tengo cuenta", style = typography.bodyMedium)
            }
        }
    }
}
