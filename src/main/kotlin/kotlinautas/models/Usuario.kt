package kotlinautas.models

import kotlinx.serialization.Serializable

@Serializable
data class Usuario(
    val id: Int,
    val nome: String,
    val senha: String,
)

val usuariosFaker = mutableListOf<Usuario>(
    Usuario(1, "Victor", "123"),
    Usuario(2, "Ederson", "123"),
    Usuario(3, "Bruno Lopes", "123"),
    Usuario(4, "Pachi", "123"),
)
