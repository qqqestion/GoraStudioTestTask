package ru.tashkent.gora.network

import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import ru.tashkent.gora.data.remote.Album
import ru.tashkent.gora.data.remote.Photo
import ru.tashkent.gora.data.remote.User

interface JsonPlaceholderApi {

    @GET("users")
    suspend fun users(): Response<List<User>>

    @GET("albums")
    suspend fun albumsForUser(@Query("userId") userId: Int): Response<List<Album>>

    @GET("photos")
    suspend fun photosForAlbum(@Query("albumId") albumId: Int): Response<List<Photo>>

    companion object {
        fun create(): JsonPlaceholderApi {
            return Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl("https://jsonplaceholder.typicode.com/")
                .build()
                .create(JsonPlaceholderApi::class.java)
        }
    }
}