package lnx.jetitable.hilt

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import lnx.jetitable.BuildConfig
import lnx.jetitable.api.github.GithubApiService
import lnx.jetitable.api.timetable.HtmlConverterFactory
import lnx.jetitable.api.timetable.TimeTableApiService
import lnx.jetitable.datastore.CookieDataStore
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Inject
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ApiModule {

    @Provides
    @Singleton
    fun provideOkHttpClient(cookieDataStore: CookieDataStore): OkHttpClient {
        return OkHttpClient.Builder()
            .cookieJar(cookieDataStore)
            .addInterceptor(
                HttpLoggingInterceptor().apply {
                    level = if (BuildConfig.DEBUG) {
                        HttpLoggingInterceptor.Level.BODY
                    } else {
                        HttpLoggingInterceptor.Level.NONE
                    }
                }
            ).build()
    }

    @Provides
    @Singleton
    fun provideRetrofitBuilder(client: OkHttpClient): Retrofit.Builder {
        return Retrofit.Builder()
            .addConverterFactory(HtmlConverterFactory())
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
    }

    @Provides
    @Singleton
    fun provideTimeTableApiService(retrofitBuilder: Retrofit.Builder): TimeTableApiService {
        return retrofitBuilder
            .baseUrl(TimeTableApiService.BASE_URL)
            .build()
            .create(TimeTableApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideGithubApiService(retrofitBuilder: Retrofit.Builder): GithubApiService {
        return retrofitBuilder
            .baseUrl(GithubApiService.BASE_URL)
            .build()
            .create(GithubApiService::class.java)
    }
}