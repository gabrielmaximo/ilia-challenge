package br.com.iliachallenge.punchtheclock.controller

import br.com.iliachallenge.punchtheclock.dto.RegisterRequestDTO
import br.com.iliachallenge.punchtheclock.dto.RegisterResponseDTO
import br.com.iliachallenge.punchtheclock.service.RegisterService
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/register")
class RegisterController(
    private val registerService: RegisterService,
) {
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    fun createRegister(@RequestBody requestDTO: RegisterRequestDTO): RegisterResponseDTO {
        return registerService.create(requestDTO.moment)
    }
}