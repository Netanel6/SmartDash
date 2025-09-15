package com.netanel.smartdash.app.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun LoadingCard() {
    Card(shape = MaterialTheme.shapes.medium) {
        // simple centered loader; you can replace with shimmer later
        Box(
            modifier = Modifier
                .wrapContentSize(Alignment.Center)
                .padding(24.dp)
        ) {
            CircularProgressIndicator()
        }
    }
}