package com.bigbratan.emulair.navigation

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
class NavigationViewModel @Inject constructor() : ViewModel() {
    val startDestinationFlow: MutableStateFlow<String?> = MutableStateFlow(null)

    init {
        startDestinationFlow.value = Destination.Main.route

        /*if (*//* has shown onboarding *//*) {
            viewModelScope.launch {
                startDestination.value = Destination.Main.route
            }
        } else {
            startDestination.value = Destination.Onboarding.route
        }*/
    }
}