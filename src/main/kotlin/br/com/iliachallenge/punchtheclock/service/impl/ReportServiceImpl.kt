package br.com.iliachallenge.punchtheclock.service.impl

import br.com.iliachallenge.punchtheclock.dto.ReportResponseDTO
import br.com.iliachallenge.punchtheclock.dto.WorkedDayDTO
import br.com.iliachallenge.punchtheclock.exception.NotFound
import br.com.iliachallenge.punchtheclock.repository.RegisterRepository
import br.com.iliachallenge.punchtheclock.service.ReportService
import br.com.iliachallenge.punchtheclock.util.TimeManipulationUtil
import br.com.iliachallenge.punchtheclock.util.format
import java.time.Duration
import java.time.LocalDate
import java.time.LocalTime
import java.time.YearMonth
import org.springframework.stereotype.Service

@Service
class ReportServiceImpl(
    private val registerRepository: RegisterRepository,
    private val timeManipulationUtil: TimeManipulationUtil
) : ReportService {
    companion object {
        val DAILY_JOURNEY: LocalTime = LocalTime.of(8, 0)
    }

    override fun showMonthSummary(inDateRange: YearMonth): ReportResponseDTO {
        val registers = this.registerRepository.findByDateBetweenOrderByDateAsc(
            LocalDate.of(inDateRange.year, inDateRange.month, 1),
            LocalDate.of(inDateRange.year, inDateRange.month, inDateRange.lengthOfMonth())

        )

        if (registers.isNullOrEmpty()) throw NotFound("Not found any registers in this month.")

        val registersPerDay = registers.chunked(4)
        val workedDays = registersPerDay.mapIndexed { i, register ->
            val hours = register.map { it.time }.sorted()
            val date = register[i].date
            WorkedDayDTO(
                date.dayOfMonth,
                hours
            )
        }

        val workedHoursPerDay = workedDays.map { this.timeManipulationUtil.workedHours(it.hours) }
        val workedHoursOnMonth = Duration.ofNanos(workedHoursPerDay.sumOf { it.toNanoOfDay() }).format()
        val workedMillisOnMonth = workedHoursPerDay.map { Duration.between(DAILY_JOURNEY, it).toMillis() }
        val duration = Duration.ofMillis(workedMillisOnMonth.sum())

        var missingHours: String? = null
        var overtime: String? = null

        if(duration.isNegative) missingHours = duration.format()
        else overtime = duration.format()

        return ReportResponseDTO(inDateRange, workedHoursOnMonth, missingHours, overtime, workedDays)
    }
}
