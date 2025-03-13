package cl.mi_empresa.apptregalo

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController


@Composable
fun PerfilUsuario(navController: NavController, viewModel: MainViewModel) {
    var passwordVisible by remember { mutableStateOf(false) }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CustomTopBar2(title = "Mi Perfil", navController = navController)
        Image(
            painter = painterResource(id = R.drawable.donar_inicio),
            contentDescription = "Foto de perfil",
            modifier = Modifier
                .size(300.dp)
                .clip(CircleShape)
        )
        Spacer(modifier = Modifier.height(5.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp)
                .border(2.dp, Color(0xFF8BC34A), RoundedCornerShape(4.dp))
                .background(Color.Transparent)
                .padding(8.dp)
        ) {
            Column {
                Text(
                    "Mi Correo Electrónico: ${viewModel.mailLogin}",
                    style = MaterialTheme.typography.bodyMedium.copy(fontSize = 16.sp, fontWeight = FontWeight.Bold)
                )
                Spacer(modifier = Modifier.height(25.dp))  // Espacio entre los textos
                PasswordField(viewModel, passwordVisible)
            }
        }
        Spacer(modifier = Modifier.height(15.dp))
        TextButton(
            onClick = {
                //navegar a editar perfil- pendiente cuando el profe habilite get de usuarios.
            },
            modifier = Modifier
                .padding(top = 16.dp)
                .fillMaxWidth(),
            colors = ButtonDefaults.run { buttonColors(Color(0xFF8BC34A)) } //verde claro
        ) {
            Row {
                val icon: Painter = painterResource(id = R.drawable.editar)
                Icon(
                    painter = icon,
                    contentDescription = "editar icon",
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("Editar Perfil")
            }
        }
        Spacer(modifier = Modifier.height(35.dp))
        CustomBottomBar(navController = navController)
    }
}

@Composable
fun PasswordField(viewModel: MainViewModel, passwordVisible: Boolean) {
    var isVisible by remember { mutableStateOf(passwordVisible) }
    Row(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = "Mi Contraseña: " + if (isVisible) viewModel.passLogin else "********",
            style = MaterialTheme.typography.bodyMedium.copy(
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            ),
            modifier = Modifier.weight(1f)
        )
        Icon(
            painter = if (isVisible) painterResource(id = R.drawable.no_ver) else painterResource(id = R.drawable.ver),
            //painter = painterResource(id = R.drawable.ver),
            //painter = painterResource(id = R.drawable.no_ver)
            contentDescription = if (isVisible) "Ocultar contraseña" else "Mostrar contraseña",
            modifier = Modifier
                .clickable { isVisible = !isVisible }
                .padding(8.dp)
                .size(24.dp),
            tint = Color.Gray
        )
    }
}

