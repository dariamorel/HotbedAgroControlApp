package com.example.hotbedagrocontrolapp.presentation.components.statistics

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.hotbedagrocontrolapp.domain.entities.statistics.AnaliseType
import com.example.hotbedagrocontrolapp.domain.entities.statistics.DateTime
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId

@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ChooseDateTimeWheel(
    dateTime: DateTime,
    modifier: Modifier = Modifier,
    onDismissRequest: () -> Unit,
    onSelectedChange: (DateTime) -> Unit
) {
    val initialMillis = dateTime.localDateTime
      .atZone(ZoneId.systemDefault())
      .toInstant()
      .toEpochMilli()

    val datePickerState = rememberDatePickerState(
      initialSelectedDateMillis = initialMillis
    )
      DatePickerDialog(
          onDismissRequest = onDismissRequest,
          modifier = modifier,
          confirmButton = {
              TextButton(
                  onClick = {
                      val selectedMillis = datePickerState.selectedDateMillis
                      if (selectedMillis != null) {
                          val selectedDate = Instant.ofEpochMilli(selectedMillis)
                              .atZone(ZoneId.systemDefault())
                              .toLocalDate()
                          val newDateTime = LocalDateTime.of(
                              selectedDate.year,
                              selectedDate.month,
                              selectedDate.dayOfMonth,
                              0,
                              0
                          )
                          onSelectedChange(DateTime(AnaliseType.DAY, newDateTime))
                      }
                      onDismissRequest()
                  }
              ) {
                  Text(
                      text = "OK",
                      color = MaterialTheme.colorScheme.onSurface
                  )
              }
          },
          dismissButton = {
              TextButton(onClick = onDismissRequest) {
                  Text(
                      text = "Отмена",
                      color = MaterialTheme.colorScheme.onSurface
                  )
              }
          },
          colors = DatePickerDefaults.colors(
              containerColor = MaterialTheme.colorScheme.surface
          )
      ) {
          DatePicker(
              state = datePickerState,
              showModeToggle = false,
              colors = DatePickerDefaults.colors(
                  containerColor = MaterialTheme.colorScheme.surface,
                  titleContentColor = MaterialTheme.colorScheme.onSurface,
                  headlineContentColor = MaterialTheme.colorScheme.onSurface,
                  weekdayContentColor = MaterialTheme.colorScheme.onSurface,
                  subheadContentColor = MaterialTheme.colorScheme.onSurface,
                  navigationContentColor = MaterialTheme.colorScheme.onSurface,
                  yearContentColor = MaterialTheme.colorScheme.onSurface,
                  currentYearContentColor = MaterialTheme.colorScheme.onSurface,
                  selectedYearContentColor = MaterialTheme.colorScheme.onBackground,
                  selectedYearContainerColor = MaterialTheme.colorScheme.background,
                  dayContentColor = MaterialTheme.colorScheme.onSurface,
                  selectedDayContentColor = MaterialTheme.colorScheme.onBackground,
                  selectedDayContainerColor = MaterialTheme.colorScheme.background,
                  todayContentColor = MaterialTheme.colorScheme.background,
                  todayDateBorderColor = MaterialTheme.colorScheme.background
              )
          )
      }
}