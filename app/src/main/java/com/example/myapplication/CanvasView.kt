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
import kotlin.random.Random

val board = CanvasView.Board()
val snake = CanvasView.Snake(_board = board)

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
    val fruit = Fruit(_board = board, _snake = snake)
    val drawer : Drawer

    init {
        drawer = Drawer(_board = board, _border_height = 1000, _border_width = 1000, _paintboard = paint2, _paintSnake = paint, _xOffset = 120, _yOffset = 100, _fruit = fruit)
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

    class Fruit(_count : Int = 1, _board : Board, _snake: Snake) {
        private val snake = _snake
        private val countInMoment = _count
        private val board = _board
        private var fruits = ArrayList<Point>()

        init {
            getNewFruits(snake)
        }
        fun getFruits() : ArrayList<Point> {
            return fruits
        }
        fun eatFruit(i : Int) {
            fruits.removeAt(i)
        }
        fun getNewFruits(snake:Snake) : ArrayList<Point> {
            for(i in 0 until countInMoment) {
                while(true) {
                    val fruit = newFruit()
                    if(!inSnake(fruit, snake)) {
                        fruits.add(fruit)
                        break
                    }
                }
            }
            return fruits
        }
        private fun newFruit() : Point {
            val point = Point((0..board.getWith()).random(), (0..board.getHeight()).random())
            return point
        }
        private fun inSnake(point: Point, snake: Snake) : Boolean {
            for(i in 0 until snake.getSnake().size) {
                if(point == snake.getSnake()[i]) return true
            }
            return false
        }
    }

    class Snake(_start_l:Int = 12, _speed:Int = 1, _dir:Dirs = Dirs.RIGHT, _board:Board) {
        val start_l = _start_l
        var lenght = _start_l
        val board = _board
        var speed = _speed
        var dir = _dir
        private var snake_parts = ArrayList<Point>()

        init {
            reStart()
        }

        fun eatFruit() {
            val p : Point
            p = when(dir) {
                Dirs.UP -> Point(snake_parts[lenght - 1].x, snake_parts[lenght - 1].y - 1)
                Dirs.LEFT -> Point(snake_parts[lenght - 1].x - 1, snake_parts[lenght - 1].y)
                Dirs.RIGHT -> Point(snake_parts[lenght - 1].x + 1, snake_parts[lenght - 1].y)
                Dirs.DOWN -> Point(snake_parts[lenght - 1].x, snake_parts[lenght - 1].y + 1)
            }
            snake_parts.add(p)
            lenght ++
        }

        fun reStart() {
            snake_parts = ArrayList<Point>()
            for(i in 0..start_l) {
                val newP = Point(board.getWith()/2 - start_l + i, board.getHeight()/2)
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
                        snake_parts[a] = Point(1, snake_parts[a].y)
                    else snake_parts[a] = Point(snake_parts[a].x + speed, snake_parts[a].y)
                }
                Dirs.DOWN -> {
                    if(snake_parts[a].y + speed > board.getHeight())
                        snake_parts[a] = Point(snake_parts[a].x, 1)
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

    class Drawer(_paintSnake:Paint, _paintboard:Paint, _border_width:Int, _border_height:Int, _board:Board, _xOffset:Int, _yOffset:Int, _fruit:Fruit) {
        private val fruit = _fruit
        private val board = _board
        private val xOffset = _xOffset.toFloat()
        private val yOffset = _yOffset.toFloat()
        private val paintSnake = _paintSnake
        private val paintBoard = _paintboard
        private val border_width = _border_width.toFloat()
        private val border_height = _border_height.toFloat()
        private val sq_w = border_width / board.getWith().toFloat()
        private val sq_h = border_height / board.getHeight().toFloat()

        private fun eatFruit(snake: Snake) {
            val i = checkFruitForward(snake)
            if(i != null){
                snake.eatFruit()
                fruit.eatFruit(i)
                if(fruit.getFruits().isEmpty()) fruit.getNewFruits(snake)
            }
        }

        private fun checkFruitForward(snake: Snake) : Int? {
            when(snake.dir) {
                Dirs.UP -> {
                    for(i in 0 until fruit.getFruits().size) {
                        if(fruit.getFruits()[i].y + 1 == snake.getSnake()[snake.lenght - 1].y &&
                            fruit.getFruits()[i].x == snake.getSnake()[snake.lenght - 1].x)
                            return i
                    }
                    return null
                }
                Dirs.DOWN -> {
                    for(i in 0 until fruit.getFruits().size) {
                        if(fruit.getFruits()[i].y - 1 == snake.getSnake()[snake.lenght - 1].y &&
                            fruit.getFruits()[i].x == snake.getSnake()[snake.lenght - 1].x)
                            return i
                    }
                    return null
                }
                Dirs.RIGHT -> {
                    for(i in 0 until fruit.getFruits().size) {
                        if(fruit.getFruits()[i].y == snake.getSnake()[snake.lenght - 1].y &&
                            fruit.getFruits()[i].x - 1 == snake.getSnake()[snake.lenght - 1].x)
                            return i
                    }
                    return null
                }
                Dirs.LEFT -> {
                    for(i in 0 until fruit.getFruits().size) {
                        if(fruit.getFruits()[i].y == snake.getSnake()[snake.lenght - 1].y &&
                            fruit.getFruits()[i].x + 1 == snake.getSnake()[snake.lenght - 1].x)
                            return i
                    }
                    return null
                }
            }
        }

        private fun checkSnake(snake:Snake) : Boolean { // if snake ate itself, snake is false
            for(i in 0..snake.getSnake().size - 2){
                if(snake.getSnake()[snake.getSnake().size - 1].x == snake.getSnake()[i].x
                    && snake.getSnake()[snake.getSnake().size - 1].y == snake.getSnake()[i].y)
                    return false
            }
            return true
        }

        fun Draw(snake:Snake, canvas: Canvas?) {
            eatFruit(snake)
            if(!checkSnake(snake))
                snake.reStart()
            canvas?.drawRect(xOffset, yOffset, border_width + xOffset, border_height + yOffset, paintBoard)
            for(i in fruit.getFruits()){
                canvas?.drawRect(
                    xOffset + (i.x * sq_w),
                    yOffset + (i.y * sq_h),
                    xOffset + ((i.x - 1) * sq_w),
                    yOffset + ((i.y - 1) * sq_h),
                    paintSnake)
            }
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