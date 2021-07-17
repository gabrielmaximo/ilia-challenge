package br.com.iliachallenge.punchTheClock.service.impl

import br.com.iliachallenge.punchTheClock.dto.SummaryResponseDTO
import br.com.iliachallenge.punchTheClock.dto.WorkedDayDTO
import br.com.iliachallenge.punchTheClock.exception.NotFound
import br.com.iliachallenge.punchTheClock.repository.RegisterRepository
import br.com.iliachallenge.punchTheClock.service.ReportService
import br.com.iliachallenge.punchTheClock.util.TimeManipulationUtil
import br.com.iliachallenge.punchTheClock.util.TimeManipulationUtil.Companion.DAILY_JOURNEY
import java.time.Duration
import java.time.LocalDate
import java.time.YearMonth
import org.springframework.stereotype.Service

@Service
class ReportServiceImpl(
    private val registerRepository: RegisterRepository,
    private val timeManipulationUtil: TimeManipulationUtil
) : ReportService {
    override fun getMonthSummary(inDateRange: YearMonth): SummaryResponseDTO {
        val registers = registerRepository.findByDateBetweenOrderByDateAsc(
            LocalDate.of(inDateRange.year, inDateRange.month, 1),
            LocalDate.of(inDateRange.year, inDateRange.month, inDateRange.lengthOfMonth())

        ).orEmpty()

        if (registers.isEmpty()) throw NotFound("Not found registers in this month.")

        val registersPerDay = registers.chunked(4)
        val workedDays = registersPerDay.mapIndexed { i,x ->
            val hours = x.map { y -> y.time }
            val date = x[i].date
            WorkedDayDTO(
                date,
                hours
            )
        }

        val workedHoursPerDay = workedDays.map { timeManipulationUtil.workedHours(it.hours) }
        val workedHoursOnMonth = timeManipulationUtil.workedHours(workedHoursPerDay)
        val differenceTime = Duration.between(DAILY_JOURNEY, workedHoursOnMonth)

        var missingHours = "0"
        var overtime = "0"

        if(differenceTime.isNegative) missingHours = timeManipulationUtil.reckoning(differenceTime)
        else overtime = timeManipulationUtil.reckoning(differenceTime)

        return SummaryResponseDTO(inDateRange, workedHoursOnMonth, missingHours, overtime, workedDays)
    }
}