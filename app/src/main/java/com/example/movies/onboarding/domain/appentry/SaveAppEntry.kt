package com.example.movies.onboarding.domain.appentry

import com.example.movies.onboarding.domain.LocalUserManger

class SaveAppEntry(
    private val localUserManger: LocalUserManger
) {

    suspend operator fun invoke(){
        localUserManger.saveAppEntry()
    }

}