package lnx.jetitable.api

import android.content.Context
import lnx.jetitable.BuildConfig
import lnx.jetitable.api.github.GithubApiService
import lnx.jetitable.api.timetable.HtmlConverterFactory
import lnx.jetitable.api.timetable.TimeTableApiService
import lnx.jetitable.datastore.CookieDataStore
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitHolder(context: Context) {
    private val cookieDataStore = CookieDataStore(context)
    private val okHttpClient = OkHttpClient.Builder()
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

    private val retrofit = Retrofit.Builder()
        .baseUrl(TimeTableApiService.BASE_URL)
        .addConverterFactory(HtmlConverterFactory())
        .addConverterFactory(GsonConverterFactory.create())
        .client(okHttpClient)
        .build()


    companion object {
        private var TIMETABLE: TimeTableApiService? = null
        private var GITHUB: GithubApiService? = null

        fun getTimeTableApiInstance(context: Context): TimeTableApiService {
            if (TIMETABLE == null) {
                TIMETABLE = RetrofitHolder(context).retrofit.create(TimeTableApiService::class.java)
            }
            return TIMETABLE!!
        }

        fun getGitHubApiInstance(): GithubApiService {
            if (GITHUB == null) {
                val client = OkHttpClient.Builder()
                    .addInterceptor(
                        HttpLoggingInterceptor().apply {
                            level = if (BuildConfig.DEBUG) {
                                HttpLoggingInterceptor.Level.BODY
                            } else {
                                HttpLoggingInterceptor.Level.NONE
                            }
                        }
                    ).build()
                GITHUB = Retrofit.Builder()
                    .baseUrl("https://api.github.com/")
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
                    .create(GithubApiService::class.java)
            }
            return GITHUB!!
        }
    }
}