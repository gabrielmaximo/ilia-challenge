package br.com.iliachallenge.punchTheClock.repository

import br.com.iliachallenge.punchTheClock.model.Register
import java.time.LocalDate
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface RegisterRepository : CrudRepository<Register, Long> {
    fun findAllByDate(date: LocalDate): List<Register>?

    fun findByDateBetweenOrderByDateAsc(startDate: LocalDate, endDate: LocalDate): List<Register>?
}