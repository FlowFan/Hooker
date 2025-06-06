package com.plugin.annotation

import org.objectweb.asm.Opcodes

object HookMethodOpcodes {
    const val INVOKESTATIC = Opcodes.INVOKESTATIC
    const val INVOKEVIRTUAL = Opcodes.INVOKEVIRTUAL
    const val INVOKESPECIAL = Opcodes.INVOKESPECIAL
    const val INVOKEDYNAMIC = Opcodes.INVOKEDYNAMIC
    const val INVOKEINTERFACE = Opcodes.INVOKEINTERFACE
}