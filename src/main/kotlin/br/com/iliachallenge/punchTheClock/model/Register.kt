package br.com.iliachallenge.punchTheClock.model

import java.time.LocalDate
import java.time.LocalTime
import java.util.UUID
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.Table


@Entity
@Table(name = "Registers")
data class Register(
    @Id
    @GeneratedValue
    val id: UUID = UUID.randomUUID(),

    @Column
    val date: LocalDate,

    @Column
    val time: LocalTime,
)