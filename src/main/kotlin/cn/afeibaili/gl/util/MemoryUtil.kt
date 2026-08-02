package cn.afeibaili.gl.util

import org.lwjgl.system.MemoryStack

inline fun <T> memoryStack(action: (MemoryStack) -> T): T {
    MemoryStack.stackPush().use { stack ->
        return action(stack)
    }
}