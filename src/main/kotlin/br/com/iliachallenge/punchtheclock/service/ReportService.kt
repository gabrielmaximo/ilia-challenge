package br.com.iliachallenge.punchtheclock.service

import br.com.iliachallenge.punchtheclock.dto.ReportResponseDTO
import java.time.YearMonth

interface ReportService {
    fun showMonthSummary(inDateRange: YearMonth): ReportResponseDTO
}
