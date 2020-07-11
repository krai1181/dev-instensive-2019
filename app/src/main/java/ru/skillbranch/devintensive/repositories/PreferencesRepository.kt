package ru.skillbranch.devintensive.repositories

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import androidx.appcompat.app.AppCompatDelegate
import ru.skillbranch.devintensive.App
import ru.skillbranch.devintensive.models.Profile

object PreferencesRepository {

   private const val FIRSTNAME = "FIRSTNAME"
   private const val LASTNAME = "LASTNAME"
   private const val ABOUT = "ABOUT"
   private const val REPOSITORY = "REPOSITORY"
   private const val RATING = "RATING"
   private const val RESPECT = "RESPECT"
   private const val APP_THEME = "APP_THEME"

    private val PREFS_NAME = "kotlincodes"

    val pref: SharedPreferences by lazy {
        val ctx = App.applicationContext()
        ctx.getSharedPreferences(PREFS_NAME,Context.MODE_PRIVATE)
    }

    fun getProfile(): Profile = Profile(
        pref.getString(FIRSTNAME , "")!!,
        pref.getString(LASTNAME , "")!!,
        pref.getString(ABOUT , "")!!,
        pref.getString(REPOSITORY , "")!!,
        pref.getInt(RATING , 0),
        pref.getInt(RESPECT , 0)
    )

    fun saveAppTheme(theme: Int) {
        putValue(APP_THEME to theme)
    }

    fun getAppTheme(): Int = pref.getInt(APP_THEME, AppCompatDelegate.MODE_NIGHT_NO)

    fun saveProfile(profile: Profile) {
        with(profile){
            putValue(FIRSTNAME to firstName)
            putValue(LASTNAME to lastName)
            putValue(ABOUT to about)
            putValue(REPOSITORY to repository)
            putValue(RATING to rating)
            putValue(RESPECT to respect)
        }

    }

    private fun putValue(pair: Pair<String, Any>) = pref.edit().apply {
        val key = pair.first
        when (val value = pair.second){
            is String -> putString(key,value)
            is Int -> putInt(key,value)
            is Float -> putFloat(key,value)
            is Long -> putLong(key,value)
            is Boolean -> putBoolean(key,value)
            else -> error("Only primitives types")

        }
        apply()
    }


}
