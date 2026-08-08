package org.resortguestapp.project

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import org.jetbrains.compose.resources.painterResource
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.unit.dp

import resortguestapp.shared.generated.resources.Res
import resortguestapp.shared.generated.resources.compose_multiplatform

@Composable
@Preview
fun App() {
    MaterialTheme {

        var statusText by remember { mutableStateOf("Disconnect") }
        var isPaired by remember { mutableStateOf(false) }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            Text(
                text = "Text Resort Companion",
                style = MaterialTheme.typography.headlineLarge
            )

            Spacer(modifier = Modifier.height(16.dp))

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ){
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Digital Bankroll", style = MaterialTheme.typography.titleMedium)
                    Text("$2,500.00", style = MaterialTheme.typography.displaySmall)
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = {
                    isPaired = !isPaired
                    statusText = if (isPaired) "Paired to Slot #104" else "Disconnected"
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ){
                Text(if (isPaired) "Unpair from Machine" else "Tap to Pair with Slot Machine")
            }

            Spacer(modifier = Modifier.height(16.dp))
            Text(text = "Status: $statusText", style = MaterialTheme.typography.bodyLarge)
        }

    }
}