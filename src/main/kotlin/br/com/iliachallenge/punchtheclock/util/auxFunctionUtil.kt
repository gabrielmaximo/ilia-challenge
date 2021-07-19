package br.com.iliachallenge.punchtheclock.util

import java.time.Duration

fun Duration.format() = toString().replace(Regex("[-PT]"), "")