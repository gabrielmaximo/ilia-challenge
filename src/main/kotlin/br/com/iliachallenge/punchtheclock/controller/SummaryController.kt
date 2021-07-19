package br.com.iliachallenge.punchtheclock.controller

import br.com.iliachallenge.punchtheclock.service.ReportService
import java.time.YearMonth
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/report")
class SummaryController(private val reportService: ReportService) {
    @GetMapping("/{yearMonth}")
    fun show(@PathVariable yearMonth: YearMonth) = reportService.showMonthSummary(yearMonth)
}