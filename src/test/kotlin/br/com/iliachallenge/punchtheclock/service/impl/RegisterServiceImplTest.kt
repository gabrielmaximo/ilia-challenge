package br.com.iliachallenge.punchtheclock.service.impl

import br.com.iliachallenge.punchtheclock.exception.Conflict
import br.com.iliachallenge.punchtheclock.exception.Forbidden
import br.com.iliachallenge.punchtheclock.model.Register
import br.com.iliachallenge.punchtheclock.repository.RegisterRepository
import io.mockk.every
import io.mockk.mockk
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.util.UUID
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

internal class RegisterServiceImplTest {
    private val registerRepository = mockk<RegisterRepository>()
    private val registerService = RegisterServiceImpl(this.registerRepository)

        companion object {
            val DATE: LocalDate = LocalDate.of(2021, 7, 20)
        }

    @BeforeEach
    private fun setupAll() {
        every { registerRepository.save(ofType(Register::class)) } answers {
            Register(date = arg<Register>(n = 0).date, time = arg<Register>(0).time)
        }
    }

    @Test
    fun `Should be able to punch the clock on entry`() {
        this.setupEntry()

        val time = LocalTime.of(9, 0)
        val register = this.registerService.create(LocalDateTime.of(DATE, time))

        assertThat(register.id).isNotNull.isInstanceOf(UUID::class.java)
        assertThat(register.date).isEqualTo(DATE)
        assertThat(register.time).isEqualTo(time)
    }

    @Test
    fun `Should not be able to punch the clock on weekends`() {
        this.setupEntry()

        val saturday = LocalDate.of(2021, 7, 17)
        val sunday = LocalDate.of(2021, 7, 18)
        val time = LocalTime.of(9, 0)

        val saturdayTry = assertThrows<Forbidden> { this.registerService.create(LocalDateTime.of(saturday, time)) }
        val sundayTry = assertThrows<Forbidden> { this.registerService.create(LocalDateTime.of(sunday, time)) }

        assertThat(saturdayTry.msg).isEqualTo("It is not allowed to work on weekends!")
        assertThat(sundayTry.msg).isEqualTo("It is not allowed to work on weekends!")
    }

    @Test
    fun `Should be able to punch the clock on lunchtime`() {
        this.setupLunch()

        val time = LocalTime.of(12, 0)
        val register = this.registerService.create(LocalDateTime.of(DATE, time))

        assertThat(register.id).isNotNull.isInstanceOf(UUID::class.java)
        assertThat(register.date).isEqualTo(DATE)
        assertThat(register.time).isEqualTo(time)
    }

    @Test
    fun `Should not be able to punch the clock when lunchtime is less or equal that the entry time`() {
        this.setupLunch()

        val lessTime = LocalTime.of(8, 0)
        val equalTime = LocalTime.of(9, 0)

        val lessTry = assertThrows<Conflict> { this.registerService.create(LocalDateTime.of(DATE, lessTime)) }
        val equalTry = assertThrows<Conflict> { this.registerService.create(LocalDateTime.of(DATE, equalTime)) }

        assertThat(lessTry.msg).isEqualTo("Please provide a time later than the entry time.")
        assertThat(equalTry.msg).isEqualTo("Please provide a time later than the entry time.")
    }

    @Test
    fun `Should be able to punch the clock on return of the lunchtime`() {
        this.setupReturn()

        val time = LocalTime.of(13, 0)
        val register = this.registerService.create(LocalDateTime.of(DATE, time))

        assertThat(register.id).isNotNull.isInstanceOf(UUID::class.java)
        assertThat(register.date).isEqualTo(DATE)
        assertThat(register.time).isEqualTo(time)
    }

