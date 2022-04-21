package com.example.ceritaku.data.remote.utils

sealed class MediatorResult <out R> private constructor(){
    data class Sucess<out T>(val data: T): MediatorResult<T>()
    data class Error(val error : String): MediatorResult<Nothing>()
    object Loading : MediatorResult<Nothing>()
}