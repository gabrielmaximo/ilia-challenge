package br.com.iliachallenge.punchTheClock.util

import java.time.Duration
import java.time.LocalTime
import org.springframework.stereotype.Component

@Component
class TimeManipulationUtil {
    companion object {
        val DAILY_JOURNEY: LocalTime = LocalTime.of(8, 0)
    }

    fun workedHours(hours: List<LocalTime>): LocalTime {
        var i = 0
        var j = hours.size / 2
        val resultList = listOf<LocalTime>()
        while (i!=j) {
            resultList.plus(this.calculate(hours[j], hours[i]))
            i++
            j--
        }

        return this.calculate(resultList[0], resultList[1])
    }

    fun reckoning(time: Duration): String {
        val result = time
            .toString()
            .replace(Regex("[-PTS]"), "")
            .split(Regex("[HM]"))
            .joinToString(":")

        return if (result.last()==':') result.dropLast(1)
        else result
    }

    private fun calculate(
        greaterTime: LocalTime,
        lessTime: LocalTime,
    ): LocalTime {
        val hour = greaterTime.minusHours(lessTime.hour.toLong()).hour
        val min = greaterTime.minusMinutes(lessTime.minute.toLong()).minute
        val sec = greaterTime.minusSeconds(lessTime.second.toLong()).second
        val nano = greaterTime.minusNanos(lessTime.nano.toLong()).nano

        return LocalTime.of(hour, min, sec, nano)
    }

}