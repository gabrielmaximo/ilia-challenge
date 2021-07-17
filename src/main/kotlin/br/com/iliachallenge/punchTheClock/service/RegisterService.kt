package br.com.iliachallenge.punchTheClock.service

import br.com.iliachallenge.punchTheClock.dto.RegisterResponseDTO
import java.time.LocalDateTime

interface RegisterService {
    fun create(moment: LocalDateTime): RegisterResponseDTO
}