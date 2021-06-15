package ru.tashkent.gora.repositories

import retrofit2.Response
import ru.tashkent.gora.data.remote.Photo
import ru.tashkent.gora.data.remote.User
import ru.tashkent.gora.exceptions.Failure
import ru.tashkent.gora.functional.Either
import ru.tashkent.gora.network.JsonPlaceholderApi
import ru.tashkent.gora.network.NetworkHandler
import java.net.SocketTimeoutException

class Repository(
    private val service: JsonPlaceholderApi,
    private val networkHandler: NetworkHandler
) {

    suspend fun users(): Either<Failure, List<User>> {
        if (!networkHandler.isNetworkAvailable()) {
            return Either.Left(Failure.NetworkError)
        }
        return safeNetworkAction {
            handleResponse(
                service.users(), { it }, emptyList()
            )
        }
    }

    suspend fun photosForUser(userId: Int): Either<Failure, List<Photo>> {
        if (!networkHandler.isNetworkAvailable()) {
            return Either.Left(Failure.NetworkError)
        }
        return safeNetworkAction {
            val albums =
                service.albumsForUser(userId).body()
                    ?: return@safeNetworkAction Either.Left(Failure.ServerError)
            Either.Right(albums.flatMap {
                service.photosForAlbum(it.id).body() ?: emptyList()
            })
        }
    }

    private suspend fun <T> safeNetworkAction(
        action: suspend () -> Either<Failure, T>
    ): Either<Failure, T> {
        return try {
            action()
        } catch (exc: SocketTimeoutException) {
            Either.Left(Failure.TimeoutError)
        } catch (exc: Throwable) {
            Either.Left(Failure.ServerError)
        }
    }

    private fun <F, T> handleResponse(
        response: Response<F>,
        transform: (F) -> T,
        default: F
    ): Either<Failure, T> {
        return when (response.isSuccessful) {
            true -> Either.Right(transform(response.body() ?: default))
            false -> Either.Left(Failure.ServerError)
        }
    }
}