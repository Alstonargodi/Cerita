package com.example.ceritaku.data.remote.utils

sealed class Result <out R> private constructor(){
    data class Sucess<out T>(val data: T): Result<T>()
    data class Error(val error : String): Result<Nothing>()
    object Loading : Result<Nothing>()
}