package dts.app.med_app_android.Retrofit

import android.content.Context
import dts.app.med_app_android.Constants
import dts.app.med_app_android.Constants.USER_ROLE

class RoleManager(context: Context) {

    private var prefs = context.getSharedPreferences(Constants.USER_ROLE_FILE, Context.MODE_PRIVATE)

    fun saveRole(role: String) {
        val editor = prefs.edit()
        editor.putString(USER_ROLE, role)
        editor.apply()
    }

    fun getRole(): String? {
        return prefs.getString(USER_ROLE, null)
    }

    fun removeRole() {
        val editor = prefs.edit()
        editor.remove(USER_ROLE)
        editor.apply()
    }
}