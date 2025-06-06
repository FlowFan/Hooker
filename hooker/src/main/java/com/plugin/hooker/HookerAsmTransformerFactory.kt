package com.plugin.hooker

import com.android.build.api.instrumentation.AsmClassVisitorFactory
import com.android.build.api.instrumentation.ClassContext
import com.android.build.api.instrumentation.ClassData
import com.android.build.api.instrumentation.InstrumentationParameters
import com.plugin.hooker.HookMethod.hookMethods
import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.Opcodes
import org.objectweb.asm.commons.AdviceAdapter

internal abstract class HookerAsmTransformerFactory : AsmClassVisitorFactory<InstrumentationParameters.None> {
    override fun createClassVisitor(
        classContext: ClassContext,
        nextClassVisitor: ClassVisitor
    ): ClassVisitor = object : ClassVisitor(Opcodes.ASM9, nextClassVisitor) {
        override fun visitMethod(
            access: Int,
            name: String?,
            descriptor: String?,
            signature: String?,
            exceptions: Array<out String?>?
        ): MethodVisitor? {
            val parentClass = classContext.currentClassData.className
            return object :
                AdviceAdapter(Opcodes.ASM9, super.visitMethod(access, name, descriptor, signature, exceptions), access, name, descriptor) {
                override fun visitMethodInsn(
                    opcodeAndSource: Int,
                    owner: String?,
                    name: String?,
                    descriptor: String?,
                    isInterface: Boolean
                ) {
                    val targetKey = "$owner.$name"
                    val hookInfo = hookMethods[targetKey]
                    if (hookInfo != null && hookInfo.oriDescriptor == descriptor && hookInfo.newClass != parentClass && opcodeAndSource == hookInfo.oriOpcode) {
                        println("Class = $parentClass, HookInfo = $hookInfo")
                        super.visitMethodInsn(
                            INVOKESTATIC,
                            hookInfo.newClass.replace('.', '/'),
                            hookInfo.newMethod,
                            hookInfo.newDescriptor,
                            false
                        )
                    } else {
                        super.visitMethodInsn(opcodeAndSource, owner, name, descriptor, isInterface)
                    }
                }
            }
        }
    }

    override fun isInstrumentable(classData: ClassData): Boolean {
        return true
    }
}