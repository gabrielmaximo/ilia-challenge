package br.com.iliachallenge.punchTheClock.service

import br.com.iliachallenge.punchTheClock.dto.ReportResponseDTO
import java.time.YearMonth

interface ReportService {
    fun getMonthSummary(inDateRange: YearMonth): ReportResponseDTO
}
