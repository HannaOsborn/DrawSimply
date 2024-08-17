package com.bignerdranch.android.drawsimply

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.content.pm.ActivityInfo
import android.content.pm.PackageManager
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.Gravity
import android.view.MotionEvent
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bignerdranch.android.drawsimply.draw.DrawView
import com.google.android.material.slider.Slider
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.util.*


private const val GALLERY_REQ_CODE = 0
private const val STORAGE_PERMS = 1

@Suppress("DEPRECATION", "DEPRECATED_IDENTITY_EQUALS")
class DrawActivity : AppCompatActivity() {

    private lateinit var addButton: ImageButton
    private lateinit var penButton: ImageButton
    private lateinit var undoButton: ImageButton
    private lateinit var clearButton: ImageButton
    private lateinit var saveButton: ImageButton
    private lateinit var redoButton: ImageButton
    private lateinit var canvas: DrawView
    private lateinit var imageView: ImageView

    private lateinit var penV2Button: ImageButton
    private lateinit var fillStrokeButton: ImageButton
    private lateinit var eraserButton: ImageButton
    private lateinit var sphereButton: ImageButton
    private lateinit var cubeButton: ImageButton
    private lateinit var semiSphereButton: ImageButton
    private lateinit var returnButton: ImageButton
    private lateinit var backgroundButton: ImageButton

    private lateinit var androidView: ImageView
    private lateinit var strokeSlider: Slider
    private lateinit var alphaSlider: Slider
    private lateinit var colorPicker: ImageView
    private lateinit var colorPreview: ImageView
    private lateinit var bitMap: Bitmap

    private var backgroundColor = Color.WHITE
    private var eraserMode = false

    @Override
    @SuppressLint("MissingInflatedId", "ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_draw)
        this.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LOCKED

        canvas = findViewById(R.id.draw_view)
        canvas.isSaveEnabled = true
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.pen_editor_dialog)
        dialog.setCancelable(false)
        androidView = dialog.findViewById(R.id.androidView)
        strokeSlider = dialog.findViewById(R.id.strokeSlider)
        strokeSlider.valueFrom = 1f
        strokeSlider.valueTo = 250f
        strokeSlider.value = canvas.getStrokeWidth()
        strokeSlider.stepSize = 1f
        strokeSlider.thumbRadius = 50
        strokeSlider.isTickVisible = false
        strokeSlider.setThumbStrokeColorResource(R.color.blueX)
        strokeSlider.thumbStrokeWidth = 7f
        alphaSlider = dialog.findViewById(R.id.alphaSlider)
        alphaSlider.valueFrom = 0f
        alphaSlider.valueTo = 255f
        alphaSlider.value = canvas.getAlphaValue()
        alphaSlider.stepSize = 1f
        alphaSlider.thumbRadius = 50
        alphaSlider.isTickVisible = false
        alphaSlider.setThumbStrokeColorResource(R.color.blueX)
        alphaSlider.thumbStrokeWidth = 7f
        colorPicker = dialog.findViewById(R.id.colorPicker)
        colorPicker.isDrawingCacheEnabled = true
        colorPicker.buildDrawingCache(true)
        bitMap = (colorPicker.drawable as BitmapDrawable).bitmap
        colorPreview = dialog.findViewById(R.id.colorPreview)
        fillStrokeButton = dialog.findViewById(R.id.fillstroke)
        penV2Button = dialog.findViewById(R.id.pen)
        cubeButton = dialog.findViewById(R.id.cube)
        sphereButton = dialog.findViewById(R.id.sphere)
        eraserButton = dialog.findViewById(R.id.eraser)
        semiSphereButton = dialog.findViewById(R.id.semiSphere)
        returnButton = dialog.findViewById(R.id.returnButton)
        backgroundButton = dialog.findViewById(R.id.background)
        addButton = findViewById(R.id.add_button)
        penButton = findViewById(R.id.pen_button)
        undoButton = findViewById(R.id.undo_button)
        clearButton = findViewById(R.id.clear_button)
        saveButton = findViewById(R.id.save_button)
        redoButton = findViewById(R.id.redo_button)
        imageView = findViewById(R.id.imageView)

