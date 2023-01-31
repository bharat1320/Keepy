package com.project.keepy.network

import com.project.keepy.BuildConfig
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Module
@InstallIn(SingletonComponent::class)
object RetrofitInstance {

//    @Provides
//    @Singleton
//    fun createNewsApiService() : NewsApis {
//        return createService().create(NewsApis::class.java)
//    }

    fun createService(): Retrofit {
        return Retrofit.Builder()
            .client(client)
            .baseUrl(BuildConfig.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    var client = OkHttpClient.Builder().addInterceptor(getInterceptor()).build()
//            .addInterceptor(
//                ChuckerInterceptor.Builder(context)
//                    .collector(ChuckerCollector(context))
//                    .maxContentLength(250000L)
//                    .redactHeaders(emptySet())
//                    .alwaysReadResponseBody(false)
//                    .build()
//            ).build()

    private fun getInterceptor(): Interceptor {
        val interceptor = HttpLoggingInterceptor()
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        return interceptor
    }
}