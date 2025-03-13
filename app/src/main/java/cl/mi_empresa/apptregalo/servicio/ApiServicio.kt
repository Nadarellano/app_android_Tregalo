package cl.mi_empresa.apptregalo.servicio

import cl.mi_empresa.apptregalo.modelo.Producto
import cl.mi_empresa.apptregalo.modelo.Respuesta
import cl.mi_empresa.apptregalo.modelo.Usuario
import cl.mi_empresa.apptregalo.modelo.UsuarioLogin
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiServicio {

    @POST("route/usuario_duoc_almacenar")
    suspend fun usuarioAlmacenar(@Body usuario: Usuario): Response<List<Respuesta>>

    @POST("route/usuario_duoc_login")
    suspend fun usuarioLogin(@Body usuario: UsuarioLogin): Response<List<Respuesta>>

    @POST("route/producto_duoc_almacenar")
    suspend fun productoAlmacenar(@Body producto: Producto  ): Response<List<Respuesta>>

    @GET("route/producto_duoc_obtener_x_mail")
    suspend fun obtenerProductosxMail(@Query("mail") mailLogueado: String): Response<List<Any>>
}
