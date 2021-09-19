package kotlinautas.models

import kotlinx.serialization.Serializable
import org.valiktor.functions.hasSize
import org.valiktor.functions.isNotEmpty
import org.valiktor.validate
import java.util.*

@Serializable
data class Usuario(
    val id: String = UUID.randomUUID().toString(),
    val nome: String,
    val senha: String,
){
    init{
        validate(this){
            validate(Usuario::nome).isNotEmpty().hasSize(1, 50)
            validate(Usuario::senha).isNotEmpty().hasSize(1, 50)
        }
    }
}