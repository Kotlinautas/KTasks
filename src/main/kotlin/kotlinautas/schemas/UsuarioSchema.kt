package kotlinautas.schemas

import kotlinautas.models.Usuario
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.Table

object Usuarios: Table() {
    val id: Column<Int> = integer("id")
    val nome: Column<String> = varchar("nome", 50)
    val senha: Column<String> = varchar("senha", 50)
    override val primaryKey = PrimaryKey(id, name = "PK_Usuarios_Id")

    fun toUsuario(row: ResultRow) = Usuario(id = row[id], nome = row[nome], senha = row[senha])
}