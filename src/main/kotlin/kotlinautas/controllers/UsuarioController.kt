package kotlinautas.controllers

import io.ktor.application.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import kotlinautas.models.Usuario
import kotlinautas.models.usuariosFaker

fun Route.usuarioRoute() {
    route("/usuarios") {
        get {
            return@get call.respond(usuariosFaker)
        }
        post {
            val usuario = call.receive<Usuario>()

            return@post call.respond(usuario)
        }
    }
}