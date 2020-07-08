package ru.skillbranch.devintensive

import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import ru.skillbranch.devintensive.extensions.KeyboardListener
import ru.skillbranch.devintensive.extensions.hideKeyboard
import ru.skillbranch.devintensive.extensions.isKeyboardOpen
import ru.skillbranch.devintensive.models.Bender


class MainActivity : AppCompatActivity(), View.OnClickListener, KeyboardListener {

    lateinit var benderImage: ImageView
    lateinit var textTxt: TextView
    lateinit var et_message: EditText
    lateinit var sendBtn: ImageView


    lateinit var benderObj: Bender

    val STATUS_KEY = "STATUS"
    val question_key = "STATUS"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val status = savedInstanceState?.getString(STATUS_KEY) ?: Bender.Status.NORMAL.name
        val question = savedInstanceState?.getString(question_key) ?: Bender.Question.NAME.name
        benderObj = Bender(Bender.Status.valueOf(status), Bender.Question.valueOf(question))

        benderImage = iv_bender

        Log.d("MainActivity", "onCreate")
        val (r, g, b) = benderObj.status.color
        benderImage.setColorFilter(Color.rgb(r,g,b), PorterDuff.Mode.MULTIPLY)

        textTxt = tv_text
        et_message = edt_text
        sendBtn = iv_send

        textTxt.text = benderObj.askQuestion()
        sendBtn.setOnClickListener(this)

        et_message.setOnEditorActionListener { v, actionId, _ ->
           if (actionId == EditorInfo.IME_ACTION_DONE) {
                val (answer, color) = benderObj.listenAnswer(v.text.toString())
                et_message.text.clear()
                val (r, g, b) = color
                benderImage.setColorFilter(Color.rgb(r, g, b), PorterDuff.Mode.MULTIPLY)
                textTxt.text = answer
                this.hideKeyboard()

            }
            false


        }


        this.isKeyboardOpen(this)

    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(STATUS_KEY, benderObj.status.name)
        outState.putString(question_key, benderObj.question.name)
        Log.d("MainActivity","onSaveInstanceState ${benderObj.status.name}")
    }

    override fun onStart() {
        super.onStart()
        Log.d("MainActivity", "onStart")
    }

    override fun onResume() {
        super.onResume()
        Log.d("MainActivity", "onResume")
    }

    override fun onPause() {
        super.onPause()
        Log.d("MainActivity", "onPause")
    }

    override fun onRestart() {
        super.onRestart()
        Log.d("MainActivity", "onRestart")
    }

    override fun onStop() {
        super.onStop()
        Log.d("MainActivity", "onStop")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("MainActivity", "onDestroy")
    }

    override fun onClick(v: View?) {
        if (v?.id == iv_send.id) {
            val (answer, color) = benderObj.listenAnswer(et_message.text.toString())
            et_message.text.clear()
            val (r, g, b) = color
            benderImage.setColorFilter(Color.rgb(r,g,b), PorterDuff.Mode.MULTIPLY)
            textTxt.text = answer
            this.hideKeyboard()
        }
    }

    override fun onSoftKeyboardShown(isShowing: Boolean) {
        Log.d("MainActivity","onSoftKeyboardShown $isShowing")
    }


}
