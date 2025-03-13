package cl.mi_empresa.apptregalo.util

import cl.mi_empresa.apptregalo.servicio.ApiServicio
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {
    val api: ApiServicio by lazy {
        Retrofit.Builder()
            .baseUrl("https://programadormaldito.cl/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiServicio::class.java)
    }
}