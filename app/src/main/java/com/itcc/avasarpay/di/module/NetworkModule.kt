package com.itcc.avasarpay.di.module

import android.content.Context
import com.itcc.avasarpay.data.api.NetworkConnectionInterceptor
import com.itcc.avasarpay.data.api.NetworkService
import com.itcc.avasarpay.data.api.SessionInterceptor
import com.itcc.avasarpay.di.BaseUrl
import com.itcc.avasarpay.utils.AppConstant
import com.itcc.avasarpay.utils.DefaultDispatcherProvider
import com.itcc.avasarpay.utils.DispatcherProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

/**
 *Created By Hardik on 05-03-2024.
 */
@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @BaseUrl
    @Provides
    fun provideBaseUrl() = "https://avasarpay.itcc.net.au/api/"



    @Provides
    @Singleton
    fun provideDispatcherProvider(): DispatcherProvider = DefaultDispatcherProvider()

    @Provides
    @Singleton
    fun providesLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
    }

    @Provides
    @Singleton
    fun providesNetworkConnectionInterceptor(
        @ApplicationContext context: Context
    ): NetworkConnectionInterceptor {
        return NetworkConnectionInterceptor(context)
    }

    @Provides
    @Singleton
    fun providesOkHttpClient(
        loggingInterceptor: HttpLoggingInterceptor,
        networkConnectionInterceptor: NetworkConnectionInterceptor,
        sessionInterceptor: SessionInterceptor,
    ): OkHttpClient {
        return OkHttpClient().newBuilder()
            .connectTimeout(AppConstant.CONNECT_TIMEOUT.toLong(), TimeUnit.SECONDS)
            .readTimeout(AppConstant.READ_TIMEOUT.toLong(), TimeUnit.SECONDS)
            .addInterceptor(networkConnectionInterceptor)
            .addInterceptor(sessionInterceptor)
            .addInterceptor(loggingInterceptor).build()
    }

    @Singleton
    @Provides
    fun provideGsonFactory(): GsonConverterFactory = GsonConverterFactory.create()

    @Provides
    @Singleton
    fun provideNetwork(
        @BaseUrl url: String,
        okHttpClient: OkHttpClient,
        gsonConverterFactory: GsonConverterFactory

    ): NetworkService {

        return Retrofit
            .Builder()
            .baseUrl(url)
            .client(okHttpClient)
            .addConverterFactory(gsonConverterFactory)
            .build()
            .create(NetworkService::class.java)

    }

}