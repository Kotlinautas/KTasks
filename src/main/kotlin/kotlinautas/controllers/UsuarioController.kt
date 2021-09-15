package kotlinautas.controllers

import io.ktor.application.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import kotlinautas.models.Usuario
import kotlinautas.schemas.Usuarios
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction

fun Route.usuarioRoute() {
    route("/usuarios") {
        buscaUsuarios()
        buscaUsuario()
        insereUsuario()
    }
}

fun Route.buscaUsuarios() {
    get {
        try {
            val usuarios = transaction {
                Usuarios.selectAll().map { Usuarios.toUsuario(it) }
            }

            return@get call.respond(usuarios)
        } catch (erro: Exception) {
            return@get call.respondText("Erro ao buscar usuários", status = HttpStatusCode.InternalServerError)
        }
    }
}

fun Route.buscaUsuario() {
    get("{id}") {
        try {
            val id = call.parameters["id"] ?: return@get call.respondText("Informe um ID", status = HttpStatusCode.PaymentRequired)

            val usuario = transaction {
                Usuarios.select { Usuarios.id eq id }.firstOrNull()
            } ?: return@get call.respondText("Usuário não encontrado", status = HttpStatusCode.BadRequest)

            return@get call.respond(Usuarios.toUsuario(usuario))
        } catch (erro: Exception) {
            return@get call.respondText("Erro ao buscar usuário", status = HttpStatusCode.InternalServerError)
        }
    }
}

private fun Route.insereUsuario() {
    post {
        try {
            val usuario = call.receive<Usuario>()

            val insercao = transaction {
                Usuarios.insert {
                    it[id] = usuario.id
                    it[nome] = usuario.nome
                    it[senha] = usuario.senha
                }
            }

            if (insercao.equals(0)) {
                return@post call.respondText("Erro ao criar usuário", status = HttpStatusCode.InternalServerError)
            }

            return@post call.respond(usuario)
        } catch (erro: Exception) {
            return@post call.respondText("Erro ao criar usuário", status = HttpStatusCode.InternalServerError)
        }
    }
}