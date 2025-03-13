package cl.mi_empresa.apptregalo

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import cl.mi_empresa.apptregalo.dao.ProductoDAO
import cl.mi_empresa.apptregalo.modelo.Producto
import cl.mi_empresa.apptregalo.modelo.ProductosxMailItem
import cl.mi_empresa.apptregalo.modelo.ProductsResponse
import cl.mi_empresa.apptregalo.modelo.Usuario
import cl.mi_empresa.apptregalo.modelo.UsuarioLogin
import cl.mi_empresa.apptregalo.ui.theme.AppTregaloTheme
import cl.mi_empresa.apptregalo.util.RetrofitInstance
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
class MainViewModel : ViewModel() {
    val isLoading = mutableStateOf(true)
    private val _userCreationResult = MutableStateFlow<UserCreationResult?>(null)
    val userCreationResult = _userCreationResult.asStateFlow()
    private val _productosxMail = MutableStateFlow<List<ProductosxMailItem>>(emptyList())

    var mailLogin by mutableStateOf("")
    var passLogin by mutableStateOf("")

    var allProduct by mutableStateOf<List<ProductsResponse>>(emptyList())

    fun createUser(mail: String, pass: String, nombre: String, apellido: String) {
        val usuarioCrear = Usuario(mail, pass, nombre, apellido)

        viewModelScope.launch {
            isLoading.value = true
            try {
                val response = RetrofitInstance.api.usuarioAlmacenar(usuarioCrear)
                if (response.isSuccessful) {
                    val respuesta = response.body()?.get(0)?.RESPUESTA
                    if (respuesta == "NOK") {
                        _userCreationResult.value = UserCreationResult.Error("El usuario ya existe o datos ingresados invalidos")
                        println("HC: El usuario ya existe")

                    } else {
                        _userCreationResult.value = UserCreationResult.Success(respuesta)
                        println("HC: Usuario creado exitosamente")
                    }
                } else {
                    _userCreationResult.value = UserCreationResult.Error(response.message())
                    println("HC: Credenciales invalidas")
                }
            } catch (e: Exception) {
                _userCreationResult.value = UserCreationResult.Error("Error de conexión")
                println("HC: Error de conexión")
            } finally {
                isLoading.value = false
                resetUserCreationResult()
            }
        }
    }

    fun loginUsuario(mail: String, pass: String) {
        val usuario = UsuarioLogin(mail, pass)

        viewModelScope.launch {
            isLoading.value = true
            try {
                val response = RetrofitInstance.api.usuarioLogin(usuario = usuario)

                if (response.isSuccessful) {
                    val respuesta = response.body()?.get(0)?.RESPUESTA
                    if (respuesta == "LOGIN OK") {
                        mailLogin = mail
                        passLogin = pass

                        println("akm5: mailLogin = $mailLogin")

                        _userCreationResult.value = UserCreationResult.Success(respuesta)
                    } else {
                        println("RESPUESTA ERROR: Invalid credentials")
                        _userCreationResult.value = UserCreationResult.Error("Credenciales inválidas")
                    }
                } else {
                    println("RESPUESTA ERROR: ${response.message()}")
                    _userCreationResult.value = UserCreationResult.Error(response.message())
                }
            } catch (e: Exception) {
                println("Exception occurred: ${e.message}")
                _userCreationResult.value = UserCreationResult.Error(e.message ?: "Ha ocurrido un error")
            } finally {
                isLoading.value = false
                resetUserCreationResult()
            }
        }

    }

    // Inicializamos la carga de productos por correo
    fun cargarProductosxMail(mailLogueado: String) {
        viewModelScope.launch {
            isLoading.value = true
            try {
                // Limpiamos la lista de productos para cada búsqueda
                _productosxMail.value = emptyList()

                val response = RetrofitInstance.api.obtenerProductosxMail(mailLogueado)
                if (response.isSuccessful && response.body() != null) {
                    println("NA: Respuesta exitosa: ${response.body()}")
                    val responseBody = response.body()!!

                    // Seguridad en la respuesta (Deserealizar)
                    val productosxmail = (responseBody[0] as List<Map<String, Any>>).map { productoMap ->
                        ProductosxMailItem(
                            pCodigo = productoMap["p_codigo"] as String,
                            pNombre = productoMap["p_nombre"] as String,
                            pDescripcion = productoMap["p_descripcion"] as String,
                            pPrecio = (productoMap["p_precio"] as Double).toInt(),
                            pMailCreado = productoMap["p_mail_creado"] as String
                        )
                    }
                    _productosxMail.value = productosxmail
                    println("NA: Productos cargados correctamente: $productosxmail")
                } else {
                    println("NA: Respuesta fallida o vacía: ${response.message()}")
                    _productosxMail.value = emptyList()
                }
            } catch (e: Exception) {
                println("NA: Excepción al obtener productos: ${e.message}")
                _productosxMail.value = emptyList()
            } finally {
                isLoading.value = false
                resetUserCreationResult()
            }
        }
    }



