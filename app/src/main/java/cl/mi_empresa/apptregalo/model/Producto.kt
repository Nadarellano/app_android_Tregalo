package cl.mi_empresa.apptregalo.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "producto_table")
data class Producto(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val nombre: String,
    val descripcion: String,
    val mail: String

)