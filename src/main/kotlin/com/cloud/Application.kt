package com.cloud

import io.ktor.server.engine.*
import io.ktor.server.netty.*
import com.cloud.plugins.*

fun main() {
    embeddedServer(Netty, port = 8080, host = "0.0.0.0") {
        configureRouting()
        configureTemplating()
    }.start(wait = true)
}
