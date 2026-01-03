package com.example.snesemulator

import android.net.Uri
import android.os.Bundle
import android.view.SurfaceHolder
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.snesemulator.databinding.ActivityEmulatorBinding
import kotlinx.coroutines.*
import java.io.File

class EmulatorActivity : AppCompatActivity(), SurfaceHolder.Callback {

    private lateinit var binding: ActivityEmulatorBinding
    private var romUri: Uri? = null
    private var romPath: String? = null
    private var isRunning = false
    private var inputState = 0

    private val scope = CoroutineScope(Dispatchers.IO + Job())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEmulatorBinding.inflate(layoutInflater)
        setContentView(binding.root)

        romUri = intent.getStringExtra("ROM_URI")?.let { Uri.parse(it) }

        setupSurfaceView()
        setupControls()
        loadRom()
    }

    private fun setupSurfaceView() {
        binding.emulatorSurfaceView.holder.addCallback(this)
    }

    private fun setupControls() {
        val controls = VirtualControls(this, binding.virtualControlsOverlay)
        controls.setOnInputChangeListener { input ->
            inputState = input
        }
    }

    private fun loadRom() {
        romUri?.let { uri ->
            // Copy ROM to cache
            val inputStream = contentResolver.openInputStream(uri)
            val file = File(cacheDir, "rom.tmp")
            inputStream?.use { input ->
                file.outputStream().use { output ->
                    input.copyTo(output)
                }
            }
            romPath = file.absolutePath

            EmulatorJNI.init()
            if (EmulatorJNI.loadRom(romPath!!)) {
                startEmulation()
            }
        }
    }

    private fun startEmulation() {
        isRunning = true
        scope.launch {
            while (isRunning) {
                EmulatorJNI.setInput(inputState)
                EmulatorJNI.runFrame()
                delay(16) // ~60 FPS
            }
        }
    }

    override fun surfaceCreated(holder: SurfaceHolder) {
        // Pass surface to JNI if needed, but assuming JNI handles it
    }

    override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {
        // Handle surface changes
    }

    override fun surfaceDestroyed(holder: SurfaceHolder) {
        // Handle surface destruction
    }

    override fun onDestroy() {
        super.onDestroy()
        isRunning = false
        scope.cancel()
        romPath?.let {
            EmulatorJNI.unloadRom()
            EmulatorJNI.deinit()
            File(it).delete()
        }
    }
}