        backgroundButton.setOnClickListener {
            backgroundColor = canvas.getColor()
            canvas.setBackgroundColor(canvas.getColor())
            Toast.makeText(this, R.string.background_toast, Toast.LENGTH_SHORT)
                .also { it.setGravity(Gravity.TOP, 0, 0) }.show()
        }
        penButton.setOnClickListener {
            Toast.makeText(this, R.string.pen_toast, Toast.LENGTH_SHORT)
                .also { it.setGravity(Gravity.TOP, 0, 0) }.show()
            dialog.show()
        }

        returnButton.setOnClickListener {
            Toast.makeText(this, R.string.return_toast, Toast.LENGTH_SHORT)
                .also { it.setGravity(Gravity.TOP, 0, 0) }.show()
            dialog.dismiss()
        }
        strokeSlider.addOnChangeListener { _, value, _ ->
            canvas.setStrokeWidth(value)
            strokeSlider.value = canvas.getStrokeWidth()
            Toast.makeText(this, R.string.strokeSize_toast, Toast.LENGTH_SHORT).also {
                it.setGravity(
                    Gravity.TOP, 0, 0
                )
            }.show()
        }
        alphaSlider.addOnChangeListener { _, value, _ ->
            canvas.setAlphaValue(value)
            alphaSlider.value = canvas.getAlphaValue()
            Toast.makeText(this, R.string.alphaSize_toast, Toast.LENGTH_SHORT).also {
                it.setGravity(
                    Gravity.TOP, 0, 0
                )
            }.show()
        }
        colorPicker.setOnTouchListener { _, motionEvent ->
            try {
                if (motionEvent.action == MotionEvent.ACTION_DOWN || motionEvent.action == MotionEvent.ACTION_MOVE) {
                    Toast.makeText(this, R.string.color_toast, Toast.LENGTH_SHORT).also {
                        it.setGravity(
                            Gravity.TOP, 0, 0
                        )
                    }.show()
                    bitMap = colorPicker.drawingCache
                    val pixels = bitMap.getPixel(motionEvent.x.toInt(), motionEvent.y.toInt())
                    val r = Color.red(pixels)
                    val g = Color.green(pixels)
                    val b = Color.blue(pixels)
                    colorPreview.setBackgroundColor(Color.rgb(r, g, b))
                    canvas.setColor(Color.rgb(r, g, b))
                }
            } catch (e: Exception) {
                return@setOnTouchListener false
            }
            return@setOnTouchListener true
        }
        cubeButton.setOnClickListener {
            Toast.makeText(this, R.string.cube_toast, Toast.LENGTH_SHORT)
                .also { it.setGravity(Gravity.TOP, 0, 0) }.show()
            canvas.setCap("SQUARE")
        }
        sphereButton.setOnClickListener {
            Toast.makeText(this, R.string.sphere_toast, Toast.LENGTH_SHORT)
                .also { it.setGravity(Gravity.TOP, 0, 0) }.show()
            canvas.setCap("ROUND")
        }
        semiSphereButton.setOnClickListener {
            Toast.makeText(this, R.string.butt_toast, Toast.LENGTH_SHORT)
                .also { it.setGravity(Gravity.TOP, 0, 0) }.show()
            canvas.setCap("BUTT")
        }
        penV2Button.setOnClickListener {
            Toast.makeText(this, R.string.penStyle_toast, Toast.LENGTH_SHORT)
                .also { it.setGravity(Gravity.TOP, 0, 0) }.show()
            canvas.setStyle("STROKE", canvas.getColor(), backgroundColor)
        }
        fillStrokeButton.setOnClickListener {
            Toast.makeText(this, R.string.fill_stroke_toast, Toast.LENGTH_SHORT)
                .also { it.setGravity(Gravity.TOP, 0, 0) }.show()
            canvas.setStyle("FILL_AND_STROKE", canvas.getColor(), backgroundColor)
        }
        eraserButton.setOnClickListener {
            Toast.makeText(this, R.string.eraser_toast, Toast.LENGTH_SHORT)
                .also { it.setGravity(Gravity.TOP, 0, 0) }.show()
            canvas.eraserMode(!eraserMode, backgroundColor)
        }

