package ru.skillbranch.devintensive.viewmodels

import android.util.Log
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.skillbranch.devintensive.models.Profile
import ru.skillbranch.devintensive.repositories.PreferencesRepository

class ProfileViewModel : ViewModel() {

    private val repository = PreferencesRepository
    private val _profileData = MutableLiveData<Profile>()
    val profileData: LiveData<Profile>
        get() = _profileData

    private val _appTheme = MutableLiveData<Int>()
    val appTheme: LiveData<Int>
        get() = _appTheme

    init {
        _profileData.value = repository.getProfile()
        _appTheme.value = repository.getAppTheme()
        Log.d("ProfileViewModel", "init")

    }


    fun saveProfileData(profile: Profile) {
        repository.saveProfile(profile)
        _profileData.value = profile
    }

    override fun onCleared() {
        super.onCleared()
        Log.d("ProfileViewModel", "onCleared")
    }

    fun switchTheme() {
        if (_appTheme.value == AppCompatDelegate.MODE_NIGHT_YES)
            _appTheme.value = AppCompatDelegate.MODE_NIGHT_NO
        else
            _appTheme.value = AppCompatDelegate.MODE_NIGHT_YES


        repository.saveAppTheme(appTheme.value!!)
    }
}