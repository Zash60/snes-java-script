package com.example.snesemulator

import android.content.Context
import android.content.SharedPreferences
import android.net.Uri
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class RomManager(private val context: Context) {

    private val prefs: SharedPreferences = context.getSharedPreferences("rom_prefs", Context.MODE_PRIVATE)
    private val gson = Gson()

    fun getRecentRoms(): List<Rom> {
        val json = prefs.getString("recent_roms", "[]")
        val type = object : TypeToken<List<Rom>>() {}.type
        return gson.fromJson(json, type)
    }

    fun addRecentRom(uri: Uri) {
        val name = getRomName(uri)
        val rom = Rom(uri, name)
        val recent = getRecentRoms().toMutableList()
        recent.removeIf { it.uri == uri }
        recent.add(0, rom)
        if (recent.size > 10) recent.removeAt(recent.size - 1)
        val json = gson.toJson(recent)
        prefs.edit().putString("recent_roms", json).apply()
    }

    fun isValidRom(uri: Uri): Boolean {
        val name = getRomName(uri)
        return name.endsWith(".smc") || name.endsWith(".sfc") || name.endsWith(".zip")
    }

    private fun getRomName(uri: Uri): String {
        return uri.lastPathSegment ?: "Unknown"
    }
}