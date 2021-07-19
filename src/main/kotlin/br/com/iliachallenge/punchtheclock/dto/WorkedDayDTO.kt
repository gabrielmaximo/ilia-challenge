package br.com.iliachallenge.punchtheclock.dto

import java.time.LocalTime

data class WorkedDayDTO(
    val day: Int,
    val hours: List<LocalTime>
)