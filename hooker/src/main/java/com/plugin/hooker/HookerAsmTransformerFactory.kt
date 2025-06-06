package com.plugin.hooker

import com.android.build.api.instrumentation.AsmClassVisitorFactory
import com.android.build.api.instrumentation.ClassContext
import com.android.build.api.instrumentation.ClassData
import com.android.build.api.instrumentation.InstrumentationParameters
import com.plugin.hooker.HookMethod.hookMethods
import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.Label
import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.Opcodes
import org.objectweb.asm.commons.AdviceAdapter

internal abstract class HookerAsmTransformerFactory : AsmClassVisitorFactory<InstrumentationParameters.None> {
    override fun createClassVisitor(
        classContext: ClassContext,
        nextClassVisitor: ClassVisitor
    ): ClassVisitor = object : ClassVisitor(Opcodes.ASM9, nextClassVisitor) {
        private var sourceFile: String? = null

        override fun visitSource(source: String?, debug: String?) {
            super.visitSource(source, debug)
            sourceFile = source
        }

        override fun visitMethod(
            access: Int,
            name: String?,
            descriptor: String?,
            signature: String?,
            exceptions: Array<out String?>?
        ): MethodVisitor? {
            val parentClass = classContext.currentClassData.className
            val methodName = name
            return object :
                AdviceAdapter(Opcodes.ASM9, super.visitMethod(access, methodName, descriptor, signature, exceptions), access, methodName, descriptor) {
                private var lineNumber: Int = 0

                override fun visitLineNumber(line: Int, start: Label?) {
                    super.visitLineNumber(line, start)
                    lineNumber = line
                }

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
                        println("HookInfo = $hookInfo")
                        println("$parentClass.$methodName($sourceFile:$lineNumber)")
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