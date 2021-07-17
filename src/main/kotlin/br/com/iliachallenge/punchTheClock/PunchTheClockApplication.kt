package br.com.iliachallenge.punchTheClock

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class PunchTheClockApplication

fun main(args: Array<String>) {
	runApplication<PunchTheClockApplication>(*args)
}
