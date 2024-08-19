package lnx.jetitable.timetable.api

import android.content.Context
import lnx.jetitable.BuildConfig
import lnx.jetitable.datastore.CookieManager
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory

class RetrofitHolder(context: Context) {
    private val cookieDataStore = CookieManager(context)
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
        .baseUrl(ApiService.BASE_URL)
        .addConverterFactory(ScalarsConverterFactory.create())
        .addConverterFactory(GsonConverterFactory.create())
        .client(okHttpClient)
        .build()


    companion object {
        private var INSTANCE: ApiService? = null

        fun getInstance(context: Context): ApiService {
            if (INSTANCE == null) {
                INSTANCE = RetrofitHolder(context).retrofit.create(ApiService::class.java)
            }
            return INSTANCE!!
        }
    }
}