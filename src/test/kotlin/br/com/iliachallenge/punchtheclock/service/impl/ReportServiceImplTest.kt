package br.com.iliachallenge.punchtheclock.service.impl

import br.com.iliachallenge.punchtheclock.exception.NotFound
import br.com.iliachallenge.punchtheclock.model.Register
import br.com.iliachallenge.punchtheclock.repository.RegisterRepository
import br.com.iliachallenge.punchtheclock.util.TimeManipulationUtil
import io.mockk.every
import io.mockk.mockk
import java.time.LocalDate
import java.time.LocalTime
import java.time.YearMonth
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

internal class ReportServiceImplTest {
    private val registerRepository = mockk<RegisterRepository>()
    private val reportService = ReportServiceImpl(this.registerRepository, TimeManipulationUtil())

    @Test
    fun `Should be able to get a month report`() {
        val yearMonth = YearMonth.of(2021,7)
        val report =  this.reportService.showMonthSummary(yearMonth)

        assertThat(report.month).isEqualTo(yearMonth)
        assertThat(report.missingHours).isNull()
        assertThat(report.overtime).isEqualTo("2H")
        assertThat(report.totalHours).isEqualTo("34H")
        assertThat(report.registers).size().isEqualTo(4)
        report.registers.forEach {
            assertThat(it.hours).size().isEqualTo(4)
            assertThat(it.day).isNotNull.isBetween(1, 31)
        }
    }

    @Test
    fun `Should not be able to generate monthly reports in the month that doesn't have any time register`() {
        val yearMonth = YearMonth.of(2021,8)
        val report =  assertThrows<NotFound> { this.reportService.showMonthSummary(yearMonth) }

        assertThat(report.msg).isEqualTo("Not found any registers in this month.")
    }

    @BeforeEach
    private fun setupRegistersOnMonth() {
        every {
            registerRepository.findByDateBetweenOrderByDateAsc(ofType(LocalDate::class),
                ofType(LocalDate::class))
        } answers {
            if (arg<LocalDate>(0).month.value==7 && arg<LocalDate>(1).month.value==7)
                listOf(
                    Register(date = LocalDate.of(2021, 7, 20), time = LocalTime.of(9, 0)),
                    Register(date = LocalDate.of(2021, 7, 20), time = LocalTime.of(12, 0)),
                    Register(date = LocalDate.of(2021, 7, 20), time = LocalTime.of(13, 0)),
                    Register(date = LocalDate.of(2021, 7, 20), time = LocalTime.of(17, 0)),

                    Register(date = LocalDate.of(2021, 7, 21), time = LocalTime.of(8, 0)),
                    Register(date = LocalDate.of(2021, 7, 21), time = LocalTime.of(12, 0)),
                    Register(date = LocalDate.of(2021, 7, 21), time = LocalTime.of(13, 0)),
                    Register(date = LocalDate.of(2021, 7, 21), time = LocalTime.of(19, 0)),

                    Register(date = LocalDate.of(2021, 7, 22), time = LocalTime.of(8, 0)),
                    Register(date = LocalDate.of(2021, 7, 22), time = LocalTime.of(12, 0)),
                    Register(date = LocalDate.of(2021, 7, 22), time = LocalTime.of(13, 0)),
                    Register(date = LocalDate.of(2021, 7, 22), time = LocalTime.of(19, 0)),

                    Register(date = LocalDate.of(2021, 7, 23), time = LocalTime.of(9, 0)),
                    Register(date = LocalDate.of(2021, 7, 23), time = LocalTime.of(12, 0)),
                    Register(date = LocalDate.of(2021, 7, 23), time = LocalTime.of(13, 0)),
                    Register(date = LocalDate.of(2021, 7, 23), time = LocalTime.of(17, 0))
                )
            else null
        }
    }
}