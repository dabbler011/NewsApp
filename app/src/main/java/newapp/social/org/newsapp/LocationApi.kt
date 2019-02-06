package newapp.social.org.newsapp

import kotlinx.coroutines.Deferred
import retrofit2.http.GET
import retrofit2.http.Headers


interface LocationApi {

    @GET ("?api-key=139f36eac4dc7ad4e9b3329d0acfdf33f8752520550e0fcc99db05bd")
    fun getLocation(): Deferred<Location>

}