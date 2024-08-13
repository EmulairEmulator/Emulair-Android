package com.bigbratan.emulair.utils

import androidx.compose.runtime.Immutable

sealed class DataState<out T> {
    data object Idle : DataState<Nothing>()

    data object Loading : DataState<Nothing>()

    @Immutable
    data class Success<out T>(val data: T) : DataState<T>()

    data object Error : DataState<Nothing>()
}

inline fun <T> DataState<T>.perform(
    onIdle: () -> Unit = {},
    onLoading: () -> Unit = {},
    onSuccess: (T) -> Unit = {},
    onError: () -> Unit = {},
) {
    when (this) {
        is DataState.Idle -> onIdle()
        is DataState.Loading -> onLoading()
        is DataState.Success -> onSuccess(data)
        is DataState.Error -> onError()
    }
}