package com.example.snesemulator

import android.view.MotionEvent
import android.view.View
import android.widget.Button
import android.widget.RelativeLayout

class VirtualControls(private val overlay: RelativeLayout) {

    companion object {
        const val BUTTON_UP = 1
        const val BUTTON_DOWN = 2
        const val BUTTON_LEFT = 4
        const val BUTTON_RIGHT = 8
        const val BUTTON_A = 16
        const val BUTTON_B = 32
        const val BUTTON_X = 64
        const val BUTTON_Y = 128
        const val BUTTON_START = 256
        const val BUTTON_SELECT = 512
    }

    private var inputState = 0
    private val buttons = mutableMapOf<View, Int>()
    private var onInputChange: ((Int) -> Unit)? = null

    init {
        setupButtons()
    }

    private fun setupButtons() {
        buttons[overlay.findViewById<Button>(R.id.button_up)] = BUTTON_UP
        buttons[overlay.findViewById<Button>(R.id.button_down)] = BUTTON_DOWN
        buttons[overlay.findViewById<Button>(R.id.button_left)] = BUTTON_LEFT
        buttons[overlay.findViewById<Button>(R.id.button_right)] = BUTTON_RIGHT
        buttons[overlay.findViewById<Button>(R.id.button_a)] = BUTTON_A
        buttons[overlay.findViewById<Button>(R.id.button_b)] = BUTTON_B
        buttons[overlay.findViewById<Button>(R.id.button_x)] = BUTTON_X
        buttons[overlay.findViewById<Button>(R.id.button_y)] = BUTTON_Y
        buttons[overlay.findViewById<Button>(R.id.button_start)] = BUTTON_START
        buttons[overlay.findViewById<Button>(R.id.button_select)] = BUTTON_SELECT

        buttons.forEach { (button, mask) ->
            button.setOnTouchListener { _, event ->
                when (event.action) {
                    MotionEvent.ACTION_DOWN -> {
                        inputState = inputState or mask
                        onInputChange?.invoke(inputState)
                    }
                    MotionEvent.ACTION_UP -> {
                        inputState = inputState and mask.inv()
                        onInputChange?.invoke(inputState)
                    }
                }
                true
            }
        }
    }

    fun setOnInputChangeListener(listener: (Int) -> Unit) {
        onInputChange = listener
    }
}