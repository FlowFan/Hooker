package com.plugin.hooker

internal data class HookInfo(
    val oriClass: String,
    val oriMethod: String,
    val oriOpcode: Int,
    val oriDescriptor: String,
    val newClass: String,
    val newMethod: String,
    val newDescriptor: String
)