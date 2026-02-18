package lnx.jetitable.misc

import kotlinx.coroutines.flow.Flow

interface ConnectivityObserver {
    val isConnected: Flow<ConnectionState>
}