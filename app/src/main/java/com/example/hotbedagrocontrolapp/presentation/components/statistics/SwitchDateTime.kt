package com.example.hotbedagrocontrolapp.presentation.components.statistics

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextDecoration
import com.example.hotbedagrocontrolapp.domain.entities.statistics.DateTime
import com.example.hotbedagrocontrolapp.ui.theme.DarkBrown

/**
 * Выпадающий список для изменения даты.
 *
 * @param dateTime Текущая дата.
 * @param onSelectedChange Обработка выбранного значения.
 */
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun SwitchDateTime(
    dateTime: DateTime,
    modifier: Modifier = Modifier,
    onSelectedChange: (DateTime) -> Unit
) {
    var isOpen by remember { mutableStateOf(false) }

    Row(modifier, verticalAlignment = Alignment.CenterVertically) {
        Icon(
            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
            modifier = Modifier.clickable { onSelectedChange(dateTime.minus(1)) },
            contentDescription = "DateTime back"
        )
        Text(
            text = dateTime.fullString,
            color = MaterialTheme.colorScheme.onBackground,
            style = MaterialTheme.typography.titleSmall.copy(
                textDecoration = TextDecoration.Underline
            ),
            modifier = Modifier.clickable { isOpen = true }
        )
        Icon(
            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
            modifier = Modifier.clickable { onSelectedChange(dateTime.plus(1)) },
            contentDescription = "DateTime forward"
        )
    }
    if (isOpen) {
        ChooseDateTimeWheel(
            dateTime = dateTime,
            onDismissRequest = { isOpen = false },
            onSelectedChange = onSelectedChange
        )
    }
}