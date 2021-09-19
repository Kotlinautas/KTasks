package kotlinautas.utils

import org.valiktor.ConstraintViolationException
import org.valiktor.i18n.mapToMessage
import java.util.*

fun returnLocatedValidationErros(ex: ConstraintViolationException): String {
	return ex.constraintViolations
		.mapToMessage(baseName = "messages", locale = Locale("pt", "BR"))
		.joinToString(", ") { "${it.property}: ${it.message}" }
}