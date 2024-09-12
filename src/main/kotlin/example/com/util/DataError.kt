package example.com.util

sealed interface DataError: Error {

    enum class Auth: DataError {
        INVALID_CREDENTIALS,
        USER_EXISTS,
        USER_DOES_NOT_EXISTS,
        UNKNOWN
    }

    enum class Insert: DataError {
        USER_DOES_NOT_EXISTS,
        COULD_NOT_INSERT
    }
}