# Hooker

基于 ASM 的字节码插桩插件，支持 AGP 8+。

# 使用

### 1.添加依赖

```kotlin
dependencies {
    implementation("io.github.flowfan:hooker-annotation:1.0.0")
}
```

### 2.添加插件

```kotlin
plugins {
    id("com.plugin.hooker") version "1.0.0"
}
```

### 3.使用

```kotlin
@Keep
object SystemHook {
    @JvmStatic
    @HookMethodReplace(
        oriClass = Settings.System::class,
        oriMethod = "getString",
        oriOpcode = HookMethodOpcodes.INVOKESTATIC
    )
    fun getString(resolver: ContentResolver, name: String): String? {
        TODO()
    }
}
```
