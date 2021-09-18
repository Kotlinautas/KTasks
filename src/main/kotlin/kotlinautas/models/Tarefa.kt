package kotlinautas.models

import kotlinx.serialization.Serializable
import java.util.*

@Serializable
data class Tarefa(
    val id: String = UUID.randomUUID().toString(),
    val nome: String,
    val estado: Boolean = false,
    val usuarioId: String
)