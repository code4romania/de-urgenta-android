package ro.code4.deurgenta.ui.auth.reset

import ro.code4.deurgenta.ui.auth.reset.FieldValidationStatus.EmailNotValid
import ro.code4.deurgenta.ui.auth.reset.FieldValidationStatus.PasswordTooShort
import ro.code4.deurgenta.ui.auth.reset.FieldValidationStatus.Valid

/**
 * A class to validate input fields.
 */
interface FieldValidator {
    fun validate(input: String): FieldValidationStatus
}

/**
 * This validator will do a basic check for a email: contains one "@" character and at least a point in the email
 * domain part of the address.
 */
class EmailFieldValidator : FieldValidator {
    override fun validate(input: String): FieldValidationStatus {
        val emailParts = input.split("@")
        if (emailParts.size != 2) {
            return EmailNotValid
        } else {
            val arePartsEmpty = emailParts[0].isEmpty() || emailParts[1].isEmpty()
            val hasDomainPoint = emailParts[1].contains(".")
            val hasDomainPartsEmpty = emailParts[1].split(".").any { it.isEmpty() }
            if (arePartsEmpty || !hasDomainPoint || hasDomainPartsEmpty) {
                return EmailNotValid
            }
        }
        return Valid
    }
}

class PasswordFieldValidator : FieldValidator {
    override fun validate(input: String): FieldValidationStatus {
        return when {
            input.length < 8 -> PasswordTooShort
            else -> Valid
        }
    }
}

/**
 * Holds different validation outcomes, currently for email and password input. Typical usage will be a when over this
 * enum handling the desired cases and using an else with an exception for any other values.
 */
enum class FieldValidationStatus {
    Valid, EmailNotValid, PasswordTooShort
}