package com.example.hotbedagrocontrolapp.presentation.ui.components.statistics

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
import androidx.compose.ui.Modifier
import com.example.hotbedagrocontrolapp.domain.entities.statistics.AnaliseType
import com.example.hotbedagrocontrolapp.domain.entities.statistics.DateTime
import com.example.hotbedagrocontrolapp.ui.theme.DarkBrown
import java.time.LocalDateTime

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun SwitchDateTime(
    dateTime: DateTime,
    modifier: Modifier = Modifier,
    onSelectedChange: (DateTime) -> Unit = {}
) {
    Row(modifier) {
        Icon(
            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
            modifier = Modifier.clickable { onSelectedChange(dateTime.minus(1)) },
            contentDescription = "DateTime back"
        )
        Text(
            text = dateTime.fullString,
            color = DarkBrown,
            style = MaterialTheme.typography.titleMedium
        )
        Icon(
            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
            modifier = Modifier.clickable { onSelectedChange(dateTime.plus(1)) },
            contentDescription = "DateTime forward"
        )
    }
}