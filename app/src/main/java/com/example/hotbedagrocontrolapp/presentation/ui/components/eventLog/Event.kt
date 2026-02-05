package com.example.hotbedagrocontrolapp.presentation.ui.components.eventLog

import android.os.Build
import androidx.annotation.RequiresApi
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
import com.example.hotbedagrocontrolapp.domain.entities.elements.Control
import com.example.hotbedagrocontrolapp.domain.entities.elements.ControlResponse
import com.example.hotbedagrocontrolapp.domain.entities.elements.Response
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun Event(
    control: Control,
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
            text = "${dateTime.format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm"))}",
            style = MaterialTheme.typography.titleSmall,
            fontSize = 15.sp,
            textAlign = TextAlign.Left,
            color = MaterialTheme.colorScheme.onPrimary
        )
        Text(
            text = "${control.elementName} - ${response.dataToString}",
            style = MaterialTheme.typography.titleSmall,
            fontSize = 15.sp,
            textAlign = TextAlign.Left,
            color = MaterialTheme.colorScheme.onPrimary
        )

    }
}