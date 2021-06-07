package `fun`.kotlingang.kds.optional


/**
 * Used to differ stored nulls from nothing stored
 */
sealed class Optional<out T> {
    object NotPresent : Optional<Nothing>()
    data class Value<out T> (
        val value: T
    ) : Optional<T>()
}

val Optional<*>.isPresent get() = this is Optional.Value<*>

inline fun <T, R> Optional<T>.map(transform: (T) -> R): Optional<R> = when(this) {
    is Optional.NotPresent -> this
    is Optional.Value -> Optional.Value(transform(value))
}

inline fun <T> Optional<T>.fillDefault(default: () -> T): Optional.Value<T> = when(this) {
    is Optional.NotPresent -> Optional.Value(default())
    is Optional.Value -> this
}

inline fun <T> Optional<T>.getOrDefault(default: () -> T): T = fillDefault(default).value
