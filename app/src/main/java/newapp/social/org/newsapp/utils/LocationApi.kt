package newapp.social.org.newsapp.utils

import kotlinx.coroutines.Deferred
import newapp.social.org.newsapp.models.Location
import retrofit2.http.GET


interface LocationApi {

    @GET ("?api-key=139f36eac4dc7ad4e9b3329d0acfdf33f8752520550e0fcc99db05bd")
    fun getLocation(): Deferred<Location>

}