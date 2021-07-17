package br.com.iliachallenge.punchTheClock.service

import br.com.iliachallenge.punchTheClock.dto.SummaryResponseDTO
import java.time.YearMonth

interface ReportService {
    fun getMonthSummary(inDateRange: YearMonth): SummaryResponseDTO
}
