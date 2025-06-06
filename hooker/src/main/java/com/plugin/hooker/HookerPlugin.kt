package com.plugin.hooker

import com.android.build.api.instrumentation.FramesComputationMode
import com.android.build.api.instrumentation.InstrumentationScope
import com.android.build.api.variant.AndroidComponentsExtension
import org.gradle.api.Plugin
import org.gradle.api.Project

class HookerPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        target.extensions.getByType(AndroidComponentsExtension::class.java).onVariants { variant ->
            variant.instrumentation.transformClassesWith(
                HookerAsmCollectorFactory::class.java,
                InstrumentationScope.ALL
            ) {}
            variant.instrumentation.transformClassesWith(
                HookerAsmTransformerFactory::class.java,
                InstrumentationScope.ALL
            ) {}
            variant.instrumentation.setAsmFramesComputationMode(
                FramesComputationMode.COMPUTE_FRAMES_FOR_INSTRUMENTED_METHODS
            )
        }
    }
}