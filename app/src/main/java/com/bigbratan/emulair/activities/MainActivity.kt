package com.bigbratan.emulair.activities

import android.content.Context
import android.hardware.input.InputManager
import android.os.Bundle
import android.view.InputDevice
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.bigbratan.emulair.navigation.Navigation
import com.bigbratan.emulair.ui.theme.EmulairTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private var isGamePadConnected = mutableStateOf(false)

    private val inputDeviceListener = object : InputManager.InputDeviceListener {
        override fun onInputDeviceAdded(deviceId: Int) {
            updateGamePadStatus()
        }

        override fun onInputDeviceRemoved(deviceId: Int) {
            updateGamePadStatus()
        }

        override fun onInputDeviceChanged(deviceId: Int) {
            updateGamePadStatus()
        }
    }

    private fun updateGamePadStatus() {
        val inputManager = getSystemService(Context.INPUT_SERVICE) as InputManager
        val inputDevices = inputManager.inputDeviceIds

        isGamePadConnected.value = inputDevices.any { deviceId ->
            val device = inputManager.getInputDevice(deviceId)
            device?.sources?.and(InputDevice.SOURCE_GAMEPAD) == InputDevice.SOURCE_GAMEPAD
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        super.onCreate(savedInstanceState)

        WindowInsetsControllerCompat(window, window.decorView).let { controller ->
            controller.hide(WindowInsetsCompat.Type.systemBars())
            controller.systemBarsBehavior =
                WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        }

        val inputManager = getSystemService(Context.INPUT_SERVICE) as InputManager
        inputManager.registerInputDeviceListener(inputDeviceListener, null)

        updateGamePadStatus()

        setContent {
            EmulairTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    Navigation(isGamePadConnected = isGamePadConnected)
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        val inputManager = getSystemService(Context.INPUT_SERVICE) as InputManager
        inputManager.unregisterInputDeviceListener(inputDeviceListener)
    }
}