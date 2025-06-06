package com.plugin.hooker

internal object HookMethod {
    internal const val ANNOTATION_DESCRIPTOR = "Lcom/plugin/annotation/HookMethodReplace;"
    internal const val ORI_CLASS = "oriClass"
    internal const val ORI_METHOD = "oriMethod"
    internal const val ORI_OPCODE = "oriOpcode"
    internal val hookMethods = mutableMapOf<String, HookInfo>()
}