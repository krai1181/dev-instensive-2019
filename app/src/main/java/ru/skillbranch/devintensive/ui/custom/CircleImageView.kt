package ru.skillbranch.devintensive.ui.custom

import android.content.Context
import android.content.res.TypedArray
import android.graphics.*
import android.util.AttributeSet
import android.util.TypedValue
import android.widget.ImageView
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.annotation.Dimension
import androidx.annotation.Px
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.core.graphics.toRectF
import ru.skillbranch.devintensive.R


class CircleImageView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ImageView(context, attrs, defStyleAttr) {

    @Px
    private var borderWidth: Float = getBorderWidth()
    @ColorInt
    private var borderColor: Int = getBorderColor()

    private var initials: String = ""

    private var circlePaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private var borderPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private var textPaint = Paint(Paint.ANTI_ALIAS_FLAG)

    private val viewRect = Rect()

    companion object {
        private const val DEFAULT_BORDER_WIDTH = 2f
        private const val DEFAULT_BORDER_COLOR = Color.WHITE
    }

    init {
        if (attrs != null) {
            val a = context.obtainStyledAttributes(attrs, R.styleable.CircleImageView)
            borderColor = a.getColor(
                R.styleable.CircleImageView_ci_borderColor,
                DEFAULT_BORDER_COLOR
            )
            borderWidth = a.getDimension(
                R.styleable.CircleImageView_ci_borderWidth,
                DEFAULT_BORDER_WIDTH
            )
            a.recycle()
        }

        scaleType = ScaleType.CENTER_CROP


        //init birder
        borderPaint.apply {
            color = borderColor
            style = Paint.Style.STROKE
            strokeWidth = borderWidth
        }



    }


    @Dimension
    fun getBorderWidth(): Float {
        return borderWidth
    }

    fun setBorderWidth(@Dimension dp: Int) {
        val scale = context.resources.displayMetrics.density
        borderWidth = (dp * scale)
    }

    fun getBorderColor(): Int = borderColor

    fun setBorderColor(hex: String) {
        borderColor = Color.parseColor(hex)
        borderPaint.color = borderColor
    }

    fun setBorderColor(@ColorRes colorId: Int) {
        borderColor = ContextCompat.getColor(context, colorId)

    }

    fun setInitials(data: String) {
        initials = data
        invalidate()
    }


    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        setMeasuredDimension(MeasureSpec.getSize(widthMeasureSpec), MeasureSpec.getSize(widthMeasureSpec))
    }


    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        if (w == 0) return
        viewRect.apply {
            left = 0
            top = 0
            right = w
            bottom = h
        }

        prepareBitmap(w, h)
    }

    private fun prepareBitmap(w: Int, h: Int) {
        val srcBmt = drawable.toBitmap(w, h, Bitmap.Config.ARGB_8888)
        circlePaint.shader = BitmapShader(srcBmt, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)
    }

    override fun onDraw(canvas: Canvas) {

        if (initials.isNotEmpty()){
            drawInitials(canvas)
        }else {
            drawAvatar(canvas)
        }

        val half = (borderWidth / 2).toInt()
        viewRect.inset(half, half)
        canvas.drawOval(viewRect.toRectF(), borderPaint)


    }

    private fun drawAvatar(canvas: Canvas) {
        canvas.drawOval(viewRect.toRectF(), circlePaint)
    }

    private fun fetchAccentColor(): Int {
        val typedValue = TypedValue()
        val a: TypedArray = context.obtainStyledAttributes(typedValue.data, intArrayOf(R.attr.colorAccent))
        val color = a.getColor(0, 0)
        a.recycle()
        return color
    }



    private fun drawInitials(canvas: Canvas) {

        textPaint.color = fetchAccentColor()
        canvas.drawOval(viewRect.toRectF(), textPaint)
        textPaint.apply {
            color = Color.WHITE
            textAlign = Paint.Align.CENTER
            textSize = height * 0.33f
        }

        val offsetY = (textPaint.descent() + textPaint.ascent()) / 2
        canvas.drawText(initials, viewRect.exactCenterX(), viewRect.exactCenterY() - offsetY, textPaint)
    }


}