        addButton.setOnClickListener {
            if (storagePerms()) {
                Toast.makeText(this, R.string.add_toast, Toast.LENGTH_SHORT)
                    .also { it.setGravity(Gravity.TOP, 0, 0) }.show()
                val gallery =
                    Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                startActivityForResult(gallery, GALLERY_REQ_CODE)
            } else {
                storagePerms()
            }
        }
        undoButton.setOnClickListener {
            Toast.makeText(this, R.string.undo_toast, Toast.LENGTH_SHORT)
                .also { it.setGravity(Gravity.TOP, 0, 0) }.show()
            canvas.undo()
        }
        clearButton.setOnClickListener {
            Toast.makeText(this, R.string.clear_toast, Toast.LENGTH_SHORT)
                .also { it.setGravity(Gravity.TOP, 0, 0) }.show()
            canvas.clearCanvas()
            canvas.setBackgroundColor(Color.TRANSPARENT)
            colorPreview.setBackgroundColor(Color.BLACK)
            alphaSlider.value = 255f
            strokeSlider.value = 8f
            imageView.setImageResource(android.R.color.transparent)
        }
        saveButton.setOnClickListener {
            if (storagePerms()) {
                saveImage()
            } else {
                storagePerms()
            }
        }
        redoButton.setOnClickListener {
            Toast.makeText(this, R.string.redo_toast, Toast.LENGTH_SHORT).also {
                it.setGravity(
                    Gravity.TOP, 0, 0
                )
            }.show()
            canvas.redo()
        }
    }

    private fun storagePerms(): Boolean {
        return if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(
                arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE),
                STORAGE_PERMS
            )
            false
        } else {
            true
        }
    }

    @Override
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray,
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == STORAGE_PERMS) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, R.string.storage_grant, Toast.LENGTH_SHORT).also {
                    it.setGravity(
                        Gravity.TOP, 0, 0
                    )
                }.show()
            } else {
                Toast.makeText(this, R.string.storage_deny, Toast.LENGTH_SHORT).also {
                    it.setGravity(
                        Gravity.TOP, 0, 0
                    )
                }.show()
            }
        }
    }

    private fun saveImage() {
        var path = Environment.getExternalStorageDirectory().toString()
        path = "$path/Drawings"
        val dir = File(path)
        canvas.isDrawingCacheEnabled = true
        val name = "Drawing_" + System.currentTimeMillis() + ".png"
        val savedImage = MediaStore.Images.Media.insertImage(
            contentResolver,
            canvas.drawingCache,
            name,
            "Drawing"
        )
        if (savedImage != null) {
            try {
                if (!dir.isDirectory || !dir.exists()) dir.mkdirs()
                canvas.isDrawingCacheEnabled = true
                val file = File(dir, name)
                val fileOutputStream = FileOutputStream(file)
                val bitmap = canvas.drawingCache
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream)
                Toast.makeText(applicationContext, R.string.drawing_saved, Toast.LENGTH_SHORT)
                    .also {
                        it.setGravity(
                            Gravity.TOP, 0, 0
                        )
                    }.show()
            } catch (e: FileNotFoundException) {
                Toast.makeText(applicationContext, R.string.drawing_unsaved, Toast.LENGTH_SHORT)
                    .also {
                        it.setGravity(
                            Gravity.TOP, 0, 0
                        )
                    }.show()
            }
        }
        canvas.destroyDrawingCache()
    }

    @Override
    @SuppressLint("UseCompatLoadingForDrawables")
    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            if (requestCode == GALLERY_REQ_CODE) {
                if (data != null) {
                    imageView.setImageURI(data.data)
                    val bmp = (imageView.drawable as BitmapDrawable).bitmap
                    canvas.setBackground(bmp)

                    Toast.makeText(
                        applicationContext,
                        R.string.add_success_toast,
                        Toast.LENGTH_SHORT
                    )
                        .also {
                            it.setGravity(
                                Gravity.TOP, 0, 0
                            )
                        }.show()
                } else {
                    Toast.makeText(
                        applicationContext,
                        R.string.add_failed_toast,
                        Toast.LENGTH_SHORT
                    )
                        .also {
                            it.setGravity(
                                Gravity.TOP, 0, 0
                            )
                        }.show()
                }
            }
        }
    }

}