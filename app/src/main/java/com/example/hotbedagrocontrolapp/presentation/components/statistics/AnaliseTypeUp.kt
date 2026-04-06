package com.example.hotbedagrocontrolapp.presentation.components.statistics

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.hotbedagrocontrolapp.domain.entities.statistics.AnaliseType
import com.example.hotbedagrocontrolapp.presentation.components.basicComponents.BasicTitle

/**
 * Увеличение типа анализа..
 *
 * @param onSelectedChange Обработка выбранного значения.
 */
@Composable
fun AnaliseTypeUp(
    analiseType: AnaliseType,
    modifier: Modifier = Modifier,
    onSelectedChange: (AnaliseType) -> Unit = {}
) {
    Box(
        modifier = modifier
            .size(40.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(MaterialTheme.colorScheme.surface)
            .clickable { 
            val newAnaliseType = when (analiseType) {
                AnaliseType.YEAR -> AnaliseType.MONTH
                AnaliseType.MONTH -> AnaliseType.DAY
                AnaliseType.DAY -> AnaliseType.HOUR
                AnaliseType.HOUR -> AnaliseType.HOUR
            }
            onSelectedChange(newAnaliseType)
        },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "+",
            style = MaterialTheme.typography.titleSmall,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onPrimary
        )
    }
}

/**
 * Сокращение типа анализа..
 *
 * @param onSelectedChange Обработка выбранного значения.
 */
@Composable
fun AnaliseTypeDown(
    analiseType: AnaliseType,
    modifier: Modifier = Modifier,
    onSelectedChange: (AnaliseType) -> Unit = {}
) {
    Box(
        modifier = modifier
            .size(40.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(MaterialTheme.colorScheme.surface)
            .clickable {
            val newAnaliseType = when (analiseType) {
                AnaliseType.YEAR -> AnaliseType.YEAR
                AnaliseType.MONTH -> AnaliseType.YEAR
                AnaliseType.DAY -> AnaliseType.MONTH
                AnaliseType.HOUR -> AnaliseType.DAY
            }
            onSelectedChange(newAnaliseType)
        },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "-",
            style = MaterialTheme.typography.titleSmall,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onPrimary
        )
    }
}

