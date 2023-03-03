import com.soywiz.klock.timesPerSecond
import com.soywiz.klogger.Console
import com.soywiz.korev.Key
import com.soywiz.korev.Key.*
import com.soywiz.korge.scene.Scene
import com.soywiz.korge.view.*
import com.soywiz.korim.color.Colors
import com.soywiz.korim.text.TextAlignment
import com.soywiz.korio.async.launchImmediately
import com.soywiz.korio.lang.ASCII
import com.soywiz.korio.lang.toString
import com.soywiz.korio.net.*
import com.soywiz.korma.geom.Point
import com.soywiz.korma.geom.Vector2D
import kotlin.math.*
import kotlin.random.Random


fun ByteArray.toGameState(): GameState {
    val doubles = asSequence()
        .chunked(8)
        .map {
            it.toByteArray().toString(ASCII).toDouble()
        }
        .toList()
    return GameState(
        doubles[0],
        doubles[1],
        Point(doubles[2], doubles[3]),
        Point(doubles[4], doubles[5])
    )
}

object PongGame : Scene() {
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
            if (y > (sceneContainer.height - ball.radius * 2)) {
                velocity.y = -velocity.y
            }
        }

        ball.onCollision(filter = { it is SolidRect }) {
            velocity.x = -velocity.x
            velocity.y = Random.nextDouble(2.0, 8.0)
        }

        // net
        line(x0, 0.0, x0, sceneContainer.height)
    }

    override suspend fun SContainer.sceneMain() {
        val server = createTcpServer(5050, "0.0.0.0")
        server.listen { client ->
            while (true) {
                if (client.connected) {

                    val dir = client.read()
                    if (dir == 255) {
                        left -= paddleSpeed
                    }
                    else if (dir == 1) {
                        left += paddleSpeed
                    }
                    // this is for the state
//                    val buffer = ByteArray(6 * 8)
//                    client.read(buffer, 0, 6 * 8)
//                    Console.info("I GOT THIS ${buffer.toGameState()}")
//                    val (left1, right1, ballPos, ballVelocity) = buffer.toGameState()
//                    ball.x = ballPos.x
//                    ball.y = ballPos.y
//                    velocity.x = ballVelocity.x
//                    velocity.y = ballVelocity.y
//                    left.rect.y = left1
//                    right.rect.y = right1

                }
            }
        }

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
                input.keys.pressing(UP) -> {
                    left -= paddleSpeed

                    if (debug) {
                        right -= paddleSpeed
                    }
                }

                input.keys.pressing(DOWN) -> {
                    left += paddleSpeed

                    if (debug) {
                        right += paddleSpeed
                    }
                }

                input.keys.justReleased(C) -> {
                    launchImmediately { sceneContainer.changeTo({ ClientScene() }) }
                }
            }
        }
    }

}

