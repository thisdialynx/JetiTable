package lnx.jetitable.misc

sealed class DataState<T> {
    data object Loading : DataState<Nothing>()
    data class Success<T>(val data: T) : DataState<T>()
    data class Error(val messageResId: Int, val exception: Throwable? = null) : DataState<Nothing>()
    data object Empty : DataState<Nothing>()
}