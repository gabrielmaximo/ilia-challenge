package br.com.iliachallenge.punchTheClock.service.impl

import br.com.iliachallenge.punchTheClock.dto.RegisterResponseDTO
import br.com.iliachallenge.punchTheClock.exception.BadRequest
import br.com.iliachallenge.punchTheClock.exception.Forbidden
import br.com.iliachallenge.punchTheClock.model.Register
import br.com.iliachallenge.punchTheClock.repository.RegisterRepository
import br.com.iliachallenge.punchTheClock.service.RegisterService
import java.time.DayOfWeek
import java.time.LocalDateTime
import java.time.LocalTime
import org.springframework.stereotype.Service

@Service
class RegisterServiceImpl(private val registerRepository: RegisterRepository) : RegisterService {
    override fun create(moment: LocalDateTime): RegisterResponseDTO {
        val registers = this.registerRepository.findAllByDate(moment.toLocalDate()).orEmpty()

        return when (registers.size) {
            0 -> this.entry(moment)
            1 -> this.lunch(moment, registers[0].time)
            2 -> this.returned(moment, registers[1].time)
            3 -> this.exited(moment, registers[2].time)
            else -> throw Forbidden("Only possible to punch the clock 4 times for day.")
        }
    }

    private fun entry(moment: LocalDateTime): RegisterResponseDTO {
        val dayOfWeek = moment.dayOfWeek

        if (dayOfWeek==DayOfWeek.SATURDAY || dayOfWeek==DayOfWeek.SUNDAY)
            throw Forbidden("It is not allowed to work on weekends!")

        val res = registerRepository.save(
            Register(
                date = moment.toLocalDate(),
                time = moment.toLocalTime()
            )
        )

        return RegisterResponseDTO(res.id, res.date, res.time)
    }

    private fun lunch(moment: LocalDateTime, entryTime: LocalTime): RegisterResponseDTO {
        val lunchTime = moment.toLocalTime()

        if (lunchTime.isBefore(entryTime) || lunchTime.equals(entryTime))
            throw BadRequest("Please provide a time later than the entry time.")

        val res = registerRepository.save(
            Register(
                date = moment.toLocalDate(),
                time = lunchTime
            )
        )

        return RegisterResponseDTO(res.id, res.date, res.time)
    }

    private fun returned(moment: LocalDateTime, lunchTime: LocalTime): RegisterResponseDTO {
        val returnTime = moment.toLocalTime()

        if (returnTime.isBefore(lunchTime) || returnTime.equals(lunchTime))
            throw BadRequest("Please provide a time later than the lunch time.")

        val oneHourLater = lunchTime.plusHours(1)

        if (returnTime.isBefore(oneHourLater))
            throw Forbidden("At least one hour of lunch break is required")

        val res = registerRepository.save(
            Register(
                date = moment.toLocalDate(),
                time = returnTime
            )
        )

        return RegisterResponseDTO(res.id, res.date, res.time)
    }

    private fun exited(moment: LocalDateTime, returnTime: LocalTime): RegisterResponseDTO {
        val exitTime = moment.toLocalTime()

        if (exitTime.isBefore(returnTime) || exitTime.equals(returnTime))
            throw BadRequest("Please provide a time later than the return time.")

        val res = registerRepository.save(
            Register(
                date = moment.toLocalDate(),
                time = exitTime
            )
        )

        return RegisterResponseDTO(res.id, res.date, res.time)
    }
}