package kotlinautas.schemas

import kotlinautas.models.Tarefa
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.Table

object Tarefas : Table() {
	val id: Column<String> = char("id", 36)
	val nome: Column<String> = text("nome")
	val estado: Column<Boolean> = bool("estado")
	val usuarioId = (char("usuario_id", 36) references Usuarios.id)
	override val primaryKey = PrimaryKey(id, name = "PK_Tarefas_Id")

	fun toTask(row: ResultRow): Tarefa = Tarefa(
		id = row[id],
		nome = row[nome],
		estado = row[estado],
		usuarioId = row[usuarioId],
	)
}