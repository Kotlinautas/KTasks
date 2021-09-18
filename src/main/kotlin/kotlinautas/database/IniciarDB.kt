package kotlinautas.database

import kotlinautas.schemas.Tarefas
import kotlinautas.schemas.Usuarios
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction

fun iniciarDB() {
    Database.connect("jdbc:h2:mem:regular;DB_CLOSE_DELAY=-1;", "org.h2.Driver")
    transaction {
        SchemaUtils.create(Usuarios)
        SchemaUtils.create(Tarefas)
    }
}