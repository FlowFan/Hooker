package com.plugin.hooker

import android.content.ContentResolver
import android.provider.Settings
import androidx.annotation.Keep
import com.plugin.annotation.HookMethodOpcodes
import com.plugin.annotation.HookMethodReplace

@Keep
object SystemHook {
    @JvmStatic
    @HookMethodReplace(
        oriClass = Settings.System::class,
        oriMethod = "getString",
        oriOpcode = HookMethodOpcodes.INVOKESTATIC
    )
    fun getString(resolver: ContentResolver, name: String): String? {
        println("🔔 Hooked Settings.System.getString for: $name")

        if (name == "special_setting") {
            return "custom_value"
        }

        return Settings.System.getString(resolver, name)
    }
}