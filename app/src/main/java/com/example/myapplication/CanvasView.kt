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

    var kek = 0F
    var ticX = 0f
    var ticY = 0f
    var canv : Canvas? = null
    val paint = Paint().apply {
        isAntiAlias = true
        color = Color.RED
        style = Paint.Style.FILL
    }
    val paint2 = Paint().apply {
        isAntiAlias = true
        color = Color.WHITE
        style = Paint.Style.STROKE
    }

    val drawer : Drawer

    val border_height : Int
    val border_width : Int

    init {
        border_height = this.height
        border_width = this.width
        drawer = Drawer(_board = board, _border_height = 1000, _border_width = 1000, _paintboard = paint2, _paintSnake = paint)
    }

    val timer1 = object : CountDownTimer(30000, 100) {

        override fun onTick(millisUntilFinished: Long) {
            kek += 1
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



    fun DrawLol() {
        paint.strokeWidth = 8F
        val defTic = 10f

        when(Dir){
            Dirs.UP -> ticY -= defTic
            Dirs.DOWN -> ticY += defTic
            Dirs.RIGHT -> ticX += defTic
            Dirs.LEFT -> ticX -=defTic
        }
        canv?.drawRect(ticX + 0f,ticY + 0f,ticX + 300f,ticY + 500f, paint)
    }

    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canv = canvas
        //DrawLol()
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
                    if(snake_parts[a].y - speed < 0)
                        snake_parts[a] = Point(snake_parts[a].x, board.getHeight())
                    else snake_parts[a] = Point(snake_parts[a].x,snake_parts[a].y - speed)
                }
                Dirs.LEFT -> {
                    if(snake_parts[a].x - speed < 0)
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

    class Drawer(_paintSnake:Paint, _paintboard:Paint, _border_width:Int, _border_height:Int, _board:Board) {
        val board = _board
        val paintSnake = _paintSnake
        val paintBoard = _paintboard
        val border_width = _border_width
        val border_height = _border_height
        val sq_w = border_width / board.getWith()
        val sq_h = border_height / board.getHeight()
        fun Draw(snake:Snake, canvas: Canvas?) {
            canvas?.drawRect(0f, 0f, border_width.toFloat(), border_height.toFloat(), paintBoard)
            for(i in snake.getSnake()) {
                canvas?.drawRect(
                    (i.x * sq_w).toFloat(),
                    (i.y * sq_h).toFloat(),
                    ((i.x - 1) * sq_w).toFloat(),
                    ((i.y - 1) * sq_h).toFloat(),
                    paintSnake)
            }
        }

    }
}