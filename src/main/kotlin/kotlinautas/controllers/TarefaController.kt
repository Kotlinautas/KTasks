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