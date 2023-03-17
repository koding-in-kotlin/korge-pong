import com.soywiz.klogger.*
import com.soywiz.korev.*
import com.soywiz.korge.input.*
import com.soywiz.korge.scene.*
import com.soywiz.korge.ui.*
import com.soywiz.korge.view.*
import com.soywiz.korim.text.*
import com.soywiz.korio.async.*
import com.soywiz.korio.net.*
import com.soywiz.korma.geom.*
import kotlin.random.*

val SEND_UP = -1
val SEND_DOWN = 1

class ClientScene : Scene() {

    lateinit var gameStateText: Text
    override suspend fun SContainer.sceneMain() {
        val client = createTcpClient()

        gameStateText = text("State", 16.0) {
            x = 30.0
            y = 120.0
            alignment = TextAlignment.LEFT
        }
        client.connect("127.0.0.1", 5050)
        uiVerticalStack(150.0) {
            uiButton("Send the State") {
                onClick {
                    val state = GameState(
                        Random.nextDouble(0.0, 200.0),
                        Random.nextDouble(300.0, 400.0),
                        Point(121.0, 100.0),
                        Vector2D(0.0, 0.0),
                    )
                    client.write(state.toMessage())
                }
            }
            uiButton("UP") {
                onClick {
                    client.write(SEND_UP)
                }
            }
            uiButton("DOWN") {
                onClick {
                    client.write(SEND_DOWN)
                }
            }
        }

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
                Console.info("I GOT THIS ${buffer.toGameState()}")
                gameStateText.text = buffer.toGameState().toString()
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
}
