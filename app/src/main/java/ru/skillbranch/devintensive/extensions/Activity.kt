package ru.skillbranch.devintensive.extensions

import android.app.Activity
import android.graphics.Rect
import android.view.View
import android.view.inputmethod.InputMethodManager


fun Activity.hideKeyboard() {
    val imm: InputMethodManager = this.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    var view: View? = this.currentFocus
    if (view == null) {
        view = View(this)
    }
    imm.hideSoftInputFromWindow(view.windowToken, 0)
}

var globalKeyboardListener: KeyboardListener? = null

interface KeyboardListener {
    fun onSoftKeyboardShown(isShowing: Boolean)
}

fun Activity.isKeyboardOpen(keyboardListener: KeyboardListener?) {
    globalKeyboardListener = keyboardListener
    val rootView = this.window.decorView
    rootView.viewTreeObserver.addOnGlobalLayoutListener {
        val r = Rect()
        rootView.getWindowVisibleDisplayFrame(r)
        val heightDiff = rootView.rootView.height - (r.bottom - r.top)
        globalKeyboardListener?.onSoftKeyboardShown(heightDiff > 200)
    }

}
