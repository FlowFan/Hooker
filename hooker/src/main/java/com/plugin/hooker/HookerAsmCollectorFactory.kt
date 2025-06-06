package com.plugin.hooker

import com.android.build.api.instrumentation.AsmClassVisitorFactory
import com.android.build.api.instrumentation.ClassContext
import com.android.build.api.instrumentation.ClassData
import com.android.build.api.instrumentation.InstrumentationParameters
import com.plugin.hooker.HookMethod.ANNOTATION_DESCRIPTOR
import com.plugin.hooker.HookMethod.ORI_CLASS
import com.plugin.hooker.HookMethod.ORI_METHOD
import com.plugin.hooker.HookMethod.ORI_OPCODE
import com.plugin.hooker.HookMethod.hookMethods
import org.objectweb.asm.*

internal abstract class HookerAsmCollectorFactory : AsmClassVisitorFactory<InstrumentationParameters.None> {
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
            val newClass = classContext.currentClassData.className
            val newName = name
            val newDescriptor = descriptor
            return object :
                MethodVisitor(Opcodes.ASM9, super.visitMethod(access, newName, newDescriptor, signature, exceptions)) {
                private var oriClass: String? = null
                private var oriMethod: String? = null
                private var oriOpcode: Int? = null
                private var oriClassDescriptor: String? = null

                override fun visitAnnotation(descriptor: String?, visible: Boolean): AnnotationVisitor? {
                    if (descriptor == ANNOTATION_DESCRIPTOR) {
                        return object : AnnotationVisitor(Opcodes.ASM9) {
                            override fun visit(name: String?, value: Any?) {
                                when (name) {
                                    ORI_CLASS -> {
                                        oriClass = (value as? Type)?.internalName
                                        oriClassDescriptor = (value as? Type)?.descriptor
                                    }

                                    ORI_METHOD -> oriMethod = value as? String
                                    ORI_OPCODE -> oriOpcode = value as? Int
                                }
                                super.visit(name, value)
                            }

                            override fun visitEnd() {
                                oriMethod = oriMethod ?: newName
                                if (oriClass != null && oriMethod != null && oriOpcode != null && oriClassDescriptor != null) {
                                    hookMethods["$oriClass.$oriMethod"] = HookInfo(
                                        oriClass = oriClass!!,
                                        oriMethod = oriMethod!!,
                                        oriOpcode = oriOpcode!!,
                                        oriDescriptor = if (oriOpcode == Opcodes.INVOKESTATIC) {
                                            newDescriptor!!
                                        } else {
                                            newDescriptor!!.replaceFirst(oriClassDescriptor!!, "")
                                        },
                                        newClass = newClass,
                                        newMethod = newName!!,
                                        newDescriptor = newDescriptor
                                    )
                                }
                            }
                        }
                    }
                    return super.visitAnnotation(descriptor, visible)
                }
            }
        }
    }

    override fun isInstrumentable(classData: ClassData): Boolean = true
}