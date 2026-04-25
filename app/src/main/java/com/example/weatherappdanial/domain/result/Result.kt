package com.example.weatherappdanial.domain.result

typealias RootError = Error
interface Result<out D, out E: RootError> {
    data class Error<out D,out E: RootError>(val error: E): Result<D,E>
    data class Success<out D,out E: RootError>(val data: D): Result<D,E>
}