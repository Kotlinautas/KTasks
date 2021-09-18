package kotlinautas.plugins

import io.ktor.routing.*
import io.ktor.application.*
import io.ktor.response.*
import kotlinautas.controllers.tarefaRoute
import kotlinautas.controllers.usuarioRoute

fun Application.configureRouting() {
    routing {
        get("/") {
            call.respondText("Hello World!")
        }
        usuarioRoute()
        tarefaRoute()
    }
}
