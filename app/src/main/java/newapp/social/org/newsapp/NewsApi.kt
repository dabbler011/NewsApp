package newapp.social.org.newsapp

import kotlinx.coroutines.Deferred
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface NewsApi {

    @Headers("X-Api-Key:b983f3452aac48229a34adc9974bd5c8")
    @GET("/v2/top-headlines/")
    fun getHeadlines(
        @Query("category") category: String,
        @Query("country") country: String
    ): Deferred<ArticleResponse>

}