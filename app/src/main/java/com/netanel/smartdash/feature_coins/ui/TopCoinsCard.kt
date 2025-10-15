package com.netanel.smartdash.feature_coins.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
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

    val topCoins = coins.take(5)

    Card(
        modifier = modifier,
        shape = MaterialTheme.shapes.large,
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(
                    text = "Crypto Market",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = "Top crypto • 24h",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }

            topCoins.firstOrNull()?.let { leader ->
                Surface(
                    color = MaterialTheme.colorScheme.primaryContainer,
                    contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                    shape = MaterialTheme.shapes.large
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 18.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                            Text(
                                text = "${leader.marketCapRank ?: 1}. ${leader.name}",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.SemiBold,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                            Text(
                                text = leader.symbol.uppercase(Locale.getDefault()),
                                style = MaterialTheme.typography.labelMedium,
                                color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f)
                            )
                        }

                        Column(
                            horizontalAlignment = Alignment.End,
                            verticalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            val priceText = leader.priceUsd?.let { priceFormatter.format(it) } ?: "—"
                            Text(
                                text = priceText,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )

                            val changeColor = leader.changePercent24h.changeColor(MaterialTheme.colorScheme)
                            val changeText = leader.changePercent24h.formatChange(percentFormatter)
                            Text(
                                text = changeText,
                                style = MaterialTheme.typography.bodyMedium,
                                color = changeColor,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }
            }

            if (topCoins.size > 1) {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    topCoins.drop(1).forEachIndexed { index, coin ->
                        Surface(
                            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f),
                            contentColor = MaterialTheme.colorScheme.onSurface,
                            shape = MaterialTheme.shapes.medium
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 16.dp, vertical = 14.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                                    Text(
                                        text = "${coin.marketCapRank ?: index + 2}. ${coin.name}",
                                        style = MaterialTheme.typography.bodyLarge,
                                        fontWeight = FontWeight.Medium,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                    Text(
                                        text = coin.symbol.uppercase(Locale.getDefault()),
                                        style = MaterialTheme.typography.labelMedium,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }

                                Column(
                                    horizontalAlignment = Alignment.End,
                                    verticalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    val priceText = coin.priceUsd?.let { priceFormatter.format(it) } ?: "—"
                                    Text(
                                        text = priceText,
                                        style = MaterialTheme.typography.bodyLarge,
                                        fontWeight = FontWeight.SemiBold
                                    )

                                    val changeColor = coin.changePercent24h.changeColor(MaterialTheme.colorScheme)
                                    val changeText = coin.changePercent24h.formatChange(percentFormatter)
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
            Surface(
                modifier = Modifier.clickable(onClick = onClick),
                color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.12f),
                shape = MaterialTheme.shapes.medium
            ) {
                Text(
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp).clickable(onClick = onClick),
                    text = "Tap to refresh prices",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}

private fun Double?.changeColor(scheme: ColorScheme): Color {
    return when {
        this == null -> scheme.onSurfaceVariant
        this >= 0.0 -> scheme.tertiary
        else -> scheme.error
    }
}

private fun Double?.formatChange(formatter: NumberFormat): String {
    return this?.let {
        val pct = formatter.format(it / 100.0)
        if (it >= 0) "+$pct" else pct
    } ?: "—"
}
