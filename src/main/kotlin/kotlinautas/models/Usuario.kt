package kotlinautas.models

import kotlinx.serialization.Serializable
import java.util.*

@Serializable
data class Usuario(
    val id: String = UUID.randomUUID().toString(),
    val nome: String,
    val senha: String,
)