package cl.mi_empresa.apptregalo.modelo

data class ProductosxMailItem(
    val pCodigo: String,
    val pNombre: String,
    val pDescripcion: String,
    val pPrecio: Int,
    val pMailCreado: String
)


data class Metadata(
    val fieldCount: Int,
    val affectedRows: Int,
    val insertId: Int,
    val serverStatus: Int,
    val warningCount: Int,
    val message: String,
    val protocol41: Boolean,
    val changedRows: Int
)