package com.example.movies.onboarding.presentation

sealed class OnboardingEvent {

    data object SaveAppEntry: OnboardingEvent()

}