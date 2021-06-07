package `fun`.kotlingang.kds.optional


/**
 * Used to differ stored nulls from nothing stored
 */
public sealed class Optional<out T> {
    public object NotPresent : Optional<Nothing>()
    public data class Value<out T> (
        val value: T
    ) : Optional<T>()
}

public val Optional<*>.isPresent: Boolean get() = this is Optional.Value<*>

public inline fun <T, R> Optional<T>.map(transform: (T) -> R): Optional<R> = when(this) {
    is Optional.NotPresent -> this
    is Optional.Value -> Optional.Value(transform(value))
}

public inline fun <T> Optional<T>.fillDefault(default: () -> T): Optional.Value<T> = when(this) {
    is Optional.NotPresent -> Optional.Value(default())
    is Optional.Value -> this
}

public inline fun <T> Optional<T>.getOrDefault(default: () -> T): T = fillDefault(default).value
