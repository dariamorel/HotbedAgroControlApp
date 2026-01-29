package com.example.hotbedagrocontrolapp.domain.entities.statistics

/**
 * Возможные типы анализа данных.
 *
 * @param type Тип анализа данных.
 */
enum class AnaliseType(val type: String) {
    YEAR("по году"), MONTH("по месяцу"), DAY("по дню"), HOUR("по часу")
}