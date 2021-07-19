package br.com.iliachallenge.punchTheClock.util

import java.time.Duration

fun Duration.format() = toString().replace(Regex("[-PT]"), "")