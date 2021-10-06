package utils

@Suppress("UNCHECKED_CAST")
sealed class Try<T> {
    companion object {
        operator fun <T> invoke(func: () -> T): Try<T> =
            try {
                Success(func())
            } catch (error: Exception) {
                Failure(error)
            }
    }

    abstract fun <R> map(transform: (T) -> R): Try<R>
    abstract fun <R> flatMap(func: (T) -> Try<R>): Try<R>
}

@Suppress("UNCHECKED_CAST")
class Success<T>(val value: T) : Try<T>() {
    override fun <R> map(transform: (T) -> R): Try<R> = Try { transform(value) }
    override fun <R> flatMap(func: (T) -> Try<R>): Try<R> =
        Try { func(value) }.let {
            when (it) {
                is Success -> it.value
                is Failure -> it as Try<R>
            }
        }
}

@Suppress("UNCHECKED_CAST")
class Failure<T>(val error: Exception) : Try<T>() {
    override fun <R> map(transform: (T) -> R): Try<R> = this as Try<R>
    override fun <R> flatMap(func: (T) -> Try<R>): Try<R> = this as Try<R>
}