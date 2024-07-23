package com.bigbratan.emulair.navigation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NavigationViewModel @Inject constructor() : ViewModel() {
    val startDestination: MutableStateFlow<String?> = MutableStateFlow(null)

    init {
        startDestination.value = Destination.Main.route

        /*if (*//* has shown onboarding *//*) {
            viewModelScope.launch {
                startDestination.value = Destination.Main.route
            }
        } else {
            startDestination.value = Destination.Onboarding.route
        }*/
    }
}