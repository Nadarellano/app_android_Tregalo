package cl.mi_empresa.apptregalo.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import cl.mi_empresa.apptregalo.model.Producto
import kotlinx.coroutines.flow.Flow

@Dao
interface ProductoDAO {
    @Query("SELECT * FROM producto_table WHERE mail = :mail")
    fun getProductos(mail: String): Flow<List<Producto>>

    @Query("SELECT * FROM producto_table")
    fun getAllProductos(): Flow<List<Producto>>

    @Insert
    suspend fun insert(producto: Producto)

    @Query("DELETE FROM producto_table WHERE mail = :mail")
    suspend fun deleteAll(mail: String)

    @Delete
    suspend fun deleteProducto(producto: Producto)
}


//producto_table