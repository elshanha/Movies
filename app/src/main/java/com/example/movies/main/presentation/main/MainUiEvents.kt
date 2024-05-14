package com.example.movies.main.presentation.main

sealed class MainUiEvents {
    data class Refresh(val type: String) : MainUiEvents()
    data class OnPaginate(val type: String) : MainUiEvents()
    data class AddToFavorites(val type: String) : MainUiEvents()
}