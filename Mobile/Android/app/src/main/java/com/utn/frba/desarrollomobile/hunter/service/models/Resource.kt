package com.utn.frba.desarrollomobile.hunter.service.models

class Resource<T> private constructor(
    private val status: ResourceStatus,
    internal val data: T?,
    private val message: String?,
    private val responseCode: Int?
) {

    val isSuccessful: Boolean
        get() = ResourceStatus.SUCCESS == status

    val isError: Boolean
        get() = ResourceStatus.ERROR == status

    val isLoading: Boolean
        get() = ResourceStatus.LOADING == status

    fun isResponseCode(code: Int?): Boolean {
        return if (code == null) {
            responseCode == null
        } else {
            code == responseCode
        }
    }

    companion object {
        fun <T> success(data: T): Resource<T> {
            return success(data, null as Int?)
        }

        fun <T> success(data: T, responseCode: Int?): Resource<T> {
            return Resource(
                ResourceStatus.SUCCESS,
                data,
                null as String?,
                responseCode
            )
        }

        fun <T> error(msg: String?, data: T?): Resource<T> {
            return error(msg, data, null as Int?)
        }

        fun <T> error(msg: String?, data: T?, responseCode: Int?): Resource<T> {
            return Resource(ResourceStatus.ERROR, data, msg, responseCode)
        }

        fun <T> loading(data: T? = null): Resource<T> {
            return Resource(
                ResourceStatus.LOADING,
                data,
                null as String?,
                null as Int?
            )
        }
    }

    enum class ResourceStatus {
        SUCCESS, ERROR, LOADING
    }

}
