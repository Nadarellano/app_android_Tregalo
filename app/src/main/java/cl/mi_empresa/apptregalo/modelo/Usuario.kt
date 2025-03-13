package cl.mi_empresa.apptregalo.modelo

import androidx.room.Entity
import androidx.room.PrimaryKey


data class Usuario (
    var mail: String,
    var pass: String,
    var nombre: String,
    var apellido: String
)
