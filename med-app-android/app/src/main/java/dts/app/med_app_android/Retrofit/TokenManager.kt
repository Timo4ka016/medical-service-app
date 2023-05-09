package dts.app.med_app_android.Retrofit

import android.content.Context
import dts.app.med_app_android.Constants.PREFS_TOKEN_FILE
import dts.app.med_app_android.Constants.USER_TOKEN

class TokenManager(context: Context) {

    private var prefs = context.getSharedPreferences(PREFS_TOKEN_FILE, Context.MODE_PRIVATE)

    fun saveToken(token: String) {
        val editor = prefs.edit()
        editor.putString(USER_TOKEN, token)
        editor.apply()
    }

    fun getToken(): String? {
        return prefs.getString(USER_TOKEN, null)
    }

    fun removeToken() {
        val editor = prefs.edit()
        editor.remove(USER_TOKEN)
        editor.apply()
    }

}