    @Test
    fun `Should not be able to punch the clock when return time is less or equal to the lunchtime`() {
        this.setupReturn()

        val lessTime = LocalTime.of(11, 0)
        val equalTime = LocalTime.of(12, 0)

        val lessTry = assertThrows<Conflict> { this.registerService.create(LocalDateTime.of(DATE, lessTime)) }
        val equalTry = assertThrows<Conflict> { this.registerService.create(LocalDateTime.of(DATE, equalTime)) }

        assertThat(lessTry.msg).isEqualTo("Please provide a time later than the lunchtime.")
        assertThat(equalTry.msg).isEqualTo("Please provide a time later than the lunchtime.")
    }

    @Test
    fun `Should not be able to punch the clock when returned time is less than one hour of lunch`() {
        this.setupReturn()

        val time = LocalTime.of(12, 59)
        val lessOneHourTry = assertThrows<Forbidden> { this.registerService.create(LocalDateTime.of(DATE, time)) }

        assertThat(lessOneHourTry.msg).isEqualTo("At least one hour of lunch break is required.")
    }

    @Test
    fun `Should be able to punch the clock on exit time`() {
        this.setupExit()

        val time = LocalTime.of(18, 0)
        val register = this.registerService.create(LocalDateTime.of(DATE, time))

        assertThat(register.id).isNotNull.isInstanceOf(UUID::class.java)
        assertThat(register.date).isEqualTo(DATE)
        assertThat(register.time).isEqualTo(time)
    }

    @Test
    fun `Should not be able to punch the clock when exit time is less or equal that the lunchtime`() {
        this.setupExit()

        val lessTime = LocalTime.of(12, 0)
        val equalTime = LocalTime.of(13, 0)

        val lessTry = assertThrows<Conflict> { this.registerService.create(LocalDateTime.of(DATE, lessTime)) }
        val equalTry = assertThrows<Conflict> { this.registerService.create(LocalDateTime.of(DATE, equalTime)) }

        assertThat(lessTry.msg).isEqualTo("Please provide a time later than the return time.")
        assertThat(equalTry.msg).isEqualTo("Please provide a time later than the return time.")
    }

    @Test
    fun `Should not be able to punch the clock more than 4 times a day`() {
        this.getDayRegisters()

        val time = LocalTime.of(10, 30)
        val oneMoreRegisterTry = assertThrows<Forbidden> { this.registerService.create(LocalDateTime.of(DATE, time)) }

        assertThat(oneMoreRegisterTry.msg).isEqualTo("Only possible to punch the clock 4 times for day.")
    }

    private fun setupEntry() {
        every { registerRepository.findAllByDateOrderByTimeAsc(ofType(LocalDate::class)) } returns null
    }

    private fun setupLunch() {
        every { registerRepository.findAllByDateOrderByTimeAsc(ofType(LocalDate::class)) } answers {
            listOf(
                Register(date = arg(n = 0), time = LocalTime.of(9,0))
            )
        }
    }

    private fun setupReturn() {
        every { registerRepository.findAllByDateOrderByTimeAsc(ofType(LocalDate::class)) } answers {
            listOf(
                Register(date = arg(n = 0), time = LocalTime.of(9,0)),
                Register(date = arg(n = 0), time = LocalTime.of(12,0))
            )
        }
    }

    private fun setupExit() {
        every { registerRepository.findAllByDateOrderByTimeAsc(ofType(LocalDate::class)) } answers {
            listOf(
                Register(date = arg(n = 0), time = LocalTime.of(9,0)),
                Register(date = arg(n = 0), time = LocalTime.of(12,0)),
                Register(date = arg(n = 0), time = LocalTime.of(13,0))
            )
        }
    }

    private fun getDayRegisters() {
        every { registerRepository.findAllByDateOrderByTimeAsc(ofType(LocalDate::class)) } answers {
            listOf(
                Register(date = arg(n = 0), time = LocalTime.of(9,0)),
                Register(date = arg(n = 0), time = LocalTime.of(12,0)),
                Register(date = arg(n = 0), time = LocalTime.of(13,0)),
                Register(date = arg(n = 0), time = LocalTime.of(18,0))
            )
        }
    }
}