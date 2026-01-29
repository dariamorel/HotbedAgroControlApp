package com.example.hotbedagrocontrolapp.presentation.ui.components.eventLog

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.hotbedagrocontrolapp.domain.entities.elements.ControlResponse
import com.example.hotbedagrocontrolapp.domain.entities.elements.Response
import java.time.LocalDateTime

@Composable
fun Event(
    dateTime: LocalDateTime,
    response: Response,
    modifier: Modifier = Modifier
) {
    Column(modifier.fillMaxWidth()
        .clip(RoundedCornerShape(20.dp))
        .background(MaterialTheme.colorScheme.surface)
        .padding(10.dp)
    ) {
        Text(
            text = dateTime.toString(),
            style = MaterialTheme.typography.titleSmall,
            fontSize = 15.sp,
            textAlign = TextAlign.Left,
            color = MaterialTheme.colorScheme.onPrimary
        )
        Text(
            text = "Значение: ${response.dataToString}",
            style = MaterialTheme.typography.titleSmall,
            fontSize = 15.sp,
            textAlign = TextAlign.Left,
            color = MaterialTheme.colorScheme.onPrimary
        )
    }
}