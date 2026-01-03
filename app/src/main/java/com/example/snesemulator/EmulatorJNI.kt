package com.example.snesemulator

class EmulatorJNI {

    companion object {
        init {
            System.loadLibrary("snes9x")
        }

        @JvmStatic
        external fun init()

        @JvmStatic
        external fun loadRom(romPath: String): Boolean

        @JvmStatic
        external fun runFrame()

        @JvmStatic
        external fun setInput(input: Int)

        @JvmStatic
        external fun unloadRom()

        @JvmStatic
        external fun deinit()
    }
}