package kotlinautas.controllers

import io.ktor.application.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import kotlinautas.models.Usuario
import kotlinautas.schemas.Usuarios
import kotlinautas.utils.returnLocatedValidationErros
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import org.valiktor.ConstraintViolationException

fun Route.usuarioRoute() {
    route("/usuarios") {
        buscaUsuarios()
        buscaUsuario()
        insereUsuario()
        atualizaUsuario()
        apagaUsuario()
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

fun Route.insereUsuario() {
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
        }catch (ex: ConstraintViolationException){
            println(returnLocatedValidationErros(ex))
            return@post call.respondText("Erro ao criar usuário", status = HttpStatusCode.InternalServerError)
        }
        catch (erro: Exception) {
            return@post call.respondText("Erro ao criar usuário", status = HttpStatusCode.InternalServerError)
        }
    }
}

fun Route.atualizaUsuario() {
    put("{id}") {
        try {
            val id = call.parameters["id"] ?: return@put call.respondText("Informe um ID", status = HttpStatusCode.PaymentRequired)

            val usuario = call.receive<Usuario>()

            val edicao = transaction {
                Usuarios.update({ Usuarios.id eq id }) {
                    it[nome] = usuario.nome
                    it[senha] = usuario.senha
                }
            }

            if (edicao.equals(0)) {
                return@put call.respondText("Erro ao modificar usuário", status = HttpStatusCode.InternalServerError)
            }

            return@put call.respond(Usuario(id, usuario.nome, usuario.senha))
        }catch (ex: ConstraintViolationException){
            println(returnLocatedValidationErros(ex))
            return@put call.respondText("Erro ao atualizar usuário", status = HttpStatusCode.InternalServerError)
        } catch (erro: Exception) {
            return@put call.respondText("Erro ao atualizar usuário", status = HttpStatusCode.InternalServerError)
        }
    }
}

fun Route.apagaUsuario() {
    delete("{id}") {
        try {
            val id = call.parameters["id"] ?: return@delete call.respondText("Informe um ID", status = HttpStatusCode.PaymentRequired)

            val remocao = transaction {
                Usuarios.deleteWhere { Usuarios.id eq id }
            }

            if (remocao.equals(0)) {
                return@delete call.respondText("Usuário não encontrado", status = HttpStatusCode.InternalServerError)
            }

            return@delete call.respondText("Usuário apagado")
        } catch (erro: Exception) {
            return@delete call.respondText("Erro ao apagar usuário", status = HttpStatusCode.InternalServerError)
        }
    }
}