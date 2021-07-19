package br.com.iliachallenge.punchtheclock.service

import br.com.iliachallenge.punchtheclock.dto.RegisterResponseDTO
import java.time.LocalDateTime

interface RegisterService {
    fun create(moment: LocalDateTime): RegisterResponseDTO
}