package br.com.iliachallenge.punchTheClock.util

import java.time.Duration
import java.time.LocalTime
import org.springframework.stereotype.Component

@Component
class TimeManipulationUtil {
    fun workedHours(hours: List<LocalTime>): LocalTime {
        val end = hours.size - 1
        val resultList = mutableListOf<LocalTime>()

        for (i in 0 until hours.size / 2 )
            resultList.add(this.calculate(hours[end - i], hours[i]))

        return this.calculate(resultList[0], resultList[1])
    }

    private fun calculate(
        greaterTime: LocalTime,
        lessTime: LocalTime,
    ): LocalTime {
        return greaterTime.minus(Duration.ofNanos(lessTime.toNanoOfDay()))
    }

}