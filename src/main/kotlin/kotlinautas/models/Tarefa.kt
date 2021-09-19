package kotlinautas.models

import kotlinx.serialization.Serializable
import org.valiktor.functions.hasSize
import org.valiktor.functions.isNotEmpty
import org.valiktor.functions.isNotNull
import org.valiktor.validate
import java.util.*

@Serializable
data class Tarefa(
    val id: String = UUID.randomUUID().toString(),
    val nome: String,
    val estado: Boolean = false,
    val usuarioId: String
){
    init {
        validate(this){
            validate(Tarefa::nome).isNotEmpty()
            validate(Tarefa::usuarioId).isNotEmpty()
        }
    }
}