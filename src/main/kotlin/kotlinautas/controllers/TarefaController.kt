package kotlinautas.controllers

import io.ktor.routing.*
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import kotlinautas.models.Tarefa
import kotlinautas.schemas.Tarefas
import kotlinautas.utils.returnLocatedValidationErros
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import org.valiktor.ConstraintViolationException

fun Route.tarefaRoute() {
    route("/tarefas") {
        insereTarefa()
        atualizaTarefa()
        apagaTarefa()
    }
}

fun Route.insereTarefa() {
    post {
        try {
            val tarefa = call.receive<Tarefa>()

            val insercao = transaction {
                Tarefas.insert {
                    it[id] = tarefa.id
                    it[nome] = tarefa.nome
                    it[estado] = tarefa.estado
                    it[usuarioId] = tarefa.usuarioId
                }
            }

            if (insercao.equals(0)) {
                return@post call.respondText("Erro ao criar tarefa", status = HttpStatusCode.InternalServerError)
            }

            return@post call.respond(tarefa)
        }catch (ex: ConstraintViolationException){
            println(returnLocatedValidationErros(ex))
            return@post call.respondText("Erro ao criar tarefa", status = HttpStatusCode.InternalServerError)
        } catch (erro: Exception) {
            println(erro)
            return@post call.respondText("Erro ao criar tarefa", status = HttpStatusCode.InternalServerError)
        }
    }
}
fun Route.atualizaTarefa() {
    put("{id}") {
        try {
            val id = call.parameters["id"] ?: return@put call.respondText("Informe um ID", status = HttpStatusCode.PaymentRequired)

            val tarefa = call.receive<Tarefa>()

            val edicao = transaction {
                Tarefas.update({ Tarefas.id eq id }) {
                    it[nome] = tarefa.nome
                    it[estado] = tarefa.estado
                }
            }

            if (edicao.equals(0)) {
                return@put call.respondText("Erro ao modificar tarefa", status = HttpStatusCode.InternalServerError)
            }

            return@put call.respond(Tarefa(id, tarefa.nome, tarefa.estado, tarefa.usuarioId))
        }catch (ex: ConstraintViolationException){
            println(returnLocatedValidationErros(ex))
            return@put call.respondText("Erro ao atualizar tarefa", status = HttpStatusCode.InternalServerError)
        } catch (erro: Exception) {
            return@put call.respondText("Erro ao atualizar tarefa", status = HttpStatusCode.InternalServerError)
        }
    }
}

fun Route.apagaTarefa() {
    delete("{id}") {
        try {
            val id = call.parameters["id"] ?: return@delete call.respondText("Informe um ID", status = HttpStatusCode.PaymentRequired)

            val remocao = transaction {
                Tarefas.deleteWhere { Tarefas.id eq id }
            }

            if (remocao.equals(0)) {
                return@delete call.respondText("Tarefa não encontrado", status = HttpStatusCode.InternalServerError)
            }

            return@delete call.respondText("Tarefa apagada")
        } catch (erro: Exception) {
            return@delete call.respondText("Erro ao apagar tarefa", status = HttpStatusCode.InternalServerError)
        }
    }
}