    fun crearProducto(nombre: String, descripcion: String) {
        viewModelScope.launch {
            val CodigoRange = 1000..10000
            var codigo_ = CodigoRange.random()

            var producto_crear = Producto(codigo_.toString(), nombre, descripcion, 0, mailLogin)
            println("akm2" + producto_crear.mail)
            println("akm3" + producto_crear.nombre)

            println(" azar " + codigo_)


            isLoading.value = true
            var response = RetrofitInstance.api.productoAlmacenar(producto_crear)
            if (response.isSuccessful) {
                val respuesta = response.body()?.get(0)?.RESPUESTA
                println("resp " + respuesta)
                _userCreationResult.value = UserCreationResult.Success(respuesta)
            } else {
                _userCreationResult.value = UserCreationResult.Error(response.message())
            }
            isLoading.value = false
            resetUserCreationResult()
        }
    }

    fun resetUserCreationResult() {
        viewModelScope.launch {
            delay(1000)
            _userCreationResult.value = null
        }
    }

    sealed class UserCreationResult {
        data class Success(val data: Any?): UserCreationResult()
        data class Error(val message: String): UserCreationResult()
    }
}



class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppTregaloTheme {
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    MiAplicacion()
                }
            }
        }
    }
}

@Composable
fun MiAplicacion() {
    val navController = rememberNavController()
    val viewModel: MainViewModel = viewModel()

    NavHost(navController = navController, startDestination = "login") {
        composable("login") { PantallaLogin(navController, viewModel) }
        composable("crear-usuario") { CrearCuenta(navController) }
        composable("segunda-pantalla") { SegundaPantalla(navController, viewModel) }
        composable("perfil-usuario") { PerfilUsuario(navController, viewModel) }
        composable("ingresar-producto") {
            val usuarioLogin = UsuarioLogin(viewModel.mailLogin, viewModel.passLogin)
            IngresarProducto(navController, usuarioLogin)
        }
        composable("mostrar-productos") {
            val usuarioLogin = UsuarioLogin(viewModel.mailLogin, viewModel.passLogin)
            MostrarProductos(navController, usuarioLogin)
        }
        composable("buscar-productos") { BuscarProductos(navController) }


    }
}

@Composable
fun PantallaLogin(navController: NavController, viewModel: MainViewModel) {
    var mail by remember { mutableStateOf("") }
    var pass by remember { mutableStateOf("") }

    // VIEW MODEL
    val context = LocalContext.current
    //var viewModel: MainViewModel = viewModel()
    val userCreationResult by viewModel.userCreationResult.collectAsState()

    LaunchedEffect(userCreationResult) {
        when (val result = userCreationResult) {
            is MainViewModel.UserCreationResult.Success -> {
                val respuesta = result.data as String

                if(respuesta == "LOGIN OK") {
                    Toast.makeText(context, "Credenciales Correctas",
                        Toast.LENGTH_LONG).show()
                    navController.navigate("segunda-pantalla")
                    println("AKM: USUARIO CORRECTO. NAVEGAMOS!!!")
                } else {
                    Toast.makeText(context, "Credenciales Inválidas",
                        Toast.LENGTH_LONG).show()
                }
            }
            is MainViewModel.UserCreationResult.Error -> {
                Toast.makeText(context, result.message, Toast.LENGTH_LONG).show()
                println("NA: ${result.message}")
            }
            null -> {

            }
        }
    }


    // PANTALLA
    Surface(color = MaterialTheme.colorScheme.background) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center

        ) {
            Spacer(modifier = Modifier.height(10.dp))

            Box(
                modifier = Modifier
                    .size(220.dp)
                    .clip(CircleShape)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.iconoapp),
                    contentDescription = "ícono app",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(300.dp)
                        .clip(CircleShape)
                )
            }
            Text(
                text = "Tregalo",
                style = MaterialTheme.typography.headlineLarge,
                color = Color(0xFF00796B), // Verde oscuro
                modifier = Modifier.padding(top = 8.dp)
            )
            Text(
                text = "Aplicación de Donaciones",
                style = MaterialTheme.typography.headlineSmall,
                color = Color(0xFF00796B), // Verde oscuro
                modifier = Modifier.padding(top = 8.dp)
            )
            Spacer(modifier = Modifier.height(22.dp))
            OutlinedTextField(
                value = mail,
                onValueChange = { mail = it },
                label = { Text("Correo Electrónico") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next)
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = pass,
                onValueChange = { pass = it },
                label = { Text("Contraseña") },
                modifier = Modifier.fillMaxWidth(),
                visualTransformation = PasswordVisualTransformation(),
                singleLine = true,
                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done)
            )
            Spacer(modifier = Modifier.height(32.dp))
            Button(
                onClick = {
                    if (mail.isBlank() || pass.isBlank()) {
                        Toast.makeText(context, "Por favor, ingrese correo y contraseña", Toast.LENGTH_SHORT).show()
                    } else {
                        viewModel.loginUsuario(mail, pass)
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(4.dp)
            ) {
                Text("Ingresar", style = MaterialTheme.typography.bodyLarge)
            }
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = {
                    navController.navigate("crear-usuario")

                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(4.dp),

                ) {
                Text("Crear Cuenta", style = MaterialTheme.typography.bodyLarge)
            }
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}
