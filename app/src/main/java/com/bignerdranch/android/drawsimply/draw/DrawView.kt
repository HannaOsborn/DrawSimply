package com.bignerdranch.android.drawsimply.draw

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import kotlin.math.abs


@Suppress("DEPRECATION")
class DrawView : View {
    private var tf: Float = 4f
    private var pathV: Path? = null
    private var paintV: Paint? = null
    private var xV: Float? = null
    private var yV: Float? = null
    private var pathList: ArrayList<PaintPath>? = null
    private var undoList: ArrayList<PaintPath>? = null
    private var eventList = ArrayList<MotionEvent>()
    private var paint = Paint()
    private var prevColor = Color.BLACK

    constructor(context: Context) : this(context, null)

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    init {
        pathList = ArrayList()
        undoList = ArrayList()
        paint.isAntiAlias = true
        paint.color = Color.BLACK
        paint.strokeWidth = 8f
        paint.strokeJoin = Paint.Join.ROUND
        paint.strokeCap = Paint.Cap.BUTT
        paint.style = Paint.Style.STROKE
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        val x = event!!.x
        val y = event.y
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                start(x, y)
                invalidate()
            }
            MotionEvent.ACTION_MOVE -> {
                move(x, y)
                invalidate()
            }
            MotionEvent.ACTION_UP -> {
                up()
                invalidate()
            }
            else -> {

            }
        }
        invalidate()
        eventList.add(MotionEvent.obtain(event))
        return true
    }

    @Override
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if (pathList!!.isNotEmpty()) {
            for (i: PaintPath in pathList!!)
                canvas.drawPath(i.path!!, i.paint)
        }
    }

    private fun up() {
        pathV!!.lineTo(xV!!, yV!!)
    }

    private fun move(x: Float, y: Float) {
        val xD: Float = abs(x - xV!!)
        val yD: Float = abs(y - yV!!)

        if (xD >= tf || yD >= tf) {
            pathV!!.quadTo(xV!!, yV!!, (x + xV!!) / 2, (y + yV!!) / 2)
            xV = x
            yV = y
        }
    }

    private fun start(x: Float, y: Float) {
        pathV = Path()
        paintV = paint
        val paintPath = PaintPath(pathV, paintV!!)
        pathList?.add(paintPath)

        pathV!!.reset()
        pathV!!.moveTo(x, y)
        xV = x
        yV = y
    }

    fun getColor(): Int {
        return paint.color
    }

    fun getAlphaValue(): Float {
        return paint.alpha.toFloat()
    }

    fun getStrokeWidth(): Float {
        return paint.strokeWidth
    }

    fun setAlphaValue(value: Float) {
        pathList?.add(PaintPath(Path(), Paint()))
        pathList?.get(pathList!!.size - 1)?.paint!!.color = paint.color
        pathList?.get(pathList!!.size - 1)?.paint!!.strokeCap = paint.strokeCap
        pathList?.get(pathList!!.size - 1)?.paint!!.strokeWidth = paint.strokeWidth
        pathList?.get(pathList!!.size - 1)?.paint!!.style = paint.style
        pathList?.get(pathList!!.size - 1)?.paint!!.isAntiAlias = true
        pathList?.get(pathList!!.size - 1)?.paint!!.strokeJoin = paint.strokeJoin
        pathList?.get(pathList!!.size - 1)?.paint!!.alpha = value.toInt()
        paint = pathList!![pathList!!.size - 1].paint
        invalidate()
    }

    fun setStrokeWidth(size: Float) {
        pathList?.add(PaintPath(Path(), Paint()))
        pathList?.get(pathList!!.size - 1)?.paint!!.color = paint.color
        pathList?.get(pathList!!.size - 1)?.paint!!.strokeCap = paint.strokeCap
        pathList?.get(pathList!!.size - 1)?.paint!!.alpha = paint.alpha
        pathList?.get(pathList!!.size - 1)?.paint!!.style = paint.style
        pathList?.get(pathList!!.size - 1)?.paint!!.isAntiAlias = true
        pathList?.get(pathList!!.size - 1)?.paint!!.strokeJoin = paint.strokeJoin
        pathList?.get(pathList!!.size - 1)?.paint!!.strokeWidth = size
        paint = pathList!![pathList!!.size - 1].paint
        invalidate()
    }

    fun setColor(color: Int) {
        pathList?.add(PaintPath(Path(), Paint()))
        pathList?.get(pathList!!.size - 1)?.paint!!.strokeCap = paint.strokeCap
        pathList?.get(pathList!!.size - 1)?.paint!!.style = paint.style
        pathList?.get(pathList!!.size - 1)?.paint!!.alpha = paint.alpha
        pathList?.get(pathList!!.size - 1)?.paint!!.isAntiAlias = true
        pathList?.get(pathList!!.size - 1)?.paint!!.strokeJoin = paint.strokeJoin
        pathList?.get(pathList!!.size - 1)?.paint!!.strokeWidth = paint.strokeWidth
        pathList?.get(pathList!!.size - 1)?.paint!!.color = color
        paint = pathList!![pathList!!.size - 1].paint
        invalidate()
    }

    fun setStyle(style: String, color: Int, backgroundColor: Int) {
        if (color != backgroundColor) {
            when (style) {
                "STROKE" -> {
                    eraserMode(false, 0)
                    pathList?.add(PaintPath(Path(), Paint()))
                    pathList?.get(pathList!!.size - 1)?.paint!!.strokeCap = paint.strokeCap
                    pathList?.get(pathList!!.size - 1)?.paint!!.alpha = paint.alpha
                    pathList?.get(pathList!!.size - 1)?.paint!!.color = color
                    pathList?.get(pathList!!.size - 1)?.paint!!.isAntiAlias = true
                    pathList?.get(pathList!!.size - 1)?.paint!!.strokeJoin = paint.strokeJoin
                    pathList?.get(pathList!!.size - 1)?.paint!!.strokeWidth = paint.strokeWidth
                    pathList?.get(pathList!!.size - 1)?.paint!!.style = Paint.Style.STROKE
                    paint = pathList!![pathList!!.size - 1].paint
                    invalidate()
                }
                else -> {
                    eraserMode(false, 0)
                    pathList?.add(PaintPath(Path(), Paint()))
                    pathList?.get(pathList!!.size - 1)?.paint!!.strokeCap = paint.strokeCap
                    pathList?.get(pathList!!.size - 1)?.paint!!.alpha = paint.alpha
                    pathList?.get(pathList!!.size - 1)?.paint!!.color = color
                    pathList?.get(pathList!!.size - 1)?.paint!!.isAntiAlias = true
                    pathList?.get(pathList!!.size - 1)?.paint!!.strokeJoin = paint.strokeJoin
                    pathList?.get(pathList!!.size - 1)?.paint!!.strokeWidth = paint.strokeWidth
                    pathList?.get(pathList!!.size - 1)?.paint!!.style = Paint.Style.FILL_AND_STROKE
                    paint = pathList!![pathList!!.size - 1].paint
                    invalidate()
                }
            }
        } else {
            when (style) {
                "STROKE" -> {
                    eraserMode(false, 0)
                    pathList?.add(PaintPath(Path(), Paint()))
                    pathList?.get(pathList!!.size - 1)?.paint!!.strokeCap = paint.strokeCap
                    pathList?.get(pathList!!.size - 1)?.paint!!.alpha = paint.alpha
                    pathList?.get(pathList!!.size - 1)?.paint!!.color = prevColor
                    pathList?.get(pathList!!.size - 1)?.paint!!.isAntiAlias = true
                    pathList?.get(pathList!!.size - 1)?.paint!!.strokeJoin = paint.strokeJoin
                    pathList?.get(pathList!!.size - 1)?.paint!!.strokeWidth = paint.strokeWidth
                    pathList?.get(pathList!!.size - 1)?.paint!!.style = Paint.Style.STROKE
                    paint = pathList!![pathList!!.size - 1].paint
                    invalidate()
                }
                else -> {
                    eraserMode(false, 0)
                    pathList?.add(PaintPath(Path(), Paint()))
                    pathList?.get(pathList!!.size - 1)?.paint!!.strokeCap = paint.strokeCap
                    pathList?.get(pathList!!.size - 1)?.paint!!.alpha = paint.alpha
                    pathList?.get(pathList!!.size - 1)?.paint!!.color = prevColor
                    pathList?.get(pathList!!.size - 1)?.paint!!.isAntiAlias = true
                    pathList?.get(pathList!!.size - 1)?.paint!!.strokeJoin = paint.strokeJoin
                    pathList?.get(pathList!!.size - 1)?.paint!!.strokeWidth = paint.strokeWidth
                    pathList?.get(pathList!!.size - 1)?.paint!!.style = Paint.Style.FILL_AND_STROKE
                    paint = pathList!![pathList!!.size - 1].paint
                    invalidate()
                }
            }
        }
    }

    fun setCap(cap: String) {
        when (cap) {
            "BUTT" -> {
                pathList?.add(PaintPath(Path(), Paint()))
                pathList?.get(pathList!!.size - 1)?.paint!!.color = paint.color
                pathList?.get(pathList!!.size - 1)?.paint!!.alpha = paint.alpha
                pathList?.get(pathList!!.size - 1)?.paint!!.style = paint.style
                pathList?.get(pathList!!.size - 1)?.paint!!.isAntiAlias = true
                pathList?.get(pathList!!.size - 1)?.paint!!.strokeJoin = paint.strokeJoin
                pathList?.get(pathList!!.size - 1)?.paint!!.strokeWidth = paint.strokeWidth
                pathList?.get(pathList!!.size - 1)?.paint!!.strokeCap = Paint.Cap.BUTT
                paint = pathList!![pathList!!.size - 1].paint
                invalidate()
            }
            "SQUARE" -> {
                pathList?.add(PaintPath(Path(), Paint()))
                pathList?.get(pathList!!.size - 1)?.paint!!.color = paint.color
                pathList?.get(pathList!!.size - 1)?.paint!!.alpha = paint.alpha
                pathList?.get(pathList!!.size - 1)?.paint!!.style = paint.style
                pathList?.get(pathList!!.size - 1)?.paint!!.isAntiAlias = true
                pathList?.get(pathList!!.size - 1)?.paint!!.strokeJoin = paint.strokeJoin
                pathList?.get(pathList!!.size - 1)?.paint!!.strokeWidth = paint.strokeWidth
                pathList?.get(pathList!!.size - 1)?.paint!!.strokeCap = Paint.Cap.SQUARE
                paint = pathList!![pathList!!.size - 1].paint
                invalidate()
            }
            else -> {
                pathList?.add(PaintPath(Path(), Paint()))
                pathList?.get(pathList!!.size - 1)?.paint!!.color = paint.color
                pathList?.get(pathList!!.size - 1)?.paint!!.alpha = paint.alpha
                pathList?.get(pathList!!.size - 1)?.paint!!.style = paint.style
                pathList?.get(pathList!!.size - 1)?.paint!!.isAntiAlias = true
                pathList?.get(pathList!!.size - 1)?.paint!!.strokeJoin = paint.strokeJoin
                pathList?.get(pathList!!.size - 1)?.paint!!.strokeWidth = paint.strokeWidth
                pathList?.get(pathList!!.size - 1)?.paint!!.strokeCap = Paint.Cap.ROUND
                paint = pathList!![pathList!!.size - 1].paint
                invalidate()
            }
        }
    }

    fun clearCanvas() {
        undoList?.clear()
        pathList?.clear()
        paint.color = Color.BLACK
        paint.style = Paint.Style.STROKE
        paint.strokeCap = Paint.Cap.BUTT
        paint.alpha = 255
        paint.strokeWidth = 8f
        invalidate()
    }

    fun undo() {
        if (pathList?.size!! > 0) {
            undoList?.add(pathList!!.removeAt(pathList!!.size - 1))
        }
        invalidate()
    }

    fun redo() {
        if (undoList?.size!! > 0) {
            pathList?.add(undoList!!.removeAt(undoList!!.size - 1))
        }
        invalidate()
    }

    fun eraserMode(eraserMode: Boolean, backgroundColor: Int) {
        if (eraserMode) {
            prevColor = paint.color
            pathList?.add(PaintPath(Path(), Paint()))
            pathList?.get(pathList!!.size - 1)?.paint!!.strokeCap = paint.strokeCap
            pathList?.get(pathList!!.size - 1)?.paint!!.alpha = paint.alpha
            pathList?.get(pathList!!.size - 1)?.paint!!.style = paint.style
            pathList?.get(pathList!!.size - 1)?.paint!!.isAntiAlias = true
            pathList?.get(pathList!!.size - 1)?.paint!!.strokeJoin = paint.strokeJoin
            pathList?.get(pathList!!.size - 1)?.paint!!.strokeWidth = paint.strokeWidth
            pathList?.get(pathList!!.size - 1)?.paint!!.color = backgroundColor
            paint = pathList!![pathList!!.size - 1].paint
            invalidate()
        } else {
            pathList?.add(PaintPath(Path(), Paint()))
            pathList?.get(pathList!!.size - 1)?.paint!!.strokeCap = paint.strokeCap
            pathList?.get(pathList!!.size - 1)?.paint!!.alpha = paint.alpha
            pathList?.get(pathList!!.size - 1)?.paint!!.style = paint.style
            pathList?.get(pathList!!.size - 1)?.paint!!.isAntiAlias = true
            pathList?.get(pathList!!.size - 1)?.paint!!.strokeJoin = paint.strokeJoin
            pathList?.get(pathList!!.size - 1)?.paint!!.strokeWidth = paint.strokeWidth
            pathList?.get(pathList!!.size - 1)?.paint!!.color = prevColor
            paint = pathList!![pathList!!.size - 1].paint
            invalidate()
        }
    }

    fun setBackground(bmp: Bitmap) {
        val d: Drawable = BitmapDrawable(resources, bmp)
        background = d
    }

}