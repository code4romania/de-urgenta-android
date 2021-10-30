package ro.code4.deurgenta.helper

/**
 * Simple class to represent the result of an operation. [Right] represents a successful operation while [Left]
 * represents and error.
 */
sealed class Either<out L, out R> {
    data class Left<L>(val data: L) : Either<L, Nothing>()
    data class Right<R>(val data: R) : Either<Nothing, R>()
}
