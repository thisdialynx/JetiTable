package lnx.jetitable.misc

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import androidx.core.content.getSystemService
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject

enum class ConnectionFailureReason {
    LOST,
    UNAVAILABLE
}
sealed class ConnectionState {
    object Success : ConnectionState()
    object Loading : ConnectionState()
    data class Failure(val reason: ConnectionFailureReason) : ConnectionState()
}
class AndroidConnectivityObserver @Inject constructor(
    @ApplicationContext private val context: Context
) : ConnectivityObserver {
    private val connectivityManager = context.getSystemService<ConnectivityManager>()!!
    override val isConnected: Flow<ConnectionState>
        get() = callbackFlow {
            val callback = object : ConnectivityManager.NetworkCallback() {
                override fun onAvailable(network: Network) {
                    super.onAvailable(network)
                    trySend(ConnectionState.Success)
                }

                override fun onLost(network: Network) {
                    super.onLost(network)
                    trySend(ConnectionState.Failure(ConnectionFailureReason.LOST))
                }

                override fun onUnavailable() {
                    super.onUnavailable()
                    trySend(ConnectionState.Failure(ConnectionFailureReason.UNAVAILABLE))
                }

                override fun onCapabilitiesChanged(
                    network: Network,
                    networkCapabilities: NetworkCapabilities
                ) {
                    super.onCapabilitiesChanged(network, networkCapabilities)
                    val connected = networkCapabilities.hasCapability(
                        NetworkCapabilities.NET_CAPABILITY_VALIDATED
                    )
                    val isConnected = if (connected) {
                        ConnectionState.Success
                    } else {
                        ConnectionState.Failure(ConnectionFailureReason.UNAVAILABLE)
                    }

                    trySend(isConnected)
                }
            }

            connectivityManager.registerDefaultNetworkCallback(callback)

            awaitClose {
                connectivityManager.unregisterNetworkCallback(callback)
            }
        }
}