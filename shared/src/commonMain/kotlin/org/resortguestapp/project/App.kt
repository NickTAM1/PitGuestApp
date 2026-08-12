package org.resortguestapp.project

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.websocket.WebSockets
import io.ktor.client.plugins.websocket.webSocket
import io.ktor.websocket.Frame
import io.ktor.websocket.readText
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

private const val BACKEND_URL = "ws://10.0.2.2:3000/ws"
// Physical phone: ws://10.131.171.97:3000/ws

private enum class ConnectionStatus {
    DISCONNECTED,
    CONNECTING,
    CONNECTED,
    ERROR
}

@Composable
fun App() {
    val scope = rememberCoroutineScope()

    val client = remember {
        HttpClient(CIO) {
            install(WebSockets)
        }
    }

    var status by remember {
        mutableStateOf(ConnectionStatus.DISCONNECTED)
    }

    var lastMessage by remember {
        mutableStateOf("")
    }

    var errorMessage by remember {
        mutableStateOf("")
    }

    var connectionJob by remember {
        mutableStateOf<Job?>(null)
    }

    DisposableEffect(Unit) {
        onDispose {
            connectionJob?.cancel()
            client.close()
        }
    }

    val isConnected = status == ConnectionStatus.CONNECTED
    val isConnecting = status == ConnectionStatus.CONNECTING

    MaterialTheme {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Pit Boss Guest App",
                style = MaterialTheme.typography.headlineLarge
            )

            Spacer(Modifier.height(24.dp))

            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(Modifier.padding(16.dp)) {
                    Text(
                        text = "Digital Bankroll",
                        style = MaterialTheme.typography.titleMedium
                    )

                    Text(
                        text = "$2,500.00",
                        style = MaterialTheme.typography.displaySmall
                    )
                }
            }

            Spacer(Modifier.height(24.dp))

            Text(
                text = when (status) {
                    ConnectionStatus.DISCONNECTED -> "Status: Disconnected"
                    ConnectionStatus.CONNECTING -> "Status: Connecting..."
                    ConnectionStatus.CONNECTED -> "Status: Connected"
                    ConnectionStatus.ERROR -> "Status: Connection error"
                },
                color = if (isConnected) Color.Green else Color.Unspecified
            )

            Spacer(Modifier.height(16.dp))

            Button(
                enabled = !isConnecting,
                onClick = {
                    if (isConnected) {
                        connectionJob?.cancel()
                        connectionJob = null
                        status = ConnectionStatus.DISCONNECTED
                    } else {
                        status = ConnectionStatus.CONNECTING
                        errorMessage = ""

                        connectionJob = scope.launch {
                            try {
                                client.webSocket(urlString = BACKEND_URL) {
                                    status = ConnectionStatus.CONNECTED

                                    send(Frame.Text("""{"type":"pair","slotId":104}"""))

                                    for (frame in incoming) {
                                        if (frame is Frame.Text) {
                                            lastMessage = frame.readText()
                                        }
                                    }
                                }
                            } catch (exception: CancellationException) {
                                throw exception
                            } catch (exception: Exception) {
                                status = ConnectionStatus.ERROR
                                errorMessage =
                                    exception.message ?: "Unknown connection error"
                            } finally {
                                if (status != ConnectionStatus.ERROR) {
                                    status = ConnectionStatus.DISCONNECTED
                                }
                            }
                        }
                    }
                }
            ) {
                Text(
                    when {
                        isConnecting -> "Connecting..."
                        isConnected -> "Disconnect"
                        else -> "Connect to Dashboard"
                    }
                )
            }

            if (lastMessage.isNotEmpty()) {
                Spacer(Modifier.height(16.dp))
                Text("Server message: $lastMessage")
            }

            if (errorMessage.isNotEmpty()) {
                Spacer(Modifier.height(16.dp))
                Text(
                    text = errorMessage,
                    color = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}
