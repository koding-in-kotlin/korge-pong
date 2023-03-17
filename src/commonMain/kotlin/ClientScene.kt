import com.soywiz.klock.*
import com.soywiz.klogger.*
import com.soywiz.korev.*
import com.soywiz.korge.input.*
import com.soywiz.korge.scene.*
import com.soywiz.korge.ui.*
import com.soywiz.korge.view.*
import com.soywiz.korge.view.Circle
import com.soywiz.korim.color.*
import com.soywiz.korim.text.*
import com.soywiz.korio.async.*
import com.soywiz.korio.net.*
import com.soywiz.korma.geom.*
import kotlin.random.*

val SEND_UP = -1
val SEND_DOWN = 1

class ClientScene : Scene() {
    private var scoreLeft = 0
    private var scoreRight = 0

    lateinit var right: Paddle
    lateinit var left: Paddle

    lateinit var scoreLeftText: Text
    lateinit var scoreRightText: Text
    lateinit var middleText: Text

    lateinit var ball: Circle

    override suspend fun SContainer.sceneInit() {
        val x0 = sceneContainer.width / 2
        val y0 = sceneContainer.height / 2
        scoreLeftText = text("LEFT", 32.0) {
            x = 50.0
            y = 25.0
            alignment = TextAlignment.LEFT
        }
        scoreRightText = text("RIGHT", 32.0) {
            x = sceneContainer.width - 50
            y = 25.0
            alignment = TextAlignment.RIGHT
        }
        middleText = text("TEXT", 32.0) {
            x = sceneContainer.width / 2
            y = 25.0
            alignment = TextAlignment.CENTER
        }

        ball = circle(5.0, Colors.WHITE) {
            x = x0
            y = y0
        }

        left = Paddle(sceneContainer, sceneContainer.width / 10, 50, 5)
        right = Paddle(sceneContainer, sceneContainer.width * 9 / 10, 50, 5)

        // net
        line(x0, 0.0, x0, sceneContainer.height)
    }

    lateinit var gameStateText: Text
    override suspend fun SContainer.sceneMain() {
        val client = createTcpClient()

        gameStateText = text("State", 16.0) {
            x = 30.0
            y = 120.0
            alignment = TextAlignment.LEFT
        }
        client.connect("127.0.0.1", 5050)

        this.addUpdater {
            when {
                input.keys.pressing(Key.UP) -> {
                    launchImmediately { client.write(SEND_UP) }
                }

                input.keys.pressing(Key.DOWN) -> {
                    launchImmediately { client.write(SEND_DOWN) }
                }
            }
        }

        while (true) {
            if (client.connected) {
                val buffer = ByteArray(6 * 8)
                client.read(buffer, 0, 6 * 8)
                gameStateText.text = buffer.toGameState().toString()
                val (left1, right1, ballPos, _) = buffer.toGameState()
                ball.x = ballPos.x / 2
                ball.y = ballPos.y / 2
                left.rect.y = left1 / 2
                right.rect.y = right1 / 2

            }
        }

    }
}
