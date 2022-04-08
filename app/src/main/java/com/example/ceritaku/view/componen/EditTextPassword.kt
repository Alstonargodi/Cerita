package com.example.ceritaku.view.componen

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat
import com.example.ceritaku.R

class EditTextPassword:  AppCompatEditText, View.OnTouchListener {
    private lateinit var hideButton : Drawable

    constructor(context: Context) : super(context) {
        initButton(ContextCompat.getDrawable(context,R.drawable.ic_hidepass) as Drawable)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        initButton(ContextCompat.getDrawable(context,R.drawable.ic_hidepass) as Drawable)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        initButton(ContextCompat.getDrawable(context,R.drawable.ic_hidepass) as Drawable)
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

    }

    private fun initButton(button : Drawable){
        hideButton = button
        setOnTouchListener(this)

        addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun afterTextChanged(p0: Editable?) {
                showButton()
            }

        })
    }

    private fun hideButton(){
        setButtonDrawable(endText = hideButton)
        initButton(ContextCompat.getDrawable(context,R.drawable.ic_hidepass) as Drawable)
    }

    private fun showButton(){
        setButtonDrawable(endText = hideButton)
        initButton(ContextCompat.getDrawable(context, R.drawable.ic_showpass) as Drawable)
    }


    private fun setButtonDrawable(
        startText : Drawable? = null,
        topText : Drawable? = null,
        bottomText : Drawable? = null,
        endText : Drawable? = null
    ){
        setCompoundDrawablesRelativeWithIntrinsicBounds(
            startText,
            topText,
            bottomText,
            endText
        )

    }


    override fun onTouch(v: View?, event: MotionEvent?): Boolean {
        if (compoundDrawables[2] != null){
            val hideButtonStart : Float
            val hideButtonEnd : Float
            var isHideButtonClicked = false
            if (layoutDirection == View.LAYOUT_DIRECTION_RTL){
                hideButtonEnd = (hideButton.intrinsicWidth + paddingStart).toFloat()
                when {
                    event!!.x < hideButtonEnd -> isHideButtonClicked = true
                }
            }else{
                hideButtonStart = (width - paddingEnd - hideButton.intrinsicWidth).toFloat()
                when {
                    event!!.x < hideButtonStart -> isHideButtonClicked = true
                }
            }

            if (isHideButtonClicked){
                when(event?.action){
                    MotionEvent.ACTION_DOWN->{
                        showButton()
                        return true
                    }
                    MotionEvent.ACTION_UP->{
                        hideButton()
                        return true
                    }
                }
            }
        }
       return false
    }
}