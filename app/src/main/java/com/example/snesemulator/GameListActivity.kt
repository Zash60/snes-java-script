package com.example.snesemulator

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.snesemulator.databinding.ActivityGameListBinding

class GameListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityGameListBinding
    private lateinit var romManager: RomManager
    private lateinit var gameAdapter: GameAdapter

    private val filePickerLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            result.data?.data?.let { uri ->
                loadRom(uri)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGameListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        romManager = RomManager(this)

        setupRecyclerView()
        setupFilePickerButton()
        loadGameList()
    }

    private fun setupRecyclerView() {
        gameAdapter = GameAdapter { romUri ->
            loadRom(romUri)
        }
        binding.gameListRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.gameListRecyclerView.adapter = gameAdapter
    }

    private fun setupFilePickerButton() {
        binding.selectRomButton.setOnClickListener {
            openFilePicker()
        }
    }

    private fun openFilePicker() {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
            addCategory(Intent.CATEGORY_OPENABLE)
            type = "*/*"
            putExtra(Intent.EXTRA_MIME_TYPES, arrayOf("application/zip", "application/octet-stream"))
        }
        filePickerLauncher.launch(intent)
    }

    private fun loadGameList() {
        // Load recent ROMs or from storage
        val roms = romManager.getRecentRoms()
        gameAdapter.submitList(roms)
    }

    private fun loadRom(uri: Uri) {
        // Validate and load ROM
        if (romManager.isValidRom(uri)) {
            romManager.addRecentRom(uri)
            // Start EmulatorActivity
            val intent = Intent(this, EmulatorActivity::class.java).apply {
                putExtra("ROM_URI", uri.toString())
            }
            startActivity(intent)
        } else {
            Toast.makeText(this, "Invalid ROM file", Toast.LENGTH_SHORT).show()
        }
    }
}