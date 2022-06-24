package com.example.myapplication

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Point
import android.os.CountDownTimer
import android.util.AttributeSet
import android.util.Log
import android.view.View

public val board = CanvasView.Board()
public val snake = CanvasView.Snake(_board = board)

class CanvasView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    var canv : Canvas? = null
    val paint = Paint().apply {
        isAntiAlias = true
        color = Color.RED
        style = Paint.Style.FILL
    }
    val paint2 = Paint().apply {
        isAntiAlias = true
        color = Color.BLACK
        style = Paint.Style.STROKE
    }

    val drawer : Drawer

    init {
        drawer = Drawer(_board = board, _border_height = 1000, _border_width = 1000, _paintboard = paint2, _paintSnake = paint, _xOffset = 120, _yOffset = 100)
    }

    val timer1 = object : CountDownTimer(30000, 100) {

        override fun onTick(millisUntilFinished: Long) {
            snake.goForward()
            invalidate()

        }
        override fun onFinish() {
        this.start()
        }
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        timer1.start()
    }

    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canv = canvas
        drawer.Draw(snake, canvas)
    }

    class Snake(start_l:Int = 12, _speed:Int = 1, _dir:Dirs = Dirs.RIGHT, _board:Board) {

        var lenght = start_l
        val board = _board
        var speed = _speed
        var dir = _dir
        private var snake_parts = ArrayList<Point>()

        init {
            for(i in 0..lenght) {
                val newP = Point(board.getWith()/2 - lenght + i, board.getHeight()/2)
                snake_parts.add(newP)
            }
        }

        fun goForward() {
            val a = snake_parts.size - 1

            for(i in 0..(snake_parts.size - 2)) {
                snake_parts[i] = snake_parts[i + 1]
            }
            when (dir) {
                Dirs.UP -> {
                    if(snake_parts[a].y - speed - 1 < 0)
                        snake_parts[a] = Point(snake_parts[a].x, board.getHeight())
                    else snake_parts[a] = Point(snake_parts[a].x,snake_parts[a].y - speed)
                }
                Dirs.LEFT -> {
                    if(snake_parts[a].x - speed - 1 < 0)
                        snake_parts[a] = Point(board.getWith(), snake_parts[a].y)
                    else snake_parts[a] = Point(snake_parts[a].x - speed, snake_parts[a].y)
                }
                Dirs.RIGHT -> {
                    if(snake_parts[a].x + speed > board.getWith())
                        snake_parts[a] = Point(0, snake_parts[a].y)
                    else snake_parts[a] = Point(snake_parts[a].x + speed, snake_parts[a].y)
                }
                Dirs.DOWN -> {
                    if(snake_parts[a].y + speed > board.getHeight())
                        snake_parts[a] = Point(snake_parts[a].x, 0)
                    else snake_parts[a] = Point(snake_parts[a].x, snake_parts[a].y + speed)
                }
            }
        }
        fun changeDir(_dir:Dirs) {
            when {
                dir == Dirs.LEFT && _dir == Dirs.RIGHT -> return
                dir == Dirs.DOWN && _dir == Dirs.UP -> return
                dir == Dirs.RIGHT && _dir == Dirs.LEFT -> return
                dir == Dirs.UP && _dir == Dirs.DOWN -> return
                else -> dir = _dir
            }
        }
        fun getSnake() : List<Point> {
            return snake_parts
        }

    }

    class Board(_size_w:Int = 50, _size_h: Int = 50) {

        protected val size_w = _size_w
        protected val size_h = _size_h

        fun getWith() : Int{
            return size_w
        }
        fun getHeight() : Int{
            return size_h
        }
    }

    class Drawer(_paintSnake:Paint, _paintboard:Paint, _border_width:Int, _border_height:Int, _board:Board, _xOffset:Int, _yOffset:Int) {
        val board = _board
        val xOffset = _xOffset.toFloat()
        val yOffset = _yOffset.toFloat()
        val paintSnake = _paintSnake
        val paintBoard = _paintboard
        val border_width = _border_width.toFloat()
        val border_height = _border_height.toFloat()
        val sq_w = border_width / board.getWith().toFloat()
        val sq_h = border_height / board.getHeight().toFloat()
        fun Draw(snake:Snake, canvas: Canvas?) {
            canvas?.drawRect(xOffset, yOffset, border_width + xOffset, border_height + yOffset, paintBoard)
            for(i in snake.getSnake()) {
                canvas?.drawRect(
                    xOffset + (i.x * sq_w),
                    yOffset + (i.y * sq_h),
                    xOffset + ((i.x - 1) * sq_w),
                    yOffset + ((i.y - 1) * sq_h),
                    paintSnake)
            }
        }

    }
}