package com.example.movies.onboarding.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.movies.onboarding.domain.LocalUserManger
import com.example.movies.util.Constants
import com.example.movies.util.Constants.USER_SETTINGS

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


private val readOnlyProperty = preferencesDataStore(name = USER_SETTINGS)

val Context.dataStore: DataStore<Preferences> by readOnlyProperty


class LocalUserMangerImpl(
    private val context: Context
) : LocalUserManger {

    companion object PreferenceKeys {
        private val CALENDER_VIEW_KEY = intPreferencesKey("calender_view_key")
      //  private val DEFAULT_CALENDER_VIEW = CalenderView.MONTHLY.ordinal
        val APP_ENTRY = booleanPreferencesKey(Constants.APP_ENTRY)
    }


    override suspend fun saveAppEntry() {
        context.dataStore.edit { settings ->
            settings[APP_ENTRY] = true
        }
    }

    override fun readAppEntry(): Flow<Boolean> {
        return context.dataStore.data.map { preferences ->
            preferences[APP_ENTRY] ?: false
        }
    }

//    val calenderViewKey: Flow<Int> = context.dataStore.data.map { preferences ->
//        preferences[CALENDER_VIEW_KEY] ?: DEFAULT_CALENDER_VIEW
//    }

    suspend fun setCalenderView(calenderView: Int) {
        context.dataStore.edit { preferences ->
            preferences[CALENDER_VIEW_KEY] = calenderView
        }
    }

}



