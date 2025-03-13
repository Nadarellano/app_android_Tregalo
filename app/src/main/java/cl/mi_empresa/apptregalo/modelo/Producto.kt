package cl.mi_empresa.apptregalo.modelo

data class Producto (
    var codigo: String,
    var nombre: String,
    var descripcion: String,
    var precio: Int,
    var mail: String
)