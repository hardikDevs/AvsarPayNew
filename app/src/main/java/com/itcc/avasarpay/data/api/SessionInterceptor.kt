package com.itcc.avasarpay.data.api


import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.content.ContextCompat
import com.itcc.avasarpay.ui.home.DashboardActivity
import com.itcc.stonna.utils.Logger
import com.itcc.stonna.utils.SessionManager
import dagger.hilt.android.qualifiers.ApplicationContext
import okhttp3.Interceptor
import okhttp3.Response
import org.json.JSONObject
import java.io.IOException
import javax.inject.Inject


class SessionInterceptor @Inject constructor(@ApplicationContext context: Context) : Interceptor {

    private val context = context.applicationContext

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val original = chain.request()
        val session = SessionManager(context)

        val builder = original.newBuilder()
        builder.header("Accept", "application/json")
        if (session.isLoggedIn) {
            //  var token = session.login.token
              var token = ""

             Log.d("UserToken", token)
            builder.header("Authorization", "Bearer "+token)
        }
        builder.method(original.method, original.body)
        val request = builder.build()
        val response = chain.proceed(request)
        if (response.code == 401) {
            Logger.e("Session Expired : Endpoint", response.request.url.encodedPath)

            val intent = Intent(context, DashboardActivity::class.java)
            try {
                val jsonObject = response.body?.string()?.let { JSONObject(it) }
                val msg = jsonObject?.optString("msg")
              //  intent.putExtra(Constant.ERROR, msg)
            } catch (e: Exception) {
               // intent.putExtra(Constant.ERROR, "${e.message}")
            }
            session.clearSession()
           // intent.putExtra(Constant.UNAUTHORIZED, response.code)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            ContextCompat.startActivity(context, intent, null)
        }
        return response
    }
}