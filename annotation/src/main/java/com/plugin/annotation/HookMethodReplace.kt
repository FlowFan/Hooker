package com.plugin.annotation

import kotlin.reflect.KClass

annotation class HookMethodReplace(
    val oriClass: KClass<*>,
    val oriMethod: String = "",
    val oriOpcode: Int = HookMethodOpcodes.INVOKESTATIC
)