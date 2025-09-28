package com.netanel.smartdash.feature_coins.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.netanel.smartdash.feature_coins.domain.model.TopCoin
import java.text.NumberFormat
import java.util.Locale

@Composable
fun TopCoinsCard(
    modifier: Modifier = Modifier,
    coins: List<TopCoin>,
    onClick: () -> Unit
) {
    val priceFormatter = remember { NumberFormat.getCurrencyInstance(Locale.US) }
    val percentFormatter = remember {
        NumberFormat.getPercentInstance(Locale.US).apply {
            minimumFractionDigits = 2
            maximumFractionDigits = 2
        }
    }

    Card(
        modifier = modifier,
        onClick = onClick,
        shape = MaterialTheme.shapes.medium,
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "Top Crypto (24h)",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface
            )

            coins.take(5).forEachIndexed { index, coin ->
                if (index > 0) {
                    Spacer(modifier = Modifier.height(4.dp))
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                        Text(
                            text = "${coin.marketCapRank ?: index + 1}. ${coin.name}",
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = coin.symbol,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    Column(horizontalAlignment = Alignment.End) {
                        val priceText = coin.priceUsd?.let { priceFormatter.format(it) } ?: "-"
                        Text(
                            text = priceText,
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.primary
                        )

                        val change = coin.changePercent24h
                        val changeColor: Color = when {
                            change == null -> MaterialTheme.colorScheme.onSurfaceVariant
                            change >= 0.0 -> MaterialTheme.colorScheme.tertiary
                            else -> MaterialTheme.colorScheme.error
                        }
                        val changeText = change?.let {
                            val pct = percentFormatter.format(it / 100.0)
                            if (it >= 0) "+$pct" else pct
                        } ?: "—"
                        Text(
                            text = changeText,
                            style = MaterialTheme.typography.bodySmall,
                            color = changeColor,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }
        }
    }
}
