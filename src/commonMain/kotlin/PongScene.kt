import com.soywiz.klock.*
import com.soywiz.korev.*
import com.soywiz.korge.scene.*
import com.soywiz.korge.view.*
import com.soywiz.korge.view.Circle
import com.soywiz.korim.color.*
import com.soywiz.korim.text.*
import com.soywiz.korma.geom.*
import kotlin.random.*

class Paddle(val sceneContainer: Container, val x: Double) {
    var rect: SolidRect
    private val height = 100
    init {
        rect = sceneContainer.solidRect(10, height) {
            x = this@Paddle.x
            y = (sceneContainer.height - height) / 2
        }
    }
    
    operator fun minus(dy: Double): Paddle {
        rect.y -= dy
        if (rect.y < 0) {
            rect.y = 0.0
        }
        return this
    }

    operator fun plus(dy: Double): Paddle {
        rect.y += dy
        if (rect.y > (sceneContainer.height - height)) {
            rect.y = sceneContainer.height - height
        }
        return this
    }
}

class PongScene : Scene() {
    private var scoreLeft = 0
    private var scoreRight = 0
    private val paddleSpeed = 10.0

    val debug = false
    var velocity: Vector2D = Vector2D(xx(), yy())

    private fun yy() = listOf(-1.0, 1.0).shuffled().first() * Random.nextDouble(2.0, 8.0)

    private fun xx() = listOf(-7.0, 7.0).shuffled().first()

    lateinit var right: Paddle
    lateinit var left: Paddle
    lateinit var scoreLeftText: Text
    lateinit var scoreRightText: Text

    lateinit var ball: Circle

    override suspend fun SContainer.sceneInit() {
        val x0 = sceneContainer.width / 2
        val y0 = sceneContainer.height / 2
        scoreLeftText = text("LEFT", 32.0) {
            x = 100.0
            y = 50.0
            alignment = TextAlignment.LEFT
        }
        scoreRightText = text("RIGHT", 32.0) {
            x = sceneContainer.width - 100
            y = 50.0
            alignment = TextAlignment.RIGHT
        }

        ball = circle(10.0, Colors.WHITE) {
            x = x0
            y = y0
        }

        left = Paddle(sceneContainer, sceneContainer.width / 10)
        right = Paddle(sceneContainer, sceneContainer.width * 9 / 10)

        ball.addFixedUpdater(60.timesPerSecond) {
            x += velocity.x
            y += velocity.y

            if (x > sceneContainer.width) {
                scoreLeft += 1
                x = x0
                y = y0
                scoreLeftText.text = "$scoreLeft"
                velocity.x = xx()
                velocity.y = yy()
            }

            if (x < 0) {
                scoreRight += 1
                x = x0
                y = y0
                scoreRightText.text = "$scoreRight"
                velocity.x = xx()
                velocity.y = yy()
            }

            if (y < 0) {
                velocity.y = -velocity.y
            }
            if (y > (sceneContainer.height - ball.radius*2)) {
                velocity.y = -velocity.y
            }
        }

        ball.onCollision(filter = {it is SolidRect}) {
            velocity.x = -velocity.x
            velocity.y = Random.nextDouble(2.0, 8.0)
        }

        // net
        line(x0, 0.0, x0, sceneContainer.height)
    }

    override suspend fun SContainer.sceneMain() {
        this.addFixedUpdater(10.timesPerSecond) {
            if (ball.y > right.rect.y) {
                right.rect.y += paddleSpeed
                // check the boundaries
            }
            if (ball.y < right.rect.y) {
                right.rect.y -= paddleSpeed
                // checke the boundaries
            }
        }
        this.addUpdater {
            when {
                input.keys.pressing(Key.UP) -> {
                    left -= paddleSpeed

                    if (debug) {
                        right -= paddleSpeed
                    }
                }
                input.keys.pressing(Key.DOWN) -> {
                    left += paddleSpeed

                    if (debug) {
                        right += paddleSpeed
                    }
                }
            }
        }
    }

}

