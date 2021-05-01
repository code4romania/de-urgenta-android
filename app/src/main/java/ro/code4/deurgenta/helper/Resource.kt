package ro.code4.deurgenta.helper

import ro.code4.deurgenta.helper.Status.*

data class Resource<out T>(val status: Status, val data: T?, val error: Throwable?) {

    companion object {
        fun <T> success(data: T? = null): Resource<T> {
            return Resource(Success, data, null)
        }

        fun <T> error(error: Throwable, data: T? = null): Resource<T> {
            return Resource(Error, data, error)
        }

        fun <T> loading(data: T? = null): Resource<T> {
            return Resource(Loading, data, null)
        }
    }
}

enum class Status {
    Loading, Success, Error
}