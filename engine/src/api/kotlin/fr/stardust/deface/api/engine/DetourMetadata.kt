package fr.stardust.deface.api.engine

interface DetourMetadata {
    fun getClassName(): String

    fun getMethodName(): String

    fun getMethodDescription(): String
}