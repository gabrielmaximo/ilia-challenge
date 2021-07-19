package br.com.iliachallenge.punchtheclock.repository

import br.com.iliachallenge.punchtheclock.model.Register
import java.time.LocalDate
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface RegisterRepository : CrudRepository<Register, Long> {
    fun findAllByDateOrderByTimeAsc(date: LocalDate): List<Register>?

    fun findByDateBetweenOrderByDateAsc(startDate: LocalDate, endDate: LocalDate): List<Register>?
}